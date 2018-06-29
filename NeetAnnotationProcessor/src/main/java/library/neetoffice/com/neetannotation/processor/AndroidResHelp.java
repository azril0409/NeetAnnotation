package library.neetoffice.com.neetannotation.processor;

import com.squareup.javapoet.CodeBlock;

import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.Element;
import javax.lang.model.element.Name;

import library.neetoffice.com.neetannotation.NDagger;
import library.neetoffice.com.neetannotation.ViewById;

public class AndroidResHelp {
    private static CodeBlock createCodeBlock(int rid, String resName, String elementName, String context_from, String defType, String defPackage) {
        if (rid > 0) {
            return CodeBlock.builder().add(String.valueOf(rid)).build();
        } else if (!resName.isEmpty()) {
            return CodeBlock.builder().add("$N.getResources().getIdentifier($S, $S, $N)",context_from, resName, defType, defPackage).build();
        }
        return CodeBlock.builder().add("$N.getResources().getIdentifier($S, $S, $N)",context_from, elementName, defType, defPackage).build();
    }

    public static CodeBlock id(int rid, String resName, String elementName, String context_from, String defPackage) {
        return createCodeBlock(rid, resName, elementName, context_from, "id", defPackage);
    }

    public static CodeBlock layout(int rid, String resName, String elementName, String context_from, String defPackage) {
        return createCodeBlock(rid, resName, elementName, context_from, "layout", defPackage);
    }

    public static CodeBlock string(int rid, String resName, String elementName, String context_from, String defPackage) {
        return createCodeBlock(rid, resName, elementName, context_from, "string", defPackage);
    }

    public static CodeBlock array(int rid, String resName, String elementName, String context_from, String defPackage) {
        return createCodeBlock(rid, resName, elementName, context_from, "array", defPackage);
    }

    public static CodeBlock bool(int rid, String resName, String elementName, String context_from, String defPackage) {
        return createCodeBlock(rid, resName, elementName, context_from, "bool", defPackage);
    }

    public static CodeBlock dimen(int rid, String resName, String elementName, String context_from, String defPackage) {
        return createCodeBlock(rid, resName, elementName, context_from, "dimen", defPackage);
    }

    public static CodeBlock integer(int rid, String resName, String elementName, String context_from, String defPackage) {
        return createCodeBlock(rid, resName, elementName, context_from, "integer", defPackage);
    }

    public static CodeBlock color(int rid, String resName, String elementName, String context_from, String defPackage) {
        return createCodeBlock(rid, resName, elementName, context_from, "color", defPackage);
    }

    public static CodeBlock drawable(int rid, String resName, String elementName, String context_from, String defPackage) {
        return createCodeBlock(rid, resName, elementName, context_from, "drawable", defPackage);
    }

    public static CodeBlock anim(int rid, String resName, String elementName, String context_from, String defPackage) {
        return createCodeBlock(rid, resName, elementName, context_from, "anim", defPackage);
    }

}
