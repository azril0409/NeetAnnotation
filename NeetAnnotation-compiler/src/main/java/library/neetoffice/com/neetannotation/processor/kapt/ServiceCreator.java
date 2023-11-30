package library.neetoffice.com.neetannotation.processor.kapt;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeMirror;

import library.neetoffice.com.neetannotation.AfterInject;
import library.neetoffice.com.neetannotation.DefaultBoolean;
import library.neetoffice.com.neetannotation.DefaultByte;
import library.neetoffice.com.neetannotation.DefaultChar;
import library.neetoffice.com.neetannotation.DefaultDouble;
import library.neetoffice.com.neetannotation.DefaultFloat;
import library.neetoffice.com.neetannotation.DefaultInt;
import library.neetoffice.com.neetannotation.DefaultLong;
import library.neetoffice.com.neetannotation.DefaultShort;
import library.neetoffice.com.neetannotation.NService;
import library.neetoffice.com.neetannotation.StartAction;
import library.neetoffice.com.neetannotation.processor.AndroidClass;

public class ServiceCreator extends BaseCreator {
    private static final String RETURN_VALUE = "returnValue";
    private static final String INTENT = "intent";
    private static final String CONTEXT = "context";
    private static final String BUNDLE = "bundle";
    private static final String INTENT_BUILDER = "IntentBuilder";

    private final ExtraHelp extraHelp;
    private final MainProcessor mainProcessor;

    public ServiceCreator(MainProcessor processor, ProcessingEnvironment processingEnv) {
        super(processor, processingEnv);
        mainProcessor = processor;
        extraHelp = new ExtraHelp(this);
    }

    @Override
    void process(TypeElement serviceElement, RoundEnvironment roundEnv) {
        boolean isAabstract = serviceElement.getModifiers().contains(Modifier.ABSTRACT);
        if (isAabstract) {
            return;
        }
        if (!isSubService(serviceElement)) {
            return;
        }
        final String packageName = getPackageName(serviceElement);
        final String className = serviceElement.getSimpleName() + "_";
        final List<TypeElement> serviceElements = findSuperElements(serviceElement, roundEnv, NService.class);
        serviceElements.add(serviceElement);
        final List<VariableElement> extras = new ArrayList<>();
        //======================================================
        final TypeSpec.Builder tb = TypeSpec.classBuilder(className)
                .addAnnotation(AndroidClass.Keep)
                .superclass(getClassName(serviceElement.asType()))
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL);

        final MethodSpec.Builder onCreateMethodBuilder = createOnCreateMethodBuilder();
        final MethodSpec.Builder onStartCommandMethodBuilder = createOnStartCommandMethodBuilder();
        final TypeSpec.Builder intentBuilder = createIntentBuilder(className);

        final boolean haveDagger = DaggerHelp.process(this, serviceElement);
        final CodeBlock.Builder afterAnnotationCode = CodeBlock.builder();

