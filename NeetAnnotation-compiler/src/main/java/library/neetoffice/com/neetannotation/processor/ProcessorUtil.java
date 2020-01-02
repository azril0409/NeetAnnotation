package library.neetoffice.com.neetannotation.processor;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.TypeName;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.AnnotationValue;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Name;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeMirror;
import javax.tools.Diagnostic;

public class ProcessorUtil {
    final ProcessingEnvironment processingEnv;

    public ProcessorUtil(ProcessingEnvironment processingEnv) {
        this.processingEnv = processingEnv;
    }

    void printMessage(Diagnostic.Kind kind, CharSequence message) {
        processingEnv.getMessager().printMessage(kind, message);
    }

    String getPackageName(Element element) {
        return processingEnv.getElementUtils().getPackageOf(element).getQualifiedName().toString();
    }

    public TypeElement asElement(VariableElement varEle) {
        final TypeMirror typeMirror = varEle.asType();
        final Element element = processingEnv.getTypeUtils().asElement(typeMirror);
        return (element instanceof TypeElement) ? (TypeElement) element : null;
    }

    void writeTo(JavaFile javaFile) {
        try {
            javaFile.writeTo(processingEnv.getFiler());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    TypeName getClassName(TypeMirror typeMirror) {
        final TypeName typeName = ClassName.get(typeMirror);
        if (typeName.isPrimitive()) {
            if ("char".equals(typeMirror.toString())) {
                return ClassName.get(Character.class);
            } else if ("byte".equals(typeMirror.toString())) {
                return ClassName.get(Byte.class);
            } else if ("short".equals(typeMirror.toString())) {
                return ClassName.get(Short.class);
            } else if ("int".equals(typeMirror.toString())) {
                return ClassName.get(Integer.class);
            } else if ("long".equals(typeMirror.toString())) {
                return ClassName.get(Long.class);
            } else if ("float".equals(typeMirror.toString())) {
                return ClassName.get(Float.class);
            } else if ("double".equals(typeMirror.toString())) {
                return ClassName.get(Double.class);
            } else if ("boolean".equals(typeMirror.toString())) {
                return ClassName.get(Boolean.class);
            }
        }
        return typeName;
    }

    public String toModelCase(Name name) {
        return name.toString().charAt(0) + name.toString().toLowerCase().substring(1);
    }

    public boolean isSubActivity(TypeElement element) {
        return isInstanceOf(element, AndroidClass.Activity);
    }

    public boolean isSubFragment(TypeElement element) {
        return isInstanceOf(element, AndroidClass.Fragment);
    }

    public boolean isSubAndroidViewModel(TypeElement element) {
        return isInstanceOf(element, AndroidClass.AndroidViewModel);
    }

    private boolean isInstanceOf(TypeElement element, ClassName className) {
        return isInstanceOf(element.asType(),className);
    }

    public boolean isInstanceOf(TypeMirror elementClass, ClassName subClass) {
       return processingEnv.getTypeUtils().isAssignable(processingEnv.getTypeUtils().erasure(elementClass), processingEnv.getElementUtils().getTypeElement(subClass.toString()).asType());
    }

    Object findAnnotationValue(Element element, Class<?> annotationClass, String executableName) {
        Object value = null;
        final List<? extends AnnotationMirror> annotationMirrors = element.getAnnotationMirrors();
        for (AnnotationMirror annotationMirror : annotationMirrors) {
            final Map<? extends ExecutableElement, ? extends AnnotationValue> map = annotationMirror.getElementValues();
            if (annotationClass.getName().equals(annotationMirror.getAnnotationType().toString())) {
                for (ExecutableElement executable : map.keySet()) {
                    final AnnotationValue annotationValue = map.get(executable);
                    if (executableName.equals(executable.getSimpleName().toString())) {
                        value = annotationValue.getValue();
                    }
                }
            }
        }
        return value;
    }

    Object findAnnotationValue(Element element, TypeName annotationClass, String executableName) {
        Object value = null;
        AnnotationMirror annotationMirror = AnnotationHelp.findAnnotationMirror(element, annotationClass);
        if (annotationMirror != null) {
            value = AnnotationHelp.findAnnotationValue(annotationMirror, executableName);
        }
        return value;
    }
}