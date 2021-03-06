package library.neetoffice.com.neetannotation.processor;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;
import com.squareup.javapoet.TypeVariableName;

import java.util.HashMap;
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

import library.neetoffice.com.neetannotation.AfterInject;
import library.neetoffice.com.neetannotation.InjectInitialEntity;
import library.neetoffice.com.neetannotation.NViewModel;
import library.neetoffice.com.neetannotation.NamedAs;
import library.neetoffice.com.neetannotation.Published;
import library.neetoffice.com.neetannotation.ViewModelOf;

public class ViewModelCreator extends BaseCreator {
    static final String FIND_SUBJECT_BY_NAME = "findSubjectByName";
    static final String FIND_SUBJECT_BY_NAME_PARAMETER_NAME = "name";
    static final String VIEW_MODEL_STORE_OWNER = "viewModelStoreOwner";
    static final String DAGGER_NAME = "dagger";
    static final String CONSTRUCTOR_APPLICATION_PARAMETER = "application";
    static final String CONSTRUCTOR_ACTIVITY_PARAMETER = "activity";
    static final String CONSTRUCTOR_CONTEXT_PARAMETER = "context";
    static final String CONTEXT_FROM = "application";
    static final TypeVariableName SUBJECT_PARAMETERIZED_TYPE_NAME = TypeVariableName.get("T");
    static final HashMap<String, String> publishedNameMap = new HashMap();
    private final MainProcessor mainProcessor;
    final SubscribeHelp subscribeHelp;

    public ViewModelCreator(MainProcessor processor, ProcessingEnvironment processingEnv) {
        super(processor, processingEnv);
        this.mainProcessor = processor;
        subscribeHelp = new SubscribeHelp(this);
    }

    @Override
    void release() {
        super.release();
        publishedNameMap.clear();
    }

    @Override
    void process(TypeElement viewModelElement, RoundEnvironment roundEnv) {
        boolean isAabstract = viewModelElement.getModifiers().contains(Modifier.ABSTRACT);
        if (isAabstract) {
            return;
        }
        final NViewModel aNViewModel = viewModelElement.getAnnotation(NViewModel.class);
        boolean isSingle = aNViewModel.isSingle();
        final boolean haveDagger = DaggerHelp.process(viewModelElement);
        final String className = viewModelElement.getSimpleName() + "_";
        final List<TypeElement> viewModelElements = findSuperElements(viewModelElement, roundEnv, NViewModel.class);
        viewModelElements.add(viewModelElement);

        final SubscribeHelp.Builder subscribeBuilder = subscribeHelp.builder();

        final TypeSpec.Builder tb = TypeSpec.classBuilder(className)
                .superclass(getClassName(viewModelElement.asType()))
                .addSuperinterface(AndroidClass.LifecycleObserver)
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL);

        //final FieldSpec.Builder viewModelStoreOwner = FieldSpec.builder(ClassName.get(mainProcessor.contextPackageName, ViewModelStoreOwnerCreator.CLASS_NAME), VIEW_MODEL_STORE_OWNER, Modifier.PRIVATE, Modifier.STATIC);
        //tb.addField(viewModelStoreOwner.build());

        final CodeBlock.Builder init = CodeBlock.builder();
        final CodeBlock.Builder afterAnnotationCode = CodeBlock.builder();
        final CodeBlock.Builder onClearedCode = CodeBlock.builder();

        if (haveDagger) {
            init.add(createDaggerCode(viewModelElement));
        }

