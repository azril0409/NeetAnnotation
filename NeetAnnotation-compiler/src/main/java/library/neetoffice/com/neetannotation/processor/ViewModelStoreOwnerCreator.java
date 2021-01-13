package library.neetoffice.com.neetannotation.processor;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;


import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;

import library.neetoffice.com.neetannotation.AfterInject;

public class ViewModelStoreOwnerCreator extends BaseCreator {
    static final String CLASS_NAME = "SimpleViewModelStoreOwner";
    static final String APPLICATION = "application";
    static final String VIEW_MODEL_STORE = "viewModelStore";
    static final String VIEW_MODEL_PROVIDER_FACTORY = "viewModelProviderFactory";
    private final MainProcessor mainProcessor;

    public ViewModelStoreOwnerCreator(MainProcessor mainProcessor, ProcessingEnvironment processingEnv) {
        super(mainProcessor, processingEnv);
        this.mainProcessor = mainProcessor;
    }

    @Override
    void process(TypeElement applicationElement, RoundEnvironment roundEnv) {
        final String className = applicationElement.getSimpleName() + "_";
        final List<? extends Element> enclosedElements = applicationElement.getEnclosedElements();
        ArrayList<CodeBlock> onCreateElements = new ArrayList();
        final boolean haveDagger = DaggerHelp.process(applicationElement);
        if (haveDagger) {
            onCreateElements.add(createDaggerInjectCode(applicationElement));
        }
        for (Element element : enclosedElements) {
            onCreateElements.add(createAfterAnnotationCode(element));
        }
        createApplaction(onCreateElements, getPackageName(applicationElement), className, getClassName(applicationElement.asType()));

    }

    void createSimpleViewModelStoreOwner(String packageName) {
        final TypeSpec.Builder tb = TypeSpec.classBuilder(CLASS_NAME)
                .addModifiers(Modifier.PUBLIC)
                .addSuperinterface(AndroidClass.ViewModelStoreOwner)
                .addSuperinterface(AndroidClass.HasDefaultViewModelProviderFactory);

        final FieldSpec.Builder viewModelStore = FieldSpec.builder(AndroidClass.ViewModelStore, VIEW_MODEL_STORE, Modifier.PRIVATE, Modifier.FINAL)
                .initializer("new $T()", AndroidClass.ViewModelStore);
        tb.addField(viewModelStore.build());

        final FieldSpec.Builder viewModelProviderFactory = FieldSpec.builder(AndroidClass.Factory, VIEW_MODEL_PROVIDER_FACTORY, Modifier.PRIVATE, Modifier.FINAL);
        tb.addField(viewModelProviderFactory.build());

        final MethodSpec.Builder constructor = MethodSpec.constructorBuilder()
                .addModifiers(Modifier.PUBLIC)
                .addParameter(AndroidClass.Application, APPLICATION)
                .addStatement("$N = $T.getInstance($N)", VIEW_MODEL_PROVIDER_FACTORY, AndroidClass.AndroidViewModelFactory, APPLICATION);
        tb.addMethod(constructor.build());

        final MethodSpec.Builder getDefaultViewModelProviderFactory = MethodSpec.methodBuilder("getDefaultViewModelProviderFactory")
                .addAnnotation(Override.class)
                .addModifiers(Modifier.PUBLIC)
                .returns(AndroidClass.Factory)
                .addStatement("return $N", VIEW_MODEL_PROVIDER_FACTORY);
        tb.addMethod(getDefaultViewModelProviderFactory.build());

        final MethodSpec.Builder getViewModelStore = MethodSpec.methodBuilder("getViewModelStore")
                .addAnnotation(Override.class)
                .addModifiers(Modifier.PUBLIC)
                .returns(AndroidClass.ViewModelStore)
                .addStatement("return $N", VIEW_MODEL_STORE);
        tb.addMethod(getViewModelStore.build());
        writeTo(packageName, tb.build());
    }

    void createApplaction(String packageName) {
        createApplaction(new ArrayList(), packageName, "Application_", AndroidClass.Application);
    }

    void createApplaction(List<CodeBlock> onCreateElements, String packageName, String className, TypeName claz) {
        final TypeSpec.Builder tb = TypeSpec.classBuilder(className)
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                .superclass(claz)
                .addSuperinterface(AndroidClass.ViewModelStoreOwner)
                .addSuperinterface(AndroidClass.HasDefaultViewModelProviderFactory);


        final FieldSpec.Builder viewModelStore = FieldSpec.builder(AndroidClass.ViewModelStore, VIEW_MODEL_STORE, Modifier.PRIVATE, Modifier.FINAL)
                .initializer("new $T()", AndroidClass.ViewModelStore);
        tb.addField(viewModelStore.build());

        final FieldSpec.Builder viewModelProviderFactory = FieldSpec.builder(AndroidClass.Factory, VIEW_MODEL_PROVIDER_FACTORY, Modifier.PRIVATE);
        tb.addField(viewModelProviderFactory.build());

        final MethodSpec.Builder onCreate = MethodSpec.methodBuilder("onCreate")
                .addModifiers(Modifier.PUBLIC)
                .addAnnotation(Override.class)
                .addStatement("super.onCreate()")
                .addCode("$T.setErrorHandler(new $T()", RxJavaClass.RxJavaPlugins, RxJavaClass.Consumer(ClassName.get(Throwable.class)))
                .beginControlFlow("")
                .addCode("@$T public void accept($T throwable) throws $T", Override.class, Throwable.class, Exception.class)
                .beginControlFlow("")
                .endControlFlow()
                .endControlFlow()
                .addStatement(")")
                .addStatement("$N = $T.getInstance(this)", VIEW_MODEL_PROVIDER_FACTORY, AndroidClass.AndroidViewModelFactory);
        for (CodeBlock codeBlock : onCreateElements) {
            onCreate.addCode(codeBlock);
        }
        tb.addMethod(onCreate.build());


        final MethodSpec.Builder getDefaultViewModelProviderFactory = MethodSpec.methodBuilder("getDefaultViewModelProviderFactory")
                .addAnnotation(Override.class)
                .addModifiers(Modifier.PUBLIC)
                .returns(AndroidClass.Factory)
                .addStatement("return $N", VIEW_MODEL_PROVIDER_FACTORY);
        tb.addMethod(getDefaultViewModelProviderFactory.build());

        final MethodSpec.Builder getViewModelStore = MethodSpec.methodBuilder("getViewModelStore")
                .addAnnotation(Override.class)
                .addModifiers(Modifier.PUBLIC)
                .returns(AndroidClass.ViewModelStore)
                .addStatement("return $N", VIEW_MODEL_STORE);
        tb.addMethod(getViewModelStore.build());
        writeTo(packageName, tb.build());
    }

    CodeBlock createDaggerInjectCode(TypeElement activityElement) {
        return CodeBlock.builder()
                .addStatement("Dagger_$N.builder().$N(new $T(this)).build().inject(this)", activityElement.getSimpleName(), toModelCase(AndroidClass.CONTEXT_MODULE_NAME), mainProcessor.contextModule)
                .build();
    }

    CodeBlock createAfterAnnotationCode(Element afterAnnotationElement) {
        final AfterInject aAfterInject = afterAnnotationElement.getAnnotation(AfterInject.class);
        if (aAfterInject == null) {
            return CodeBlock.builder().build();
        }
        return CodeBlock.builder().addStatement("$N()", afterAnnotationElement.getSimpleName()).build();
    }
}
