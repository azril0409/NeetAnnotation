package library.neetoffice.com.neetannotation.processor.kapt;

import com.squareup.javapoet.ArrayTypeName;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeMirror;

import library.neetoffice.com.neetannotation.DefaultBoolean;
import library.neetoffice.com.neetannotation.DefaultByte;
import library.neetoffice.com.neetannotation.DefaultChar;
import library.neetoffice.com.neetannotation.DefaultDouble;
import library.neetoffice.com.neetannotation.DefaultFloat;
import library.neetoffice.com.neetannotation.DefaultInt;
import library.neetoffice.com.neetannotation.DefaultLong;
import library.neetoffice.com.neetannotation.DefaultShort;
import library.neetoffice.com.neetannotation.DefaultString;
import library.neetoffice.com.neetannotation.Extra;
import library.neetoffice.com.neetannotation.SaveInstance;
import library.neetoffice.com.neetannotation.processor.AndroidClass;

public class ExtraHelp {
    static final String BUNDLE = "bundle";
    static final String CONTEXT = "context";
    static final String INTENT = "intent";
    static final String FRAGMENT = "fragment";
    static final String ARGUMENT_BUILDER = "ArgumentBuilder";
    private final BaseCreator creator;

    public ExtraHelp(BaseCreator creator) {
        this.creator = creator;
    }

    public Builder builder(String getBundleMethod, String getSaveInstanceBundleMethod) {
        return new Builder(creator, getBundleMethod, getSaveInstanceBundleMethod);
    }

    public static class Builder {
        private final BaseCreator creator;
        private final String getBundleMethod;
        private final String getSaveInstanceBundleMethod;
        private final ArrayList<Element> extraElements = new ArrayList<>();
        private final ArrayList<Element> saveInstanceElements = new ArrayList<>();

        public Builder(BaseCreator creator, String getBundleMethod, String getSaveInstanceBundleMethod) {
            this.creator = creator;
            this.getBundleMethod = getBundleMethod;
            this.getSaveInstanceBundleMethod = getSaveInstanceBundleMethod;
        }

        private String getExtraKey(Element element) {
            final Extra aExtra = element.getAnnotation(Extra.class);
            final String key;
            if (aExtra != null && !aExtra.value().isEmpty()) {
                key = aExtra.value();
            } else {
                key = "_" + element.getSimpleName().toString().toUpperCase();
            }
            return key;
        }

        private String getSaveInstanceKey(Element element) {
            final SaveInstance aExtra = element.getAnnotation(SaveInstance.class);
            final String key;
            if (aExtra.value().isEmpty()) {
                key = "_" + element.getSimpleName().toString().toUpperCase();
            } else {
                key = aExtra.value();
            }
            return key;
        }

        private boolean isArray(TypeMirror typeMirror) {
            return typeMirror.toString().startsWith("[]", typeMirror.toString().length() - 2);
        }

        private boolean isCollection(TypeMirror typeMirror) {
            return creator.isInstanceOf(typeMirror, ClassName.get(Collection.class));
        }

        private boolean isParcelable(TypeMirror typeMirror) {
            return creator.isInstanceOf(typeMirror, AndroidClass.Parcelable);
        }

        private boolean isSerializable(TypeMirror typeMirror) {
            return creator.isInstanceOf(typeMirror, ClassName.get(Serializable.class));
        }

