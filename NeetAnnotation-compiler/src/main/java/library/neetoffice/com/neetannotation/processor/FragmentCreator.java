package library.neetoffice.com.neetannotation.processor;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;

import java.util.Iterator;
import java.util.List;

import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;

import library.neetoffice.com.neetannotation.AfterAnnotation;
import library.neetoffice.com.neetannotation.FragmentBy;
import library.neetoffice.com.neetannotation.NFragment;
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
    private final SecondProcessor mainProcessor;

    public FragmentCreator(SecondProcessor processor, ProcessingEnvironment processingEnv) {
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
        boolean isAabstract = fragmentElement.getModifiers().contains(Modifier.ABSTRACT);
        if (isAabstract) {
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
        final ExtraHelp.Builder extraBuilder = extraHelp.builder(GET_BUNDEL_METHOD,SAVE_INSTANCE_STATE);
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
        final MethodSpec.Builder onCreateViewMethodBuilder = createOnCreateViewMethodBuilder(fragmentElement);
        final MethodSpec.Builder onViewCreatedMethodBuilder = createOnViewCreatedMethodBuilder();
        final MethodSpec.Builder onSaveInstanceStateMethodBuilder = createOnSaveInstanceStateMethodBuilder();
        final MethodSpec.Builder onActivityResultMethodBuilder = createOnActivityResult();
        final MethodSpec.Builder onCreateOptionsMenuBuilder = createOnCreateOptionsMenuMethodBuilder();
        final MethodSpec.Builder onOptionsItemSelectedBuilder = createOnOptionsItemSelectedMethodBuilder();
        final MethodSpec.Builder onDestroyMethodBuilder = createOnDestroy();

        final CodeBlock.Builder findViewByIdCode = CodeBlock.builder();
        final CodeBlock.Builder findFragmentByCode = CodeBlock.builder();
        final CodeBlock.Builder afterAnnotationCode = CodeBlock.builder();
        final CodeBlock.Builder resCode = CodeBlock.builder();

        for (TypeElement superFragmentElement : fragmentElements) {
            final List<? extends Element> enclosedElements = superFragmentElement.getEnclosedElements();
            for (Element element : enclosedElements) {
                findViewByIdCode.add(createFindViewByIdCode(element));
                findFragmentByCode.add(createFindFragmentByCode(element));
                afterAnnotationCode.add(createAfterAnnotationCode(element));
                resCode.add(resourcesHelp.bindResourcesAnnotation(element, CONTEXT_FROM, DEF_PACKAGE));
                extraBuilder.parseElement(element);
                listenerBuilder.parseElement(element);
                subscribeBuilder.parseElement(element);
                menuBuilder.parseElement(element);
                activityResultBuilder.parseElement(element);
                handleHelpBuilder.parseElement(element);
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
        onDestroyMethodBuilder.addCode(subscribeBuilder.createDispose());
        onDestroyMethodBuilder.addStatement("super.onDestroy()");
        //
        tb.addType(extraBuilder.createArgument(packageName, className));
        tb.addMethod(onCreateMethodBuilder.build());
        tb.addMethod(onActivityCreateMethodBuilder.build());
        tb.addMethod(onCreateViewMethodBuilder.build());
        tb.addMethod(onViewCreatedMethodBuilder.build());
        tb.addMethod(onSaveInstanceStateMethodBuilder.build());
        tb.addMethod(onActivityResultMethodBuilder.build());
        tb.addMethod(onCreateOptionsMenuBuilder.build());
        tb.addMethod(onOptionsItemSelectedBuilder.build());
        tb.addMethod(onDestroyMethodBuilder.build());
        for (MethodSpec methodSpec : handleHelpBuilder.createMotheds()) {
            tb.addMethod(methodSpec);
        }
        writeTo(packageName, tb.build());
    }

    MethodSpec.Builder createOnCreateMethodBuilder() {
        return MethodSpec.methodBuilder("onCreate")
                .addModifiers(Modifier.PUBLIC)
                .addParameter(AndroidClass.Bundle, SAVE_INSTANCE_STATE)
                .addAnnotation(Override.class)
                .returns(void.class)
                .addStatement("super.onCreate($N)", SAVE_INSTANCE_STATE);
    }

    MethodSpec.Builder createOnActivityCreateMethodBuilder() {
        return MethodSpec.methodBuilder("onActivityCreated")
                .addModifiers(Modifier.PUBLIC)
                .addParameter(AndroidClass.Bundle, SAVE_INSTANCE_STATE)
                .addAnnotation(Override.class)
                .returns(void.class)
                .addStatement("super.onActivityCreated($N)", SAVE_INSTANCE_STATE);
    }

    MethodSpec.Builder createOnCreateViewMethodBuilder(TypeElement fragmentElement) {
        final NFragment aNFragment = fragmentElement.getAnnotation(NFragment.class);
        MethodSpec.Builder mb = MethodSpec.methodBuilder("onCreateView")
                .addModifiers(Modifier.PUBLIC)
                .addParameter(AndroidClass.LayoutInflater, INFLATER)
                .addParameter(AndroidClass.ViewGroup, CONTAINER)
                .addParameter(AndroidClass.Bundle, SAVE_INSTANCE_STATE)
                .addAnnotation(Override.class)
                .returns(AndroidClass.View);
        if (aNFragment.value() == 0 && aNFragment.resName().isEmpty()) {
            return mb.addStatement("return super.onCreateView($N, $N, $N)", INFLATER, CONTAINER, SAVE_INSTANCE_STATE);
        }
        return mb.addCode("return $N.inflate(", INFLATER)
                .addCode(AndroidResHelp.layout(aNFragment.value(), aNFragment.resName(), fragmentElement.getSimpleName().toString(), CONTEXT_FROM, DEF_PACKAGE))
                .addStatement(", $N, false)", CONTAINER);
    }

    MethodSpec.Builder createOnViewCreatedMethodBuilder() {
        return MethodSpec.methodBuilder("onViewCreated")
                .addModifiers(Modifier.PUBLIC)
                .addParameter(AndroidClass.View, VIEW)
                .addParameter(AndroidClass.Bundle, SAVE_INSTANCE_STATE)
                .addAnnotation(Override.class)
                .returns(void.class)
                .addStatement(" super.onViewCreated($N, $N)", VIEW, SAVE_INSTANCE_STATE);
    }

    MethodSpec.Builder createOnSaveInstanceStateMethodBuilder() {
        return MethodSpec.methodBuilder("onSaveInstanceState")
                .addModifiers(Modifier.PUBLIC)
                .addParameter(AndroidClass.Bundle, OUT_STATE)
                .addAnnotation(Override.class)
                .returns(void.class)
                .addStatement("super.onSaveInstanceState($N)", OUT_STATE);
    }

    MethodSpec.Builder createOnActivityResult() {
        return MethodSpec.methodBuilder("onActivityResult")
                .addModifiers(Modifier.PUBLIC)
                .addParameter(int.class, REQUEST_CODE)
                .addParameter(int.class, RESULT_CODE)
                .addParameter(AndroidClass.Intent, DATA)
                .addAnnotation(Override.class)
                .returns(void.class)
                .addStatement("super.onActivityResult($N, $N, $N)", REQUEST_CODE, RESULT_CODE, DATA);
    }

    MethodSpec.Builder createOnCreateOptionsMenuMethodBuilder() {
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

    private MethodSpec.Builder createOnDestroy() {
        return MethodSpec.methodBuilder("onDestroy")
                .addModifiers(Modifier.PUBLIC)
                .addAnnotation(Override.class)
                .returns(void.class);
    }


    CodeBlock createFindViewByIdCode(Element viewByIdElement) {
        final ViewById aViewById = viewByIdElement.getAnnotation(ViewById.class);
        if (aViewById == null) {
            return CodeBlock.builder().build();
        }
        return CodeBlock.builder()
                .add("$N = $N.findViewById(", viewByIdElement.getSimpleName(), VIEW)
                .add(AndroidResHelp.id(aViewById.value(), aViewById.resName(), viewByIdElement.getSimpleName().toString(), CONTEXT_FROM, DEF_PACKAGE))
                .addStatement(")")
                .build();
    }

    CodeBlock createFindFragmentByCode(Element element) {
        final FragmentBy aFragmentBy = element.getAnnotation(FragmentBy.class);
        if (aFragmentBy == null) {
            return CodeBlock.builder().build();
        }
        if (aFragmentBy.id() != 0) {
            return CodeBlock.builder()
                    .add("$N = ($T)$N.getChildFragmentManager().findFragmentById(", element.getSimpleName(), element.asType(), CONTEXT_FROM)
                    .add(AndroidResHelp.id(aFragmentBy.id(), aFragmentBy.resName(), element.getSimpleName().toString(), CONTEXT_FROM, DEF_PACKAGE))
                    .addStatement(")")
                    .build();
        } else if (!aFragmentBy.tag().isEmpty()) {
            return CodeBlock.builder()
                    .addStatement("$N = ($T)$N.getChildFragmentManager().findFragmentByTag($N)", element.getSimpleName(), element.asType(), CONTEXT_FROM, aFragmentBy.tag())
                    .build();
        }
        return CodeBlock.builder()
                .add("$N = ($T)$N.getChildFragmentManager().findFragmentById(", element.getSimpleName(), element.asType(), CONTEXT_FROM)
                .add(AndroidResHelp.id(aFragmentBy.id(), aFragmentBy.resName(), element.getSimpleName().toString(), CONTEXT_FROM, DEF_PACKAGE))
                .addStatement(")")
                .build();
    }


    CodeBlock createAfterAnnotationCode(Element afterAnnotationElement) {
        final AfterAnnotation aAfterAnnotation = afterAnnotationElement.getAnnotation(AfterAnnotation.class);
        if (aAfterAnnotation == null) {
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


    CodeBlock createDaggerInjectCode(TypeElement fragmentElement) {
        final CodeBlock.Builder code = CodeBlock.builder();
        if (isSubFragment(fragmentElement)) {
            code.addStatement("Dagger_$N.builder().$N(new $T(this)).build().inject(this)", fragmentElement.getSimpleName(), toModelCase(AndroidClass.CONTEXT_MODULE_NAME), mainProcessor.contextModule);
        } else {
            code.addStatement("Dagger_$N.create().inject(this)", fragmentElement.getSimpleName());
        }
        return code.build();
    }
}
