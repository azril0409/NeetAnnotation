package library.neetoffice.com.neetannotation.processor;

import com.squareup.javapoet.AnnotationSpec;
import com.squareup.javapoet.ArrayTypeName;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;

import org.checkerframework.checker.units.qual.C;
import org.checkerframework.checker.units.qual.K;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeMirror;
import javax.tools.Diagnostic;

import library.neetoffice.com.neetannotation.ActivityResult;
import library.neetoffice.com.neetannotation.DefaultBoolean;
import library.neetoffice.com.neetannotation.DefaultByte;
import library.neetoffice.com.neetannotation.DefaultChar;
import library.neetoffice.com.neetannotation.DefaultDouble;
import library.neetoffice.com.neetannotation.DefaultFloat;
import library.neetoffice.com.neetannotation.DefaultInt;
import library.neetoffice.com.neetannotation.DefaultLong;
import library.neetoffice.com.neetannotation.DefaultShort;
import library.neetoffice.com.neetannotation.Extra;
import library.neetoffice.com.neetannotation.NamedAs;

/***/
public class ActivityResultHelp {
    private final static String ACTIVITY = "activity";
    private final BaseCreator creator;
    private final ExtraHelp extraHelp;

    /**
     * @param creator BaseCreator
     */
    public ActivityResultHelp(BaseCreator creator) {
        this.creator = creator;
        this.extraHelp = new ExtraHelp(creator);
    }

    /**
     * @param intentDataName String
     * @return ActivityResultHelp.Builder
     */
    public Builder builder(String intentDataName) {
        return new Builder(creator, extraHelp.builder("", ""), intentDataName);
    }

    /***/
    public static class Builder {
        private final BaseCreator creator;
        private final ExtraHelp.Builder extraHelp;
        private final String intentDataName;
        private final HashMap<String, ArrayList<Element>> elements = new HashMap<>();

        /**
         * @param creator        BaseCreator
         * @param extraHelp      ExtraHelp
         * @param intentDataName String
         */
        public Builder(BaseCreator creator, ExtraHelp.Builder extraHelp, String intentDataName) {
            this.creator = creator;
            this.extraHelp = extraHelp;
            this.intentDataName = intentDataName;
        }

        void parseElement(Element typeElement, Element element) {
            final ActivityResult aActivityResult = element.getAnnotation(ActivityResult.class);
            if (aActivityResult != null) {
                final String[] keys;
                NamedAs namedAs = element.getAnnotation(NamedAs.class);
                if (namedAs != null) {
                    keys = namedAs.value();
                } else {
                    keys = new String[1];
                    final String key = element.getSimpleName().toString();
                    keys[0] = key;
                }
                for (String key : keys) {
                    final ArrayList<Element> list = elements.getOrDefault(key, new ArrayList<>());
                    if (!list.isEmpty()) {
                        final Element e = list.get(0);
                        final ActivityResult a = e.getAnnotation(ActivityResult.class);
                        if (a.value() != aActivityResult.value()) {
                            creator.processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR, "ActivityResult's value contract from " + typeElement.toString() + "." + element.getSimpleName() + " must be same.");
                        }
                    }
                    list.add(element);
                    elements.put(key, list);
                }
            }
        }

        @Deprecated
        final CodeBlock createOnActivityResultCode() {
            final CodeBlock.Builder code = CodeBlock.builder();
            return code.build();
        }

