package library.neetoffice.com.neetannotation.processor;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeMirror;
import javax.tools.Diagnostic;

import library.neetoffice.com.neetannotation.DefaultBoolean;
import library.neetoffice.com.neetannotation.DefaultByte;
import library.neetoffice.com.neetannotation.DefaultChar;
import library.neetoffice.com.neetannotation.DefaultDouble;
import library.neetoffice.com.neetannotation.DefaultFloat;
import library.neetoffice.com.neetannotation.DefaultInt;
import library.neetoffice.com.neetannotation.DefaultLong;
import library.neetoffice.com.neetannotation.DefaultShort;
import library.neetoffice.com.neetannotation.Extra;
import library.neetoffice.com.neetannotation.SaveInstance;

public class ExtraHelp {
    private static final String BUNDLE = "bundle";
    private static final String CONTEXT = "context";
    private static final String INTENT = "intent";
    private final BaseCreator creator;

    public ExtraHelp(BaseCreator creator) {
        this.creator = creator;
    }

    public Builder builder(String getBundleMethod) {
        return new Builder(creator, getBundleMethod);
    }

    public static class Builder {
        private final BaseCreator creator;
        private final String getBundleMethod;
        private final ArrayList<Element> extraElements = new ArrayList<>();
        private final ArrayList<Element> saveInstanceElements = new ArrayList<>();

        public Builder(BaseCreator creator, String getBundleMethod) {
            this.creator = creator;
            this.getBundleMethod = getBundleMethod;
        }

