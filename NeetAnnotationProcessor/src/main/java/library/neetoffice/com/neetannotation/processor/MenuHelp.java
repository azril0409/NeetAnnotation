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
            final ViewById aViewById = aOptionsMenu.toolBar();
            if (aViewById != null) {
                if (creator.isSubFragmentActivity(typeElement)) {
                    if (aViewById.value() == 0 && aViewById.resName().isEmpty()) {
                        return CodeBlock.builder().build();
                    }
                    return CodeBlock.builder()
                            .add("$T _toolbar = findViewById(", AndroidClass.View)
                            .add(AndroidResHelp.id(aViewById.value(), aViewById.resName(), "", context_from, defPackage))
                            .addStatement(")")
                            .beginControlFlow("if(_toolbar instanceof $T)", AndroidClass.Toolbar_v7)
                            .addStatement("setSupportActionBar(($T)_toolbar)", AndroidClass.Toolbar_v7)
                            .endControlFlow()
                            .build();
                } else if (creator.isSubActivity(typeElement)) {
                    if (aViewById.value() == 0 && aViewById.resName().isEmpty()) {
                        return CodeBlock.builder().build();
                    }
                    return CodeBlock.builder()
                            .add("$T _toolbar = findViewById(", AndroidClass.View)
                            .add(AndroidResHelp.id(aViewById.value(), aViewById.resName(), "", context_from, defPackage))
                            .addStatement(")")
                            .beginControlFlow("if(_toolbar instanceof $T)", AndroidClass.Toolbar)
                            .addStatement("setSupportActionBar(($T)_toolbar)", AndroidClass.Toolbar)
                            .endControlFlow()
                            .build();
                } else if (creator.isSubFragment(typeElement)) {
                    return CodeBlock.builder()
                            .addStatement("setHasOptionsMenu(true)")
                            .build();
                }
            }
            return CodeBlock.builder().build();
        }

        public void parseElement(Element element) {
            if (element.getAnnotation(MenuSelected.class) != null) {
                final MenuSelected aMenuSelected = element.getAnnotation(MenuSelected.class);
                for (int rid : aMenuSelected.value()) {
                    menuSelectedBundles.add(new MenuSelectedBundle(AndroidResHelp.id(rid, "", "toolbar", context_from, defPackage), element));
                }
                for (String resName : aMenuSelected.resName()) {
                    menuSelectedBundles.add(new MenuSelectedBundle(AndroidResHelp.id(0, resName, "toolbar", context_from, defPackage), element));
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
            return CodeBlock.builder()
                    .add("$N.inflate(", inflater)
                    .add(AndroidResHelp.menu(aOptionsMenu.value(), aOptionsMenu.resName(), context_from, defPackage))
                    .addStatement(", $N)", menu)
                    .build();
        }

        CodeBlock createFindMenuItemCode(String menu) {
            final CodeBlock.Builder code = CodeBlock.builder();
            for (Element element : menuItemElements) {
                if (creator.isInstanceOf(element.asType(), AndroidClass.MenuItem)) {
                    final OptionsMenuItem aOptionsMenuItem = element.getAnnotation(OptionsMenuItem.class);
                    code.add("this.$N = $N.findItem(", element.getSimpleName(), menu)
                            .add(AndroidResHelp.id(aOptionsMenuItem.value(), aOptionsMenuItem.resName(), element.getSimpleName().toString(), context_from, defPackage))
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