        private CodeBlock createActivityResultCode(ExecutableElement element) {
            final CodeBlock.Builder code = CodeBlock.builder();
            final Iterator<? extends VariableElement> parameters = element.getParameters().iterator();
            final CodeBlock.Builder parameterCode = CodeBlock.builder();
            while (parameters.hasNext()) {
                final VariableElement parameter = parameters.next();
                final TypeMirror parameterType = parameter.asType();
                final ActivityResult.Extra aExtra = parameter.getAnnotation(ActivityResult.Extra.class);
                if (aExtra == null) {
                    if (creator.isInstanceOf(parameterType, AndroidClass.Intent)) {
                        parameterCode.add(intentDataName);
                    } else if (creator.isInstanceOf(parameterType, AndroidClass.Bundle)) {
                        parameterCode.add("$N.getExtras()", intentDataName);
                    } else if (creator.isInstanceOf(parameterType, AndroidClass.Uri)) {
                        parameterCode.add("$N.getData()", intentDataName);
                    } else {
                        parameterCode.add(BaseCreator.addNullCode(parameterType));
                    }
                } else {
                    final String key = getExtraKey(parameter);
                    if ("char".equals(parameterType.toString())) {
                        final DefaultChar aDefaultChar = parameter.getAnnotation(DefaultChar.class);
                        if (aDefaultChar != null) {
                            parameterCode.add("$N.getCharExtra($S,(char)$L)", intentDataName, key, aDefaultChar.value());
                        } else {
                            parameterCode.add("$N.getCharExtra($S,(char) 0)", intentDataName, key);
                        }
                    } else if ("byte".equals(parameterType.toString())) {
                        final DefaultByte aDefaultByte = parameter.getAnnotation(DefaultByte.class);
                        if (aDefaultByte != null) {
                            parameterCode.add("$N.getByteExtra($S,(byte)$L)", intentDataName, key, aDefaultByte.value());
                        } else {
                            parameterCode.add("$N.getByteExtra($S,(byte) 0)", intentDataName, key);
                        }
                    } else if ("short".equals(parameterType.toString())) {
                        final DefaultShort aDefaultShort = parameter.getAnnotation(DefaultShort.class);
                        if (aDefaultShort != null) {
                            parameterCode.add("$N.getShortExtra($S,(short)$L)", intentDataName, key, aDefaultShort.value());
                        } else {
                            parameterCode.add("$N.getShortExtra($S,0)", intentDataName, key);
                        }
                    } else if ("int".equals(parameterType.toString())) {
                        final DefaultInt aDefaultInt = parameter.getAnnotation(DefaultInt.class);
                        if (aDefaultInt != null) {
                            parameterCode.add("$N.getIntExtra($S,(int)$L)", intentDataName, key, aDefaultInt.value());
                        } else {
                            parameterCode.add("$N.getIntExtra($S,0)", intentDataName, key);
                        }
                    } else if ("long".equals(parameterType.toString())) {
                        final DefaultLong aDefaultLong = parameter.getAnnotation(DefaultLong.class);
                        if (aDefaultLong != null) {
                            parameterCode.add("$N.getLongExtra($S,(long)$L)", intentDataName, key, aDefaultLong.value());
                        } else {
                            parameterCode.add("$N.getLongExtra($S,0L)", intentDataName, key);
                        }
                    } else if ("float".equals(parameterType.toString())) {
                        final DefaultFloat aDefaultFloat = parameter.getAnnotation(DefaultFloat.class);
                        if (aDefaultFloat != null) {
                            parameterCode.add("$N.getFloatExtra($S,(float)$L)", intentDataName, key, aDefaultFloat.value());
                        } else {
                            parameterCode.add("$N.getFloatExtra($S,0F)", intentDataName, key);
                        }
                    } else if ("double".equals(parameterType.toString())) {
                        final DefaultDouble aDefaultDouble = parameter.getAnnotation(DefaultDouble.class);
                        if (aDefaultDouble != null) {
                            parameterCode.add("$N.getDoubleExtra($S,(double)$L)", intentDataName, key, aDefaultDouble.value());
                        } else {
                            parameterCode.add("$N.getDoubleExtra($S,0.0)", intentDataName, key);
                        }
                    } else if ("boolean".equals(parameterType.toString())) {
                        final DefaultBoolean aDefaultBoolean = parameter.getAnnotation(DefaultBoolean.class);
                        if (aDefaultBoolean != null) {
                            parameterCode.add("$N.getBooleanExtra($S,$L)", intentDataName, key, aDefaultBoolean.value());
                        } else {
                            parameterCode.add("$N.getBooleanExtra($S,false)", intentDataName, key);
                        }
                    } else {
                        parameterCode.add("($T)$N.getExtras().get($S)", parameter.asType(), intentDataName, key);
                    }
                }
                if (parameters.hasNext()) {
                    parameterCode.add(",");
                }
            }
            code.add("$N(", element.getSimpleName());
            code.add(parameterCode.build());
            code.addStatement(")");
            return code.build();
        }