        for (TypeElement superActivityElement : serviceElements) {
            final List<? extends Element> enclosedElements = superActivityElement.getEnclosedElements();
            for (Element element : enclosedElements) {
                afterAnnotationCode.add(createAfterAnnotationCode(element));
                onStartCommandMethodBuilder.addCode(createStartActionCode(element));
                if (element.getAnnotation(StartAction.class) != null) {
                    final List<? extends VariableElement> parameters = ((ExecutableElement) element).getParameters();
                    for (VariableElement parameter : parameters) {
                        if (parameter.getAnnotation(StartAction.Extra.class) != null) {
                            extras.add(parameter);
                        }
                    }
                }
            }
        }
        if (haveDagger) {
            onCreateMethodBuilder.addCode(createDaggerInjectCode(serviceElement));
        }
        onCreateMethodBuilder.addCode(afterAnnotationCode.build());
        //
        onStartCommandMethodBuilder.addStatement("return $N", RETURN_VALUE);
        //
        for (VariableElement extra : extras) {
            intentBuilder.addMethod(createPutExtraMethod(extra, ClassName.get(packageName, className, INTENT_BUILDER)));
        }
        intentBuilder.addMethod(createNewTaskMethod(ClassName.get(packageName, className, INTENT_BUILDER)));
        intentBuilder.addMethod(createAddFlagsMethod(ClassName.get(packageName, className, INTENT_BUILDER)));
        intentBuilder.addMethod(createBuildMethod());
        intentBuilder.addMethod(createStartServiceMethod());
        //
        tb.addMethod(onCreateMethodBuilder.build());
        tb.addMethod(onStartCommandMethodBuilder.build());
        tb.addType(intentBuilder.build());
        writeTo(packageName, tb.build());
    }

    MethodSpec.Builder createOnCreateMethodBuilder() {
        return MethodSpec.methodBuilder("onCreate")
                .addModifiers(Modifier.PUBLIC)
                .addAnnotation(Override.class)
                .returns(void.class)
                .addStatement("super.onCreate()");
    }


    CodeBlock createAfterAnnotationCode(Element afterAnnotationElement) {
        final AfterInject aAfterInject = afterAnnotationElement.getAnnotation(AfterInject.class);
        if (aAfterInject == null) {
            return CodeBlock.builder().build();
        }
        return CodeBlock.builder()
                .addStatement("$N()", afterAnnotationElement.getSimpleName())
                .build();
    }

    MethodSpec.Builder createOnStartCommandMethodBuilder() {
        final String FLAGS = "flags";
        final String START_ID = "startId";
        return MethodSpec.methodBuilder("onStartCommand")
                .addModifiers(Modifier.PUBLIC)
                .addParameter(AndroidClass.Intent, INTENT)
                .addParameter(int.class, FLAGS)
                .addParameter(int.class, START_ID)
                .addAnnotation(Override.class)
                .returns(int.class)
                .addStatement("$T $N = super.onStartCommand($N, $N, $N)", int.class, RETURN_VALUE, INTENT, FLAGS, START_ID);
    }

    CodeBlock createStartActionCode(Element element) {
        final CodeBlock.Builder code = CodeBlock.builder();
        final StartAction aStartAction = element.getAnnotation(StartAction.class);
        if (aStartAction == null) {
            return code.build();
        }
        final ExecutableElement method = (ExecutableElement) element;
        final CodeBlock.Builder parameterCode = CodeBlock.builder();
        final Iterator<? extends VariableElement> iterator = method.getParameters().iterator();
        while (iterator.hasNext()) {
            final VariableElement parameter = iterator.next();
            final TypeMirror parameterType = parameter.asType();
            final StartAction.Extra aExtra = parameter.getAnnotation(StartAction.Extra.class);
            if (aExtra == null) {
                if (isInstanceOf(parameterType, AndroidClass.Intent)) {
                    parameterCode.add(INTENT);
                } else if (isInstanceOf(parameterType, AndroidClass.Bundle)) {
                    parameterCode.add("$N.getExtras()", INTENT);
                } else if (isInstanceOf(parameterType, AndroidClass.Uri)) {
                    parameterCode.add("$N.getData()", INTENT);
                } else {
                    parameterCode.add(addNullCode(parameterType));
                }
            } else {
                final String key = getKey(parameter);
                if ("char".equals(parameterType.toString())) {
                    final DefaultChar aDefaultChar = parameter.getAnnotation(DefaultChar.class);
                    if (aDefaultChar != null) {
                        parameterCode.add("$N.getCharExtra($S,(char)$N)", INTENT, key, aDefaultChar.value());
                    } else {
                        parameterCode.add("$N.getCharExtra($S,(char) 0)", INTENT, key);
                    }
                } else if ("byte".equals(parameterType.toString())) {
                    final DefaultByte aDefaultByte = parameter.getAnnotation(DefaultByte.class);
                    if (aDefaultByte != null) {
                        parameterCode.add("$N.getByteExtra($S,(byte)$N)", INTENT, key, aDefaultByte.value());
                    } else {
                        parameterCode.add("$N.getByteExtra($S,(byte) 0)", INTENT, key);
                    }
                } else if ("short".equals(parameterType.toString())) {
                    final DefaultShort aDefaultShort = parameter.getAnnotation(DefaultShort.class);
                    if (aDefaultShort != null) {
                        parameterCode.add("$N.getShortExtra($S,(short)$N)", INTENT, key, aDefaultShort.value());
                    } else {
                        parameterCode.add("$N.getShortExtra($S,(short) 0)", INTENT, key);
                    }
                } else if ("int".equals(parameterType.toString())) {
                    final DefaultInt aDefaultInt = parameter.getAnnotation(DefaultInt.class);
                    if (aDefaultInt != null) {
                        parameterCode.add("$N.getIntExtra($S,(int)$N)", INTENT, key, aDefaultInt.value());
                    } else {
                        parameterCode.add("$N.getIntExtra($S,(int) 0)", INTENT, key);
                    }
                } else if ("long".equals(parameterType.toString())) {
                    final DefaultLong aDefaultLong = parameter.getAnnotation(DefaultLong.class);
                    if (aDefaultLong != null) {
                        parameterCode.add("$N.getLongExtra($S,(long)$N)", INTENT, key, aDefaultLong.value());
                    } else {
                        parameterCode.add("$N.getLongExtra($S,(long) 0)", INTENT, key);
                    }
                } else if ("float".equals(parameterType.toString())) {
                    final DefaultFloat aDefaultFloat = parameter.getAnnotation(DefaultFloat.class);
                    if (aDefaultFloat != null) {
                        parameterCode.add("$N.getFloatExtra($S,(float)$N)", INTENT, key, aDefaultFloat.value());
                    } else {
                        parameterCode.add("$N.getFloatExtra($S,(float) 0)", INTENT, key);
                    }
                } else if ("double".equals(parameterType.toString())) {
                    final DefaultDouble aDefaultDouble = parameter.getAnnotation(DefaultDouble.class);
                    if (aDefaultDouble != null) {
                        parameterCode.add("$N.getDoubleExtra($S,(double)$N)", INTENT, key, aDefaultDouble.value());
                    } else {
                        parameterCode.add("$N.getDoubleExtra($S,(double) 0)", INTENT, key);
                    }
                } else if ("boolean".equals(parameterType.toString())) {
                    final DefaultBoolean aDefaultBoolean = parameter.getAnnotation(DefaultBoolean.class);
                    if (aDefaultBoolean != null) {
                        parameterCode.add("$N.getBooleanExtra($S,(boolean)$L)", INTENT, key, aDefaultBoolean.value());
                    } else {
                        parameterCode.add("$N.getBooleanExtra($S,false)", INTENT, key);
                    }
                } else {
                    parameterCode.add("($T)$N.getExtras().get($S)", parameter.asType(), INTENT, key);
                }
            }
            if (iterator.hasNext()) {
                parameterCode.add(",");
            }
        }
        final String action = aStartAction.value();
        final int returnValue = aStartAction.returnValue();
        if (!action.isEmpty()) {
            code.beginControlFlow("if($S.equals($N.getAction()))", action, INTENT);
        }
        code.addStatement("$N = $N|" + returnValue, RETURN_VALUE, RETURN_VALUE);
        code.add("$N(", element.getSimpleName());
        code.add(parameterCode.build());
        code.addStatement(")");
        if (!action.isEmpty()) {
            code.endControlFlow();
        }
        return code.build();
    }

    private String getKey(VariableElement parameter) {
        final StartAction.Extra aExtra = parameter.getAnnotation(StartAction.Extra.class);
        if (aExtra.value().isEmpty()) {
            return parameter.getSimpleName().toString();
        }
        return aExtra.value();
    }


    CodeBlock createDaggerInjectCode(TypeElement serviceElement) {
        final CodeBlock.Builder code = CodeBlock.builder();
        final boolean isSubService = isSubService(serviceElement);
        if (isSubService) {
            code.addStatement("Dagger_$N.builder().$N(new $T(this)).build().inject(this)", serviceElement.getSimpleName(), toModelCase(AndroidClass.CONTEXT_MODULE_NAME), mainProcessor.contextModule);
        } else {
            code.addStatement("Dagger_$N.create().inject(this)", serviceElement.getSimpleName());
        }
        return code.build();
    }

    TypeSpec.Builder createIntentBuilder(String className) {
        final TypeSpec.Builder intentBuilder = TypeSpec.classBuilder(INTENT_BUILDER)
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC);
        intentBuilder.addField(FieldSpec.builder(AndroidClass.Context, CONTEXT)
                .addModifiers(Modifier.PRIVATE, Modifier.FINAL)
                .build());
        intentBuilder.addField(FieldSpec.builder(AndroidClass.Intent, INTENT)
                .addModifiers(Modifier.PRIVATE, Modifier.FINAL)
                .initializer("new $T()", AndroidClass.Intent)
                .build());
        intentBuilder.addField(FieldSpec.builder(AndroidClass.Bundle, BUNDLE)
                .addModifiers(Modifier.PRIVATE, Modifier.FINAL)
                .initializer("new $T()", AndroidClass.Bundle)
                .build());
        intentBuilder.addMethod(MethodSpec.constructorBuilder()
                .addModifiers(Modifier.PUBLIC)
                .addParameter(AndroidClass.Context, CONTEXT)
                .addStatement("this.$N = $N", CONTEXT, CONTEXT)
                .addStatement("intent.setClass($N, $N.class)", CONTEXT, className)
                .build());
        return intentBuilder;
    }

    MethodSpec createPutExtraMethod(VariableElement extra, TypeName returnType) {
        final MethodSpec.Builder mb = MethodSpec.methodBuilder(extra.getSimpleName().toString())
                .addModifiers(Modifier.PUBLIC)
                .addParameter(getClassName(extra.asType()), extra.getSimpleName().toString())
                .returns(returnType);
        mb.addCode(extraHelp.builder(BUNDLE, BUNDLE).createPutExtraCode(extra, BUNDLE, getExtraKey(extra), extra.getSimpleName().toString()));
        mb.addStatement("return this");
        return mb.build();
    }

    private String getExtraKey(Element element) {
        final StartAction.Extra aExtra = element.getAnnotation(StartAction.Extra.class);
        final String key;
        if (aExtra.value().isEmpty()) {
            key = element.getSimpleName().toString();
        } else {
            key = aExtra.value();
        }
        return key;
    }

    MethodSpec createAddFlagsMethod(TypeName returnType) {
        return MethodSpec.methodBuilder("addFlags")
                .addModifiers(Modifier.PUBLIC)
                .addParameter(int.class, "flags")
                .returns(returnType)
                .addStatement("$N.addFlags($N)", INTENT, "flags")
                .addStatement("return this")
                .build();
    }

    MethodSpec createNewTaskMethod(TypeName returnType) {
        return MethodSpec.methodBuilder("newTask")
                .addModifiers(Modifier.PUBLIC)
                .returns(returnType)
                .addStatement("$N.addFlags($T.FLAG_ACTIVITY_CLEAR_TOP | $T.FLAG_ACTIVITY_CLEAR_TASK | $T.FLAG_ACTIVITY_NEW_TASK)", INTENT, AndroidClass.Intent, AndroidClass.Intent, AndroidClass.Intent)
                .addStatement("return this")
                .build();
    }

    MethodSpec createBuildMethod() {
        return MethodSpec.methodBuilder("build")
                .addModifiers(Modifier.PUBLIC)
                .returns(AndroidClass.Intent)
                .addStatement("intent.putExtras(bundle)")
                .addStatement("return intent")
                .build();
    }

    private MethodSpec createStartServiceMethod() {
        return MethodSpec.methodBuilder("startService")
                .addModifiers(Modifier.PUBLIC)
                .returns(void.class)
                .addStatement("$N.startService(build())", CONTEXT)
                .build();
    }

}