        CodeBlock createPutExtraCode(Element extraElement, String bundleName, String key, String varName) {
            final CodeBlock.Builder code = CodeBlock.builder();
            final TypeMirror extraType = extraElement.asType();
            if (PrimitiveTypeUtil.isByteArray(extraType)) {
                code.addStatement("$N.putByteArray($S,$N)", bundleName, key, varName);
            } else if (PrimitiveTypeUtil.isCharArray(extraType)) {
                code.addStatement("$N.putCharArray($S,$N)", bundleName, key, varName);
            } else if (PrimitiveTypeUtil.isShortArray(extraType)) {
                code.addStatement("$N.putShortArray($S,$N)", bundleName, key, varName);
            } else if (PrimitiveTypeUtil.isIntArray(extraType)) {
                code.addStatement("$N.putIntArray($S,$N)", bundleName, key, varName);
            } else if (PrimitiveTypeUtil.isLongArray(extraType)) {
                code.addStatement("$N.putLongArray($S,$N)", bundleName, key, varName);
            } else if (PrimitiveTypeUtil.isLongArray(extraType)) {
                code.addStatement("$N.putFloatArray($S,$N)", bundleName, key, varName);
            } else if (PrimitiveTypeUtil.isDoubleArray(extraType)) {
                code.addStatement("$N.putDoubleArray($S,$N)", bundleName, key, varName);
            } else if (PrimitiveTypeUtil.isBooleanArray(extraType)) {
                code.addStatement("$N.putBooleanArray($S,$N)", bundleName, key, varName);
            } else if (ArrayTypeName.of(CharSequence.class).equals(ClassName.get(extraType))) {
                code.addStatement("$N.putCharSequenceArray($S,$N)", bundleName, key, varName);
            } else if (ArrayTypeName.of(String.class).equals(ClassName.get(extraType))) {
                code.addStatement("$N.putStringArray($S,$N)", bundleName, key, varName);
            } else if (isArray(extraType)) {
                code.addStatement("$N.putParcelableArray($S,$N)", bundleName, key, varName);
            } else if (isCollection(extraElement.asType())) {
                final DeclaredType declaredType = (DeclaredType) extraElement.asType();
                if (ClassName.get(declaredType.getTypeArguments().get(0)).equals(ClassName.get(Integer.class))) {
                    code.addStatement("$N.putIntegerArrayList($S,new $T<$T>($N))", bundleName, key, ArrayList.class, Integer.class, varName);
                } else if (ClassName.get(declaredType.getTypeArguments().get(0)).equals(ClassName.get(CharSequence.class))) {
                    code.addStatement("$N.putCharSequenceArrayList($S,new $T<$T>($N))", bundleName, key, ArrayList.class, CharSequence.class, varName);
                } else if (ClassName.get(declaredType.getTypeArguments().get(0)).equals(ClassName.get(String.class))) {
                    code.addStatement("$N.putStringArrayList($S,new $T<$T>($N))", bundleName, key, ArrayList.class, String.class, varName);
                } else {
                    final String type = declaredType.getTypeArguments().get(0).toString().replace("? extends ", "");
                    code.addStatement("$N.putParcelableArrayList($S,new $T<$T>($N))", bundleName, key, ArrayList.class, ClassName.bestGuess(type), varName);
                }
            } else if (ClassName.get(extraType).equals(ClassName.get(String.class))) {
                code.addStatement("$N.putString($S,$N)", bundleName, key, varName);
            } else if (isParcelable(extraElement.asType())) {
                code.addStatement("$N.putParcelable($S,$N)", bundleName, key, varName);
            } else if (isSerializable(extraElement.asType())) {
                code.addStatement("$N.putSerializable($S,$N)", bundleName, key, varName);
            } else if (creator.isInstanceOf(extraElement.asType(), AndroidClass.Size)) {
                code.addStatement("$N.putSize($S,$N)", bundleName, key, varName);
            } else if (creator.isInstanceOf(extraElement.asType(), AndroidClass.SizeF)) {
                code.addStatement("$N.putSizeF($S,$N)", bundleName, key, varName);
            } else {
                if (PrimitiveTypeUtil.isByte(extraType)) {
                    code.addStatement("$N.putByte($S,$N)", bundleName, key, varName);
                } else if (PrimitiveTypeUtil.isChar(extraType)) {
                    code.addStatement("$N.putChar($S,$N)", bundleName, key, varName);
                } else if (PrimitiveTypeUtil.isShort(extraType)) {
                    code.addStatement("$N.putShort($S,$N)", bundleName, key, varName);
                } else if (PrimitiveTypeUtil.isInt(extraType)) {
                    code.addStatement("$N.putInt($S,$N)", bundleName, key, varName);
                } else if (PrimitiveTypeUtil.isLong(extraType)) {
                    code.addStatement("$N.putLong($S,$N)", bundleName, key, varName);
                } else if (PrimitiveTypeUtil.isFloat(extraType)) {
                    code.addStatement("$N.putFloat($S,$N)", bundleName, key, varName);
                } else if (PrimitiveTypeUtil.isDouble(extraType)) {
                    code.addStatement("$N.putDouble($S,$N)", bundleName, key, varName);
                } else if (PrimitiveTypeUtil.isBoolean(extraType)) {
                    code.addStatement("$N.putBoolean($S,$N)", bundleName, key, varName);
                } else if (ClassName.get(CharSequence.class).equals(ClassName.get(extraType))) {
                    code.addStatement("$N.putCharSequence($S,$N)", bundleName, key, varName);
                }
            }
            return code.build();
        }