        private String getExtraKey(Element element) {
            final Extra aExtra = element.getAnnotation(Extra.class);
            final String key;
            if (aExtra.value().isEmpty()) {
                key = "_" + element.getSimpleName().toString().toUpperCase();
            } else {
                key = aExtra.value();
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
            return typeMirror.toString().substring(typeMirror.toString().length() - 2, typeMirror.toString().length()).equals("[]");
        }

        private boolean isCollection(TypeMirror typeMirror) {
            return creator.isInstanceOf(typeMirror, ClassName.get(Collection.class));
        }

        private boolean isParcelable(TypeMirror typeMirror) {
            return creator.isInstanceOf(typeMirror, (AndroidClass.Parcelable));
        }

        private boolean isSerializable(TypeMirror typeMirror) {
            return creator.isInstanceOf(typeMirror, ClassName.get(Serializable.class));
        }

        CodeBlock createPutExtraCode(Element extraElement, String bundleName, String key, String varName) {
            final CodeBlock.Builder code = CodeBlock.builder();
            final TypeMirror extraType = extraElement.asType();
            final String extraTypeString = extraType.toString();
            if (isArray(extraType)) {
                final String parameterTypeString = extraTypeString.substring(0, extraTypeString.length() - 2);
                if ("byte".equals(parameterTypeString)) {
                    code.addStatement("$N.putByteArray($S,$N)", bundleName, key, varName);
                } else if ("char".equals(parameterTypeString)) {
                    code.addStatement("$N.putCharArray($S,$N)", bundleName, key, varName);
                } else if ("short".equals(parameterTypeString)) {
                    code.addStatement("$N.putShortArray($S,$N)", bundleName, key, varName);
                } else if ("int".equals(parameterTypeString)) {
                    code.addStatement("$N.putIntArray($S,$N)", bundleName, key, varName);
                } else if ("long".equals(parameterTypeString)) {
                    code.addStatement("$N.putLongArray($S,$N)", bundleName, key, varName);
                } else if ("float".equals(parameterTypeString)) {
                    code.addStatement("$N.putFloatArray($S,$N)", bundleName, key, varName);
                } else if ("double".equals(parameterTypeString)) {
                    code.addStatement("$N.putDoubleArray($S,$N)", bundleName, key, varName);
                } else if ("boolean".equals(parameterTypeString)) {
                    code.addStatement("$N.putBooleanArray($S,$N)", bundleName, key, varName);
                } else if (ClassName.get(CharSequence.class).toString().equals(parameterTypeString)) {
                    code.addStatement("$N.putCharSequenceArray($S,$N)", bundleName, key, varName);
                } else if (ClassName.get(String.class).toString().equals(parameterTypeString)) {
                    code.addStatement("$N.putStringArray($S,$N)", bundleName, key, varName);
                } else {
                    code.addStatement("$N.putParcelableArray($S,$N)", bundleName, key, varName);
                }
            } else if (isCollection(extraElement.asType())) {
                final DeclaredType declaredType = (DeclaredType) extraElement.asType();
                if (ClassName.get(declaredType.getTypeArguments().get(0)).equals(ClassName.get(Integer.class))) {
                    code.addStatement("$N.putIntegerArrayList($S,new $T<>($N))", bundleName, key, ArrayList.class, varName);
                } else if (ClassName.get(declaredType.getTypeArguments().get(0)).equals(ClassName.get(CharSequence.class))) {
                    code.addStatement("$N.putCharSequenceArrayList($S,new $T<>($N))", bundleName, key, ArrayList.class, varName);
                } else if (ClassName.get(declaredType.getTypeArguments().get(0)).equals(ClassName.get(String.class))) {
                    code.addStatement("$N.putStringArrayList($S,new $T<>($N))", bundleName, key, ArrayList.class, varName);
                } else {
                    code.addStatement("$N.putParcelableArrayList($S,new $T<>($N))", bundleName, key, ArrayList.class, varName);
                }
            } else if (isParcelable(extraElement.asType())) {
                code.addStatement("$N.putParcelable($S,$N)", bundleName, key, varName);
            } else if (isSerializable(extraElement.asType())) {
                code.addStatement("$N.putSerializable($S,$N)", bundleName, key, varName);
            } else if (creator.isInstanceOf(extraElement.asType(), AndroidClass.Size)) {
                code.addStatement("$N.putSize($S,$N)", bundleName, key, varName);
            } else if (creator.isInstanceOf(extraElement.asType(), AndroidClass.SizeF)) {
                code.addStatement("$N.putSizeF($S,$N)", bundleName, key, varName);
            } else {
                if ("byte".equals(extraTypeString)) {
                    code.addStatement("$N.putByte($S,$N)", bundleName, key, varName);
                } else if ("char".equals(extraTypeString)) {
                    code.addStatement("$N.putChar($S,$N)", bundleName, key, varName);
                } else if ("short".equals(extraTypeString)) {
                    code.addStatement("$N.putShort($S,$N)", bundleName, key, varName);
                } else if ("int".equals(extraTypeString)) {
                    code.addStatement("$N.putInt($S,$N)", bundleName, key, varName);
                } else if ("long".equals(extraTypeString)) {
                    code.addStatement("$N.putLong($S,$N)", bundleName, key, varName);
                } else if ("float".equals(extraTypeString)) {
                    code.addStatement("$N.putFloat($S,$N)", bundleName, key, varName);
                } else if ("double".equals(extraTypeString)) {
                    code.addStatement("$N.putDouble($S,$N)", bundleName, key, varName);
                } else if ("boolean".equals(extraTypeString)) {
                    code.addStatement("$N.putBoolean($S,$N)", bundleName, key, varName);
                } else if (ClassName.get(CharSequence.class).toString().equals(extraTypeString)) {
                    code.addStatement("$N.putCharSequence($S,$N)", bundleName, key, varName);
                } else if (ClassName.get(String.class).toString().equals(extraTypeString)) {
                    code.addStatement("$N.putString($S,$N)", bundleName, key, varName);
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

        CodeBlock createSaveInstanceState(String outStateName) {
            final CodeBlock.Builder code = CodeBlock.builder();
            for (Element element : saveInstanceElements) {
                code.add(createPutExtraCode(element, outStateName, getSaveInstanceKey(element), element.getSimpleName().toString()));
            }
            return code.build();
        }

        CodeBlock createGetExtra(String savedInstanceStateName) {
            final CodeBlock.Builder code = CodeBlock.builder();
            for (Element element : extraElements) {
                code.add(createGetExtra(element));
            }
            if (!saveInstanceElements.isEmpty()) {
                code.beginControlFlow("if($N != null)", savedInstanceStateName);
                for (Element element : saveInstanceElements) {
                    code.add(createGetExtra(element));
                }
                code.endControlFlow();
            }
            return code.build();
        }

        private CodeBlock createGetExtra(Element element) {
            final String key = getExtraKey(element);
            final TypeMirror variableType = element.asType();
            final CodeBlock.Builder cb = CodeBlock.builder().add("$N = ", element.getSimpleName());
            if ("char".equals(variableType.toString())) {
                final DefaultChar aDefaultChar = element.getAnnotation(DefaultChar.class);
                if (aDefaultChar != null) {
                    return cb.addStatement("$N.getChar($S,(char)$L)", getBundleMethod, key, aDefaultChar.value())
                            .build();
                }
                return cb.addStatement("$N.getChar($S)", getBundleMethod, key)
                        .build();
            } else if ("byte".equals(variableType.toString())) {
                final DefaultByte aDefaultByte = element.getAnnotation(DefaultByte.class);
                if (aDefaultByte != null) {
                    return cb.addStatement("$N.getByte($S,(byte)$L)", getBundleMethod, key, aDefaultByte.value())
                            .build();
                }
                return cb.addStatement("$N.getByte($S)", getBundleMethod, key)
                        .build();
            } else if ("short".equals(variableType.toString())) {
                final DefaultShort aDefaultShort = element.getAnnotation(DefaultShort.class);
                if (aDefaultShort != null) {
                    return cb.addStatement("$N.getShort($S,(byte)$L)", getBundleMethod, key, aDefaultShort.value())
                            .build();
                }
                return cb.addStatement("$N.getShort($S)", getBundleMethod, key)
                        .build();
            } else if ("int".equals(variableType.toString())) {
                final DefaultInt aDefaultInt = element.getAnnotation(DefaultInt.class);
                if (aDefaultInt != null) {
                    return cb.addStatement("$N.getInt($S,(byte)$L)", getBundleMethod, key, aDefaultInt.value())
                            .build();
                }
                return cb.addStatement("$N.getInt($S)", getBundleMethod, key)
                        .build();
            } else if ("long".equals(variableType.toString())) {
                final DefaultLong aDefaultLong = element.getAnnotation(DefaultLong.class);
                if (aDefaultLong != null) {
                    return cb.addStatement("$N.getLong($S,(byte)$L)", getBundleMethod, key, aDefaultLong.value())
                            .build();
                }
                return cb.addStatement("$N.getLong($S)", getBundleMethod, key)
                        .build();
            } else if ("float".equals(variableType.toString())) {
                final DefaultFloat aDefaultFloat = element.getAnnotation(DefaultFloat.class);
                if (aDefaultFloat != null) {
                    return cb.addStatement("$N.getFloat($S,(byte)$L)", getBundleMethod, key, aDefaultFloat.value())
                            .build();
                }
                return cb.addStatement("$N.getFloat($S)", getBundleMethod, key)
                        .build();
            } else if ("double".equals(variableType.toString())) {
                final DefaultDouble aDefaultDouble = element.getAnnotation(DefaultDouble.class);
                if (aDefaultDouble != null) {
                    return cb.addStatement("$N.getDouble($S,(byte)$L)", getBundleMethod, key, aDefaultDouble.value())
                            .build();
                }
                return cb.addStatement("$N.getDouble($S)", getBundleMethod, key)
                        .build();
            } else if ("boolean".equals(variableType.toString())) {
                final DefaultBoolean aDefaultBoolean = element.getAnnotation(DefaultBoolean.class);
                if (aDefaultBoolean != null) {
                    return cb.addStatement("$N.getBoolean($S,$L)", getBundleMethod, key, aDefaultBoolean.value())
                            .build();
                }
                return cb.addStatement("$N.getBoolean($S)", getBundleMethod, key)
                        .build();
            }
            return cb.addStatement("($T)$N.get($S)", variableType, getBundleMethod, key)
                    .build();
        }

        MethodSpec createSetExtraMethod(VariableElement extraElement, TypeName returnType) {
            final ParameterSpec parameter = ParameterSpec.get(extraElement);
            final MethodSpec.Builder mb = MethodSpec.methodBuilder(extraElement.getSimpleName().toString())
                    .addModifiers(Modifier.PUBLIC)
                    .addParameter(parameter.type, parameter.name)
                    .returns(returnType);
            mb.addCode(createPutExtraCode(extraElement, BUNDLE, getExtraKey(extraElement), parameter.name));
            mb.addStatement("return this");
            return mb.build();
        }

        TypeSpec createNewIntent(String packageName, String className) {
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
                intentBuilder.addMethod(createSetExtraMethod((VariableElement) extraElement, typeName));
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
                    .addStatement("intent.putExtras(bundle)")
                    .addStatement("return intent")
                    .build());
            intentBuilder.addMethod(MethodSpec.methodBuilder("startActivity")
                    .addModifiers(Modifier.PUBLIC)
                    .returns(void.class)
                    .addStatement("$N.startActivity(build())", CONTEXT)
                    .build());
            return intentBuilder.build();
        }

        TypeSpec createArgument(String packageName, String className) {
            final ClassName fragmentType = ClassName.get(packageName, className);
            final TypeSpec.Builder argumentBuilder = TypeSpec.classBuilder("ArgumentBuilder")
                    .addModifiers(Modifier.PUBLIC, Modifier.STATIC);
            argumentBuilder.addField(FieldSpec.builder(AndroidClass.Bundle, BUNDLE)
                    .addModifiers(Modifier.PRIVATE, Modifier.FINAL)
                    .initializer("new $T()", AndroidClass.Bundle)
                    .build());
            for (Element extraElement : extraElements) {
                argumentBuilder.addMethod(createSetExtraMethod((VariableElement) extraElement,
                        ClassName.get(packageName, className, "ArgumentBuilder")));
            }
            final String FRAGMENT = "fragment";
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
