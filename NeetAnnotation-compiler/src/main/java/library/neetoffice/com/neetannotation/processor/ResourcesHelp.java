package library.neetoffice.com.neetannotation.processor;

import com.squareup.javapoet.CodeBlock;

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
        final String resName = AndroidResHelp.parseResName(aResString.value(), aResString.resName());
        return CodeBlock.builder()
                .add("$N = $N.getResources().getString(", element.getSimpleName(), context_from)
                .add(AndroidResHelp.string(resName, aResString.resPackage(), element.getSimpleName(), context_from, defPackage))
                .addStatement(")")
                .build();
    }

    private CodeBlock addStatementResStringArray(Element element, String context_from, String defPackage) {
        final ResStringArray aResStringArray = element.getAnnotation(ResStringArray.class);
        if (aResStringArray == null) {
            return CodeBlock.builder().build();
        }
        final String resName = AndroidResHelp.parseResName(aResStringArray.value(), aResStringArray.resName());
        return CodeBlock.builder()
                .add("$N = $N.getResources().getStringArray(", element.getSimpleName(), context_from)
                .add(AndroidResHelp.array(resName, aResStringArray.resPackage(), element.getSimpleName(), context_from, defPackage))
                .addStatement(")")
                .build();
    }

    private CodeBlock addStatementResBoolean(Element element, String context_from, String defPackage) {
        final ResBoolean aResBoolean = element.getAnnotation(ResBoolean.class);
        if (aResBoolean == null) {
            return CodeBlock.builder().build();
        }
        final String resName = AndroidResHelp.parseResName(aResBoolean.value(), aResBoolean.resName());
        return CodeBlock.builder()
                .add("$N = $N.getResources().getBoolean(", element.getSimpleName(), context_from)
                .add(AndroidResHelp.bool(resName, aResBoolean.resPackage(), element.getSimpleName(), context_from, defPackage))
                .addStatement(")")
                .build();
    }

    private CodeBlock addStatementResDimen(Element element, String context_from, String defPackage) {
        final ResDimen aResDimen = element.getAnnotation(ResDimen.class);
        if (aResDimen == null) {
            return CodeBlock.builder().build();
        }
        final String resName = AndroidResHelp.parseResName(aResDimen.value(), aResDimen.resName());
        return CodeBlock.builder()
                .add("$N = $N.getResources().getDimensionPixelSize(", element.getSimpleName(), context_from)
                .add(AndroidResHelp.dimen(resName, aResDimen.resPackage(), element.getSimpleName(), context_from, defPackage))
                .addStatement(")")
                .build();
    }

    private CodeBlock addStatementResInt(Element element, String context_from, String defPackage) {
        final ResInt aResInt = element.getAnnotation(ResInt.class);
        if (aResInt == null) {
            return CodeBlock.builder().build();
        }
        final String resName = AndroidResHelp.parseResName(aResInt.value(), aResInt.resName());
        return CodeBlock.builder()
                .add("$N = $N.getResources().getInteger(", element.getSimpleName(), context_from)
                .add(AndroidResHelp.integer(resName, aResInt.resPackage(), element.getSimpleName(), context_from, defPackage))
                .addStatement(")")
                .build();
    }

    private CodeBlock addStatementResIntArray(Element element, String context_from, String defPackage) {
        final ResIntArray aResIntArray = element.getAnnotation(ResIntArray.class);
        if (aResIntArray == null) {
            return CodeBlock.builder().build();
        }
        final String resName = AndroidResHelp.parseResName(aResIntArray.value(), aResIntArray.resName());
        return CodeBlock.builder()
                .add("$N = $N.getResources().getIntArray(", element.getSimpleName(), context_from)
                .add(AndroidResHelp.array(resName, aResIntArray.resPackage(), element.getSimpleName(), context_from, defPackage))
                .addStatement(")")
                .build();
    }

    private CodeBlock addStatementResColor(Element element, String context_from, String defPackage) {
        final ResColor aResColor = element.getAnnotation(ResColor.class);
        if (aResColor == null) {
            return CodeBlock.builder().build();
        }
        final String resName = AndroidResHelp.parseResName(aResColor.value(), aResColor.resName());
        return CodeBlock.builder()
                .add("$N = $N.getResources().getColor(", element.getSimpleName(), context_from)
                .add(AndroidResHelp.color(resName, aResColor.resPackage(), element.getSimpleName(), context_from, defPackage))
                .addStatement(",$N.getTheme())", context_from)
                .build();
    }

    private CodeBlock addStatementResDrawable(Element element, String context_from, String defPackage) {
        final ResDrawable aResDrawable = element.getAnnotation(ResDrawable.class);
        if (aResDrawable == null) {
            return CodeBlock.builder().build();
        }
        final String resName = AndroidResHelp.parseResName(aResDrawable.value(), aResDrawable.resName());
        return CodeBlock.builder()
                .add("$N = $N.getResources().getDrawable(", element.getSimpleName(), context_from)
                .add(AndroidResHelp.drawable(resName, aResDrawable.resPackage(), element.getSimpleName(), context_from, defPackage))
                .addStatement(",$N.getTheme())", context_from)
                .build();
    }

    private CodeBlock addStatementaResAnimation(Element element, String context_from, String defPackage) {
        final ResAnimation aResAnimation = element.getAnnotation(ResAnimation.class);
        if (aResAnimation == null) {
            return CodeBlock.builder().build();
        }
        final String resName = AndroidResHelp.parseResName(aResAnimation.value(), aResAnimation.resName());
        return CodeBlock.builder()
                .add("$N = $T.loadAnimation($N,", element.getSimpleName(), AndroidClass.AnimationUtils, context_from)
                .add(AndroidResHelp.anim(resName, aResAnimation.resPackage(), element.getSimpleName(), context_from, defPackage))
                .addStatement(")")
                .build();
    }

    private CodeBlock addStatementaResLayoutAnimation(Element element, String context_from, String defPackage) {
        final ResLayoutAnimation aResLayoutAnimation = element.getAnnotation(ResLayoutAnimation.class);
        if (aResLayoutAnimation == null) {
            return CodeBlock.builder().build();
        }
        final String resName = AndroidResHelp.parseResName(aResLayoutAnimation.value(), aResLayoutAnimation.resName());
        return CodeBlock.builder()
                .add("$N = $T.loadLayoutAnimation($N,", element.getSimpleName(), AndroidClass.AnimationUtils, context_from)
                .add(AndroidResHelp.anim(resName, aResLayoutAnimation.resPackage(), element.getSimpleName(), context_from, defPackage))
                .addStatement(")")
                .build();
    }
}
