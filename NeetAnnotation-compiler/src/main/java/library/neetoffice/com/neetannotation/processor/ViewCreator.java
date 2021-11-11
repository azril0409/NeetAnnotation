package library.neetoffice.com.neetannotation.processor;

import com.squareup.javapoet.AnnotationSpec;
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
import library.neetoffice.com.neetannotation.NActivity;
import library.neetoffice.com.neetannotation.NView;
import library.neetoffice.com.neetannotation.ViewById;

public class ViewCreator extends BaseCreator {
    private static final String CONTEXT = "context";
    private static final String THIS = "this";
    private static final String DEF_PACKAGE = "context.getApplicationContext().getPackageName()";
    private static final String INIT_METHOD = "_init";

    final ResourcesHelp resourcesHelp;
    final ListenerHelp listenerHelp;
    final SubscribeHelp subscribeHelp;
    private final MainProcessor mainProcessor;

    public ViewCreator(MainProcessor processor, ProcessingEnvironment processingEnv) {
        super(processor, processingEnv);
        mainProcessor = processor;
        resourcesHelp = new ResourcesHelp();
        listenerHelp = new ListenerHelp(this);
        subscribeHelp = new SubscribeHelp(this);
    }

    @Override
    void process(TypeElement viewElement, RoundEnvironment roundEnv) {
        boolean isAbstract = viewElement.getModifiers().contains(Modifier.ABSTRACT);
        if (isAbstract) {
            return;
        }
        if (!isInstanceOf(viewElement.asType(), AndroidClass.View)) {
            return;
        }
        final String packageName = getPackageName(viewElement);
        final String className = viewElement.getSimpleName() + "_";
        final List<TypeElement> viewElements = findSuperElements(viewElement, roundEnv, NView.class);
        viewElements.add(viewElement);
        //======================================================
        final ListenerHelp.Builder listenerBuilder = listenerHelp.builder(className, THIS, CONTEXT, DEF_PACKAGE);
        final SubscribeHelp.Builder subscribeBuilder = subscribeHelp.builder();
        //======================================================

        final TypeSpec.Builder tb = TypeSpec.classBuilder(className)
                .addAnnotation(AndroidClass.Keep)
                .superclass(getClassName(viewElement.asType()))
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL);

        tb.addMethod(MethodSpec.constructorBuilder()
                .addModifiers(Modifier.PUBLIC)
                .addParameter(AndroidClass.Context, CONTEXT)
                .addStatement("super($N)", CONTEXT)
                .addStatement("$N($N)", INIT_METHOD, CONTEXT)
                .build());

        tb.addMethod(MethodSpec.constructorBuilder()
                .addModifiers(Modifier.PUBLIC)
                .addParameter(AndroidClass.Context, CONTEXT)
                .addParameter(AndroidClass.AttributeSet, "attrs")
                .addStatement("super($N, attrs)", CONTEXT)
                .addStatement("$N($N)", INIT_METHOD, CONTEXT)
                .build());

        tb.addMethod(MethodSpec.constructorBuilder()
                .addModifiers(Modifier.PUBLIC)
                .addParameter(AndroidClass.Context, CONTEXT)
                .addParameter(AndroidClass.AttributeSet, "attrs")
                .addParameter(int.class, "defStyleAttr")
                .addStatement("super($N, attrs, defStyleAttr)", CONTEXT)
                .addStatement("$N($N)", INIT_METHOD, CONTEXT)
                .build());

        tb.addMethod(MethodSpec.constructorBuilder()
                .addModifiers(Modifier.PUBLIC)
                .addParameter(AndroidClass.Context, CONTEXT)
                .addParameter(AndroidClass.AttributeSet, "attrs")
                .addParameter(int.class, "defStyleAttr")
                .addParameter(int.class, "defStyleRes")
                .addAnnotation(AnnotationSpec.builder(AndroidClass.RequiresApi)
                        .addMember("api", "21")
                        .build())
                .addStatement("super($N, attrs, defStyleAttr, defStyleRes)", CONTEXT)
                .addStatement("$N($N)", INIT_METHOD, CONTEXT)
                .build());
        //
        MethodSpec.Builder initMethodBuilder = MethodSpec.methodBuilder(INIT_METHOD)
                .addModifiers(Modifier.PRIVATE, Modifier.FINAL)
                .addParameter(AndroidClass.Context, CONTEXT);

        final CodeBlock.Builder findViewByIdCode = CodeBlock.builder();
        final CodeBlock.Builder afterAnnotationCode = CodeBlock.builder();
        final CodeBlock.Builder resCode = CodeBlock.builder();

