package library.neetoffice.com.neetannotation.processor;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeSpec;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.swing.text.html.parser.Entity;

import library.neetoffice.com.neetannotation.AfterInject;
import library.neetoffice.com.neetannotation.FragmentBy;
import library.neetoffice.com.neetannotation.NActivity;
import library.neetoffice.com.neetannotation.ViewById;

public class ActivityCreator extends BaseCreator {
    static final String CONTEXT_FROM = "this";
    static final String DEF_PACKAGE = "getApplicationContext().getPackageName()";
    static final String SAVE_INSTANCE_STATE = "savedInstanceState";
    static final String OUT_STATE = "outState";
    static final String GET_BUNDEL_METHOD = "getIntent().getExtras()";
    static final String ITEM = "item";
    static final String MENU = "menu";
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
        boolean isAabstract = activityElement.getModifiers().contains(Modifier.ABSTRACT);
        if (isAabstract) {
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
        final ExtraHelp.Builder extraBuilder = extraHelp.builder(GET_BUNDEL_METHOD, SAVE_INSTANCE_STATE);
        final SubscribeHelp.Builder subscribeBuilder = subscribeHelp.builder();
        final MenuHelp.Builder menuBuilder = menuHelp.builder(CONTEXT_FROM, DEF_PACKAGE);
        final ActivityResultHelp.Builder activityResultBuilder = activityResultHelp.builder(REQUEST_CODE, RESULT_CODE, DATA);
        final HandleHelp.Builder handleHelpBuilder = handleHelp.builder(className);
        //======================================================

        final TypeSpec.Builder tb = TypeSpec.classBuilder(className)
                .addAnnotation(AndroidClass.Keep)
                .superclass(getClassName(activityElement.asType()))
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL);
        //private Set<ViewModel> set = new HashSet<ViewModel>();

        final MethodSpec.Builder onCreateMethodBuilder = createOnCreateMethodBuilder();
        final MethodSpec.Builder onSaveInstanceStateMethodBuilder = createOnSaveInstanceStateMethodBuilder();
        final MethodSpec.Builder onActivityResultMethodBuilder = createOnActivityResult();
        final MethodSpec.Builder onCreateOptionsMenuBuilder = createOnCreateOptionsMenuMethodBuilder();
        final MethodSpec.Builder onOptionsItemSelectedBuilder = createOnOptionsItemSelectedMethodBuilder();
        final MethodSpec.Builder onDestroyMethodBuilder = createOnDestroy();

