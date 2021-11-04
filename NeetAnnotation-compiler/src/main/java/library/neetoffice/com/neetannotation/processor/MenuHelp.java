package library.neetoffice.com.neetannotation.processor;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.CodeBlock;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;

import library.neetoffice.com.neetannotation.OptionsMenuItem;
import library.neetoffice.com.neetannotation.MenuSelected;
import library.neetoffice.com.neetannotation.OptionsMenu;
import library.neetoffice.com.neetannotation.ViewById;

public class MenuHelp {
    private final BaseCreator creator;

    public MenuHelp(BaseCreator creator) {
        this.creator = creator;
    }

    public Builder builder(String context_from, String defPackage) {
        return new Builder(creator, context_from, defPackage);
    }

    static class MenuSelectedBundle {
        final CodeBlock id;
        final Element element;

        MenuSelectedBundle(CodeBlock id, Element element) {
            this.id = id;
            this.element = element;
        }
    }

    static class Builder {
        private final BaseCreator creator;
        private final String context_from;
        private final String defPackage;
        private final List<MenuSelectedBundle> menuSelectedBundles = new ArrayList<>();
        private final List<Element> menuItemElements = new ArrayList<>();


        Builder(BaseCreator creator, String context_from, String defPackage) {
            this.creator = creator;
            this.context_from = context_from;
            this.defPackage = defPackage;
        }


        CodeBlock parseOptionsMenuInOnCreate(TypeElement typeElement) {
            final OptionsMenu aOptionsMenu = typeElement.getAnnotation(OptionsMenu.class);
            if (aOptionsMenu == null) {
                return CodeBlock.builder().build();
            }
            if (creator.isSubFragment(typeElement)) {
                return CodeBlock.builder()
                        .addStatement("setHasOptionsMenu(true)")
                        .build();
            }
            return CodeBlock.builder().build();
        }

        public void parseElement(Element element) {
            if (element.getAnnotation(MenuSelected.class) != null) {
                final String elementName = element.getSimpleName().toString();
                final MenuSelected aMenuSelected = element.getAnnotation(MenuSelected.class);
                if (aMenuSelected.value().length != 0) {
                    for (String resName : aMenuSelected.value()) {
                        menuSelectedBundles.add(new MenuSelectedBundle(AndroidResHelp.id(resName, elementName, context_from, defPackage), element));
                    }
                } else if (aMenuSelected.resName().length != 0) {
                    for (String resName : aMenuSelected.resName()) {
                        menuSelectedBundles.add(new MenuSelectedBundle(AndroidResHelp.id(resName, elementName, context_from, defPackage), element));
                    }
                } else {
                    menuSelectedBundles.add(new MenuSelectedBundle(AndroidResHelp.id("", elementName, context_from, defPackage), element));
                }
            }
            if (element.getAnnotation(OptionsMenuItem.class) != null) {
                menuItemElements.add(element);
            }
        }

        CodeBlock createMenuInflaterCode(TypeElement typeElement, String menu, String inflater) {
            final OptionsMenu aOptionsMenu = typeElement.getAnnotation(OptionsMenu.class);
            if (aOptionsMenu == null) {
                return CodeBlock.builder().build();
            }
            final String resName = AndroidResHelp.parseResName(aOptionsMenu.value(), aOptionsMenu.resName());
            return CodeBlock.builder()
                    .add("$N.inflate(", inflater)
                    .add(AndroidResHelp.menu(resName, typeElement.getSimpleName().toString(), context_from, defPackage))
                    .addStatement(", $N)", menu)
                    .build();
        }

        CodeBlock createFindMenuItemCode(String menu) {
            final CodeBlock.Builder code = CodeBlock.builder();
            for (Element element : menuItemElements) {
                if (creator.isInstanceOf(element.asType(), AndroidClass.MenuItem)) {
                    final OptionsMenuItem aOptionsMenuItem = element.getAnnotation(OptionsMenuItem.class);
                    final String resName = AndroidResHelp.parseResName(aOptionsMenuItem.value(), aOptionsMenuItem.resName());
                    code.add("this.$N = $N.findItem(", element.getSimpleName(), menu)
                            .add(AndroidResHelp.id(resName, element.getSimpleName().toString(), context_from, defPackage))
                            .addStatement(")");
                }
            }
            return code.build();
        }

        CodeBlock createOnOptionsItemSelectedCode(String menuItemName) {
            final CodeBlock.Builder cb = CodeBlock.builder();
            for (int i = 0; i < menuSelectedBundles.size(); i++) {
                final MenuSelectedBundle bundle = menuSelectedBundles.get(i);
                if (i != 0) {
                    cb.add("else ");
                }
                cb.add("if($N.getItemId() == ", menuItemName)
                        .add(bundle.id)
                        .beginControlFlow(")")
                        .add(createMenuSelectedCode(bundle, menuItemName))
                        .endControlFlow();
            }
            return cb.build();
        }

        private CodeBlock createMenuSelectedCode(MenuSelectedBundle bundle, String menuItemName) {
            final Element element = bundle.element;
            final CodeBlock.Builder parameterCode = CodeBlock.builder();
            if (element instanceof ExecutableElement) {
                final Iterator<? extends VariableElement> parameters = ((ExecutableElement) element).getParameters().iterator();
                while (parameters.hasNext()) {
                    final VariableElement parameter = parameters.next();
                    if (AndroidClass.MenuItem.equals(ClassName.get(parameter.asType()))) {
                        parameterCode.add(menuItemName);
                    } else {
                        parameterCode.add(creator.addNullCode(creator.getClassName(parameter.asType())));
                    }
                    if (parameters.hasNext()) {
                        parameterCode.add(",");
                    }
                }
            }
            return CodeBlock.builder()
                    .add("$N(", element.getSimpleName())
                    .add(parameterCode.build())
                    .addStatement(")")
                    .build();
        }
    }
}
