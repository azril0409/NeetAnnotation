package library.neetoffice.com.neetannotation.processor.kapt;

import com.squareup.javapoet.CodeBlock;

import javax.lang.model.element.Name;

public class AndroidResHelp {
    private static CodeBlock createCodeBlock(String resName, String resPackage, String elementName, String context_from, String defType, String defPackage) {
        final String a;
        if (resPackage == null || resPackage.isEmpty()) {
            a = defPackage;
        } else {
            a = "\"" + resPackage + "\"";
        }
        if (!resName.isEmpty()) {
            return CodeBlock.builder().add("$N.getResources().getIdentifier($S, $S, $N)", context_from, resName, defType, a).build();
        } else if (!elementName.isEmpty()) {
            return CodeBlock.builder().add("$N.getResources().getIdentifier($S, $S, $N)", context_from, elementName, defType, a).build();
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

    private static String resourceFileNameFormat(Name elementName) {
        final StringBuffer name = new StringBuffer();
        for (char c : elementName.toString().toCharArray()) {
            if (Character.isUpperCase(c)) {
                name.append("_");
            }
            name.append(c);
        }
        return name.toString();
    }

    private static String renameEventName(String name, String end) {
        String string = name;
        if (string.toLowerCase().startsWith("on")) {
            string = String.valueOf(string.charAt(2)).toLowerCase() + string.substring(3);
        }
        if (string.toLowerCase().endsWith(end.toLowerCase())) {
            string = string.substring(0, string.length() - end.length());
        }
        return string;
    }

    public static CodeBlock id(String resName, String resPackage, Name elementName, String end, String context_from, String defPackage) {
        final String name = renameEventName(elementName.toString(), end);
        return createCodeBlock(resName, resPackage, name, context_from, "id", defPackage);
    }

    public static CodeBlock id(String resName, String resPackage, Name elementName, String context_from, String defPackage) {
        return createCodeBlock(resName, resPackage, elementName.toString(), context_from, "id", defPackage);
    }

    public static CodeBlock string(String resName, String resPackage, Name elementName, String context_from, String defPackage) {
        return createCodeBlock(resName, resPackage, elementName.toString(), context_from, "string", defPackage);
    }

    public static CodeBlock array(String resName, String resPackage, Name elementName, String context_from, String defPackage) {
        return createCodeBlock(resName, resPackage, elementName.toString(), context_from, "array", defPackage);
    }

    public static CodeBlock bool(String resName, String resPackage, Name elementName, String context_from, String defPackage) {
        return createCodeBlock(resName, resPackage, elementName.toString(), context_from, "bool", defPackage);
    }

    public static CodeBlock dimen(String resName, String resPackage, Name elementName, String context_from, String defPackage) {
        final String name = AndroidResHelp.resourceFileNameFormat(elementName);
        return createCodeBlock(resName, resPackage, name, context_from, "dimen", defPackage);
    }

    public static CodeBlock integer(String resName, String resPackage, Name elementName, String context_from, String defPackage) {
        return createCodeBlock(resName, resPackage, elementName.toString(), context_from, "integer", defPackage);
    }

    // have resources directory
    public static CodeBlock anim(String resName, String resPackage, Name elementName, String context_from, String defPackage) {
        final String name = resourceFileNameFormat(elementName);
        return createCodeBlock(resName, resPackage, name, context_from, "anim", defPackage);
    }

    // have resources directory
    public static CodeBlock color(String resName, String resPackage, Name elementName, String context_from, String defPackage) {
        final String name = resourceFileNameFormat(elementName);
        return createCodeBlock(resName, resPackage, name, context_from, "color", defPackage);
    }

    // have resources directory
    public static CodeBlock drawable(String resName, String resPackage, Name elementName, String context_from, String defPackage) {
        final String name = resourceFileNameFormat(elementName);
        return createCodeBlock(resName, resPackage, name, context_from, "drawable", defPackage);
    }

    // have resources directory
    public static CodeBlock layout(String resName, String resPackage, Name elementName, String context_from, String defPackage) {
        final String name = resourceFileNameFormat(elementName);
        return createCodeBlock(resName, resPackage, name, context_from, "layout", defPackage);
    }

    // have resources directory
    public static CodeBlock menu(String resName, String resPackage, Name elementName, String context_from, String defPackage) {
        final String name = resourceFileNameFormat(elementName);
        return createCodeBlock(resName, resPackage, name, context_from, "menu", defPackage);
    }
}