        void parseElement(Element element) {
            final Extra aExtra = element.getAnnotation(Extra.class);
            if (aExtra != null) {
                extraElements.add(element);
            }
            final SaveInstance aSaveInstance = element.getAnnotation(SaveInstance.class);
            if (aSaveInstance != null) {
                saveInstanceElements.add(element);
            }
        }

        final CodeBlock createSaveInstanceState(String outStateName) {
            final CodeBlock.Builder code = CodeBlock.builder();
            for (Element element : saveInstanceElements) {
                code.add(createPutExtraCode(element, outStateName, getSaveInstanceKey(element), element.getSimpleName().toString()));
            }
            return code.build();
        }

        final CodeBlock createGetExtra(String savedInstanceStateName) {
            final CodeBlock.Builder code = CodeBlock.builder();
            for (Element element : extraElements) {
                code.add(createGetExtra(getExtraKey(element), element, getBundleMethod));
            }
            if (!saveInstanceElements.isEmpty()) {
                code.beginControlFlow("if($N != null)", savedInstanceStateName);
                for (Element element : saveInstanceElements) {
                    code.add(createGetExtra(getSaveInstanceKey(element), element, getSaveInstanceBundleMethod));
                }
                code.endControlFlow();
            }
            return code.build();
        }

        private CodeBlock createGetExtra(String key, Element element, String getBundleMethod) {
            final TypeMirror variableType = element.asType();
            final CodeBlock.Builder cb = CodeBlock.builder().add("$N = ", element.getSimpleName());
            if (PrimitiveTypeUtil.isChar(variableType)) {
                final DefaultChar aDefaultChar = element.getAnnotation(DefaultChar.class);
                if (aDefaultChar != null) {
                    return cb.addStatement("$N.getChar($S,(char)$L)", getBundleMethod, key, aDefaultChar.value()).build();
                }
                return cb.addStatement("$N.getChar($S)", getBundleMethod, key).build();
            } else if (PrimitiveTypeUtil.isByte(variableType)) {
                final DefaultByte aDefaultByte = element.getAnnotation(DefaultByte.class);
                if (aDefaultByte != null) {
                    return cb.addStatement("$N.getByte($S,(byte)$L)", getBundleMethod, key, aDefaultByte.value()).build();
                }
                return cb.addStatement("$N.getByte($S)", getBundleMethod, key).build();
            } else if (PrimitiveTypeUtil.isShort(variableType)) {
                final DefaultShort aDefaultShort = element.getAnnotation(DefaultShort.class);
                if (aDefaultShort != null) {
                    return cb.addStatement("$N.getShort($S,(short)$L)", getBundleMethod, key, aDefaultShort.value()).build();
                }
                return cb.addStatement("$N.getShort($S)", getBundleMethod, key).build();
            } else if (PrimitiveTypeUtil.isInt(variableType)) {
                final DefaultInt aDefaultInt = element.getAnnotation(DefaultInt.class);
                if (aDefaultInt != null) {
                    return cb.addStatement("$N.getInt($S,(int)$L)", getBundleMethod, key, aDefaultInt.value()).build();
                }
                return cb.addStatement("$N.getInt($S)", getBundleMethod, key).build();
            } else if (PrimitiveTypeUtil.isLong(variableType)) {
                final DefaultLong aDefaultLong = element.getAnnotation(DefaultLong.class);
                if (aDefaultLong != null) {
                    return cb.addStatement("$N.getLong($S,(long)$L)", getBundleMethod, key, aDefaultLong.value()).build();
                }
                return cb.addStatement("$N.getLong($S)", getBundleMethod, key).build();
            } else if (PrimitiveTypeUtil.isFloat(variableType)) {
                final DefaultFloat aDefaultFloat = element.getAnnotation(DefaultFloat.class);
                if (aDefaultFloat != null) {
                    return cb.addStatement("$N.getFloat($S,(float)$L)", getBundleMethod, key, aDefaultFloat.value()).build();
                }
                return cb.addStatement("$N.getFloat($S)", getBundleMethod, key).build();
            } else if (PrimitiveTypeUtil.isDouble(variableType)) {
                final DefaultDouble aDefaultDouble = element.getAnnotation(DefaultDouble.class);
                if (aDefaultDouble != null) {
                    return cb.addStatement("$N.getDouble($S,(double)$L)", getBundleMethod, key, aDefaultDouble.value()).build();
                }
                return cb.addStatement("$N.getDouble($S)", getBundleMethod, key).build();
            } else if (PrimitiveTypeUtil.isBoolean(variableType)) {
                final DefaultBoolean aDefaultBoolean = element.getAnnotation(DefaultBoolean.class);
                if (aDefaultBoolean != null) {
                    return cb.addStatement("$N.getBoolean($S,$L)", getBundleMethod, key, aDefaultBoolean.value()).build();
                }
                return cb.addStatement("$N.getBoolean($S)", getBundleMethod, key).build();
            } else if (creator.isInstanceOf(variableType, ClassName.get(String.class))) {
                final DefaultString aDefaultString = element.getAnnotation(DefaultString.class);
                if (aDefaultString != null) {
                    return cb.addStatement("$N.getString($S,\"$L\")", getBundleMethod, key, aDefaultString.value()).build();
                }
                return cb.addStatement("$N.getString($S)", getBundleMethod, key).build();
            }
            return cb.addStatement("($T)$N.get($S)", variableType, getBundleMethod, key).build();
        }

