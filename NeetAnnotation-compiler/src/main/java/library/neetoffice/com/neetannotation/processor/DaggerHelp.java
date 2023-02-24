package library.neetoffice.com.neetannotation.processor;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.TypeName;

import java.util.HashSet;
import java.util.Set;

import javax.annotation.processing.RoundEnvironment;
import javax.inject.Inject;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;

import library.neetoffice.com.neetannotation.InjectInitialEntity;

public class DaggerHelp {
    private static Set<Element> injectSets = new HashSet<>();

    public static void init(RoundEnvironment roundEnv) {
        for (Element element : roundEnv.getElementsAnnotatedWith(Inject.class)) {
            injectSets.add(element.getEnclosingElement());
        }
        for (Element element : roundEnv.getElementsAnnotatedWith(InjectInitialEntity.class)) {
            injectSets.add(element.getEnclosingElement());
        }
    }

    public static void release() {
        injectSets.clear();
    }


    public static boolean process(BaseCreator baseCreator, TypeElement typeElement) {
        boolean needCreateComponent = false;
        for (Element element : injectSets) {
            if (baseCreator.isInstanceOf(typeElement.asType(), TypeName.get(element.asType()))) {
                for (Element filedElement : element.getEnclosedElements()) {
                    final Inject aInject = filedElement.getAnnotation(Inject.class);
                    if (aInject != null) {
                        needCreateComponent = true;
                    }
                    final InjectInitialEntity aInjectInitialEntity = filedElement.getAnnotation(InjectInitialEntity.class);
                    if (aInjectInitialEntity != null) {
                        needCreateComponent = true;
                    }
                    if (needCreateComponent) {
                        break;
                    }
                }
            }
        }
        return needCreateComponent;
    }

    static String findNameFromDagger(BaseCreator creator, Element element) {
        final Object value = creator.findAnnotationValue(element, DaggerClass.Named, "value");
        if (value == null) {
            return element.getSimpleName().toString();
        }
        return "n_" + value.toString();
    }
}
