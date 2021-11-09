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
import library.neetoffice.com.neetannotation.OnCreate;
import library.neetoffice.com.neetannotation.OnDestroy;
import library.neetoffice.com.neetannotation.OnPause;
import library.neetoffice.com.neetannotation.OnResume;
import library.neetoffice.com.neetannotation.OnStart;
import library.neetoffice.com.neetannotation.OnStop;
import library.neetoffice.com.neetannotation.Published;
import library.neetoffice.com.neetannotation.ViewModelOf;

public class ViewModelCreator extends BaseCreator {
    static final String DAGGER_NAME = "dagger";
    static final String CONSTRUCTOR_APPLICATION_PARAMETER = "application";
    static final String CONSTRUCTOR_ACTIVITY_PARAMETER = "activity";
    static final String CONSTRUCTOR_CONTEXT_PARAMETER = "context";
    static final String CONTEXT_FROM = "application";
    static final String OWNER = "owner";
    static final HashMap<String, String> publishedNameMap = new HashMap<>();
    private final MainProcessor mainProcessor;
    final SubscribeHelp subscribeHelp;
    final HandleHelp handleHelp;

    public ViewModelCreator(MainProcessor processor, ProcessingEnvironment processingEnv) {
        super(processor, processingEnv);
        this.mainProcessor = processor;
        subscribeHelp = new SubscribeHelp(this);
        handleHelp = new HandleHelp(this);
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
        final boolean haveDagger = DaggerHelp.process(viewModelElement);
        final String className = viewModelElement.getSimpleName() + "_";
        final List<TypeElement> viewModelElements = findSuperElements(viewModelElement, roundEnv, NViewModel.class);
        viewModelElements.add(viewModelElement);

        final SubscribeHelp.Builder subscribeBuilder = subscribeHelp.builder();
        final HandleHelp.Builder handleHelpBuilder = handleHelp.builder(className);

        final TypeSpec.Builder tb = TypeSpec.classBuilder(className)
                .superclass(getClassName(viewModelElement.asType()))
                .addSuperinterface(AndroidClass.DefaultLifecycleObserver)
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL);

        //final FieldSpec.Builder viewModelStoreOwner = FieldSpec.builder(ClassName.get(mainProcessor.contextPackageName, ViewModelStoreOwnerCreator.CLASS_NAME), VIEW_MODEL_STORE_OWNER, Modifier.PRIVATE, Modifier.STATIC);
        //tb.addField(viewModelStoreOwner.build());

