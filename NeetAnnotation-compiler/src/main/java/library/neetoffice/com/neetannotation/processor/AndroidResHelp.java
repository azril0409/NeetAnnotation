package library.neetoffice.com.neetannotation.processor;

import com.squareup.javapoet.CodeBlock;

import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.Element;
import javax.lang.model.element.Name;

import library.neetoffice.com.neetannotation.NDagger;
import library.neetoffice.com.neetannotation.ViewById;

public class AndroidResHelp {
    private static CodeBlock createCodeBlock(String resName, String elementName, String context_from, String defType, String defPackage) {
        if (!resName.isEmpty()) {
            return CodeBlock.builder().add("$N.getResources().getIdentifier($S, $S, $N)", context_from, resName, defType, defPackage).build();
        } else if (!elementName.isEmpty()) {
            return CodeBlock.builder().add("$N.getResources().getIdentifier($S, $S, $N)", context_from, elementName, defType, defPackage).build();
        } else {
            return CodeBlock.builder().build();
        }
    }

    public static String parseResName(String value, String resName) {
        if (value.isEmpty()) {
            return resName;
        }
        return value;
    }

    public static CodeBlock id(String resName, String elementName, String context_from, String defPackage) {
        return createCodeBlock(resName, elementName, context_from, "id", defPackage);
    }

    public static CodeBlock layout(String resName, String elementName, String context_from, String defPackage) {
        return createCodeBlock(resName, elementName, context_from, "layout", defPackage);
    }

    public static CodeBlock string(String resName, String elementName, String context_from, String defPackage) {
        return createCodeBlock(resName, elementName, context_from, "string", defPackage);
    }

    public static CodeBlock array(String resName, String elementName, String context_from, String defPackage) {
        return createCodeBlock(resName, elementName, context_from, "array", defPackage);
    }

    public static CodeBlock bool(String resName, String elementName, String context_from, String defPackage) {
        return createCodeBlock(resName, elementName, context_from, "bool", defPackage);
    }

    public static CodeBlock dimen(String resName, String elementName, String context_from, String defPackage) {
        return createCodeBlock(resName, elementName, context_from, "dimen", defPackage);
    }

    public static CodeBlock integer(String resName, String elementName, String context_from, String defPackage) {
        return createCodeBlock(resName, elementName, context_from, "integer", defPackage);
    }

    public static CodeBlock color(String resName, String elementName, String context_from, String defPackage) {
        return createCodeBlock(resName, elementName, context_from, "color", defPackage);
    }

    public static CodeBlock drawable(String resName, String elementName, String context_from, String defPackage) {
        return createCodeBlock(resName, elementName, context_from, "drawable", defPackage);
    }

    public static CodeBlock anim(String resName, String elementName, String context_from, String defPackage) {
        return createCodeBlock(resName, elementName, context_from, "anim", defPackage);
    }

    public static CodeBlock menu(String resName, String elementName, String context_from, String defPackage) {
        return createCodeBlock(resName, elementName, context_from, "menu", defPackage);
    }
}
