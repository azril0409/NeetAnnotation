package library.neetoffice.com.neetannotation.processor;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.inject.Named;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.ExecutableType;

import library.neetoffice.com.neetannotation.Subscribe;
import library.neetoffice.com.neetannotation.ViewModelKey;
import library.neetoffice.com.neetannotation.ViewModelOf;

public class SubscribeHelp {
    static class SubscribeElement {
        final Element element;
        Object viewModel;
        String key = "";
        String subjectName = "";
        String named = "";

        SubscribeElement(Element element) {
            this.element = element;
        }
    }

    static class ViewModelOfElement {
        final Element element;
        String key = "";

        ViewModelOfElement(Element element) {
            this.element = element;
        }

        ClassName getProcessorType(BaseCreator creator) {
            return ClassName.get(creator.getPackageName(element), element.asType().toString() + "_");
        }
    }

    private final BaseCreator creator;

    public SubscribeHelp(BaseCreator creator) {
        this.creator = creator;
    }

    Builder builder() {
        return new Builder(creator);
    }

    static class Builder {
        private final BaseCreator creator;
        private final HashMap<String, List<SubscribeElement>> subscribeMap = new HashMap<>();
        private final HashMap<String, ViewModelOfElement> viewModelOfMap = new HashMap<>();
        private final List<String> disposableFieldNames = new ArrayList<>();

        Builder(BaseCreator creator) {
            this.creator = creator;
        }

        boolean hasElement() {
            return (subscribeMap.size() + viewModelOfMap.size()) > 0;
        }

        private String mapKey(SubscribeElement subscribeElement) {
            return subscribeElement.key + "_" + subscribeElement.viewModel.toString();
        }

        private String mapKey(ViewModelOfElement viewModelOfElement) {
            return viewModelOfElement.key + "_" + ClassName.get(viewModelOfElement.element.asType()).toString();
        }

        public void parseElement(Element element) {
            if (element.getAnnotation(Subscribe.class) != null) {
                final SubscribeElement subscribeElement = new SubscribeElement(element);
                subscribeElement.viewModel = creator.findAnnotationValue(element, Subscribe.class, "viewmode");
                final Object named = creator.findAnnotationValue(element, DaggerClass.Named, "value");
                subscribeElement.named = named != null ? named.toString() : element.getSimpleName().toString();
                final Object subscribe_key = creator.findAnnotationValue(element, Subscribe.class, "subjectName");
                subscribeElement.subjectName = subscribe_key != null ? subscribe_key.toString() : "";
                if(element.getAnnotation(ViewModelKey.class) != null){
                    final Object viewModelKey = creator.findAnnotationValue(element, ViewModelKey.class,"value");
                    subscribeElement.key = viewModelKey != null ? viewModelKey.toString() : "";
                }
                if (subscribeElement.viewModel != null) {
                    final String key = mapKey(subscribeElement);
                    if (subscribeMap.containsKey(key)) {
                        subscribeMap.get(key).add(subscribeElement);
                    } else {
                        final List<SubscribeElement> list = new ArrayList<>();
                        list.add(subscribeElement);
                        subscribeMap.put(key, list);
                    }
                }
            }
            if (element.getAnnotation(ViewModelOf.class) != null) {
                final Object v;
                if(element.getAnnotation(ViewModelKey.class)!=null){
                    v = creator.findAnnotationValue(element, ViewModelKey.class, "value");
                }else {
                    v = null;
                }
                final ViewModelOfElement viewModelOfElement = new ViewModelOfElement(element);
                viewModelOfElement.key = v != null ? v.toString() : "";
                final String key = mapKey(viewModelOfElement);
                viewModelOfMap.put(key, viewModelOfElement);
            }
        }

        public CodeBlock createAddViewModelOfCode(String context_from) {
            final CodeBlock.Builder cb = CodeBlock.builder();
            for (ViewModelOfElement viewModelOf : viewModelOfMap.values()) {
                cb.add(createGetViewModeByViewModelProviders(viewModelOf, context_from));
            }
            return cb.build();
        }

        private CodeBlock createGetViewModeByViewModelProviders(ViewModelOfElement viewModelOf, String context_from) {
            final ClassName typeName = viewModelOf.getProcessorType(creator);
            final String key = viewModelOf.key;
            return CodeBlock.builder()
                    .add("this.$N = ",viewModelOf.element.getSimpleName())
                    .add(ViewModelHelp.viewModelProvidersOf(context_from,key,typeName))
                    .addStatement("")
                    .build();
        }

        private boolean isViewModeFieldinTypeElement(SubscribeElement element) {
            return viewModelOfMap.containsKey(element.key + "_" + element.viewModel.toString());
        }

        private ViewModelOfElement getViewModelOfElement(SubscribeElement element) {
            return viewModelOfMap.get(element.key + "_" + element.viewModel.toString());
        }

        String getDisposableFieldName(SubscribeElement subscribeElement) {
            final Element element = subscribeElement.element;
            final String elementName = element.getSimpleName().toString();
            if (element.asType() instanceof DeclaredType) {
                return String.format("m%s%s_Disposable", elementName.toUpperCase().charAt(0), elementName.substring(1));
            } else if (element.asType() instanceof ExecutableType) {
                return String.format("f%s%s_Disposable", elementName.toUpperCase().charAt(0), elementName.substring(1));
            }
            return "";
        }

