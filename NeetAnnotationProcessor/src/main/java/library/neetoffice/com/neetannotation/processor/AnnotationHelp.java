package library.neetoffice.com.neetannotation.processor;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.TypeName;

import java.util.List;
import java.util.Map;

import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.AnnotationValue;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.VariableElement;

public class AnnotationHelp {

    public static AnnotationMirror findAnnotationMirror(Element element, Class<?> AnnotationClass) {
        AnnotationMirror annotationMirror = null;
        final List<? extends AnnotationMirror> annotationMirrors = element.getAnnotationMirrors();
        for (AnnotationMirror annotation : annotationMirrors) {
            if (AnnotationClass.getName().equals(annotation.getAnnotationType().toString())) {
                annotationMirror = annotation;
            }
        }
        return annotationMirror;
    }

    public static AnnotationMirror findAnnotationMirror(Element element, TypeName AnnotationClass) {
        AnnotationMirror annotationMirror = null;
        final List<? extends AnnotationMirror> annotationMirrors = element.getAnnotationMirrors();
        for (AnnotationMirror annotation : annotationMirrors) {
            if (AnnotationClass.toString().equals(annotation.getAnnotationType().toString())) {
                annotationMirror = annotation;
            }
        }
        return annotationMirror;
    }

    public static Object findAnnotationValue(AnnotationMirror annotationMirror, String name) {
        final Map<? extends ExecutableElement, ? extends AnnotationValue> map = annotationMirror.getElementValues();
        Object value = null;
        for (ExecutableElement element : map.keySet()) {
            final AnnotationValue annotationValue = map.get(element);
            if (name.equals(element.getSimpleName().toString())) {
                value = annotationValue.getValue();
            }
        }
        return value;
    }

    public static CodeBlock addNullCode(VariableElement parameter) {
        final TypeName typeName = ClassName.get(parameter.asType());
        if (typeName.isPrimitive()) {
            if ("char".equals(parameter.asType().toString())) {
                return CodeBlock.builder().add("(char)0").build();
            } else if ("byte".equals(parameter.asType().toString())) {
                return CodeBlock.builder().add("(byte)0").build();
            } else if ("short".equals(parameter.asType().toString())) {
                return CodeBlock.builder().add("(short)0").build();
            } else if ("int".equals(parameter.asType().toString())) {
                return CodeBlock.builder().add("(int)0").build();
            } else if ("long".equals(parameter.asType().toString())) {
                return CodeBlock.builder().add("(long)0").build();
            } else if ("float".equals(parameter.asType().toString())) {
                return CodeBlock.builder().add("(float)0").build();
            } else if ("double".equals(parameter.asType().toString())) {
                return CodeBlock.builder().add("(double)0").build();
            } else if ("boolean".equals(parameter.asType().toString())) {
                return CodeBlock.builder().add("false").build();
            }
        }
        return CodeBlock.builder().add("null").build();
    }
}
