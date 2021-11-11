package library.neetoffice.com.neetannotation.processor;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;

import java.util.HashSet;
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
import library.neetoffice.com.neetannotation.NFragment;
import library.neetoffice.com.neetannotation.OnCreate;
import library.neetoffice.com.neetannotation.OnDestroy;
import library.neetoffice.com.neetannotation.OnPause;
import library.neetoffice.com.neetannotation.OnResume;
import library.neetoffice.com.neetannotation.OnStart;
import library.neetoffice.com.neetannotation.OnStop;
import library.neetoffice.com.neetannotation.ViewById;

public class FragmentCreator extends BaseCreator {
    static final String CONTEXT_FROM = "this";
    static final String ACTIVITY = "this.getActivity()";
    static final String DEF_PACKAGE = "getActivity().getApplicationContext().getPackageName()";
    static final String GET_BUNDEL_METHOD = "getArguments()";
    static final String INFLATER = "inflater";
    static final String CONTAINER = "container";
    static final String SAVE_INSTANCE_STATE = "savedInstanceState";
    static final String OUT_STATE = "outState";
    static final String VIEW = "view";
    static final String MENU = "menu";
    static final String ITEM = "item";
    static final String REQUEST_CODE = "requestCode";
    static final String RESULT_CODE = "resultCode";
    static final String DATA = "data";

    final ResourcesHelp resourcesHelp;
    final ListenerHelp listenerHelp;
    final ExtraHelp extraHelp;
    final SubscribeHelp subscribeHelp;
    final MenuHelp menuHelp;
    final ActivityResultHelp activityResultHelp;
    final HandleHelp handleHelp;
    private final MainProcessor mainProcessor;