        void addDisposableFieldInType(TypeSpec.Builder tb) {
            for (List<SubscribeElement> subscribeElements : subscribeMap.values()) {
                for (SubscribeElement subscribeElement : subscribeElements) {
                    tb.addField(createDisposable(subscribeElement));
                }
            }
        }

        private FieldSpec createDisposable(SubscribeElement subscribeElement) {
            final String fieldName = getDisposableFieldName(subscribeElement);
            if (fieldName.isEmpty()) {
                return null;
            }
            disposableFieldNames.add(fieldName);
            return FieldSpec.builder(RxJavaClass.Disposable, fieldName, Modifier.PRIVATE).build();
        }

        private CodeBlock createSubscribeToViewMode(SubscribeElement subscribeElement, String thisClassName, TypeName viewModelType, String viewModelName, String disposableFieldName) {
            final CodeBlock.Builder cb = CodeBlock.builder();
            final Element element = subscribeElement.element;
            if (element.asType() instanceof DeclaredType) {
                final DeclaredType declaredType = (DeclaredType) element.asType();
                final String subjectName = subscribeElement.subjectName;
                cb.add("$N = (($T)$N).<$T>findSubjectByName($S)", disposableFieldName, viewModelType, viewModelName, declaredType, subjectName);
                cb.add(ReactiveXHelp.subscribeConsumer(ClassName.get(declaredType), "t",
                        CodeBlock.builder().addStatement("$N.this.$N = t", thisClassName, element.getSimpleName()).build()));
            } else if (element instanceof ExecutableElement) {
                final ExecutableElement method = (ExecutableElement) element;
                final String subjectName = subscribeElement.subjectName;
                final List<? extends VariableElement> parameters = method.getParameters();
                final TypeName parameterType;
                if (parameters.size() > 0) {
                    parameterType = creator.getClassName(parameters.get(0).asType());
                } else {
                    parameterType = ClassName.get(Object.class);
                }
                final Iterator<? extends VariableElement> iterator = parameters.iterator();
                final StringBuffer parameterString = new StringBuffer();
                while (iterator.hasNext()) {
                    final VariableElement parameter = iterator.next();
                    if (creator.getClassName(parameter.asType()).equals(parameterType)) {
                        parameterString.append("t");
                    } else {
                        parameterString.append(AnnotationHelp.addNullCode(parameter));
                    }
                    if (iterator.hasNext()) {
                        parameterString.append(",");
                    }
                }
                cb.add("$N = (($T)$N).<$T>findSubjectByName($S)", disposableFieldName, viewModelType, viewModelName, parameterType, subjectName);
                cb.add(ReactiveXHelp.subscribeConsumer(parameterType, "t",
                        CodeBlock.builder().addStatement("$N.this.$N($N)", thisClassName, element.getSimpleName(), parameterString.toString()).build()));
            }
            return cb.build();
        }

        CodeBlock createCodeSubscribeInOnCreate(String thisClassName, String context_from) {
            final CodeBlock.Builder cb = CodeBlock.builder();
            final HashMap<String, String> localViewModelNames = new HashMap<>();
            for (List<SubscribeElement> subscribeElements : subscribeMap.values()) {
                for (SubscribeElement subscribeElement : subscribeElements) {
                    if (isViewModeFieldinTypeElement(subscribeElement)) {
                        final ViewModelOfElement viewModelOf = getViewModelOfElement(subscribeElement);
                        final ClassName typeName = viewModelOf.getProcessorType(creator);
                        final String viewModelName = viewModelOf.element.getSimpleName().toString();
                        final String disposableFieldName = getDisposableFieldName(subscribeElement);
                        cb.add(createSubscribeToViewMode(subscribeElement, thisClassName, typeName, viewModelName, disposableFieldName));
                    } else {
                        final ClassName typeName = ClassName.bestGuess(subscribeElement.viewModel.toString() + "_");
                        if (!localViewModelNames.containsKey(mapKey(subscribeElement))) {
                            final String localViewModelName = "lm" + typeName.simpleName();
                            cb.add("$T $N = ",typeName, localViewModelName);
                            cb.add(ViewModelHelp.viewModelProvidersOf(context_from,subscribeElement.key,typeName));
                            localViewModelNames.put(mapKey(subscribeElement), localViewModelName);
                            final String disposableFieldName = getDisposableFieldName(subscribeElement);
                            cb.add(createSubscribeToViewMode(subscribeElement, thisClassName, typeName, localViewModelName, disposableFieldName));
                        } else {
                            final String localViewModelName = localViewModelNames.get(mapKey(subscribeElement));
                            final String disposableFieldName = getDisposableFieldName(subscribeElement);
                            cb.add(createSubscribeToViewMode(subscribeElement, thisClassName, typeName, localViewModelName, disposableFieldName));
                        }
                    }
                }
            }
            return cb.build();
        }

        CodeBlock createDispose() {
            CodeBlock.Builder code = CodeBlock.builder();
            for (String fieldName : disposableFieldNames) {
                code.addStatement("$N.dispose()", fieldName);
            }
            return code.build();
        }
    }
}
