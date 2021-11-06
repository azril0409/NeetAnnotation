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

    public static String resourceFileNameFormat(Name elementName) {
        final StringBuffer name = new StringBuffer();
        for (char c : elementName.toString().toCharArray()) {
            if (Character.isUpperCase(c)) {
                name.append("_");
            }
            name.append(c);
        }
        return name.toString();
    }

    private static String renameActionName(String name, String end) {
        String string = name;
        if (string.toLowerCase().startsWith("on")) {
            string = String.valueOf(string.charAt(3)).toLowerCase() + string.substring(3);
        }
        if (string.toLowerCase().endsWith(end.toLowerCase())) {
            string = string.substring(0, string.length() - end.length() - 1);
        }
        return string;
    }

    public static CodeBlock id(String resName, Name elementName, String end, String context_from, String defPackage) {
        final String name = renameActionName(AndroidResHelp.resourceFileNameFormat(elementName), end);
        return createCodeBlock(resName, name, context_from, "id", defPackage);
    }

    public static CodeBlock id(String resName, Name elementName, String context_from, String defPackage) {
        final String name = AndroidResHelp.resourceFileNameFormat(elementName);
        return createCodeBlock(resName, name, context_from, "id", defPackage);
    }

    public static CodeBlock layout(String resName, Name elementName, String context_from, String defPackage) {
        final String name = AndroidResHelp.resourceFileNameFormat(elementName);
        return createCodeBlock(resName, name, context_from, "layout", defPackage);
    }

    public static CodeBlock string(String resName, Name elementName, String context_from, String defPackage) {
        final String name = AndroidResHelp.resourceFileNameFormat(elementName);
        return createCodeBlock(resName, name, context_from, "string", defPackage);
    }

    public static CodeBlock array(String resName, Name elementName, String context_from, String defPackage) {
        final String name = AndroidResHelp.resourceFileNameFormat(elementName);
        return createCodeBlock(resName, name, context_from, "array", defPackage);
    }

    public static CodeBlock bool(String resName, Name elementName, String context_from, String defPackage) {
        final String name = AndroidResHelp.resourceFileNameFormat(elementName);
        return createCodeBlock(resName, name, context_from, "bool", defPackage);
    }

    public static CodeBlock dimen(String resName, Name elementName, String context_from, String defPackage) {
        final String name = AndroidResHelp.resourceFileNameFormat(elementName);
        return createCodeBlock(resName, name, context_from, "dimen", defPackage);
    }

    public static CodeBlock integer(String resName, Name elementName, String context_from, String defPackage) {
        final String name = AndroidResHelp.resourceFileNameFormat(elementName);
        return createCodeBlock(resName, name, context_from, "integer", defPackage);
    }

    public static CodeBlock color(String resName, Name elementName, String context_from, String defPackage) {
        final String name = AndroidResHelp.resourceFileNameFormat(elementName);
        return createCodeBlock(resName, name, context_from, "color", defPackage);
    }

    public static CodeBlock drawable(String resName, Name elementName, String context_from, String defPackage) {
        final String name = AndroidResHelp.resourceFileNameFormat(elementName);
        return createCodeBlock(resName, name, context_from, "drawable", defPackage);
    }

    public static CodeBlock anim(String resName, Name elementName, String context_from, String defPackage) {
        final String name = AndroidResHelp.resourceFileNameFormat(elementName);
        return createCodeBlock(resName, name, context_from, "anim", defPackage);
    }

    public static CodeBlock menu(String resName, Name elementName, String context_from, String defPackage) {
        final String name = AndroidResHelp.resourceFileNameFormat(elementName);
        return createCodeBlock(resName, name, context_from, "menu", defPackage);
    }
}