    public FragmentCreator(MainProcessor processor, ProcessingEnvironment processingEnv) {
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
    void process(TypeElement fragmentElement, RoundEnvironment roundEnv) {
        boolean isAbstract = fragmentElement.getModifiers().contains(Modifier.ABSTRACT);
        if (isAbstract) {
            return;
        }
        if (!isSubFragment(fragmentElement)) {
            return;
        }
        final String packageName = getPackageName(fragmentElement);
        final String className = fragmentElement.getSimpleName() + "_";
        final List<TypeElement> fragmentElements = findSuperElements(fragmentElement, roundEnv, NFragment.class);
        fragmentElements.add(fragmentElement);
        //======================================================
        final ListenerHelp.Builder listenerBuilder = listenerHelp.builder(className, VIEW, CONTEXT_FROM, DEF_PACKAGE);
        final ExtraHelp.Builder extraBuilder = extraHelp.builder(GET_BUNDEL_METHOD, SAVE_INSTANCE_STATE);
        final SubscribeHelp.Builder subscribeBuilder = subscribeHelp.builder();
        final MenuHelp.Builder menuBuilder = menuHelp.builder(CONTEXT_FROM, DEF_PACKAGE);
        final ActivityResultHelp.Builder activityResultBuilder = activityResultHelp.builder(REQUEST_CODE, RESULT_CODE, DATA);
        final HandleHelp.Builder handleHelpBuilder = handleHelp.builder(className);
        //======================================================

        final TypeSpec.Builder tb = TypeSpec.classBuilder(className)
                .addAnnotation(AndroidClass.Keep)
                .superclass(getClassName(fragmentElement.asType()))
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL);

        final MethodSpec.Builder onCreateMethodBuilder = createOnCreateMethodBuilder();
        final MethodSpec.Builder onActivityCreateMethodBuilder = createOnActivityCreateMethodBuilder();
        final MethodSpec.Builder onCreateViewMethodBuilder = createOnCreateViewMethodBuilder(tb, fragmentElement);
        final MethodSpec.Builder onViewCreatedMethodBuilder = createOnViewCreatedMethodBuilder();
        final MethodSpec.Builder onSaveInstanceStateMethodBuilder = createOnSaveInstanceStateMethodBuilder();
        final MethodSpec.Builder onActivityResultMethodBuilder = createOnActivityResult();
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

        for (TypeElement superFragmentElement : fragmentElements) {
            final List<? extends Element> enclosedElements = superFragmentElement.getEnclosedElements();
            for (Element element : enclosedElements) {
                findViewByIdCode.add(createFindViewByIdCode(element));
                findFragmentByCode.add(createFindFragmentByCode(element));
                afterAnnotationCode.add(createAfterAnnotationCode(element));
                resCode.add(resourcesHelp.bindResourcesAnnotation(element, ACTIVITY, DEF_PACKAGE));
                extraBuilder.parseElement(element);
                listenerBuilder.parseElement(element);
                subscribeBuilder.parseElement(element);
                menuBuilder.parseElement(element);
                activityResultBuilder.parseElement(element);
                handleHelpBuilder.parseElement(element);
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
        final boolean haveDagger = DaggerHelp.process(fragmentElement);
        //
        onCreateMethodBuilder.addCode(menuBuilder.parseOptionsMenuInOnCreate(fragmentElement));
        onCreateMethodBuilder.addCode(extraBuilder.createGetExtra(SAVE_INSTANCE_STATE));
        onCreateMethodBuilder.addCode(resCode.build());
        //
        onViewCreatedMethodBuilder.addCode(findViewByIdCode.build());
        onViewCreatedMethodBuilder.addCode(findFragmentByCode.build());
        onViewCreatedMethodBuilder.addCode(listenerBuilder.createListenerCode());
        //
        onActivityCreateMethodBuilder.addCode(subscribeBuilder.createAddViewModelOfCode(ACTIVITY));
        onActivityCreateMethodBuilder.addCode(subscribeBuilder.createCodeSubscribeInOnCreate(className, ACTIVITY));
        onActivityCreateMethodBuilder.addCode(subscribeBuilder.createAddLifecycle(ACTIVITY));
        if (haveDagger) {
            onActivityCreateMethodBuilder.addCode(createDaggerInjectCode(fragmentElement));
        }
        onActivityCreateMethodBuilder.addCode(afterAnnotationCode.build());
        onActivityCreateMethodBuilder.addCode(onCreateCode.build());
        //
        onSaveInstanceStateMethodBuilder.addCode(extraBuilder.createSaveInstanceState(OUT_STATE));
        //
        onActivityResultMethodBuilder.addCode(activityResultBuilder.createOnActivityResultCode());
        //
        onCreateOptionsMenuBuilder.addCode(menuBuilder.createMenuInflaterCode(fragmentElement, MENU, INFLATER));
        onCreateOptionsMenuBuilder.addCode(menuBuilder.createFindMenuItemCode(MENU));
        //
        onOptionsItemSelectedBuilder.addCode(menuBuilder.createOnOptionsItemSelectedCode(ITEM));
        onOptionsItemSelectedBuilder.addStatement("return super.onOptionsItemSelected($N)", ITEM);
        //
        onStartMethodBuilder.addCode(onStartCode.build());
        //
        onResumeMethodBuilder.addCode(onResumeCode.build());
        //
        onPauseMethodBuilder.addCode(onPauseCode.build());
        //
        onStopMethodBuilder.addCode(onStopCode.build());
        //
        onDestroyMethodBuilder.addCode(subscribeBuilder.createDispose());
        onDestroyMethodBuilder.addCode(onDestroyCode.build());
        //
        //tb.addField(FieldSpec.builder(ParameterizedTypeName.get(ClassName.get(HashSet.class), AndroidClass.AndroidViewModel),"viewmodes",Modifier.PRIVATE,Modifier.FINAL).initializer("new $T()",ParameterizedTypeName.get(ClassName.get(HashSet.class), AndroidClass.AndroidViewModel)).build());
        tb.addType(extraBuilder.createArgument(packageName, className));
        tb.addType(activityResultBuilder.createActivityIntentBuilder(packageName, className));
        tb.addMethod(onCreateMethodBuilder.build());
        tb.addMethod(onActivityCreateMethodBuilder.build());
        tb.addMethod(onCreateViewMethodBuilder.build());
        tb.addMethod(onViewCreatedMethodBuilder.build());
        tb.addMethod(onSaveInstanceStateMethodBuilder.build());
        tb.addMethod(onActivityResultMethodBuilder.build());
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
                .addModifiers(Modifier.PUBLIC)
                .addParameter(AndroidClass.Bundle, SAVE_INSTANCE_STATE)
                .addAnnotation(Override.class)
                .returns(void.class)
                .addStatement("super.onCreate($N)", SAVE_INSTANCE_STATE);
    }

    private MethodSpec.Builder createOnActivityCreateMethodBuilder() {
        return MethodSpec.methodBuilder("onActivityCreated")
                .addModifiers(Modifier.PUBLIC)
                .addParameter(AndroidClass.Bundle, SAVE_INSTANCE_STATE)
                .addAnnotation(Override.class)
                .returns(void.class)
                .addStatement("super.onActivityCreated($N)", SAVE_INSTANCE_STATE);
    }

    private MethodSpec.Builder createOnCreateViewMethodBuilder(TypeSpec.Builder typeSpecBuilder, TypeElement fragmentElement) {
        final MethodSpec.Builder mb = MethodSpec.methodBuilder("onCreateView")
                .addModifiers(Modifier.PUBLIC)
                .addParameter(AndroidClass.LayoutInflater, INFLATER)
                .addParameter(AndroidClass.ViewGroup, CONTAINER)
                .addParameter(AndroidClass.Bundle, SAVE_INSTANCE_STATE)
                .addAnnotation(Override.class)
                .returns(AndroidClass.View);

        final AnnotationMirror aNFragmentMirror = AnnotationHelp.findAnnotationMirror(fragmentElement, NFragment.class);
        Object viewBindingObject = AnnotationHelp.findAnnotationValue(aNFragmentMirror, "value");
        if (viewBindingObject == null) {
            viewBindingObject = AnnotationHelp.findAnnotationValue(aNFragmentMirror, "viewBinding");
        }
        if (viewBindingObject != null) {
            final String viewBindingString = viewBindingObject.toString();
            final String[] split = viewBindingString.split("\\.");
            if (split.length > 1) {
                final String className = split[split.length - 1];
                final String packageName = viewBindingString.substring(0, viewBindingString.length() - className.length() - 1);
                if ("databinding".equals(split[split.length - 2]) && className.endsWith("Binding")) {
                    final ClassName viewBinding = ClassName.get(packageName, className);
                    typeSpecBuilder.addField(FieldSpec.builder(viewBinding, "binding")
                            .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                            .build());
                    return mb.addStatement("binding = $T.inflate($N, $N, false)", viewBinding, INFLATER, CONTAINER)
                            .addStatement("return binding.getRoot()");
                }
            }
        }
        final String resName = (String) AnnotationHelp.findAnnotationValue(aNFragmentMirror, "resName");
        if (resName == null) {
            return mb.addStatement("return super.onCreateView($N, $N, $N)", INFLATER, CONTAINER, SAVE_INSTANCE_STATE);
        }
        final String resPackage = (String) AnnotationHelp.findAnnotationValue(aNFragmentMirror, "resPackage");
        return mb.addCode("return $N.inflate(", INFLATER)
                .addCode(AndroidResHelp.layout(resName,resPackage, fragmentElement.getSimpleName(), CONTEXT_FROM, DEF_PACKAGE))
                .addStatement(", $N, false)", CONTAINER);
    }

    private MethodSpec.Builder createOnViewCreatedMethodBuilder() {
        return MethodSpec.methodBuilder("onViewCreated")
                .addModifiers(Modifier.PUBLIC)
                .addParameter(AndroidClass.View, VIEW)
                .addParameter(AndroidClass.Bundle, SAVE_INSTANCE_STATE)
                .addAnnotation(Override.class)
                .returns(void.class)
                .addStatement(" super.onViewCreated($N, $N)", VIEW, SAVE_INSTANCE_STATE);
    }

    private MethodSpec.Builder createOnSaveInstanceStateMethodBuilder() {
        return MethodSpec.methodBuilder("onSaveInstanceState")
                .addModifiers(Modifier.PUBLIC)
                .addParameter(AndroidClass.Bundle, OUT_STATE)
                .addAnnotation(Override.class)
                .returns(void.class)
                .addStatement("super.onSaveInstanceState($N)", OUT_STATE);
    }

    private MethodSpec.Builder createOnActivityResult() {
        return MethodSpec.methodBuilder("onActivityResult")
                .addModifiers(Modifier.PUBLIC)
                .addParameter(int.class, REQUEST_CODE)
                .addParameter(int.class, RESULT_CODE)
                .addParameter(AndroidClass.Intent, DATA)
                .addAnnotation(Override.class)
                .returns(void.class)
                .addStatement("super.onActivityResult($N, $N, $N)", REQUEST_CODE, RESULT_CODE, DATA);
    }

    private MethodSpec.Builder createOnCreateOptionsMenuMethodBuilder() {
        return MethodSpec.methodBuilder("onCreateOptionsMenu")
                .addModifiers(Modifier.PUBLIC)
                .addParameter(AndroidClass.Menu, MENU)
                .addParameter(AndroidClass.MenuInflater, INFLATER)
                .addAnnotation(Override.class)
                .returns(void.class)
                .addStatement("super.onCreateOptionsMenu($N, $N)", MENU, INFLATER);
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
                .addModifiers(Modifier.PUBLIC)
                .addAnnotation(Override.class)
                .returns(void.class)
                .addStatement("super.$N()", name);
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
                .add("$N = $N.findViewById(", viewByIdElement.getSimpleName(), VIEW)
                .add(AndroidResHelp.id(resName, aViewById.resPackage(), viewByIdElement.getSimpleName(), CONTEXT_FROM, DEF_PACKAGE))
                .addStatement(")")
                .build();
    }

    private CodeBlock createFindFragmentByCode(Element element) {
        final FragmentBy aFragmentBy = element.getAnnotation(FragmentBy.class);
        if (aFragmentBy == null) {
            return CodeBlock.builder().build();
        }
        String resName = aFragmentBy.value();
        if (resName.isEmpty()) {
            resName = aFragmentBy.resName();
        }
        if (!resName.isEmpty()) {
            return CodeBlock.builder()
                    .add("$N = ($T)$N", element.getSimpleName(), element.asType(), CONTEXT_FROM)
                    .add(".getChildFragmentManager()")
                    .add(".findFragmentById(")
                    .add(AndroidResHelp.id(resName, aFragmentBy.resPackage(), element.getSimpleName(), CONTEXT_FROM, DEF_PACKAGE))
                    .addStatement(")")
                    .build();
        } else if (!aFragmentBy.tag().isEmpty()) {
            return CodeBlock.builder()
                    .addStatement("$N = ($T)$N.getChildFragmentManager().findFragmentByTag($N)", element.getSimpleName(), element.asType(), CONTEXT_FROM, aFragmentBy.tag())
                    .build();
        }
        return CodeBlock.builder().build();
    }

    private CodeBlock createAfterAnnotationCode(Element afterAnnotationElement) {
        final AfterInject aAfterInject = afterAnnotationElement.getAnnotation(AfterInject.class);
        if (aAfterInject == null) {
            return CodeBlock.builder().build();
        }
        final ExecutableElement mothod = (ExecutableElement) afterAnnotationElement;
        final StringBuffer stringBuffer = new StringBuffer();
        final Iterator<? extends VariableElement> iterator = mothod.getParameters().iterator();
        while (iterator.hasNext()) {
            final VariableElement parameter = iterator.next();
            final TypeName parameterType = ClassName.get(parameter.asType());
            if (AndroidClass.Bundle.equals(parameterType)) {
                stringBuffer.append(SAVE_INSTANCE_STATE);
            } else {
                stringBuffer.append(addNullCode(parameterType));
            }
            if (iterator.hasNext()) {
                stringBuffer.append(',');
            }
        }
        return CodeBlock.builder()
                .addStatement("$N($N)", afterAnnotationElement.getSimpleName(), stringBuffer.toString())
                .build();
    }

    private CodeBlock createDaggerInjectCode(TypeElement fragmentElement) {
        final CodeBlock.Builder code = CodeBlock.builder();
        if (isSubFragment(fragmentElement)) {
            code.addStatement("Dagger_$N.builder().$N(new $T(this)).build().inject(this)", fragmentElement.getSimpleName(), toModelCase(AndroidClass.CONTEXT_MODULE_NAME), mainProcessor.contextModule);
        } else {
            code.addStatement("Dagger_$N.create().inject(this)", fragmentElement.getSimpleName());
        }
        return code.build();
    }
}
