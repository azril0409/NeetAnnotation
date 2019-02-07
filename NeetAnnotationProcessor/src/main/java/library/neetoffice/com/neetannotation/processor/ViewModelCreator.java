package library.neetoffice.com.neetannotation.processor;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;
import com.squareup.javapoet.TypeVariableName;

import java.util.Iterator;
import java.util.List;

import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.inject.Named;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;

import library.neetoffice.com.neetannotation.AfterAnnotation;
import library.neetoffice.com.neetannotation.InjectEntity;
import library.neetoffice.com.neetannotation.NViewModel;
import library.neetoffice.com.neetannotation.Subject;

public class ViewModelCreator extends BaseCreator {
    static final String FIND_SUBJECT_BY_NAME = "findSubjectByName";
    static final String FIND_SUBJECT_BY_NAME_PARAMETER_NAME = "name";
    static final String _SUBJECT = "_Subject";
    static final String DAGGER_NAME = "dagger";
    static final String CONSTRUCTOR_APPLICATION_PARAMETER = "application";
    static final String CONSTRUCTOR_ACTIVITY_PARAMETER = "activity";
    static final String CONSTRUCTOR_CONTEXT_PARAMETER = "context";
    static final TypeVariableName SUBJECT_PARAMETERIZED_TYPE_NAME = TypeVariableName.get("T");

    public ViewModelCreator(MainProcessor processor, ProcessingEnvironment processingEnv) {
        super(processor, processingEnv);
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
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL);

        final MethodSpec.Builder findSubjectByName = createFindSubjectByNameMethodBuilder();

        final CodeBlock.Builder init = CodeBlock.builder();
        final CodeBlock.Builder afterAnnotationCode = CodeBlock.builder();

        if (haveDagger) {
            init.add(createDaggerCode(viewModelElement));
        }

