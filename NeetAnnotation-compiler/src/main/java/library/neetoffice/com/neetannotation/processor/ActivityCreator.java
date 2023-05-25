package library.neetoffice.com.neetannotation.processor;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;

import java.util.Iterator;
import java.util.List;

import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;

import library.neetoffice.com.neetannotation.AfterInject;
import library.neetoffice.com.neetannotation.FragmentBy;
import library.neetoffice.com.neetannotation.NActivity;
import library.neetoffice.com.neetannotation.OnCreate;
import library.neetoffice.com.neetannotation.OnDestroy;
import library.neetoffice.com.neetannotation.OnPause;
import library.neetoffice.com.neetannotation.OnResume;
import library.neetoffice.com.neetannotation.OnStart;
import library.neetoffice.com.neetannotation.OnStop;
import library.neetoffice.com.neetannotation.ViewById;
import library.neetoffice.com.neetannotation.ViewModelOf;

/***/
public class ActivityCreator extends BaseCreator {
    static final String CONTEXT_FROM = "this";
    static final String DEF_PACKAGE = "getApplicationContext().getPackageName()";
    static final String SAVE_INSTANCE_STATE = "savedInstanceState";
    static final String OUT_STATE = "outState";
    static final String GET_BUNDLE_METHOD = "getIntent().getExtras()";
    static final String ITEM = "item";
    static final String MENU = "menu";
    static final String DATA = "data";
    static final String BINDING = "binding";
    final ResourcesHelp resourcesHelp;
    final ListenerHelp listenerHelp;
    final ExtraHelp extraHelp;
    final SubscribeHelp subscribeHelp;
    final MenuHelp menuHelp;
    final ActivityResultHelp activityResultHelp;
    final HandleHelp handleHelp;
    private final MainProcessor mainProcessor;

    /**
     * @param processor     MainProcessor
     * @param processingEnv ProcessingEnvironment
     */
    public ActivityCreator(MainProcessor processor, ProcessingEnvironment processingEnv) {
        super(processor, processingEnv);
        mainProcessor = processor;
        resourcesHelp = new ResourcesHelp();
        listenerHelp = new ListenerHelp(this);
        extraHelp = new ExtraHelp(this);
        subscribeHelp = new SubscribeHelp(this);
        menuHelp = new MenuHelp(this);
        activityResultHelp = new ActivityResultHelp(this);
        handleHelp = new HandleHelp(this);
    }

    @Override
    void process(TypeElement activityElement, RoundEnvironment roundEnv) {
        boolean isAbstract = activityElement.getModifiers().contains(Modifier.ABSTRACT);
        if (isAbstract) {
            return;
        }
        if (!isSubActivity(activityElement)) {
            return;
        }
        final String packageName = getPackageName(activityElement);
        final String className = activityElement.getSimpleName() + "_";
        final List<TypeElement> activityElements = findSuperElements(activityElement, roundEnv, NActivity.class);
        activityElements.add(activityElement);
        //======================================================
        final ListenerHelp.Builder listenerBuilder = listenerHelp.builder(className, CONTEXT_FROM, CONTEXT_FROM, DEF_PACKAGE);
        final ExtraHelp.Builder extraBuilder = extraHelp.builder(GET_BUNDLE_METHOD, SAVE_INSTANCE_STATE);
        final SubscribeHelp.Builder subscribeBuilder = subscribeHelp.builder();
        final MenuHelp.Builder menuBuilder = menuHelp.builder(CONTEXT_FROM, DEF_PACKAGE);
        final ActivityResultHelp.Builder activityResultBuilder = activityResultHelp.builder(DATA);
        final HandleHelp.Builder handleHelpBuilder = handleHelp.builder(className);
        //======================================================

        final TypeSpec.Builder tb = TypeSpec.classBuilder(className)
                .addAnnotation(AndroidClass.Keep)
                .superclass(getClassName(activityElement.asType()))
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL);

        final MethodSpec.Builder onCreateMethodBuilder = createOnCreateMethodBuilder();
        final MethodSpec.Builder onSaveInstanceStateMethodBuilder = createOnSaveInstanceStateMethodBuilder();
        final MethodSpec.Builder onCreateOptionsMenuBuilder = createOnCreateOptionsMenuMethodBuilder();
        final MethodSpec.Builder onOptionsItemSelectedBuilder = createOnOptionsItemSelectedMethodBuilder();
        final MethodSpec.Builder onStartMethodBuilder = getLifecycleMethod("onStart");
        final MethodSpec.Builder onResumeMethodBuilder = getLifecycleMethod("onResume");
        final MethodSpec.Builder onPauseMethodBuilder = getLifecycleMethod("onPause");
        final MethodSpec.Builder onStopMethodBuilder = getLifecycleMethod("onStop");
        final MethodSpec.Builder onDestroyMethodBuilder = getLifecycleMethod("onDestroy");