        final TypeSpec createActivityResult(String packageName, String className) {
            final String activityResultName = "ActivityResult";
            final String activityName = "activity";
            final TypeName typeName = ClassName.get(packageName, className, activityResultName);
            final TypeSpec.Builder activityResult = TypeSpec.classBuilder(activityResultName)
                    .addModifiers(Modifier.PUBLIC, Modifier.STATIC);
            activityResult.addField(FieldSpec.builder(AndroidClass.Activity, activityName)
                    .addModifiers(Modifier.PRIVATE, Modifier.FINAL)
                    .build());
            activityResult.addField(FieldSpec.builder(AndroidClass.Intent, ExtraHelp.INTENT)
                    .addModifiers(Modifier.PRIVATE, Modifier.FINAL)
                    .initializer("new $T()", AndroidClass.Intent)
                    .build());
            activityResult.addField(FieldSpec.builder(AndroidClass.Bundle, ExtraHelp.BUNDLE)
                    .addModifiers(Modifier.PRIVATE, Modifier.FINAL)
                    .initializer("new $T()", AndroidClass.Bundle)
                    .build());
            activityResult.addMethod(MethodSpec.constructorBuilder()
                    .addModifiers(Modifier.PUBLIC)
                    .addParameter(AndroidClass.Activity, activityName)
                    .addStatement("this.$N = $N", activityName, activityName)
                    .build());

            activityResult.addMethod(MethodSpec.methodBuilder("setData")
                    .addParameter(AndroidClass.Uri, "uri")
                    .returns(typeName)
                    .addStatement("$N.setData(uri)", ExtraHelp.INTENT)
                    .addStatement("return this")
                    .build());


            for (ArrayList<Element> extraElements : elements.values()) {
                for (Element extraElement : extraElements) {
                    final ActivityResult aActivityResult = extraElement.getAnnotation(ActivityResult.class);
                    if (aActivityResult.resultCode() != -1 && extraElements.size() != 1) {
                        continue;
                    }
                    if (extraElement instanceof ExecutableElement) {
                        final MethodSpec.Builder mb = MethodSpec.methodBuilder(extraElement.getSimpleName().toString())
                                .addModifiers(Modifier.PUBLIC)
                                .returns(typeName);
                        for (VariableElement variableElement : ((ExecutableElement) extraElement).getParameters()) {
                            final ParameterSpec parameter = ParameterSpec.get(variableElement);
                            mb.addParameter(parameter.type, parameter.name);
                            mb.addCode(extraHelp.createPutExtraCode(variableElement, ExtraHelp.BUNDLE, getExtraKey(variableElement), parameter.name));
                        }
                        mb.addStatement("return this");
                        activityResult.addMethod(mb.build());
                    }
                }
            }
            activityResult.addMethod(MethodSpec.methodBuilder("build")
                    .addModifiers(Modifier.PUBLIC)
                    .returns(AndroidClass.Intent)
                    .addStatement("$N.putExtras($N)", ExtraHelp.INTENT, ExtraHelp.BUNDLE)
                    .addStatement("return $N", ExtraHelp.INTENT)
                    .build());

            activityResult.addMethod(MethodSpec.methodBuilder("result")
                    .addModifiers(Modifier.PUBLIC)
                    .addParameter(int.class, "resultCode")
                    .addStatement("$N.setResult(resultCode,build())", activityName)
                    .build());


            activityResult.addMethod(MethodSpec.methodBuilder("resultOk")
                    .addModifiers(Modifier.PUBLIC)
                    .addStatement("$N.setResult($T.RESULT_OK,build())", activityName, AndroidClass.Activity)
                    .build());

            activityResult.addMethod(MethodSpec.methodBuilder("resultCancel")
                    .addModifiers(Modifier.PUBLIC)
                    .addStatement("$N.setResult($T.RESULT_CANCELED,build())", activityName, AndroidClass.Activity)
                    .build());

            return activityResult.build();
        }

        final String getExtraKey(Element element) {
            final ActivityResult.Extra arExtra = element.getAnnotation(ActivityResult.Extra.class);
            final String key;
            if (arExtra != null && !arExtra.value().isEmpty()) {
                key = arExtra.value();
            } else {
                key = "_" + element.getSimpleName().toString().toUpperCase();
            }
            return key;
        }

        private Element findOKActivityResultElement(List<Element> elements) {
            Element element = null;
            for (Element e : elements) {
                if (element != null) {
                    ActivityResult aActivityResult = element.getAnnotation(ActivityResult.class);
                    if (aActivityResult.resultCode() == -1) {
                        break;
                    }
                }
                element = e;
            }
            return element;
        }


        final void createActivityResultLauncherField(TypeSpec.Builder typeSpecBuilder, Element typeElement) {
            for (String name : elements.keySet()) {
                final ArrayList<Element> extraElements = elements.get(name);
                final Element element = findOKActivityResultElement(extraElements);
                final ActivityResult aActivityResult = element.getAnnotation(ActivityResult.class);
                final ActivityResult.Contract contract = aActivityResult.value();
                TypeName inputTypeName;
                switch (contract) {
                    case StartIntentSenderForResult:
                        inputTypeName = AndroidClass.IntentSenderRequest;
                        break;
                    case RequestMultiplePermissions:
                    case OpenDocument:
                    case OpenMultipleDocuments:
                        inputTypeName = ArrayTypeName.of(String.class);
                        break;
                    case RequestPermission:
                    case GetMultipleContents:
                    case GetContent:
                    case CreateDocument:
                        inputTypeName = ClassName.get(String.class);
                        break;
                    case TakePicturePreview:
                    case PickContact:
                        inputTypeName = ClassName.get(Void.class);
                        break;
                    case TakePicture:
                    case CaptureVideo:
                    case OpenDocumentTree:
                        inputTypeName = AndroidClass.Uri;
                        break;
                    default:
                        inputTypeName = AndroidClass.Intent;
                        break;
                }
                final TypeName mapTypeName = ParameterizedTypeName.get(AndroidClass.ActivityResultLauncher, inputTypeName);
                typeSpecBuilder.addField(FieldSpec
                        .builder(mapTypeName, name, Modifier.PRIVATE)
                        .build());
            }
        }

