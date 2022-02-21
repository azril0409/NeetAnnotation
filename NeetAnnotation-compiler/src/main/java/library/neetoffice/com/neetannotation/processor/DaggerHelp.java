package library.neetoffice.com.neetannotation.processor;

import javax.inject.Inject;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;

public class DaggerHelp {

    public static boolean process(TypeElement typeElement) {
        boolean needCreateComponent = false;
        for (Element element : typeElement.getEnclosedElements()) {
            Inject aInject = element.getAnnotation(Inject.class);
            if (aInject != null) {
                needCreateComponent = true;
            }
            if (needCreateComponent) {
                break;
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