        for (TypeElement viewModel : viewModelElements) {
            final List<? extends Element> enclosedElements = viewModel.getEnclosedElements();
            for (Element enclosedElement : enclosedElements) {
                if (enclosedElement.getAnnotation(Published.class) != null) {
                    final NamedAs namedAs = enclosedElement.getAnnotation(NamedAs.class);
                    if (namedAs != null) {
                        for (String name : namedAs.value()) {
                            final String key = viewModel.getQualifiedName().toString() + "_" + name;
                            publishedNameMap.put(key, enclosedElement.getSimpleName().toString());
                        }
                    }
                    final String key = viewModel.getQualifiedName().toString() + "_" + enclosedElement.getSimpleName().toString();
                    publishedNameMap.put(key, enclosedElement.getSimpleName().toString());

                    final CodeBlock instanceInteractor = createInstanceCode(viewModelElement, enclosedElement, haveDagger);
                    init.add(instanceInteractor);
                } else if (enclosedElement.getAnnotation(ViewModelOf.class) != null) {
                    subscribeBuilder.parseElement(enclosedElement);
                }
                afterAnnotationCode.add(createAfterAnnotationCode(enclosedElement));
            }
        }
        if (haveDagger) {
            init.addStatement("$N.inject(this)", DAGGER_NAME);
        }
        subscribeBuilder.addDisposableFieldInType(tb);
        onClearedCode.add(subscribeBuilder.createDispose());
        init.add(subscribeBuilder.createAddViewModelOfCode(CONTEXT_FROM));
        init.add(subscribeBuilder.createCodeSubscribeInOnCreate(className, CONTEXT_FROM));
        init.add(afterAnnotationCode.build());
        boolean haveConstructor = false;
        for (Element enclosedElement : viewModelElement.getEnclosedElements()) {
            if (enclosedElement.getKind() == ElementKind.CONSTRUCTOR) {
                tb.addMethod(createConstructorMethod(viewModelElement, (ExecutableElement) enclosedElement, init.build()));
                haveConstructor = true;
            }
        }
        if (!haveConstructor) {
            tb.addMethod(MethodSpec.constructorBuilder()
                    .addModifiers(Modifier.PUBLIC)
                    .addCode(init.build())
                    .build());
        }