        for (TypeElement viewModel : viewModelElements) {
            final List<? extends Element> enclosedElements = viewModel.getEnclosedElements();
            for (Element enclosedElement : enclosedElements) {
                if (enclosedElement.getAnnotation(Subject.class) != null) {
                    //final FieldSpec subjectField = createSubjectField(enclosedElement);
                    //tb.addField(subjectField);
                    final CodeBlock instanceInteractor = createInstanceCode(viewModelElement, (VariableElement) enclosedElement, haveDagger);
                    init.add(instanceInteractor);
                    final CodeBlock findSubjectByNameCode = createFindSubjectByNameCode(enclosedElement);
                    findSubjectByName.addCode(findSubjectByNameCode);
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
        findSubjectByName.endControlFlow();
        if (!haveConstructor) {
            tb.addMethod(MethodSpec.constructorBuilder()
                    .addModifiers(Modifier.PUBLIC)
                    .addCode(init.build())
                    .build());
        }
        tb.addMethod(findSubjectByName.build());
        writeTo(getPackageName(viewModelElement), tb.build());
    }

    MethodSpec.Builder createFindSubjectByNameMethodBuilder() {
        return MethodSpec.methodBuilder(FIND_SUBJECT_BY_NAME)
                .addTypeVariable(SUBJECT_PARAMETERIZED_TYPE_NAME)
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                .addParameter(String.class, FIND_SUBJECT_BY_NAME_PARAMETER_NAME)
                .returns(RxJavaClass.Subject(SUBJECT_PARAMETERIZED_TYPE_NAME))
                .beginControlFlow("switch ($N)", FIND_SUBJECT_BY_NAME_PARAMETER_NAME)
                .addStatement("default: return $T.create()", RxJavaClass.PublishSubject);
    }

    /*FieldSpec createSubjectField(Element interactor) {
        final String interactorName = interactor.asType().toString();
        final Element entityElement = processor.interactorCreator.interactElements.get(interactorName);
        final TypeName entityType;
        if (entityElement == null) {
            entityType = ClassName.bestGuess(interactorName.substring(0, interactorName.length() - processor.interactorCreator.PRESENTER.length()));
        } else {
            entityType = getClassName(entityElement.asType());
        }
        final String fieldName = interactor.getSimpleName().toString() + _SUBJECT;
        final TypeName subjectTypeName = RxJavaClass.Subject(entityType);
        final FieldSpec.Builder fb = FieldSpec.builder(subjectTypeName, fieldName, Modifier.PUBLIC)
                .initializer("$T.create()", RxJavaClass.PublishSubject);
        return fb.build();
    }*/

    CodeBlock createInstanceCode(TypeElement viewModelElement, VariableElement interactElement, boolean haveDagger) {
        final boolean isSubAndroidViewModel = isSubAndroidViewModel(viewModelElement);
        final String daggerMethodName = DaggerHelp.findNameFromDagger(this, interactElement);
        final InteractorCreator.InteractBuild interactBuild = processor.interactorCreator.interactBuilds.get(interactElement.asType().toString());
        final TypeName implementType;
        if (interactBuild == null) {
            final String implementTypeString = interactElement.asType().toString();
            implementType = ClassName.bestGuess(implementTypeString + "_");
        } else {
            implementType = ClassName.get(interactBuild.packageName, interactBuild.implementClassName);
        }

        final InjectEntity aInjectEntity = interactElement.getAnnotation(InjectEntity.class);
        if (aInjectEntity == null || !haveDagger) {
            return CodeBlock.builder()
                    .add("$N = new $T(", interactElement.getSimpleName(), implementType)
                    .add(addNullCode(implementType))
                    .addStatement(")")
                    .build();
        }
        if (isSubAndroidViewModel) {
            return CodeBlock.builder()
                    .add("$N = new $T(", interactElement.getSimpleName(), implementType)
                    .add("$N.$N()", DAGGER_NAME, daggerMethodName)
                    .addStatement(")")
                    .build();
        }
        return CodeBlock.builder()
                .addStatement("$N = new $T($N.$N())", interactElement.getSimpleName(), implementType, DAGGER_NAME, daggerMethodName)
                .build();
    }

    CodeBlock createFindSubjectByNameCode(Element interactElement) {
        final CodeBlock.Builder code = CodeBlock.builder();
        final Subject aSubject= interactElement.getAnnotation(Subject.class);
        final String name = aSubject.name();
        if(!name.isEmpty()){
            code.addStatement("case $S: return ($T) $N.$N()", name, RxJavaClass.Subject(SUBJECT_PARAMETERIZED_TYPE_NAME), interactElement.getSimpleName(), InteractorCreator.SUBJECT).build();
            if(!interactElement.getSimpleName().equals(name)){
                code.addStatement("case $S: return ($T) $N.$N()", interactElement.getSimpleName(), RxJavaClass.Subject(SUBJECT_PARAMETERIZED_TYPE_NAME), interactElement.getSimpleName(), InteractorCreator.SUBJECT).build();
            }
        }else {
            code.addStatement("case $S: return ($T) $N.$N()", interactElement.getSimpleName(), RxJavaClass.Subject(SUBJECT_PARAMETERIZED_TYPE_NAME), interactElement.getSimpleName(), InteractorCreator.SUBJECT).build();
        }
        return code.build();
    }

    private CodeBlock createDaggerCode(TypeElement viewModelElement) {
        final CodeBlock.Builder code = CodeBlock.builder();
        code.add("_$N $N = ", viewModelElement.getSimpleName(), DAGGER_NAME);
        final boolean isSubAndroidViewModel = isSubAndroidViewModel(viewModelElement);
        if (isSubAndroidViewModel) {
            code.addStatement("Dagger_$N.builder().$N(new $T($N)).build()", viewModelElement.getSimpleName(), toModelCase(AndroidClass.CONTEXT_MODULE_NAME), AndroidClass.CONTEXT_MODULE, CONSTRUCTOR_APPLICATION_PARAMETER);
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
}
