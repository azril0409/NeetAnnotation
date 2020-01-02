package library.neetoffice.com.neetannotation.processor;

import com.squareup.javapoet.CodeBlock;

import java.util.ArrayList;
import java.util.Iterator;

import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.VariableElement;
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

public class ActivityResultHelp {
    private final BaseCreator creator;

    public ActivityResultHelp(BaseCreator creator) {
        this.creator = creator;
    }

    public Builder builder(String requestCodeName, String resultCodeName, String intentDataName) {
        return new Builder(creator, requestCodeName, resultCodeName, intentDataName);
    }

    public static class Builder {
        private final BaseCreator creator;
        private final String requestCodeName;
        private final String resultCodeName;
        private final String intentDataName;
        private final ArrayList<Element> elements = new ArrayList<>();

        public Builder(BaseCreator creator, String requestCodeName, String resultCodeName, String intentDataName) {
            this.creator = creator;
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
                    final String key = getKey(parameter);
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

        String getKey(VariableElement parameter) {
            final ActivityResult.Extra aExtra = parameter.getAnnotation(ActivityResult.Extra.class);
            if (aExtra.value().isEmpty()) {
                return parameter.getSimpleName().toString();
            }
            return aExtra.value();
        }
    }
}
