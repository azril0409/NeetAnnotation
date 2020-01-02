package library.neetoffice.com.neetannotation.processor;

import java.util.List;

import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;

import library.neetoffice.com.neetannotation.NActivity;
import library.neetoffice.com.neetannotation.NDagger;

public class DaggerHelp {

    public static boolean process(TypeElement typeElement) {
        AnnotationMirror aNDagger = null;
        final List<? extends AnnotationMirror> annotationMirrors = typeElement.getAnnotationMirrors();
        for (AnnotationMirror annotation : annotationMirrors) {
            if (NDagger.class.getName().equals(annotation.getAnnotationType().toString())) {
                aNDagger = annotation;
            }
        }
        return aNDagger != null;
    }

    static String findNameFromDagger(BaseCreator creator, Element element) {
        final Object value = creator.findAnnotationValue(element, DaggerClass.Named, "value");
        if (value == null) {
            return element.getSimpleName().toString();
        }
        return "n_" + value.toString();
    }
}