        final CodeBlock.Builder init = CodeBlock.builder();
        final CodeBlock.Builder afterAnnotationCode = CodeBlock.builder();
        final CodeBlock.Builder onCreateCode = CodeBlock.builder();
        final CodeBlock.Builder onStartCode = CodeBlock.builder();
        final CodeBlock.Builder onResumeCode = CodeBlock.builder();
        final CodeBlock.Builder onPauseCode = CodeBlock.builder();
        final CodeBlock.Builder onStopCode = CodeBlock.builder();
        final CodeBlock.Builder onDestroyCode = CodeBlock.builder();
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
                }
                if (enclosedElement.getAnnotation(ViewModelOf.class) != null) {
                    subscribeBuilder.parseElement(enclosedElement);
                }
                if (enclosedElement.getAnnotation(OnCreate.class) != null) {
                    onCreateCode.add(parseLifecycleMethod(enclosedElement));
                }
                if (enclosedElement.getAnnotation(OnStart.class) != null) {
                    onStartCode.add(parseLifecycleMethod(enclosedElement));
                }
                if (enclosedElement.getAnnotation(OnResume.class) != null) {
                    onResumeCode.add(parseLifecycleMethod(enclosedElement));
                }
                if (enclosedElement.getAnnotation(OnPause.class) != null) {
                    onPauseCode.add(parseLifecycleMethod(enclosedElement));
                }
                if (enclosedElement.getAnnotation(OnStop.class) != null) {
                    onStopCode.add(parseLifecycleMethod(enclosedElement));
                }
                if (enclosedElement.getAnnotation(OnDestroy.class) != null) {
                    onDestroyCode.add(parseLifecycleMethod(enclosedElement));
                }
                handleHelpBuilder.parseElement(enclosedElement);
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

        tb.addMethod(getInstanceMethod(viewModelElement, className));
        tb.addMethod(getInstanceWithKeyMethod(viewModelElement, className));
        for (MethodSpec methodSpec : handleHelpBuilder.createMotheds()) {
            tb.addMethod(methodSpec);
        }
        tb.addMethod(getLifecycleMethod("onCreate", onCreateCode.build()));
        tb.addMethod(getLifecycleMethod("onStart", onStartCode.build()));
        tb.addMethod(getLifecycleMethod("onResume", onResumeCode.build()));
        tb.addMethod(getLifecycleMethod("onPause", onPauseCode.build()));
        tb.addMethod(getLifecycleMethod("onStop", onStopCode.build()));
        tb.addMethod(getLifecycleMethod("onDestroy", onDestroyCode.build()));

        final MethodSpec.Builder onCleared = MethodSpec.methodBuilder("onCleared")
                .returns(void.class)
                .addAnnotation(Override.class)
                .addModifiers(Modifier.PUBLIC)
                .addStatement("super.onCleared()")
                .addCode(onClearedCode.build());
        tb.addMethod(onCleared.build());

        writeTo(getPackageName(viewModelElement), tb.build());
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

    private MethodSpec getInstanceMethod(TypeElement viewModelElement, String className) {
        final NViewModel aNViewModel = viewModelElement.getAnnotation(NViewModel.class);
        final boolean isSingle = aNViewModel.isSingle();
        final MethodSpec.Builder methodBuilder = MethodSpec.methodBuilder("getInstance")
                .returns(getClassName(viewModelElement.asType()))
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC);
        if (isSingle) {
            methodBuilder.addParameter(AndroidClass.Context, "context")
                    .addStatement("final $T application = context.getApplicationContext()", AndroidClass.Context)
                    .beginControlFlow("if(application instanceof $T)", AndroidClass.ViewModelStoreOwner)
                    .addStatement("return new $T(($T)application).get($T.class)", AndroidClass.ViewModelProvider, AndroidClass.ViewModelStoreOwner, ClassName.get(getPackageName(viewModelElement), className))
                    .endControlFlow()
                    .addStatement("throw new RuntimeException(\"Cannot get instance by \" + context)");
        } else {
            methodBuilder.addParameter(AndroidClass.Context, "context")
                    .beginControlFlow("if(context instanceof $T)", AndroidClass.ViewModelStoreOwner)
                    .addStatement("return new $T(($T)context).get($T.class)", AndroidClass.ViewModelProvider, AndroidClass.ViewModelStoreOwner, ClassName.get(getPackageName(viewModelElement), className))
                    .endControlFlow()
                    .addStatement("final $T application = context.getApplicationContext()", AndroidClass.Context)
                    .beginControlFlow("if(application instanceof $T)", AndroidClass.ViewModelStoreOwner)
                    .addStatement("return new $T(($T)application).get($T.class)", AndroidClass.ViewModelProvider, AndroidClass.ViewModelStoreOwner, ClassName.get(getPackageName(viewModelElement), className))
                    .endControlFlow()
                    .addStatement("throw new RuntimeException(\"Cannot get instance by \" + context)");
        }
        return methodBuilder.build();
    }

    private MethodSpec getInstanceWithKeyMethod(TypeElement viewModelElement, String className) {
        final NViewModel aNViewModel = viewModelElement.getAnnotation(NViewModel.class);
        final boolean isSingle = aNViewModel.isSingle();
        final MethodSpec.Builder methodBuilder = MethodSpec.methodBuilder("getInstance")
                .addParameter(String.class, "key")
                .returns(getClassName(viewModelElement.asType()))
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC);
        if (isSingle) {
            methodBuilder.addParameter(AndroidClass.Context, "context")
                    .addStatement("final $T application = context.getApplicationContext()", AndroidClass.Context)
                    .beginControlFlow("if(application instanceof $T)", AndroidClass.ViewModelStoreOwner)
                    .addStatement("return new $T(($T)application).get(key,$T.class)", AndroidClass.ViewModelProvider, AndroidClass.ViewModelStoreOwner, ClassName.get(getPackageName(viewModelElement), className))
                    .endControlFlow()
                    .addStatement("throw new RuntimeException(\"Cannot get instance by \" + context)");
        } else {
            methodBuilder.addParameter(AndroidClass.Context, "context")
                    .beginControlFlow("if(context instanceof $T)", AndroidClass.ViewModelStoreOwner)
                    .addStatement("return new $T(($T)context).get(key,$T.class)", AndroidClass.ViewModelProvider, AndroidClass.ViewModelStoreOwner, ClassName.get(getPackageName(viewModelElement), className))
                    .endControlFlow()
                    .addStatement("final $T application = context.getApplicationContext()", AndroidClass.Context)
                    .beginControlFlow("if(application instanceof $T)", AndroidClass.ViewModelStoreOwner)
                    .addStatement("return new $T(($T)application).get(key,$T.class)", AndroidClass.ViewModelProvider, AndroidClass.ViewModelStoreOwner, ClassName.get(getPackageName(viewModelElement), className))
                    .endControlFlow()
                    .addStatement("throw new RuntimeException(\"Cannot get instance by \" + context)");
        }
        return methodBuilder.build();
    }

    private CodeBlock parseLifecycleMethod(Element element) {
        CodeBlock.Builder builder = CodeBlock.builder();
        final ExecutableElement method = (ExecutableElement) element;
        builder.add("$N(", element.getSimpleName());
        final Iterator<? extends VariableElement> parameters = method.getParameters().iterator();
        while (parameters.hasNext()) {
            if (isInstanceOf(element.asType(), AndroidClass.LifecycleOwner)) {
                builder.add(OWNER);
            } else {
                builder.add("null");
            }
            if (parameters.hasNext()) {
                builder.add(",");
            }
        }
        builder.addStatement(")");
        return builder.build();
    }

    private MethodSpec getLifecycleMethod(String name, CodeBlock code) {
        return MethodSpec.methodBuilder(name)
                .returns(void.class)
                .addAnnotation(Override.class)
                .addModifiers(Modifier.PUBLIC)
                .addParameter(AndroidClass.LifecycleOwner, OWNER)
                .addCode(code)
                .build();
    }
}
