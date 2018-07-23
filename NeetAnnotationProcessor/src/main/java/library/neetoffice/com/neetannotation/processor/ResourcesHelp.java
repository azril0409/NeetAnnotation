package library.neetoffice.com.neetannotation.processor;

import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.MethodSpec;

import javax.lang.model.element.Element;

import library.neetoffice.com.neetannotation.ResAnimation;
import library.neetoffice.com.neetannotation.ResBoolean;
import library.neetoffice.com.neetannotation.ResColor;
import library.neetoffice.com.neetannotation.ResDimen;
import library.neetoffice.com.neetannotation.ResDrawable;
import library.neetoffice.com.neetannotation.ResInt;
import library.neetoffice.com.neetannotation.ResIntArray;
import library.neetoffice.com.neetannotation.ResLayoutAnimation;
import library.neetoffice.com.neetannotation.ResString;
import library.neetoffice.com.neetannotation.ResStringArray;

public class ResourcesHelp {

    public ResourcesHelp() {
    }

    CodeBlock bindResourcesAnnotation(Element element, String context_from, String defPackage) {
        CodeBlock.Builder cb = CodeBlock.builder();
        cb.add(addStatementResString(element, context_from, defPackage));
        cb.add(addStatementResStringArray(element, context_from, defPackage));
        cb.add(addStatementResBoolean(element, context_from, defPackage));
        cb.add(addStatementResDimen(element, context_from, defPackage));
        cb.add(addStatementResInt(element, context_from, defPackage));
        cb.add(addStatementResIntArray(element, context_from, defPackage));
        cb.add(addStatementResColor(element, context_from, defPackage));
        cb.add(addStatementResDrawable(element, context_from, defPackage));
        cb.add(addStatementaResAnimation(element, context_from, defPackage));
        cb.add(addStatementaResLayoutAnimation(element, context_from, defPackage));
        return cb.build();
    }

    private CodeBlock addStatementResString(Element element, String context_from, String defPackage) {
        final ResString aResString = element.getAnnotation(ResString.class);
        if (aResString == null) {
            return CodeBlock.builder().build();
        }
        final String name = element.getSimpleName().toString();
        return CodeBlock.builder()
                .add("$N = $N.getResources().getString(", name, context_from)
                .add(AndroidResHelp.string(aResString.value(), aResString.resName(), name, context_from, defPackage))
                .addStatement(")")
                .build();
    }

    private CodeBlock addStatementResStringArray(Element element, String context_from, String defPackage) {
        final ResStringArray aResStringArray = element.getAnnotation(ResStringArray.class);
        if (aResStringArray == null) {
            return CodeBlock.builder().build();
        }
        final String name = element.getSimpleName().toString();
        return CodeBlock.builder()
                .add("$N = $N.getResources().getStringArray(", name, context_from)
                .add(AndroidResHelp.array(aResStringArray.value(), aResStringArray.resName(), name, context_from, defPackage))
                .addStatement(")")
                .build();
    }

    private CodeBlock addStatementResBoolean(Element element, String context_from, String defPackage) {
        final ResBoolean aResBoolean = element.getAnnotation(ResBoolean.class);
        if (aResBoolean == null) {
            return CodeBlock.builder().build();
        }
        final String name = element.getSimpleName().toString();
        return CodeBlock.builder()
                .add("$N = $N.getResources().getBoolean(", name, context_from)
                .add(AndroidResHelp.bool(aResBoolean.value(), aResBoolean.resName(), name, context_from, defPackage))
                .addStatement(")")
                .build();
    }

    private CodeBlock addStatementResDimen(Element element, String context_from, String defPackage) {
        final ResDimen aResDimen = element.getAnnotation(ResDimen.class);
        if (aResDimen == null) {
            return CodeBlock.builder().build();
        }
        final String name = element.getSimpleName().toString();
        return CodeBlock.builder()
                .add("$N = $N.getResources().getDimensionPixelSize(", name, context_from)
                .add(AndroidResHelp.dimen(aResDimen.value(), aResDimen.resName(), name, context_from, defPackage))
                .addStatement(")")
                .build();
    }

    private CodeBlock addStatementResInt(Element element, String context_from, String defPackage) {
        final ResInt aResInt = element.getAnnotation(ResInt.class);
        if (aResInt == null) {
            return CodeBlock.builder().build();
        }
        final String name = element.getSimpleName().toString();
        return CodeBlock.builder()
                .add("$N = $N.getResources().getInteger(", name, context_from)
                .add(AndroidResHelp.integer(aResInt.value(), aResInt.resName(), name, context_from, defPackage))
                .addStatement(")")
                .build();
    }

    private CodeBlock addStatementResIntArray(Element element, String context_from, String defPackage) {
        final ResIntArray aResIntArray = element.getAnnotation(ResIntArray.class);
        if (aResIntArray == null) {
            return CodeBlock.builder().build();
        }
        final String name = element.getSimpleName().toString();
        return CodeBlock.builder()
                .add("$N = $N.getResources().getIntArray(", name, context_from)
                .add(AndroidResHelp.array(aResIntArray.value(), aResIntArray.resName(), name, context_from, defPackage))
                .addStatement(")")
                .build();
    }

    private CodeBlock addStatementResColor(Element element, String context_from, String defPackage) {
        final ResColor aResColor = element.getAnnotation(ResColor.class);
        if (aResColor == null) {
            return CodeBlock.builder().build();
        }
        final String name = element.getSimpleName().toString();
        return CodeBlock.builder()
                .add("$N = $N.getResources().getColor(", name, context_from)
                .add(AndroidResHelp.color(aResColor.value(), aResColor.resName(), name, context_from, defPackage))
                .addStatement(",$N.getTheme())", context_from)
                .build();
    }

    private CodeBlock addStatementResDrawable(Element element, String context_from, String defPackage) {
        final ResDrawable aResDrawable = element.getAnnotation(ResDrawable.class);
        if (aResDrawable == null) {
            return CodeBlock.builder().build();
        }
        final String name = element.getSimpleName().toString();
        return  CodeBlock.builder()
                .add("$N = $N.getResources().getDrawable(", name, context_from)
                .add(AndroidResHelp.drawable(aResDrawable.value(), aResDrawable.resName(), name, context_from, defPackage))
                .addStatement(",$N.getTheme())", context_from)
                .build();
    }

    private CodeBlock addStatementaResAnimation(Element element, String context_from, String defPackage) {
        final ResAnimation aResAnimation = element.getAnnotation(ResAnimation.class);
        if (aResAnimation == null) {
            return CodeBlock.builder().build();
        }
        final String name = element.getSimpleName().toString();
        return CodeBlock.builder()
                .add("$N = AnimationUtils.loadAnimation($N", name, context_from)
                .add(AndroidResHelp.anim(aResAnimation.value(), aResAnimation.resName(), name, context_from, defPackage))
                .addStatement(")")
                .build();
    }

    private CodeBlock addStatementaResLayoutAnimation(Element element, String context_from, String defPackage) {
        final ResLayoutAnimation aResLayoutAnimation = element.getAnnotation(ResLayoutAnimation.class);
        if (aResLayoutAnimation == null) {
            return CodeBlock.builder().build();
        }
        final String name = element.getSimpleName().toString();
        return CodeBlock.builder()
                .add("$N = AnimationUtils.loadLayoutAnimation($N", name, context_from)
                .add(AndroidResHelp.anim(aResLayoutAnimation.value(), aResLayoutAnimation.resName(), name, context_from, defPackage))
                .addStatement(")")
                .build();
    }
}
