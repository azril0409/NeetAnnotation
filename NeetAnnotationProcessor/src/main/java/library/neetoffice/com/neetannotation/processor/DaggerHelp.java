package library.neetoffice.com.neetannotation.processor;

import java.util.List;

import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;

import library.neetoffice.com.neetannotation.NActivity;
import library.neetoffice.com.neetannotation.NDagger;

public class DaggerHelp {
    private final ProcessorUtil processorUtil;

    public DaggerHelp(ProcessorUtil processorUtil) {
        this.processorUtil = processorUtil;
    }

    public boolean process(TypeElement typeElement) {
        AnnotationMirror aNDagger = null;
        final List<? extends AnnotationMirror> annotationMirrors = typeElement.getAnnotationMirrors();
        for (AnnotationMirror annotation : annotationMirrors) {
            if (NDagger.class.getName().equals(annotation.getAnnotationType().toString())) {
                aNDagger = annotation;
            }
        }
        return aNDagger != null;
    }

    String createDaggerMethodName(Element element) {
        final AnnotationMirror named = AnnotationHelp.findAnnotationMirror(element, DaggerClass.Named);
        if (named == null) {
            return element.getSimpleName().toString();
        }
        final Object value = AnnotationHelp.findAnnotationValue(named, "value");
        if (value == null) {
            return element.getSimpleName().toString();
        }
        return "n_" + value.toString();
    }

    String findNameFromDagger(Element element) {
        final AnnotationMirror named = AnnotationHelp.findAnnotationMirror(element, DaggerClass.Named);
        if (named == null) {
            return element.getSimpleName().toString();
        }
        final Object value = AnnotationHelp.findAnnotationValue(named, "value");
        if (value == null) {
            return element.getSimpleName().toString();
        }
        return "n_" + value.toString();
    }
}
