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
    private final ProcessorUtil processorUtil;

    public ResourcesHelp(ProcessorUtil processorUtil) {
        this.processorUtil = processorUtil;
    }

    void bindResourcesAnnotation(MethodSpec.Builder inOnCreate, Element element, String context_from, String defPackage) {
        if (addStatementResString(inOnCreate, element, context_from, defPackage)) {
        } else if (addStatementResStringArray(inOnCreate, element, context_from, defPackage)) {
        } else if (addStatementResBoolean(inOnCreate, element, context_from, defPackage)) {
        } else if (addStatementResDimen(inOnCreate, element, context_from, defPackage)) {
        } else if (addStatementResInt(inOnCreate, element, context_from, defPackage)) {
        } else if (addStatementResIntArray(inOnCreate, element, context_from, defPackage)) {
        } else if (addStatementResColor(inOnCreate, element, context_from, defPackage)) {
        } else if (addStatementResDrawable(inOnCreate, element, context_from, defPackage)) {
        } else if (addStatementaResAnimation(inOnCreate, element, context_from, defPackage)) {
        } else if (addStatementaResLayoutAnimation(inOnCreate, element, context_from, defPackage)) {
        }
    }

    private boolean addStatementResString(MethodSpec.Builder inOnCreate, Element element, String context_from, String defPackage) {
        final ResString aResString = element.getAnnotation(ResString.class);
        if (aResString == null) {
            return false;
        }
        final String name = element.getSimpleName().toString();
        inOnCreate.addCode(CodeBlock.builder()
                .add("$N = $N.getResources().getString(", name, context_from)
                .add(AndroidResHelp.string(aResString.value(), aResString.resName(), name, context_from, defPackage))
                .addStatement(")")
                .build());
        return true;
    }

    private boolean addStatementResStringArray(MethodSpec.Builder inOnCreate, Element element, String context_from, String defPackage) {
        final ResStringArray aResStringArray = element.getAnnotation(ResStringArray.class);
        if (aResStringArray == null) {
            return false;
        }
        final String name = element.getSimpleName().toString();
        inOnCreate.addCode(CodeBlock.builder()
                .add("$N = $N.getResources().getStringArray(", name, context_from)
                .add(AndroidResHelp.array(aResStringArray.value(), aResStringArray.resName(), name, context_from, defPackage))
                .addStatement(")")
                .build());
        return true;
    }

    private boolean addStatementResBoolean(MethodSpec.Builder inOnCreate, Element element, String context_from, String defPackage) {
        final ResBoolean aResBoolean = element.getAnnotation(ResBoolean.class);
        if (aResBoolean == null) {
            return false;
        }
        final String name = element.getSimpleName().toString();
        inOnCreate.addCode(CodeBlock.builder()
                .add("$N = $N.getResources().getBoolean(", name, context_from)
                .add(AndroidResHelp.bool(aResBoolean.value(), aResBoolean.resName(), name, context_from, defPackage))
                .addStatement(")")
                .build());
        return true;
    }

    private boolean addStatementResDimen(MethodSpec.Builder inOnCreate, Element element, String context_from, String defPackage) {
        final ResDimen aResDimen = element.getAnnotation(ResDimen.class);
        if (aResDimen == null) {
            return false;
        }
        final String name = element.getSimpleName().toString();
        inOnCreate.addCode(CodeBlock.builder()
                .add("$N = $N.getResources().getDimensionPixelSize(", name, context_from)
                .add(AndroidResHelp.dimen(aResDimen.value(), aResDimen.resName(), name, context_from, defPackage))
                .addStatement(")")
                .build());
        return true;
    }

    private boolean addStatementResInt(MethodSpec.Builder inOnCreate, Element element, String context_from, String defPackage) {
        final ResInt aResInt = element.getAnnotation(ResInt.class);
        if (aResInt == null) {
            return false;
        }
        final String name = element.getSimpleName().toString();
        inOnCreate.addCode(CodeBlock.builder()
                .add("$N = $N.getResources().getInteger(", name, context_from)
                .add(AndroidResHelp.integer(aResInt.value(), aResInt.resName(), name, context_from, defPackage))
                .addStatement(")")
                .build());
        return true;
    }

    private boolean addStatementResIntArray(MethodSpec.Builder inOnCreate, Element element, String context_from, String defPackage) {
        final ResIntArray aResIntArray = element.getAnnotation(ResIntArray.class);
        if (aResIntArray == null) {
            return false;
        }
        final String name = element.getSimpleName().toString();
        inOnCreate.addCode(CodeBlock.builder()
                .add("$N = $N.getResources().getIntArray(", name, context_from)
                .add(AndroidResHelp.array(aResIntArray.value(), aResIntArray.resName(), name, context_from, defPackage))
                .addStatement(")")
                .build());
        return true;
    }

    private boolean addStatementResColor(MethodSpec.Builder inOnCreate, Element element, String context_from, String defPackage) {
        final ResColor aResColor = element.getAnnotation(ResColor.class);
        if (aResColor == null) {
            return false;
        }
        final String name = element.getSimpleName().toString();
        inOnCreate.addCode(CodeBlock.builder()
                .add("$N = $N.getResources().getColor(", name, context_from)
                .add(AndroidResHelp.color(aResColor.value(), aResColor.resName(), name, context_from, defPackage))
                .addStatement(",$N.getTheme())", context_from)
                .build());
        return true;
    }

    private boolean addStatementResDrawable(MethodSpec.Builder inOnCreate, Element element, String context_from, String defPackage) {
        final ResDrawable aResDrawable = element.getAnnotation(ResDrawable.class);
        if (aResDrawable == null) {
            return false;
        }
        final String name = element.getSimpleName().toString();
        inOnCreate.addCode(CodeBlock.builder()
                .add("$N = $N.getResources().getDrawable(", name, context_from)
                .add(AndroidResHelp.drawable(aResDrawable.value(), aResDrawable.resName(), name, context_from, defPackage))
                .addStatement(",$N.getTheme())", context_from)
                .build());
        return true;
    }

    private boolean addStatementaResAnimation(MethodSpec.Builder inOnCreate, Element element, String context_from, String defPackage) {
        final ResAnimation aResAnimation = element.getAnnotation(ResAnimation.class);
        if (aResAnimation == null) {
            return false;
        }
        final String name = element.getSimpleName().toString();
        inOnCreate.addCode(CodeBlock.builder()
                .add("$N = AnimationUtils.loadAnimation($N", name, context_from)
                .add(AndroidResHelp.anim(aResAnimation.value(), aResAnimation.resName(), name, context_from, defPackage))
                .addStatement(")")
                .build());
        return true;
    }

    private boolean addStatementaResLayoutAnimation(MethodSpec.Builder inOnCreate, Element element, String context_from, String defPackage) {
        final ResLayoutAnimation aResLayoutAnimation = element.getAnnotation(ResLayoutAnimation.class);
        if (aResLayoutAnimation == null) {
            return false;
        }
        final String name = element.getSimpleName().toString();
        inOnCreate.addCode(CodeBlock.builder()
                .add("$N = AnimationUtils.loadLayoutAnimation($N", name, context_from)
                .add(AndroidResHelp.anim(aResLayoutAnimation.value(), aResLayoutAnimation.resName(), name, context_from, defPackage))
                .addStatement(")")
                .build());
        return true;
    }
}
