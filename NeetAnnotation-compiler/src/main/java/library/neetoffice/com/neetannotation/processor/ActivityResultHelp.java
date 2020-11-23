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
import java.util.HashMap;
import java.util.Iterator;

import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeMirror;

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

public class ActivityResultHelp {
    private final BaseCreator creator;
    private final ExtraHelp extraHelp;

    public ActivityResultHelp(BaseCreator creator) {
        this.creator = creator;
        this.extraHelp = new ExtraHelp(creator);
    }

    public Builder builder(String requestCodeName, String resultCodeName, String intentDataName) {
        return new Builder(creator, extraHelp.builder("", ""), requestCodeName, resultCodeName, intentDataName);
    }

    public static class Builder {
        private final BaseCreator creator;
        private final ExtraHelp.Builder extraHelp;
        private final String requestCodeName;
        private final String resultCodeName;
        private final String intentDataName;
        private final ArrayList<Element> elements = new ArrayList<>();

        public Builder(BaseCreator creator, ExtraHelp.Builder extraHelp, String requestCodeName, String resultCodeName, String intentDataName) {
            this.creator = creator;
            this.extraHelp = extraHelp;
            this.requestCodeName = requestCodeName;
            this.resultCodeName = resultCodeName;
            this.intentDataName = intentDataName;
        }

        void parseElement(Element element) {
            final ActivityResult aActivityResult = element.getAnnotation(ActivityResult.class);
            if (aActivityResult != null) {
                elements.add(element);
            }
        }

        CodeBlock createOnActivityResultCode() {
            final CodeBlock.Builder code = CodeBlock.builder();
            for (int i = 0; i < elements.size(); i++) {
                final Element element = elements.get(i);
                if (element instanceof ExecutableElement) {
                    final ActivityResult aActivityResult = element.getAnnotation(ActivityResult.class);
                    code.beginControlFlow("if($N == $L && $N == $L)", requestCodeName, aActivityResult.value(), resultCodeName, aActivityResult.resultCode());
                    code.add(createActivityResultCode((ExecutableElement) element));
                    code.endControlFlow();
                }
            }
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
                        parameterCode.add(creator.addNullCode(parameterType));
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

        /*
        String getKey(VariableElement parameter) {
            final ActivityResult.Extra aExtra = parameter.getAnnotation(ActivityResult.Extra.class);
            if (aExtra.value().isEmpty()) {
                return parameter.getSimpleName().toString();
            }
            return aExtra.value();
        }
        */

        TypeSpec createActivityIntentBuilder(String packageName, String className) {
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


            for (Element extraElement : elements) {
                if (extraElement instanceof ExecutableElement) {
                    final MethodSpec.Builder mb = MethodSpec.methodBuilder(extraElement.getSimpleName().toString())
                            .addModifiers(Modifier.PUBLIC)
                            .returns(typeName);
                    final Iterator<? extends VariableElement> parameters = ((ExecutableElement) extraElement).getParameters().iterator();
                    while (parameters.hasNext()) {
                        final VariableElement variableElement = parameters.next();
                        final ParameterSpec parameter = ParameterSpec.get(variableElement);
                        mb.addParameter(parameter.type, parameter.name);
                        mb.addCode(extraHelp.createPutExtraCode(variableElement, ExtraHelp.BUNDLE, getExtraKey(variableElement), parameter.name));
                    }
                    mb.addStatement("return this");
                    activityResult.addMethod(mb.build());
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

        String getExtraKey(Element element) {
            final ActivityResult.Extra arExtra = element.getAnnotation(ActivityResult.Extra.class);
            final String key;
            if (arExtra != null && !arExtra.value().isEmpty()) {
                key = arExtra.value();
            } else {
                key = "_" + element.getSimpleName().toString().toUpperCase();
            }
            return key;
        }
    }
}