        final CodeBlock.Builder findViewByIdCode = CodeBlock.builder();
        final CodeBlock.Builder findFragmentByCode = CodeBlock.builder();
        final CodeBlock.Builder afterAnnotationCode = CodeBlock.builder();
        final CodeBlock.Builder resCode = CodeBlock.builder();

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
                activityResultBuilder.parseElement(element);
                handleHelpBuilder.parseElement(element);
            }
        }
        subscribeBuilder.addDisposableFieldInType(tb);
        //
        final boolean haveDagger = DaggerHelp.process(activityElement);
        //
        onCreateMethodBuilder.addCode(createSetContentViewCode(activityElement));
        onCreateMethodBuilder.addCode(menuBuilder.parseOptionsMenuInOnCreate(activityElement));
        onCreateMethodBuilder.addCode(extraBuilder.createGetExtra(SAVE_INSTANCE_STATE));
        onCreateMethodBuilder.addCode(resCode.build());
        onCreateMethodBuilder.addCode(findViewByIdCode.build());
        onCreateMethodBuilder.addCode(findFragmentByCode.build());
        onCreateMethodBuilder.addCode(listenerBuilder.createListenerCode());
        onCreateMethodBuilder.addCode(subscribeBuilder.createAddViewModelOfCode(CONTEXT_FROM));
        onCreateMethodBuilder.addCode(subscribeBuilder.createCodeSubscribeInOnCreate(className, CONTEXT_FROM));
        onCreateMethodBuilder.addCode(subscribeBuilder.createAddLifecycle(CONTEXT_FROM));
        if (haveDagger) {
            onCreateMethodBuilder.addCode(createDaggerInjectCode(activityElement));
        }
        onCreateMethodBuilder.addCode(afterAnnotationCode.build());
        //
        onSaveInstanceStateMethodBuilder.addCode(extraBuilder.createSaveInstanceState(OUT_STATE));
        //
        onActivityResultMethodBuilder.addCode(activityResultBuilder.createOnActivityResultCode());
        //
        onCreateOptionsMenuBuilder.addCode(menuBuilder.createMenuInflaterCode(activityElement, MENU, "getMenuInflater()"));
        onCreateOptionsMenuBuilder.addCode(menuBuilder.createFindMenuItemCode(MENU));
        onCreateOptionsMenuBuilder.addStatement("return super.onCreateOptionsMenu($N)", MENU);
        //
        onOptionsItemSelectedBuilder.addCode(menuBuilder.createOnOptionsItemSelectedCode(ITEM));
        onOptionsItemSelectedBuilder.addStatement("return super.onOptionsItemSelected($N)", ITEM);
        //
        onDestroyMethodBuilder.addStatement("super.onDestroy()");
        onDestroyMethodBuilder.addCode(subscribeBuilder.createDispose());
        //
        //tb.addField(FieldSpec.builder(ParameterizedTypeName.get(ClassName.get(HashSet.class), AndroidClass.AndroidViewModel),"viewmodes",Modifier.PRIVATE,Modifier.FINAL).initializer("new $T()",ParameterizedTypeName.get(ClassName.get(HashSet.class), AndroidClass.AndroidViewModel)).build());
        tb.addType(extraBuilder.createActivityIntentBuilder(packageName, className));
        tb.addType(activityResultBuilder.createActivityIntentBuilder(packageName, className));
        tb.addMethod(onCreateMethodBuilder.build());
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
                .addModifiers(Modifier.PROTECTED)
                .addParameter(AndroidClass.Bundle, SAVE_INSTANCE_STATE)
                .addAnnotation(Override.class)
                .returns(void.class)
                .addStatement("super.onCreate($N)", SAVE_INSTANCE_STATE);
    }

    MethodSpec.Builder createOnSaveInstanceStateMethodBuilder() {
        return MethodSpec.methodBuilder("onSaveInstanceState")
                .addModifiers(Modifier.PROTECTED)
                .addParameter(AndroidClass.Bundle, OUT_STATE)
                .addAnnotation(Override.class)
                .returns(void.class)
                .addStatement("super.onSaveInstanceState($N)", OUT_STATE);
    }

    MethodSpec.Builder createOnActivityResult() {
        return MethodSpec.methodBuilder("onActivityResult")
                .addModifiers(Modifier.PROTECTED)
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
                .addAnnotation(Override.class)
                .returns(boolean.class);
    }

    MethodSpec.Builder createOnOptionsItemSelectedMethodBuilder() {
        return MethodSpec.methodBuilder("onOptionsItemSelected")
                .addModifiers(Modifier.PUBLIC)
                .addParameter(AndroidClass.MenuItem, ITEM)
                .addAnnotation(Override.class)
                .returns(boolean.class);
    }

    MethodSpec.Builder createOnDestroy() {
        return MethodSpec.methodBuilder("onDestroy")
                .addModifiers(Modifier.PROTECTED)
                .addAnnotation(Override.class)
                .returns(void.class);
    }

    CodeBlock createSetContentViewCode(TypeElement activityElement) {
        final NActivity aNActivity = activityElement.getAnnotation(NActivity.class);
        if (aNActivity.value() == 0 && aNActivity.resName().isEmpty()) {
            return CodeBlock.builder().build();
        }
        return CodeBlock.builder()
                .add("setContentView(")
                .add(AndroidResHelp.layout(aNActivity.value(), aNActivity.resName(), activityElement.getSimpleName().toString().toLowerCase(), CONTEXT_FROM, DEF_PACKAGE))
                .addStatement(")")
                .build();
    }

    CodeBlock createFindViewByIdCode(Element viewByIdElement) {
        final ViewById aViewById = viewByIdElement.getAnnotation(ViewById.class);
        if (aViewById == null) {
            return CodeBlock.builder().build();
        }
        return CodeBlock.builder()
                .add("$N = findViewById(", viewByIdElement.getSimpleName())
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
                    .add("$N = ($T)$N.getSupportFragmentManager().findFragmentById(", element.getSimpleName(), element.asType(), CONTEXT_FROM)
                    .add(AndroidResHelp.id(aFragmentBy.id(), aFragmentBy.resName(), element.getSimpleName().toString(), CONTEXT_FROM, DEF_PACKAGE))
                    .addStatement(")")
                    .build();
        } else if (!aFragmentBy.tag().isEmpty()) {
            return CodeBlock.builder()
                    .addStatement("$N = ($T)$N.getSupportFragmentManager().findFragmentByTag($N)", element.getSimpleName(), element.asType(), CONTEXT_FROM, aFragmentBy.tag())
                    .build();
        }
        return CodeBlock.builder()
                .add("$N = ($T)$N.getSupportFragmentManager().findFragmentById(", element.getSimpleName(), element.asType(), CONTEXT_FROM)
                .add(AndroidResHelp.id(aFragmentBy.id(), aFragmentBy.resName(), element.getSimpleName().toString(), CONTEXT_FROM, DEF_PACKAGE))
                .addStatement(")")
                .build();
    }

    CodeBlock createAfterAnnotationCode(Element afterAnnotationElement) {
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

    CodeBlock createDaggerInjectCode(TypeElement activityElement) {
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