        final CodeBlock onCreateLauncher(Element typeElement) {
            final CodeBlock.Builder builder = CodeBlock.builder();
            for (String name : elements.keySet()) {
                CodeBlock initializerCode;
                final ArrayList<Element> extraElements = elements.get(name);
                final Element element = findOKActivityResultElement(extraElements);
                final ActivityResult aActivityResult = element.getAnnotation(ActivityResult.class);
                final ActivityResult.Contract contract = aActivityResult.value();
                switch (contract) {
                    case StartIntentSenderForResult:
                        initializerCode = createActivityResultActivityResultCallback(typeElement, extraElements, AndroidClass.StartIntentSenderForResult);
                        break;
                    case RequestMultiplePermissions:
                        initializerCode = createMultipleBooleanActivityResultCallback(typeElement, extraElements, AndroidClass.RequestMultiplePermissions);
                        break;
                    case RequestPermission:
                        initializerCode = createSingeBooleanActivityResultCallback(typeElement, extraElements, AndroidClass.RequestPermission);
                        break;
                    case TakePicturePreview:
                        initializerCode = createSingeBitmapActivityResultCallback(typeElement, extraElements, AndroidClass.TakePicturePreview);
                        break;
                    case TakePicture:
                        initializerCode = createSingeBooleanActivityResultCallback(typeElement, extraElements, AndroidClass.TakePicture);
                        break;
                    case CaptureVideo:
                        initializerCode = createSingeBooleanActivityResultCallback(typeElement, extraElements, AndroidClass.CaptureVideo);
                        break;
                    case PickContact:
                        initializerCode = createSingeUriActivityResultCallback(typeElement, extraElements, AndroidClass.PickContact);
                        break;
                    case GetContent:
                        initializerCode = createSingeUriActivityResultCallback(typeElement, extraElements, AndroidClass.GetContent);
                        break;
                    case GetMultipleContents:
                        initializerCode = createUriListActivityResultCallback(typeElement, extraElements, AndroidClass.GetMultipleContents);
                        break;
                    case OpenDocument:
                        initializerCode = createSingeUriActivityResultCallback(typeElement, extraElements, AndroidClass.OpenDocument);
                        break;
                    case OpenMultipleDocuments:
                        initializerCode = createUriListActivityResultCallback(typeElement, extraElements, AndroidClass.OpenMultipleDocuments);
                        break;
                    case OpenDocumentTree:
                        initializerCode = createSingeUriActivityResultCallback(typeElement, extraElements, AndroidClass.OpenDocumentTree);
                        break;
                    case CreateDocument:
                        initializerCode = createSingeUriActivityResultCallback(typeElement, extraElements, AndroidClass.CreateDocument);
                        break;
                    default:
                        initializerCode = createActivityResultActivityResultCallback(typeElement, extraElements, AndroidClass.StartActivityForResult);
                        break;
                }
                builder.add("$N = ", name).add(initializerCode);
            }
            return builder.build();
        }

        final CodeBlock createDispose() {
            CodeBlock.Builder builder = CodeBlock.builder();
            for (String name : elements.keySet()) {
                builder.addStatement("$N.unregister()", name);
            }
            return builder.build();
        }

        private CodeBlock createActivityResultActivityResultCallback(Element typeElement, List<Element> methodElements, ClassName contractClass) {
            final CodeBlock.Builder builder = CodeBlock.builder();
            final HashMap<Integer, ArrayList<Element>> map = new HashMap<>();
            for (Element methodElement : methodElements) {
                final ActivityResult aActivityResult = methodElement.getAnnotation(ActivityResult.class);
                final ArrayList<Element> list = map.getOrDefault(aActivityResult.value(), new ArrayList<>());
                list.add(methodElement);
                map.put(aActivityResult.resultCode(), list);
            }
            builder.addStatement("final $T data = result.getData()", AndroidClass.Intent);
            final Iterator<Integer> iterator = map.keySet().iterator();
            int count = 0;
            while (iterator.hasNext()) {
                final int resultCode = iterator.next();
                if (count == 0) {
                    builder.beginControlFlow("if($L == result.getResultCode())", resultCode);
                } else {
                    builder.nextControlFlow("else if($L == result.getResultCode())", resultCode);
                }
                final ArrayList<Element> list = map.get(resultCode);
                for (Element element : list) {
                    builder.add("$N_.this.", typeElement.getSimpleName());
                    builder.add(createActivityResultCode((ExecutableElement) element));
                }
                count++;
            }
            builder.endControlFlow();
            return createRegisterForActivityResult(contractClass, AndroidClass.ActivityResult, builder.build());
        }