        final MethodSpec createSetExtraMethod(Element extraElement, TypeName returnType) {
            final MethodSpec.Builder mb = MethodSpec.methodBuilder(extraElement.getSimpleName().toString())
                    .addModifiers(Modifier.PUBLIC)
                    .addParameter(ClassName.get(extraElement.asType()), extraElement.getSimpleName().toString())
                    .returns(returnType);
            mb.addCode(createPutExtraCode(extraElement, BUNDLE, getExtraKey(extraElement), extraElement.getSimpleName().toString()));
            mb.addStatement("return this");
            return mb.build();
        }

        final TypeSpec createActivityIntentBuilder(String packageName, String className) {
            final String intentBuilderName = "IntentBuilder";
            final TypeName typeName = ClassName.get(packageName, className, intentBuilderName);
            final TypeSpec.Builder intentBuilder = TypeSpec.classBuilder(intentBuilderName)
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
            for (Element extraElement : extraElements) {
                intentBuilder.addMethod(createSetExtraMethod(extraElement, typeName));
            }
            intentBuilder.addMethod(MethodSpec.methodBuilder("addFlags")
                    .addModifiers(Modifier.PUBLIC)
                    .addParameter(int.class, "flags")
                    .returns(typeName)
                    .addStatement("$N.addFlags($N)", INTENT, "flags")
                    .addStatement("return this")
                    .build());
            intentBuilder.addMethod(MethodSpec.methodBuilder("newTask")
                    .addModifiers(Modifier.PUBLIC)
                    .returns(typeName)
                    .addStatement("$N.addFlags($T.FLAG_ACTIVITY_CLEAR_TOP | $T.FLAG_ACTIVITY_CLEAR_TASK | $T.FLAG_ACTIVITY_NEW_TASK)", INTENT, AndroidClass.Intent, AndroidClass.Intent, AndroidClass.Intent)
                    .addStatement("return this")
                    .build());
            intentBuilder.addMethod(MethodSpec.methodBuilder("build")
                    .addModifiers(Modifier.PUBLIC)
                    .returns(AndroidClass.Intent)
                    .addStatement("$N.putExtras($N)", INTENT, BUNDLE)
                    .addStatement("return $N", INTENT)
                    .build());
            intentBuilder.addMethod(MethodSpec.methodBuilder("startActivity")
                    .addModifiers(Modifier.PUBLIC)
                    .returns(void.class)
                    .addStatement("$N.startActivity(build())", CONTEXT)
                    .build());
            intentBuilder.addMethod(MethodSpec.methodBuilder("startActivity")
                    .addModifiers(Modifier.PUBLIC)
                    .addParameter(AndroidClass.Bundle, "options")
                    .returns(void.class)
                    .addStatement("$N.startActivity(build(),options)", CONTEXT)
                    .build());
            /*
            intentBuilder.addMethod(MethodSpec.methodBuilder("startActivityForResult")
                    .addModifiers(Modifier.PUBLIC)
                    .addParameter(AndroidClass.Activity, "activity")
                    .addParameter(TypeName.INT, "requestCode")
                    .returns(void.class)
                    .addStatement("activity.startActivityForResult(build(),requestCode)")
                    .build());
            intentBuilder.addMethod(MethodSpec.methodBuilder("startActivityForResult")
                    .addModifiers(Modifier.PUBLIC)
                    .addParameter(AndroidClass.Fragment, "fragment")
                    .addParameter(TypeName.INT, "requestCode")
                    .returns(void.class)
                    .addStatement("fragment.startActivityForResult(build(),requestCode)")
                    .build());
            intentBuilder.addMethod(MethodSpec.methodBuilder("startActivityForResult")
                    .addModifiers(Modifier.PUBLIC)
                    .addParameter(AndroidClass.Activity, "activity")
                    .addParameter(TypeName.INT, "requestCode")
                    .addParameter(AndroidClass.Bundle, "options")
                    .returns(void.class)
                    .addStatement("activity.startActivityForResult(build(),requestCode,options)")
                    .build());
            intentBuilder.addMethod(MethodSpec.methodBuilder("startActivityForResult")
                    .addModifiers(Modifier.PUBLIC)
                    .addParameter(AndroidClass.Fragment, "fragment")
                    .addParameter(TypeName.INT, "requestCode")
                    .addParameter(AndroidClass.Bundle, "options")
                    .returns(void.class)
                    .addStatement("fragment.startActivityForResult(build(),requestCode,options)")
                    .build());
            */
            return intentBuilder.build();
        }

        final TypeSpec createArgument(String packageName, String className) {
            final ClassName fragmentType = ClassName.get(packageName, className);
            final TypeSpec.Builder argumentBuilder = TypeSpec.classBuilder(ARGUMENT_BUILDER)
                    .addModifiers(Modifier.PUBLIC, Modifier.STATIC);
            argumentBuilder.addField(FieldSpec.builder(AndroidClass.Bundle, BUNDLE)
                    .addModifiers(Modifier.PRIVATE, Modifier.FINAL)
                    .initializer("new $T()", AndroidClass.Bundle)
                    .build());
            for (Element extraElement : extraElements) {
                argumentBuilder.addMethod(createSetExtraMethod(extraElement,
                        ClassName.get(packageName, className, ARGUMENT_BUILDER)));
            }
            argumentBuilder.addMethod(MethodSpec.methodBuilder("build")
                    .addModifiers(Modifier.PUBLIC)
                    .returns(fragmentType)
                    .addStatement("$T $N = new $T()", fragmentType, FRAGMENT, fragmentType)
                    .addStatement("$N.setArguments($N)", FRAGMENT, BUNDLE)
                    .addStatement("return $N", FRAGMENT)
                    .build());
            return argumentBuilder.build();
        }
    }
}
