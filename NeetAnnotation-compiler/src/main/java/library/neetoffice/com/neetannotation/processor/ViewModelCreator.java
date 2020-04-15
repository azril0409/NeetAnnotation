package library.neetoffice.com.neetannotation.processor;

import com.squareup.javapoet.AnnotationSpec;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;
import com.squareup.javapoet.TypeVariableName;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.Iterator;
import java.util.List;

import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;

import library.neetoffice.com.neetannotation.AfterAnnotation;
import library.neetoffice.com.neetannotation.InjectEntity;
import library.neetoffice.com.neetannotation.ListInteractor;
import library.neetoffice.com.neetannotation.NViewModel;
import library.neetoffice.com.neetannotation.Published;

public class ViewModelCreator extends BaseCreator {
    static final String FIND_SUBJECT_BY_NAME = "findSubjectByName";
    static final String FIND_SUBJECT_BY_NAME_PARAMETER_NAME = "name";
    static final String _SUBJECT = "_Subject";
    static final String DAGGER_NAME = "dagger";
    static final String CONSTRUCTOR_APPLICATION_PARAMETER = "application";
    static final String CONSTRUCTOR_ACTIVITY_PARAMETER = "activity";
    static final String CONSTRUCTOR_CONTEXT_PARAMETER = "context";
    static final TypeVariableName SUBJECT_PARAMETERIZED_TYPE_NAME = TypeVariableName.get("T");
    private final MainProcessor mainProcessor;

    public ViewModelCreator(MainProcessor processor, ProcessingEnvironment processingEnv) {
        super(processor, processingEnv);
        this.mainProcessor = processor;
    }

    @Override
    void process(TypeElement viewModelElement, RoundEnvironment roundEnv) {
        boolean isAabstract = viewModelElement.getModifiers().contains(Modifier.ABSTRACT);
        if (isAabstract) {
            return;
        }
        final boolean haveDagger = DaggerHelp.process(viewModelElement);
        final String className = viewModelElement.getSimpleName() + "_";
        final List<TypeElement> viewModelElements = findSuperElements(viewModelElement, roundEnv, NViewModel.class);
        viewModelElements.add(viewModelElement);

        final TypeSpec.Builder tb = TypeSpec.classBuilder(className)
                .superclass(getClassName(viewModelElement.asType()))
                .addSuperinterface(AndroidClass.LifecycleObserver)
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL);


        final CodeBlock.Builder init = CodeBlock.builder();
        final CodeBlock.Builder afterAnnotationCode = CodeBlock.builder();

        if (haveDagger) {
            init.add(createDaggerCode(viewModelElement));
        }