        for (TypeElement superViewElement : viewElements) {
            final List<? extends Element> enclosedElements = superViewElement.getEnclosedElements();
            for (Element element : enclosedElements) {
                findViewByIdCode.add(createFindViewByIdCode(element));
                afterAnnotationCode.add(createAfterAnnotationCode(element));
                resCode.add(resourcesHelp.bindResourcesAnnotation(element, CONTEXT, DEF_PACKAGE));
                listenerBuilder.parseElement(element);
                subscribeBuilder.parseElement(element);
            }
        }
        //
        final boolean haveDagger = DaggerHelp.process(viewElement);
        //
        initMethodBuilder.addCode(inflateLayout(viewElement));
        initMethodBuilder.addCode(resCode.build());
        initMethodBuilder.addCode(findViewByIdCode.build());
        initMethodBuilder.addCode(listenerBuilder.createListenerCode());
        if (subscribeBuilder.hasElement()) {
            initMethodBuilder.addCode(subscribeBuilder.createAddViewModelOfCode(CONTEXT));
        }
        if (haveDagger) {
            initMethodBuilder.addCode(createDaggerInjectCode(viewElement));
        }
        initMethodBuilder.addCode(afterAnnotationCode.build());
        //
        tb.addMethod(initMethodBuilder.build());
        writeTo(packageName, tb.build());
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
                .add(AndroidResHelp.id(resName, aViewById.resPackage(), viewByIdElement.getSimpleName(), CONTEXT, DEF_PACKAGE))
                .addStatement(")")
                .build();
    }

    private CodeBlock createAfterAnnotationCode(Element afterAnnotationElement) {
        final AfterInject aAfterInject = afterAnnotationElement.getAnnotation(AfterInject.class);
        if (aAfterInject == null) {
            return CodeBlock.builder().build();
        }
        final ExecutableElement mothod = (ExecutableElement) afterAnnotationElement;
        final CodeBlock.Builder parameters = CodeBlock.builder();
        final Iterator<? extends VariableElement> iterator = mothod.getParameters().iterator();
        while (iterator.hasNext()) {
            final VariableElement parameter = iterator.next();
            if (isInstanceOf(parameter.asType(), AndroidClass.Context)) {
                parameters.add(CONTEXT);
            } else {
                parameters.add(addNullCode(parameter.asType()));
            }
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


    private CodeBlock createDaggerInjectCode(TypeElement viewElement) {
        final CodeBlock.Builder code = CodeBlock.builder();
        if (isInstanceOf(viewElement.asType(), AndroidClass.View)) {
            code.addStatement("Dagger_$N.builder().$N(new $T($N)).build().inject(this)", viewElement.getSimpleName(), toModelCase(AndroidClass.CONTEXT_MODULE_NAME), mainProcessor.contextModule, CONTEXT);
        } else {
            code.addStatement("Dagger_$N.create().inject(this)", viewElement.getSimpleName());
        }
        return code.build();
    }

    private CodeBlock inflateLayout(TypeElement viewElement) {
        final AnnotationMirror aNViewMirror = AnnotationHelp.findAnnotationMirror(viewElement, NView.class);
        Object viewBindingObject = AnnotationHelp.findAnnotationValue(aNViewMirror, "value");
        if (viewBindingObject == null) {
            viewBindingObject = AnnotationHelp.findAnnotationValue(aNViewMirror, "viewBinding");
        }
        if (viewBindingObject != null) {
            final String viewBindingString = viewBindingObject.toString();
            final String[] split = viewBindingString.split("\\.");
            if (split.length > 1) {
                final String className = split[split.length - 1];
                final String packageName = viewBindingString.substring(0, viewBindingString.length() - className.length() - 1);
                if ("databinding".equals(split[split.length - 2]) && className.endsWith("Binding")) {
                    final ClassName viewBinding = ClassName.get(packageName, className);
                    return CodeBlock.builder()
                            .addStatement("final $T factory = $T.from($N)", AndroidClass.LayoutInflater, AndroidClass.LayoutInflater, CONTEXT)
                            .addStatement("final $T binding = $T.inflate(factory, this, true)", viewBinding, viewBinding)
                            .build();
                }
            }
        }
        final String resName = (String) AnnotationHelp.findAnnotationValue(aNViewMirror, "resName");
        if (resName == null) {
            return CodeBlock.builder().build();
        }
        final String resPackage = (String) AnnotationHelp.findAnnotationValue(aNViewMirror, "resPackage");
        return CodeBlock.builder()
                .beginControlFlow("if(this instanceof $T)", AndroidClass.ViewGroup)
                .add("ViewGroup.inflate($N,", CONTEXT)
                .add(AndroidResHelp.layout(resName, resPackage, viewElement.getSimpleName(), CONTEXT, DEF_PACKAGE))
                .addStatement(",($T)this)", AndroidClass.ViewGroup)
                .endControlFlow()
                .build();
    }

}
