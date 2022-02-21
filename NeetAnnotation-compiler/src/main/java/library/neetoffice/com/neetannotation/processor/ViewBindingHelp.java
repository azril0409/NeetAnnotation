package library.neetoffice.com.neetannotation.processor;

import com.squareup.javapoet.ClassName;

import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.Element;

public class ViewBindingHelp {
    static ClassName getViewBindingClass(Element element, Class<?> annotationClass) {
        final AnnotationMirror aNActivityMirror = AnnotationHelp.findAnnotationMirror(element, annotationClass);
        Object viewBindingObject = AnnotationHelp.findAnnotationValue(aNActivityMirror, "value");
        if (viewBindingObject == null) {
            viewBindingObject = AnnotationHelp.findAnnotationValue(aNActivityMirror, "viewBinding");
        }
        if (viewBindingObject == null) {
            return null;
        }
        final String viewBindingString = viewBindingObject.toString();
        final String[] split = viewBindingString.split("\\.");
        if (split.length < 1) {
            return null;
        }
        final String className = split[split.length - 1];
        final String packageName = viewBindingString.substring(0, viewBindingString.length() - className.length() - 1);
        if ("databinding".equals(split[split.length - 2]) && className.endsWith("Binding")) {
            return ClassName.get(packageName, className);
        }
        return null;
    }

    static Boolean haveViewBindingClass(Element element, Class<?> annotationClass) {
        final ClassName className = getViewBindingClass(element, annotationClass);
        return className != null;
    }
}