        final MethodSpec.Builder getInstance = MethodSpec.methodBuilder("getInstance")
                .returns(getClassName(viewModelElement.asType()))
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC);
        if (isSingle) {
            getInstance.addParameter(AndroidClass.Context, "context")
                    .addStatement("final $T application = context.getApplicationContext()", AndroidClass.Context)
                    .beginControlFlow("if(application instanceof $T)", AndroidClass.ViewModelStoreOwner)
                    .addStatement("return new $T(($T)application).get($T.class)", AndroidClass.ViewModelProvider, AndroidClass.ViewModelStoreOwner, ClassName.get(getPackageName(viewModelElement), className))
                    .endControlFlow()
                    .addStatement("throw new RuntimeException(\"Cannot get instance by \" + context)");
        } else {
            getInstance.addParameter(AndroidClass.Context, "context")
                    .beginControlFlow("if(context instanceof $T)", AndroidClass.ViewModelStoreOwner)
                    .addStatement("return new $T(($T)context).get($T.class)", AndroidClass.ViewModelProvider, AndroidClass.ViewModelStoreOwner, ClassName.get(getPackageName(viewModelElement), className))
                    .endControlFlow()
                    .addStatement("final $T application = context.getApplicationContext()", AndroidClass.Context)
                    .beginControlFlow("if(application instanceof $T)", AndroidClass.ViewModelStoreOwner)
                    .addStatement("return new $T(($T)application).get($T.class)", AndroidClass.ViewModelProvider, AndroidClass.ViewModelStoreOwner, ClassName.get(getPackageName(viewModelElement), className))
                    .endControlFlow()
                    .addStatement("throw new RuntimeException(\"Cannot get instance by \" + context)");
        }
        tb.addMethod(getInstance.build());

        final MethodSpec.Builder getInstanceBykey = MethodSpec.methodBuilder("getInstance")
                .addParameter(String.class, "key")
                .returns(getClassName(viewModelElement.asType()))
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC);
        if (isSingle) {
            getInstanceBykey.addParameter(AndroidClass.Context, "context")
                    .addStatement("final $T application = context.getApplicationContext()", AndroidClass.Context)
                    .beginControlFlow("if(application instanceof $T)", AndroidClass.ViewModelStoreOwner)
                    .addStatement("return new $T(($T)application).get(key,$T.class)", AndroidClass.ViewModelProvider, AndroidClass.ViewModelStoreOwner, ClassName.get(getPackageName(viewModelElement), className))
                    .endControlFlow()
                    .addStatement("throw new RuntimeException(\"Cannot get instance by \" + context)");
        } else {
            getInstanceBykey.addParameter(AndroidClass.Context, "context")
                    .beginControlFlow("if(context instanceof $T)", AndroidClass.ViewModelStoreOwner)
                    .addStatement("return new $T(($T)context).get(key,$T.class)", AndroidClass.ViewModelProvider, AndroidClass.ViewModelStoreOwner, ClassName.get(getPackageName(viewModelElement), className))
                    .endControlFlow()
                    .addStatement("final $T application = context.getApplicationContext()", AndroidClass.Context)
                    .beginControlFlow("if(application instanceof $T)", AndroidClass.ViewModelStoreOwner)
                    .addStatement("return new $T(($T)application).get(key,$T.class)", AndroidClass.ViewModelProvider, AndroidClass.ViewModelStoreOwner, ClassName.get(getPackageName(viewModelElement), className))
                    .endControlFlow()
                    .addStatement("throw new RuntimeException(\"Cannot get instance by \" + context)");
        }

        tb.addMethod(getInstanceBykey.build());

        final MethodSpec.Builder onCleared = MethodSpec.methodBuilder("onCleared")
                .returns(void.class)
                .addAnnotation(Override.class)
                .addModifiers(Modifier.PUBLIC)
                .addStatement("super.onCleared()")
                .addCode(onClearedCode.build());
        tb.addMethod(onCleared.build());

        writeTo(getPackageName(viewModelElement), tb.build());
    }

    private String getSubscribeMethodName(Element element) {
        final String name;
        final AnnotationMirror named = AnnotationHelp.findAnnotationMirror(element, DaggerClass.Named);
        if (named == null) {
            publishedNameMap.put(element.getSimpleName().toString(), "NOT find " + DaggerClass.Named.simpleName());
            name = element.getSimpleName().toString();
        } else {
            final String value = (String) AnnotationHelp.findAnnotationValue(named, "value");
            publishedNameMap.put(element.getSimpleName().toString(), "Find " + DaggerClass.Named.simpleName());
            if (value.equals("null")) {
                name = element.getSimpleName().toString();
            } else {
                name = value;
            }
        }
        return name;
    }

    CodeBlock createInstanceCode(TypeElement viewModelElement, Element interactElement, boolean haveDagger) {
        final Published aPublished = interactElement.getAnnotation(Published.class);
        final String daggerMethodName = DaggerHelp.findNameFromDagger(this, interactElement);
        final InteractorCreator.InteractBuild interactBuild = mainProcessor.interactorCreator.interactBuilds.get(interactElement.asType().toString());
        final ListInteractorCreator.InteractBuild listInteractBuild = mainProcessor.listInteractorCreator.interactBuilds.get(interactElement.asType().toString());
        final SetInteractorCreator.InteractBuild setInteractBuild = mainProcessor.setInteractorCreator.interactBuilds.get(interactElement.asType().toString());
        final TypeName implementType;
        if (interactBuild != null) {
            if (aPublished.recordLastEntity()) {
                if (interactBuild.interactorClassName.startsWith(interactBuild.packageName)) {
                    implementType = ClassName.bestGuess(interactBuild.interactorClassName);
                } else {
                    implementType = ClassName.get(interactBuild.packageName, interactBuild.interactorClassName);
                }
            } else {
                if (interactBuild.publisherClassName.startsWith(interactBuild.packageName)) {
                    implementType = ClassName.bestGuess(interactBuild.publisherClassName);
                } else {
                    implementType = ClassName.get(interactBuild.packageName, interactBuild.publisherClassName);
                }
            }
        } else if (listInteractBuild != null) {
            if (aPublished.recordLastEntity()) {
                if (listInteractBuild.interactorClassName.startsWith(listInteractBuild.packageName)) {
                    implementType = ClassName.bestGuess(listInteractBuild.interactorClassName);
                } else {
                    implementType = ClassName.get(listInteractBuild.packageName, listInteractBuild.interactorClassName);
                }
            } else {
                if (listInteractBuild.publisherClassName.startsWith(listInteractBuild.packageName)) {
                    implementType = ClassName.bestGuess(listInteractBuild.publisherClassName);
                } else {
                    implementType = ClassName.get(listInteractBuild.packageName, listInteractBuild.publisherClassName);
                }
            }
        } else if (setInteractBuild != null) {
            if (aPublished.recordLastEntity()) {
                if (setInteractBuild.interactorClassName.startsWith(setInteractBuild.packageName)) {
                    implementType = ClassName.bestGuess(setInteractBuild.interactorClassName);
                } else {
                    implementType = ClassName.get(setInteractBuild.packageName, setInteractBuild.interactorClassName);
                }
            } else {
                if (setInteractBuild.publisherClassName.startsWith(setInteractBuild.packageName)) {
                    implementType = ClassName.bestGuess(setInteractBuild.publisherClassName);
                } else {
                    implementType = ClassName.get(setInteractBuild.packageName, setInteractBuild.publisherClassName);
                }
            }
        } else {
            final String typeName = getClassName(interactElement.asType()).toString();
            if (typeName.endsWith(ListInteractorCreator.INTERACTOR)) {
                final String entityTypeName = typeName.substring(0, typeName.length() - ListInteractorCreator.INTERACTOR.length());
                if (aPublished.recordLastEntity()) {
                    if (isContextInteractor(typeName)) {
                        implementType = ClassName.bestGuess(mainProcessor.contextPackageName + "." + entityTypeName + ListInteractorCreator.INTERACTOR_);
                    } else {
                        implementType = ClassName.bestGuess(entityTypeName + ListInteractorCreator.INTERACTOR_);
                    }
                } else {
                    if (isContextInteractor(typeName)) {
                        implementType = ClassName.bestGuess(mainProcessor.contextPackageName + "." + entityTypeName + ListInteractorCreator.PUBLISHER_);
                    } else {
                        implementType = ClassName.bestGuess(entityTypeName + ListInteractorCreator.PUBLISHER_);
                    }
                }
            } else if (typeName.endsWith(SetInteractorCreator.INTERACTOR)) {
                final String entityTypeName = typeName.substring(0, typeName.length() - SetInteractorCreator.INTERACTOR.length());
                if (aPublished.recordLastEntity()) {
                    if (isContextInteractor(typeName)) {
                        implementType = ClassName.bestGuess(mainProcessor.contextPackageName + "." + entityTypeName + SetInteractorCreator.INTERACTOR_);
                    } else {
                        implementType = ClassName.bestGuess(entityTypeName + SetInteractorCreator.INTERACTOR_);
                    }
                } else {
                    if (isContextInteractor(typeName)) {
                        implementType = ClassName.bestGuess(mainProcessor.contextPackageName + "." + entityTypeName + SetInteractorCreator.PUBLISHER_);
                    } else {
                        implementType = ClassName.bestGuess(entityTypeName + SetInteractorCreator.PUBLISHER_);
                    }
                }
            } else if (typeName.endsWith(InteractorCreator.INTERACTOR)) {
                final String entityTypeName = typeName.substring(0, typeName.length() - InteractorCreator.INTERACTOR.length());
                if (aPublished.recordLastEntity()) {
                    if (isContextInteractor(typeName)) {
                        implementType = ClassName.bestGuess(mainProcessor.contextPackageName + "." + entityTypeName + InteractorCreator.INTERACTOR_);
                    } else {
                        implementType = ClassName.bestGuess(entityTypeName + InteractorCreator.INTERACTOR_);
                    }
                } else {
                    if (isContextInteractor(typeName)) {
                        implementType = ClassName.bestGuess(mainProcessor.contextPackageName + "." + entityTypeName + InteractorCreator.PUBLISHER_);
                    } else {
                        implementType = ClassName.bestGuess(entityTypeName + InteractorCreator.PUBLISHER_);
                    }
                }
            } else {
                return CodeBlock.builder().build();
            }
        }

        final InjectInitialEntity aInjectInitialEntity = interactElement.getAnnotation(InjectInitialEntity.class);
        if (aInjectInitialEntity == null || !haveDagger) {
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
        code.add("final _$N $N = ", viewModelElement.getSimpleName(), DAGGER_NAME);
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
        final AfterInject aAfterInject = afterAnnotationElement.getAnnotation(AfterInject.class);
        if (aAfterInject == null) {
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
            code.addStatement("case $S: return ($T) $N.$N()", name, RxJavaClass.Subject(SUBJECT_PARAMETERIZED_TYPE_NAME), interactElement.getSimpleName(), InteractorCreator.OBSERVABLE).build();
            if (!interactElement.getSimpleName().equals(name)) {
                code.addStatement("case $S: return ($T) $N.$N()", interactElement.getSimpleName(), RxJavaClass.Subject(SUBJECT_PARAMETERIZED_TYPE_NAME), interactElement.getSimpleName(), InteractorCreator.OBSERVABLE).build();
            }
        } else {
            code.addStatement("case $S: return ($T) $N.$N()", interactElement.getSimpleName(), RxJavaClass.Subject(SUBJECT_PARAMETERIZED_TYPE_NAME), interactElement.getSimpleName(), InteractorCreator.OBSERVABLE).build();
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