        final CodeBlock.Builder findViewByIdCode = CodeBlock.builder();
        final CodeBlock.Builder findFragmentByCode = CodeBlock.builder();
        final CodeBlock.Builder afterAnnotationCode = CodeBlock.builder();
        final CodeBlock.Builder resCode = CodeBlock.builder();
        final CodeBlock.Builder onCreateCode = CodeBlock.builder();
        final CodeBlock.Builder onStartCode = CodeBlock.builder();
        final CodeBlock.Builder onResumeCode = CodeBlock.builder();
        final CodeBlock.Builder onPauseCode = CodeBlock.builder();
        final CodeBlock.Builder onStopCode = CodeBlock.builder();
        final CodeBlock.Builder onDestroyCode = CodeBlock.builder();

        for (TypeElement superActivityElement : activityElements) {
            final List<? extends Element> enclosedElements = superActivityElement.getEnclosedElements();
            for (Element element : enclosedElements) {
                findViewByIdCode.add(createFindViewByIdCode(element));
                findFragmentByCode.add(createFindFragmentByCode(element));
                afterAnnotationCode.add(createAfterAnnotationCode(element));
                resCode.add(resourcesHelp.bindResourcesAnnotation(element, CONTEXT_FROM, DEF_PACKAGE));
                extraBuilder.parseElement(element);
                listenerBuilder.parseElement(element);
                subscribeBuilder.parseElement(element);
                menuBuilder.parseElement(element);
                activityResultBuilder.parseElement(activityElement, element);
                handleHelpBuilder.parseElement(element);
                if (element.getAnnotation(ViewModelOf.class) != null) {
                    subscribeBuilder.parseElement(element);
                }
                if (element.getAnnotation(OnCreate.class) != null) {
                    onCreateCode.add(parseLifecycleMethod(element));
                }
                if (element.getAnnotation(OnStart.class) != null) {
                    onStartCode.add(parseLifecycleMethod(element));
                }
                if (element.getAnnotation(OnResume.class) != null) {
                    onResumeCode.add(parseLifecycleMethod(element));
                }
                if (element.getAnnotation(OnPause.class) != null) {
                    onPauseCode.add(parseLifecycleMethod(element));
                }
                if (element.getAnnotation(OnStop.class) != null) {
                    onStopCode.add(parseLifecycleMethod(element));
                }
                if (element.getAnnotation(OnDestroy.class) != null) {
                    onDestroyCode.add(parseLifecycleMethod(element));
                }
            }
        }
        subscribeBuilder.addDisposableFieldInType(tb);
        //
        final boolean haveDagger = DaggerHelp.process(this, activityElement);
        //OnCreate
        onCreateMethodBuilder.addCode(createSetContentViewCode(tb, activityElement));
        onCreateMethodBuilder.addCode(menuBuilder.parseOptionsMenuInOnCreate(activityElement));
        onCreateMethodBuilder.addCode(extraBuilder.createGetExtra(SAVE_INSTANCE_STATE));
        onCreateMethodBuilder.addCode(resCode.build());
        onCreateMethodBuilder.addCode(findViewByIdCode.build());
        onCreateMethodBuilder.addCode(findFragmentByCode.build());
        onCreateMethodBuilder.addCode(listenerBuilder.createListenerCode());
        onCreateMethodBuilder.addCode(subscribeBuilder.createAddViewModelOfCode(CONTEXT_FROM));
        onCreateMethodBuilder.addCode(subscribeBuilder.createCodeSubscribeInOnCreate(className, CONTEXT_FROM));
        onCreateMethodBuilder.addCode(subscribeBuilder.createAddLifecycle(CONTEXT_FROM));
        onCreateMethodBuilder.addCode(activityResultBuilder.onCreateLauncher(activityElement));
        if (haveDagger) {
            onCreateMethodBuilder.addCode(createDaggerInjectCode(activityElement));
        }
        onCreateMethodBuilder.addCode(afterAnnotationCode.build());
        onCreateMethodBuilder.addCode(onCreateCode.build());
        //
        onSaveInstanceStateMethodBuilder.addCode(extraBuilder.createSaveInstanceState(OUT_STATE));
        //
        //
        onCreateOptionsMenuBuilder.addCode(menuBuilder.createMenuInflaterCode(activityElement, MENU, "getMenuInflater()"));
        onCreateOptionsMenuBuilder.addCode(menuBuilder.createFindMenuItemCode(MENU));
        onCreateOptionsMenuBuilder.addStatement("return super.onCreateOptionsMenu($N)", MENU);
        //
        onOptionsItemSelectedBuilder.addCode(menuBuilder.createOnOptionsItemSelectedCode(ITEM));
        onOptionsItemSelectedBuilder.addStatement("return super.onOptionsItemSelected($N)", ITEM);
        //OnStart
        onStartMethodBuilder.addCode(onStartCode.build());
        //OnResume
        onResumeMethodBuilder.addCode(onResumeCode.build());
        //OnPause
        onPauseMethodBuilder.addCode(onPauseCode.build());
        //OnStop
        onStopMethodBuilder.addCode(onStopCode.build());
        //OnDestroy
        onDestroyMethodBuilder.addCode(subscribeBuilder.createDispose());
        onDestroyMethodBuilder.addCode(activityResultBuilder.createDispose());
        onDestroyMethodBuilder.addCode(onDestroyCode.build());
        //
        tb.addType(extraBuilder.createActivityIntentBuilder(packageName, className));
        //
        activityResultBuilder.createActivityResultLauncherField(tb, activityElement);
        tb.addType(activityResultBuilder.createActivityResult(packageName, className));
        tb.addType(activityResultBuilder.createLauncher(packageName, className));
        //
        tb.addMethod(onCreateMethodBuilder.build());
        tb.addMethod(onSaveInstanceStateMethodBuilder.build());
        tb.addMethod(onCreateOptionsMenuBuilder.build());
        tb.addMethod(onOptionsItemSelectedBuilder.build());
        tb.addMethod(onStartMethodBuilder.build());
        tb.addMethod(onResumeMethodBuilder.build());
        tb.addMethod(onPauseMethodBuilder.build());
        tb.addMethod(onStopMethodBuilder.build());
        tb.addMethod(onDestroyMethodBuilder.build());
        for (MethodSpec methodSpec : handleHelpBuilder.createMotheds()) {
            tb.addMethod(methodSpec);
        }
        writeTo(packageName, tb.build());
    }

    private MethodSpec.Builder createOnCreateMethodBuilder() {
        return MethodSpec.methodBuilder("onCreate")
                .addModifiers(Modifier.PROTECTED)
                .addParameter(AndroidClass.Bundle, SAVE_INSTANCE_STATE)
                .addAnnotation(Override.class)
                .returns(void.class)
                .addStatement("super.onCreate($N)", SAVE_INSTANCE_STATE);
    }

    private MethodSpec.Builder createOnSaveInstanceStateMethodBuilder() {
        return MethodSpec.methodBuilder("onSaveInstanceState")
                .addModifiers(Modifier.PROTECTED)
                .addParameter(AndroidClass.Bundle, OUT_STATE)
                .addAnnotation(Override.class)
                .returns(void.class)
                .addStatement("super.onSaveInstanceState($N)", OUT_STATE);
    }

    private MethodSpec.Builder createOnCreateOptionsMenuMethodBuilder() {
        return MethodSpec.methodBuilder("onCreateOptionsMenu")
                .addModifiers(Modifier.PUBLIC)
                .addParameter(AndroidClass.Menu, MENU)
                .addAnnotation(Override.class)
                .returns(boolean.class);
    }

    private MethodSpec.Builder createOnOptionsItemSelectedMethodBuilder() {
        return MethodSpec.methodBuilder("onOptionsItemSelected")
                .addModifiers(Modifier.PUBLIC)
                .addParameter(AndroidClass.MenuItem, ITEM)
                .addAnnotation(Override.class)
                .returns(boolean.class);
    }

    private CodeBlock parseLifecycleMethod(Element element) {
        return CodeBlock.builder()
                .addStatement("$N()", element.getSimpleName())
                .build();
    }

    private MethodSpec.Builder getLifecycleMethod(String name) {
        return MethodSpec.methodBuilder(name)
                .addModifiers(Modifier.PROTECTED)
                .addAnnotation(Override.class)
                .returns(void.class)
                .addStatement("super.$N()", name);
    }

    private CodeBlock createSetContentViewCode(TypeSpec.Builder typeSpecBuilder, TypeElement activityElement) {
        final ClassName viewBinding = ViewBindingHelp.getViewBindingClass(activityElement, NActivity.class);
        if (viewBinding != null) {
            return CodeBlock.builder()
                    .addStatement("final $T $N = $T.inflate(getLayoutInflater())", viewBinding, BINDING, viewBinding)
                    .addStatement("setContentView($N.getRoot())", BINDING)
                    .build();
        }
        final AnnotationMirror aNActivityMirror = AnnotationHelp.findAnnotationMirror(activityElement, NActivity.class);
        final String resName = (String) AnnotationHelp.findAnnotationValue(aNActivityMirror, "resName");
        if (resName == null) {
            return CodeBlock.builder().build();
        }
        final String resPackage = (String) AnnotationHelp.findAnnotationValue(aNActivityMirror, "resPackage");
        return CodeBlock.builder()
                .add("setContentView(")
                .add(AndroidResHelp.layout(resName, resPackage, activityElement.getSimpleName(), CONTEXT_FROM, DEF_PACKAGE))
                .addStatement(")")
                .build();
    }

    private CodeBlock createFindViewByIdCode(Element viewByIdElement) {
        final ViewById aViewById = viewByIdElement.getAnnotation(ViewById.class);
        if (aViewById == null) {
            return CodeBlock.builder().build();
        }
        String resName = aViewById.value();
        if (resName.isEmpty()) {
            resName = aViewById.resName();
        }
        return CodeBlock.builder()
                .add("$N = findViewById(", viewByIdElement.getSimpleName())
                .add(AndroidResHelp.id(resName, aViewById.resPackage(), viewByIdElement.getSimpleName(), CONTEXT_FROM, DEF_PACKAGE))
                .addStatement(")")
                .build();
    }

    private CodeBlock createFindFragmentByCode(Element element) {
        final FragmentBy aFragmentBy = element.getAnnotation(FragmentBy.class);
        if (aFragmentBy == null) {
            return CodeBlock.builder().build();
        }
        if (!aFragmentBy.tag().isEmpty()) {
            return CodeBlock.builder()
                    .addStatement("$N = ($T)$N.getSupportFragmentManager().findFragmentByTag($S)", element.getSimpleName(), element.asType(), CONTEXT_FROM, aFragmentBy.tag())
                    .build();
        } else {
            String resName = aFragmentBy.value();
            if (resName.isEmpty()) {
                resName = aFragmentBy.resName();
            }
            return CodeBlock.builder()
                    .add("$N = ($T)$N", element.getSimpleName(), element.asType(), CONTEXT_FROM)
                    .add(".getSupportFragmentManager()")
                    .add(".findFragmentById(")
                    .add(AndroidResHelp.id(resName, aFragmentBy.resPackage(), element.getSimpleName(), CONTEXT_FROM, DEF_PACKAGE))
                    .addStatement(")")
                    .build();
        }
    }

    private CodeBlock createAfterAnnotationCode(Element afterAnnotationElement) {
        final AfterInject aAfterInject = afterAnnotationElement.getAnnotation(AfterInject.class);
        if (aAfterInject == null) {
            return CodeBlock.builder().build();
        }
        final ExecutableElement method = (ExecutableElement) afterAnnotationElement;
        final StringBuffer stringBuffer = new StringBuffer();
        final Iterator<? extends VariableElement> iterator = method.getParameters().iterator();
        while (iterator.hasNext()) {
            final VariableElement parameter = iterator.next();
            if (isInstanceOf(parameter.asType(), AndroidClass.Bundle)) {
                stringBuffer.append(SAVE_INSTANCE_STATE);
            } else {
                stringBuffer.append(addNullCode(parameter.asType()));
            }
            if (iterator.hasNext()) {
                stringBuffer.append(',');
            }
        }
        return CodeBlock.builder()
                .addStatement("$N($N)", afterAnnotationElement.getSimpleName(), stringBuffer.toString())
                .build();
    }

    private CodeBlock createDaggerInjectCode(TypeElement activityElement) {
        final CodeBlock.Builder code = CodeBlock.builder();
        final boolean isSubActivity = isSubActivity(activityElement);
        if (isSubActivity) {
            code.addStatement("Dagger_$N.builder().$N(new $T(this)).build().inject(this)", activityElement.getSimpleName(), toModelCase(AndroidClass.CONTEXT_MODULE_NAME), mainProcessor.contextModule);
        } else {
            code.addStatement("Dagger_$N.create().inject(this)", activityElement.getSimpleName());
        }
        return code.build();
    }
}
