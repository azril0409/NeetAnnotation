package library.neetoffice.com.neetannotation.processor;

import com.google.auto.service.AutoService;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeSpec;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.ExecutableType;
import javax.lang.model.type.TypeMirror;

import library.neetoffice.com.neetannotation.AfterAnnotation;
import library.neetoffice.com.neetannotation.NActivity;
import library.neetoffice.com.neetannotation.ViewById;

@SupportedAnnotationTypes({
        "library.neetoffice.com.neetannotation.NActivity"})
@AutoService(Process.class)
public class ActivityProcessor extends AbstractProcessor {
    private static final String CONTEXT_FROM = "this";
    private static final String DEF_PACKAGE = "getApplicationContext().getPackageName()";
    private static final String SAVE_INSTANCE_STATE = "savedInstanceState";
    private ProcessorUtil processorUtil;
    private DaggerHelp daggerHelp;
    private ResourcesHelp resourcesHelp;
    private ListenerHelp listenerHelp;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnvironment) {
        super.init(processingEnvironment);
        processorUtil = new ProcessorUtil(processingEnvironment);
        daggerHelp = new DaggerHelp(processorUtil);
        resourcesHelp = new ResourcesHelp(processorUtil);
        listenerHelp = new ListenerHelp(processorUtil);
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.RELEASE_8;
    }

    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnv) {
        final HashMap<String, TypeSpec.Builder> typeMap = new HashMap<>();
        final Set<? extends Element> elements = roundEnv.getElementsAnnotatedWith(NActivity.class);
        for (Element mainElement : elements) {
            final SubscribeHelp subscribeHelp = new SubscribeHelp(processorUtil);

            final TypeElement typeMainElement = (TypeElement) mainElement;
            final String className = mainElement.getSimpleName() + "_";

            final ListenerHelp.Builder listenerBuilder = listenerHelp.builder(className, CONTEXT_FROM, CONTEXT_FROM, DEF_PACKAGE);

            final TypeSpec.Builder tb = TypeSpec.classBuilder(className)
                    .superclass(ParameterizedTypeName.get(typeMainElement.asType()))
                    .addModifiers(Modifier.PUBLIC, Modifier.FINAL);
            typeMap.put(className, tb);

            final boolean haveDagger = daggerHelp.process(typeMainElement);

            final MethodSpec.Builder onCreateMethodBuilder = MethodSpec.methodBuilder("onCreate")
                    .addModifiers(Modifier.PROTECTED)
                    .addParameter(AndroidClass.Bundle, SAVE_INSTANCE_STATE)
                    .addAnnotation(Override.class)
                    .returns(void.class)
                    .addStatement("super.onCreate($N)", SAVE_INSTANCE_STATE);

            final MethodSpec.Builder onDestroy = MethodSpec.methodBuilder("onDestroy")
                    .addModifiers(Modifier.PROTECTED)
                    .addAnnotation(Override.class)
                    .returns(void.class);

            addCodeSetContentView(onCreateMethodBuilder, typeMainElement);
            if (haveDagger) {
                addCodeDaggerInject(onCreateMethodBuilder, mainElement.getSimpleName().toString());
            }
            final List<? extends Element> enclosedElements = mainElement.getEnclosedElements();
            final List<Element> afterAnnotationElements = new ArrayList<>();
            for (Element element : enclosedElements) {
                if (element.getAnnotation(AfterAnnotation.class) != null) {
                    afterAnnotationElements.add(element);
                }
                addFindViewByIdCode(onCreateMethodBuilder, element);
                resourcesHelp.bindResourcesAnnotation(onCreateMethodBuilder, element, CONTEXT_FROM, DEF_PACKAGE);
                listenerBuilder.parseElement(element);
                subscribeHelp.parseElement(element);
            }
            subscribeHelp.addDisposableFieldInType(tb);
            onCreateMethodBuilder.addCode(listenerBuilder.createListenerCode());
            onCreateMethodBuilder.addCode(subscribeHelp.createAddViewModelOfCode(CONTEXT_FROM));
            onCreateMethodBuilder.addCode(subscribeHelp.createCodeSubscribeInOnCreate(className, CONTEXT_FROM));
            for (Element element : afterAnnotationElements) {
                addAfterAnnotationCode(onCreateMethodBuilder, element);
            }
            subscribeHelp.addStatementSubscribeInOnDestroy(onDestroy);
            onDestroy.addStatement("super.onDestroy()");
            tb.addMethod(onCreateMethodBuilder.build());
            tb.addMethod(onDestroy.build());

            final JavaFile javaFile = JavaFile.builder(processorUtil.getPackageName(typeMainElement), tb.build()).build();
            processorUtil.writeTo(javaFile);
        }
        return true;
    }

    private void addCodeSetContentView(MethodSpec.Builder inOnCreate, TypeElement activity) {
        final NActivity aNActivity = activity.getAnnotation(NActivity.class);
        inOnCreate.addCode(CodeBlock.builder()
                .add("setContentView(")
                .add(AndroidResHelp.layout(aNActivity.value(), aNActivity.resName(), activity.getSimpleName().toString().toLowerCase(), "this", DEF_PACKAGE))
                .addStatement(")")
                .build());
    }

    private void addCodeDaggerInject(MethodSpec.Builder inOnCreate, String elementName) {
        inOnCreate.addCode(CodeBlock.builder()
                .add("Dagger_$N.builder()", elementName)
                .add(".$N(new $T(getApplication()))", AndroidClass.CONTEXT_MODULE_NAME.toLowerCase().charAt(0) + AndroidClass.CONTEXT_MODULE_NAME.substring(1), AndroidClass.CONTEXT_MODULE)
                .add(".build()")
                .addStatement(".inject(this)")
                .build());
    }

    private void addFindViewByIdCode(MethodSpec.Builder inOnCreate, Element viewByIdElement) {
        final ViewById aViewById = viewByIdElement.getAnnotation(ViewById.class);
        if (aViewById == null) {
            return;
        }
        inOnCreate.addCode(CodeBlock.builder()
                .add("$N = findViewById(", viewByIdElement.getSimpleName())
                .add(AndroidResHelp.id(aViewById.value(), aViewById.resName(), viewByIdElement.getSimpleName().toString(), CONTEXT_FROM, DEF_PACKAGE))
                .addStatement(")")
                .build());
    }

    private void addAfterAnnotationCode(MethodSpec.Builder InOnCreate, Element afterAnnotationElement) {
        if (afterAnnotationElement.getAnnotation(AfterAnnotation.class) == null) {
            return;
        }
        final StringBuffer stringBuffer = new StringBuffer();
        final ExecutableType method = (ExecutableType) afterAnnotationElement.asType();
        final Iterator<? extends TypeMirror> iterator = method.getParameterTypes().iterator();
        while (iterator.hasNext()) {
            final TypeMirror parameterType = iterator.next();
            if ("android.os.Bundle".equals(parameterType.toString())) {
                stringBuffer.append(SAVE_INSTANCE_STATE);
            } else {
                stringBuffer.append("null");
            }
            if (iterator.hasNext()) {
                stringBuffer.append(',');
            }
        }
        InOnCreate.addStatement("$N($N)", afterAnnotationElement.getSimpleName(), stringBuffer.toString());
    }
}