        for (TypeElement viewModel : viewModelElements) {
            final List<? extends Element> enclosedElements = viewModel.getEnclosedElements();
            for (Element enclosedElement : enclosedElements) {
                if (enclosedElement.getAnnotation(Published.class) != null) {
                    final CodeBlock instanceInteractor = createInstanceCode(viewModelElement, enclosedElement, haveDagger);
                    init.add(instanceInteractor);
                }
                afterAnnotationCode.add(createAfterAnnotationCode(enclosedElement));
            }
        }
        if (haveDagger) {
            init.addStatement("$N.inject(this)", DAGGER_NAME);
        }
        init.add(afterAnnotationCode.build());
        boolean haveConstructor = false;
        for (Element enclosedElement : viewModelElement.getEnclosedElements()) {
            if (enclosedElement.getKind() == ElementKind.CONSTRUCTOR) {
                tb.addMethod(createConstructorMethod(viewModelElement, (ExecutableElement) enclosedElement, init.build()));
                haveConstructor = true;
            }
        }
        //findSubjectByName.endControlFlow();
        if (!haveConstructor) {
            tb.addMethod(MethodSpec.constructorBuilder()
                    .addModifiers(Modifier.PUBLIC)
                    .addCode(init.build())
                    .build());
        }
        writeTo(getPackageName(viewModelElement), tb.build());
    }

    CodeBlock createInstanceCode(TypeElement viewModelElement, Element interactElement, boolean haveDagger) {
        final boolean isSubAndroidViewModel = isSubAndroidViewModel(viewModelElement);
        final String daggerMethodName = DaggerHelp.findNameFromDagger(this, interactElement);
        final InteractorCreator.InteractBuild interactBuild = mainProcessor.interactorCreator.interactBuilds.get(interactElement.asType().toString());
        final ListInteractorCreator.InteractBuild listInteractBuild = mainProcessor.listInteractorCreator.interactBuilds.get(interactElement.asType().toString());
        final SetInteractorCreator.InteractBuild setInteractBuild = mainProcessor.setInteractorCreator.interactBuilds.get(interactElement.asType().toString());
        final TypeName implementType;
        if (interactBuild != null) {
            if (interactBuild.implementClassName.startsWith(interactBuild.packageName)) {
                implementType = ClassName.bestGuess(interactBuild.implementClassName);
            } else {
                implementType = ClassName.get(interactBuild.packageName, interactBuild.implementClassName);
            }
        } else if (listInteractBuild != null) {
            if (listInteractBuild.implementClassName.startsWith(listInteractBuild.packageName)) {
                implementType = ClassName.bestGuess(listInteractBuild.implementClassName);
            } else {
                implementType = ClassName.get(listInteractBuild.packageName, listInteractBuild.implementClassName);
            }
        } else if (setInteractBuild != null) {
            if (setInteractBuild.implementClassName.startsWith(setInteractBuild.packageName)) {
                implementType = ClassName.bestGuess(setInteractBuild.implementClassName);
            } else {
                implementType = ClassName.get(setInteractBuild.packageName, setInteractBuild.implementClassName);
            }
        } else {
            final String typeName = getClassName(interactElement.asType()).toString();
            if (isContextInteractor(typeName)) {
                implementType = ClassName.bestGuess(mainProcessor.contextPackageName + "." + typeName + "_");
            } else {
                implementType = ClassName.bestGuess(typeName + "_");
            }
        }

        final InjectEntity aInjectEntity = interactElement.getAnnotation(InjectEntity.class);
        if (aInjectEntity == null || !haveDagger) {
            return CodeBlock.builder()
                    .addStatement("$N = $T.newInstance()", interactElement.getSimpleName(), implementType)
                    .build();
        }
        return CodeBlock.builder()
                .addStatement("$N = $T.newInstance($N.$N())", interactElement.getSimpleName(), implementType, DAGGER_NAME, daggerMethodName)
                .build();
    }

    private boolean isContextInteractor(String typeName) {
        return !typeName.contains(".");
    }

    private CodeBlock createDaggerCode(TypeElement viewModelElement) {
        final CodeBlock.Builder code = CodeBlock.builder();
        code.add("_$N $N = ", viewModelElement.getSimpleName(), DAGGER_NAME);
        final boolean isSubAndroidViewModel = isSubAndroidViewModel(viewModelElement);
        if (isSubAndroidViewModel) {
            code.addStatement("Dagger_$N.builder().$N(new $T($N)).build()", viewModelElement.getSimpleName(), toModelCase(AndroidClass.CONTEXT_MODULE_NAME), mainProcessor.contextModule, CONSTRUCTOR_APPLICATION_PARAMETER);
        } else {
            code.addStatement("Dagger_$N.create()", viewModelElement.getSimpleName());
        }
        return code.build();
    }

    MethodSpec createConstructorMethod(TypeElement viewModelElement, ExecutableElement constructorElement, CodeBlock init) {
        final MethodSpec.Builder mb = MethodSpec.constructorBuilder()
                .addModifiers(Modifier.PUBLIC);
        final Iterator<? extends VariableElement> parametersIterator = constructorElement.getParameters().iterator();

        final CodeBlock.Builder code = CodeBlock.builder()
                .add("super(");
        while (parametersIterator.hasNext()) {
            final VariableElement parameter = parametersIterator.next();
            final String parameterMame;
            if (isInstanceOf(parameter.asType(), AndroidClass.Application)) {
                parameterMame = CONSTRUCTOR_APPLICATION_PARAMETER;
                mb.addParameter(AndroidClass.Application, parameterMame);
            } else if (isInstanceOf(parameter.asType(), AndroidClass.Activity)) {
                parameterMame = CONSTRUCTOR_ACTIVITY_PARAMETER;
                mb.addParameter(AndroidClass.Activity, parameterMame);
            } else if (isInstanceOf(parameter.asType(), AndroidClass.Context)) {
                parameterMame = CONSTRUCTOR_CONTEXT_PARAMETER;
                mb.addParameter(AndroidClass.Context, parameterMame);
            } else {
                parameterMame = parameter.getSimpleName().toString();
                mb.addParameter(getClassName(parameter.asType()), parameterMame);
            }
            code.add("$N", parameterMame);
            if (parametersIterator.hasNext()) {
                code.add(",");
            }
        }
        mb.addCode(code.addStatement(")").build())
                .addCode(init);
        return mb.build();
    }


    CodeBlock createAfterAnnotationCode(Element afterAnnotationElement) {
        final AfterAnnotation aAfterAnnotation = afterAnnotationElement.getAnnotation(AfterAnnotation.class);
        if (aAfterAnnotation == null) {
            return CodeBlock.builder().build();
        }
        final ExecutableElement mothod = (ExecutableElement) afterAnnotationElement;
        final CodeBlock.Builder parameters = CodeBlock.builder();
        final Iterator<? extends VariableElement> iterator = mothod.getParameters().iterator();
        while (iterator.hasNext()) {
            final VariableElement parameter = iterator.next();
            parameters.add(addNullCode(parameter.asType()));
            if (iterator.hasNext()) {
                parameters.add(",");
            }
        }
        return CodeBlock.builder()
                .add("$N(", afterAnnotationElement.getSimpleName())
                .add(parameters.build())
                .addStatement(")")
                .build();
    }


    @Deprecated
    MethodSpec.Builder createFindSubjectByNameMethodBuilder() {
        return MethodSpec.methodBuilder(FIND_SUBJECT_BY_NAME)
                .addTypeVariable(SUBJECT_PARAMETERIZED_TYPE_NAME)
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                .addParameter(String.class, FIND_SUBJECT_BY_NAME_PARAMETER_NAME)
                .returns(RxJavaClass.Subject(SUBJECT_PARAMETERIZED_TYPE_NAME))
                .beginControlFlow("switch ($N)", FIND_SUBJECT_BY_NAME_PARAMETER_NAME)
                .addStatement("default: return $T.create()", RxJavaClass.BehaviorSubject);
    }

    @Deprecated
    CodeBlock createFindSubjectByNameCode(Element interactElement) {
        final CodeBlock.Builder code = CodeBlock.builder();
        final Published aSubject = interactElement.getAnnotation(Published.class);
        final String name = interactElement.getSimpleName().toString();
        if (!name.isEmpty()) {
            code.addStatement("case $S: return ($T) $N.$N()", name, RxJavaClass.Subject(SUBJECT_PARAMETERIZED_TYPE_NAME), interactElement.getSimpleName(), InteractorCreator.SUBJECT).build();
            if (!interactElement.getSimpleName().equals(name)) {
                code.addStatement("case $S: return ($T) $N.$N()", interactElement.getSimpleName(), RxJavaClass.Subject(SUBJECT_PARAMETERIZED_TYPE_NAME), interactElement.getSimpleName(), InteractorCreator.SUBJECT).build();
            }
        } else {
            code.addStatement("case $S: return ($T) $N.$N()", interactElement.getSimpleName(), RxJavaClass.Subject(SUBJECT_PARAMETERIZED_TYPE_NAME), interactElement.getSimpleName(), InteractorCreator.SUBJECT).build();
        }
        return code.build();
    }

    public void createContextModule(String packageName) {
        final TypeSpec.Builder tb = TypeSpec.classBuilder("AndroidApplicationModel")
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL);

        final FieldSpec applicationField = FieldSpec.builder(AndroidClass.Application, "mApplication", Modifier.PRIVATE).build();

        final MethodSpec constructor = MethodSpec.constructorBuilder()
                .addModifiers(Modifier.PUBLIC)
                .addParameter(ParameterSpec.builder(AndroidClass.Application, "application").addAnnotation(AndroidClass.NonNull).build())
                .addStatement("mApplication = application")
                .build();

        final MethodSpec getApplication = MethodSpec.methodBuilder("getApplication")
                .addModifiers(Modifier.PUBLIC)
                .addTypeVariable(TypeVariableName.get("T", AndroidClass.Application))
                .returns(TypeVariableName.get("T"))
                .addStatement("return (T) mApplication")
                .build();

        tb.addField(applicationField);
        tb.addMethod(constructor);
        tb.addMethod(getApplication);

        writeTo(packageName, tb.build());
    }
}