        private CodeBlock createMultipleBooleanActivityResultCallback(Element typeElement, List<Element> methodElements, ClassName contractClass) {
            final TypeName type = ParameterizedTypeName.get(Map.class, String.class, Boolean.class);
            boolean hasMapParameter = false;
            boolean hasBooleanParameter = false;
            for (Element methodElement : methodElements) {
                final List<? extends VariableElement> parameters = ((ExecutableElement) methodElement).getParameters();
                for (VariableElement parameter : parameters) {
                    if (parameter.asType().toString().replace(" ", "").equals(type.toString().replace(" ", ""))) {
                        hasMapParameter = true;
                        break;
                    } else if (PrimitiveTypeUtil.isBoolean(parameter.asType())) {
                        hasMapParameter = true;
                        hasBooleanParameter = true;
                        break;
                    }
                }
            }
            if (!hasMapParameter) {
                final Element methodElement = methodElements.get(0);
                creator.processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR, typeElement.toString() + "." + methodElement.getSimpleName() + "() shout need java.util.Map<java.lang.String, Boolean> parameter.");
            }
            final CodeBlock.Builder builder = CodeBlock.builder();
            if (hasBooleanParameter) {
                builder.addStatement("boolean b = true")
                        .beginControlFlow("for(boolean value: result.values())")
                        .addStatement("b = b & value")
                        .endControlFlow();
            }
            final Iterator<Element> methodIterator = methodElements.iterator();
            while (methodIterator.hasNext()) {
                final Element methodElement = methodIterator.next();
                printWarningResultCode(typeElement, methodElement);
                final List<? extends VariableElement> parameters = ((ExecutableElement) methodElement).getParameters();
                final Iterator<? extends VariableElement> iterator = parameters.iterator();
                builder.add("$N_.this.$N(", typeElement.getSimpleName(), methodElement.getSimpleName());
                while (iterator.hasNext()) {
                    final VariableElement variableElement = iterator.next();
                    final ParameterSpec parameter = ParameterSpec.get(variableElement);
                    if (parameter.type.equals(type)) {
                        builder.add("result");
                    } else if (PrimitiveTypeUtil.isBoolean(parameter.type)) {
                        builder.add("b");
                    } else if (PrimitiveTypeUtil.isByte(parameter.type)) {
                        builder.add("b?");
                        builder.add(PrimitiveTypeUtil.byteDefaultValue());
                        builder.add(":(byte)1");
                    } else if (PrimitiveTypeUtil.isShort(parameter.type)) {
                        builder.add("b?");
                        builder.add(PrimitiveTypeUtil.shortDefaultValue());
                        builder.add(":(short)1");
                    } else if (PrimitiveTypeUtil.isInt(parameter.type)) {
                        builder.add("b?");
                        builder.add(PrimitiveTypeUtil.intDefaultValue());
                        builder.add(":1");
                    } else if (PrimitiveTypeUtil.isLong(parameter.type)) {
                        builder.add("b?");
                        builder.add(PrimitiveTypeUtil.longDefaultValue());
                        builder.add(":1L");
                    } else if (PrimitiveTypeUtil.isFloat(parameter.type)) {
                        builder.add("b?");
                        builder.add(PrimitiveTypeUtil.floatDefaultValue());
                        builder.add(":1f");
                    } else if (PrimitiveTypeUtil.isDouble(parameter.type)) {
                        builder.add("b?");
                        builder.add(PrimitiveTypeUtil.doubleDefaultValue());
                        builder.add(":1d");
                    } else {
                        builder.add(putNullTypeParameter(parameter.type));
                    }
                    if (iterator.hasNext()) {
                        builder.add(",");
                    }
                }
                builder.addStatement(")");
            }
            return createRegisterForActivityResult(contractClass, type, builder.build());
        }

        private CodeBlock createSingeBitmapActivityResultCallback(Element typeElement, List<Element> methodElements, ClassName contractClass) {
            boolean hasBitmapParameter = false;
            for (Element methodElement : methodElements) {
                final List<? extends VariableElement> parameters = ((ExecutableElement) methodElement).getParameters();
                for (VariableElement parameter : parameters) {
                    if (creator.isInstanceOf(parameter.asType(), AndroidClass.Bitmap)) {
                        hasBitmapParameter = true;
                        break;
                    }
                }
            }
            if (!hasBitmapParameter) {
                final Element methodElement = methodElements.get(0);
                creator.processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR, typeElement.toString() + "." + methodElement.getSimpleName() + "() shout need android.graphics.Bitmap parameter.");
            }
            final CodeBlock.Builder builder = CodeBlock.builder();
            final Iterator<Element> methodIterator = methodElements.iterator();
            while (methodIterator.hasNext()) {
                final Element methodElement = methodIterator.next();
                printWarningResultCode(typeElement, methodElement);
                final List<? extends VariableElement> parameters = ((ExecutableElement) methodElement).getParameters();
                final Iterator<? extends VariableElement> iterator = parameters.iterator();
                builder.add("$N_.this.$N(", typeElement.getSimpleName(), methodElement.getSimpleName());
                while (iterator.hasNext()) {
                    final VariableElement variableElement = iterator.next();
                    final ParameterSpec parameter = ParameterSpec.get(variableElement);
                    if (parameter.type.equals(AndroidClass.Bitmap)) {
                        builder.add("result");
                    } else {
                        builder.add(putNullTypeParameter(parameter.type));
                    }
                    if (iterator.hasNext()) {
                        builder.add(",");
                    }
                }
                builder.addStatement(")");
            }
            return createRegisterForActivityResult(contractClass, AndroidClass.Bitmap, builder.build());
        }

        private CodeBlock createSingeBooleanActivityResultCallback(Element typeElement, List<Element> methodElements, ClassName contractClass) {
            boolean hasBooleanParameter = false;
            for (Element methodElement : methodElements) {
                final List<? extends VariableElement> parameters = ((ExecutableElement) methodElement).getParameters();
                for (VariableElement parameter : parameters) {
                    if (PrimitiveTypeUtil.isBoolean(parameter.asType())) {
                        hasBooleanParameter = true;
                        break;
                    }
                }
            }
            if (!hasBooleanParameter) {
                final Element methodElement = methodElements.get(0);
                creator.processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR, typeElement.toString() + "." + methodElement.getSimpleName() + "() shout need boolean parameter.");
            }
            final CodeBlock.Builder builder = CodeBlock.builder();
            final Iterator<Element> methodIterator = methodElements.iterator();
            while (methodIterator.hasNext()) {
                final Element methodElement = methodIterator.next();
                printWarningResultCode(typeElement, methodElement);
                final List<? extends VariableElement> parameters = ((ExecutableElement) methodElement).getParameters();
                final Iterator<? extends VariableElement> iterator = parameters.iterator();
                builder.add("$N_.this.$N(", typeElement.getSimpleName(), methodElement.getSimpleName());
                while (iterator.hasNext()) {
                    final VariableElement variableElement = iterator.next();
                    final ParameterSpec parameter = ParameterSpec.get(variableElement);
                    if (PrimitiveTypeUtil.isBoolean(parameter.type)) {
                        builder.add("result");
                    } else if (PrimitiveTypeUtil.isByte(parameter.type)) {
                        builder.add("result?");
                        builder.add(PrimitiveTypeUtil.byteDefaultValue());
                        builder.add(":(byte)1");
                    } else if (PrimitiveTypeUtil.isShort(parameter.type)) {
                        builder.add("result?");
                        builder.add(PrimitiveTypeUtil.shortDefaultValue());
                        builder.add(":(short)1");
                    } else if (PrimitiveTypeUtil.isInt(parameter.type)) {
                        builder.add("result?");
                        builder.add(PrimitiveTypeUtil.intDefaultValue());
                        builder.add(":1");
                    } else if (PrimitiveTypeUtil.isLong(parameter.type)) {
                        builder.add("result?");
                        builder.add(PrimitiveTypeUtil.longDefaultValue());
                        builder.add(":1L");
                    } else if (PrimitiveTypeUtil.isFloat(parameter.type)) {
                        builder.add("result?");
                        builder.add(PrimitiveTypeUtil.floatDefaultValue());
                        builder.add(":1f");
                    } else if (PrimitiveTypeUtil.isDouble(parameter.type)) {
                        builder.add("result?");
                        builder.add(PrimitiveTypeUtil.doubleDefaultValue());
                        builder.add(":1d");
                    } else {
                        builder.add(putNullTypeParameter(parameter.type));
                    }
                    if (iterator.hasNext()) {
                        builder.add(",");
                    }
                }
                builder.addStatement(")");
            }
            return createRegisterForActivityResult(contractClass, ClassName.get(Boolean.class), builder.build());
        }

        private CodeBlock createSingeUriActivityResultCallback(Element typeElement, List<Element> methodElements, ClassName contractClass) {
            boolean hasUriParameter = false;
            for (Element methodElement : methodElements) {
                final List<? extends VariableElement> parameters = ((ExecutableElement) methodElement).getParameters();
                for (VariableElement parameter : parameters) {
                    if (creator.isInstanceOf(parameter.asType(), AndroidClass.Uri)) {
                        hasUriParameter = true;
                        break;
                    }
                }
            }
            if (!hasUriParameter) {
                final Element methodElement = methodElements.get(0);
                creator.processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR, typeElement.toString() + "." + methodElement.getSimpleName() + "() shout need android.net.Uri parameter.");
            }
            final CodeBlock.Builder builder = CodeBlock.builder();
            final Iterator<Element> methodIterator = methodElements.iterator();
            while (methodIterator.hasNext()) {
                final Element methodElement = methodIterator.next();
                printWarningResultCode(typeElement, methodElement);
                final List<? extends VariableElement> parameters = ((ExecutableElement) methodElement).getParameters();
                final Iterator<? extends VariableElement> iterator = parameters.iterator();
                builder.add("$N_.this.$N(", typeElement.getSimpleName(), methodElement.getSimpleName());
                while (iterator.hasNext()) {
                    final VariableElement variableElement = iterator.next();
                    final ParameterSpec parameter = ParameterSpec.get(variableElement);
                    if (parameter.type.equals(AndroidClass.Uri)) {
                        builder.add("result");
                    } else {
                        builder.add(putNullTypeParameter(parameter.type));
                    }
                    if (iterator.hasNext()) {
                        builder.add(",");
                    }
                }
                builder.addStatement(")");
            }
            return createRegisterForActivityResult(contractClass, AndroidClass.Uri, builder.build());
        }

        private CodeBlock createUriListActivityResultCallback(Element typeElement, List<Element> methodElements, ClassName contractClass) {
            final TypeName outputClass = ParameterizedTypeName.get(ClassName.get(List.class), AndroidClass.Uri);
            final String outputClassString = outputClass.toString().replace(" ", "");
            boolean hasUriListParameter = false;
            for (Element methodElement : methodElements) {
                final List<? extends VariableElement> parameters = ((ExecutableElement) methodElement).getParameters();
                for (VariableElement parameter : parameters) {
                    final String parameterType = parameter.asType().toString().replace("? extends ", "").replace(" ", "");
                    if (parameterType.equals(outputClassString)) {
                        hasUriListParameter = true;
                        break;
                    }
                }
            }
            if (!hasUriListParameter) {
                final Element methodElement = methodElements.get(0);
                creator.processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR, typeElement.toString() + "." + methodElement.getSimpleName() + "() shout need java.util.List<android.net.Uri> parameter.");
            }
            final CodeBlock.Builder builder = CodeBlock.builder();
            final Iterator<Element> methodIterator = methodElements.iterator();
            while (methodIterator.hasNext()) {
                final Element methodElement = methodIterator.next();
                printWarningResultCode(typeElement, methodElement);
                final List<? extends VariableElement> parameters = ((ExecutableElement) methodElement).getParameters();
                final Iterator<? extends VariableElement> iterator = parameters.iterator();
                builder.add("$N_.this.$N(", typeElement.getSimpleName(), methodElement.getSimpleName());
                while (iterator.hasNext()) {
                    final VariableElement variableElement = iterator.next();
                    final ParameterSpec parameter = ParameterSpec.get(variableElement);
                    final String parameterType = parameter.type.toString().replace("? extends ", "").replace(" ", "");
                    if (parameterType.equals(outputClassString)) {
                        builder.add("result");
                    } else {
                        builder.add(putNullTypeParameter(parameter.type));
                    }
                    if (iterator.hasNext()) {
                        builder.add(",");
                    }
                }
                builder.addStatement(")");
            }
            return createRegisterForActivityResult(contractClass, outputClass, builder.build());
        }

        private CodeBlock createRegisterForActivityResult(TypeName contractClass, TypeName outputClass, CodeBlock actionCode) {
            return CodeBlock.builder()
                    .add("registerForActivityResult(new $T()", contractClass)
                    .beginControlFlow(", new $T<$T>()", AndroidClass.ActivityResultCallback, outputClass)
                    .beginControlFlow("@$T public void onActivityResult($T result)", Override.class, outputClass)
                    .add(actionCode)
                    .endControlFlow()
                    .endControlFlow()
                    .addStatement(")")
                    .build();
        }

        private CodeBlock putNullTypeParameter(TypeName typeName) {
            if (PrimitiveTypeUtil.isBoolean(typeName)) {
                return PrimitiveTypeUtil.booleanDefaultValue();
            } else if (PrimitiveTypeUtil.isShort(typeName)) {
                return PrimitiveTypeUtil.shortDefaultValue();
            } else if (PrimitiveTypeUtil.isInt(typeName)) {
                return PrimitiveTypeUtil.intDefaultValue();
            } else if (PrimitiveTypeUtil.isLong(typeName)) {
                return PrimitiveTypeUtil.longDefaultValue();
            } else if (PrimitiveTypeUtil.isChar(typeName)) {
                return PrimitiveTypeUtil.charDefaultValue();
            } else if (PrimitiveTypeUtil.isFloat(typeName)) {
                return PrimitiveTypeUtil.floatDefaultValue();
            } else if (PrimitiveTypeUtil.isDouble(typeName)) {
                return PrimitiveTypeUtil.doubleDefaultValue();
            } else if (PrimitiveTypeUtil.isByte(typeName)) {
                return PrimitiveTypeUtil.byteDefaultValue();
            } else {
                return CodeBlock.builder().add("($T)null", typeName).build();
            }
        }

        private void printWarningResultCode(Element typeElement, Element element) {
            final ActivityResult aActivityResult = element.getAnnotation(ActivityResult.class);
            if (aActivityResult.resultCode() != -1) {
                creator.processingEnv.getMessager().printMessage(Diagnostic.Kind.WARNING, "The activityResult's resultCode from " + typeElement.toString() + "." + element.getSimpleName() + " will be efficient when value be StartActivityForResult and StartIntentSenderForResult.");
            }
        }

        final TypeSpec createLauncher(String packageName, String className) {
            final TypeName activity_TypeName = ClassName.get(packageName, className);
            final TypeName activityTypeName = ClassName.get(packageName, className.substring(0, className.length() - 1));
            final String activityName = className.toLowerCase().charAt(0) + className.substring(1, className.length() - 1);
            final TypeSpec.Builder builder = TypeSpec.classBuilder("Launcher")
                    .addModifiers(Modifier.PUBLIC, Modifier.STATIC);
            builder.addField(FieldSpec.builder(activity_TypeName, ACTIVITY, Modifier.FINAL, Modifier.PRIVATE).build());
            builder.addMethod(MethodSpec.constructorBuilder()
                    .addModifiers(Modifier.PUBLIC)
                    .addParameter(activityTypeName, activityName)
                    .addStatement("$N =($T) $N", ACTIVITY, activity_TypeName, activityName)
                    .build());
            for (String name : elements.keySet()) {
                final ArrayList<Element> extraElements = elements.get(name);
                final Element element = findOKActivityResultElement(extraElements);
                builder.addMethod(createLauncherMethod(element, false));
                builder.addMethod(createLauncherMethod(element, true));
            }
            return builder.build();
        }

        private MethodSpec createLauncherMethod(Element element, boolean enableOptions) {
            final ActivityResult aActivityResult = element.getAnnotation(ActivityResult.class);
            final ActivityResult.Contract contract = aActivityResult.value();
            TypeName inputTypeName;
            switch (contract) {
                case StartIntentSenderForResult:
                    inputTypeName = AndroidClass.IntentSenderRequest;
                    break;
                case RequestMultiplePermissions:
                case OpenDocument:
                case OpenMultipleDocuments:
                    inputTypeName = ArrayTypeName.of(String.class);
                    break;
                case RequestPermission:
                case GetMultipleContents:
                case GetContent:
                    inputTypeName = ClassName.get(String.class);
                    break;
                case TakePicturePreview:
                case PickContact:
                    inputTypeName = ClassName.get(Void.class);
                    break;
                case TakePicture:
                case CaptureVideo:
                case OpenDocumentTree:
                case CreateDocument:
                    inputTypeName = AndroidClass.Uri;
                    break;
                default:
                    inputTypeName = AndroidClass.Intent;
                    break;
            }

            final String elementName = element.getSimpleName().toString();
            final MethodSpec.Builder builder = MethodSpec.methodBuilder("launchFor" + elementName.toUpperCase().charAt(0) + elementName.substring(1));
            builder.addModifiers(Modifier.FINAL, Modifier.PUBLIC);
            if (!inputTypeName.equals(ClassName.get(Void.class))) {
                builder.addParameter(inputTypeName, "input");
                if (enableOptions) {
                    builder.addParameter(AndroidClass.ActivityOptionsCompat, "options");
                    builder.addStatement("activity.$N.launch(input,options)", elementName);
                } else {
                    builder.addStatement("activity.$N.launch(input)", elementName);
                }
            } else {
                if (enableOptions) {
                    builder.addParameter(AndroidClass.ActivityOptionsCompat, "options");
                    builder.addStatement("activity.$N.launch(null,options)", elementName);
                } else {
                    builder.addStatement("activity.$N.launch(null)", elementName);
                }
            }
            return builder.build();
        }
    }
}
