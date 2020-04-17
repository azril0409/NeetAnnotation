package library.neetoffice.com.neetannotation.processor;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.ExecutableType;

import library.neetoffice.com.neetannotation.Subscribe;
import library.neetoffice.com.neetannotation.Subscribes;
import library.neetoffice.com.neetannotation.ViewModelOf;

public class SubscribeHelp {
    private static final String EVENY_NEXT = "next";
    private static final String EVENY_ERROR = "error";
    private static final String EVENY_COMPLETED = "completed";
    private static final String CompositeDisposable_NAME = "f_disposable";

    static class SubscribeElement {
        Element element;
        String viewModel;
        String key = "";
        String subjectName = "";
        String eventType;

        static SubscribeElement createInstanceByElement(Element element) {
            SubscribeElement subscribeElement = new SubscribeElement();
            subscribeElement.eventType = EVENY_NEXT;
            subscribeElement.element = element;
            return subscribeElement;
        }

        static SubscribeElement createInstanceByCompletedElement(Element errElement) {
            SubscribeElement subscribeElement = new SubscribeElement();
            subscribeElement.eventType = EVENY_COMPLETED;
            subscribeElement.element = errElement;
            return subscribeElement;
        }
    }

    static class SubscribeGroup {
        String mapKey;
        String viewModeFieldName;
        String viewModel;
        String viewModelKey;
        String subjectName;
        ArrayList<SubscribeElement> nexts = new ArrayList<>();
        ArrayList<SubscribeElement> completeds = new ArrayList<>();

        SubscribeGroup(String mapKey, String viewModeFieldName, String viewModel, String viewModelKey, String subjectName) {
            this.mapKey = mapKey;
            this.viewModeFieldName = viewModeFieldName;
            this.viewModel = viewModel;
            this.viewModelKey = viewModelKey;
            this.subjectName = subjectName;
        }
    }

    static class ViewModelOfElement {
        final Element element;
        final String key;


        ViewModelOfElement(Element element, String key) {
            this.element = element;
            this.key = key;
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
        private final HashMap<String, HashMap<String, SubscribeGroup>> subscribeMap = new HashMap<>();
        private final HashMap<String, ViewModelOfElement> viewModelOfMap = new HashMap<>();

        Builder(BaseCreator creator) {
            this.creator = creator;
        }

        boolean hasElement() {
            return (subscribeMap.size() + viewModelOfMap.size()) > 0;
        }

        private String mapKey(SubscribeElement subscribeElement) {
            return subscribeElement.key + "_" + subscribeElement.viewModel;
        }

        private String mapKey(ViewModelOfElement viewModelOfElement) {
            return viewModelOfElement.key + "_" + ClassName.get(viewModelOfElement.element.asType()).toString();
        }

        private String viewModelName(SubscribeElement subscribeElement) {
            final String[] strings = subscribeElement.viewModel.split("\\.");
            return subscribeElement.key + "_" + strings[strings.length - 1];
        }

        public void parseElement(Element element) {
            if (element.getAnnotation(Subscribe.class) != null) {
                AnnotationMirror aSubscribe = null;
                for (AnnotationMirror annotationMirror : element.getAnnotationMirrors()) {
                    if (creator.getClassName(annotationMirror.getAnnotationType()).equals(ClassName.get(Subscribe.class))) {
                        aSubscribe = annotationMirror;
                        break;
                    }
                }
                final String name = getSubscribeMethodName(element);
                if (aSubscribe != null) {
                    final Object viewmode = creator.findAnnotationValue(aSubscribe, "viewmode");
                    final String key = (String) creator.findAnnotationValue(aSubscribe, "key");
                    if (key == null) {
                        parseSubscribe(viewmode.toString(), "", name, element);
                    } else {
                        parseSubscribe(viewmode.toString(), key, name, element);
                    }
                }
            }
            if (element.getAnnotation(Subscribes.class) != null) {
                parseSubscribes(element);
            }

            if (element.getAnnotation(ViewModelOf.class) != null) {
                final Object v = creator.findAnnotationValue(element, ViewModelOf.class, "value");
                final ViewModelOfElement viewModelOfElement = new ViewModelOfElement(element, v != null ? v.toString() : "");
                final String key = mapKey(viewModelOfElement);
                viewModelOfMap.put(key, viewModelOfElement);
            }
        }

        private void parseSubscribes(Element element) throws RuntimeException {
            AnnotationMirror aSubscribes = null;
            for (AnnotationMirror annotationMirror : element.getAnnotationMirrors()) {
                if (creator.getClassName(annotationMirror.getAnnotationType()).equals(ClassName.get(Subscribes.class))) {
                    aSubscribes = annotationMirror;
                    break;
                }
            }
            final String name = getSubscribeMethodName(element);
            final StringBuilder stringBuilder = new StringBuilder();
            if (aSubscribes != null) {
                final String aaSubscribesValue = creator.findAnnotationValue(aSubscribes, "value").toString();
                stringBuilder.append("aaSubscribesValue:");
                stringBuilder.append(aaSubscribesValue);
                stringBuilder.append("\n");
                final String[] annotationArray = aaSubscribesValue.split("@library.neetoffice.com.neetannotation.Subscribe");
                for (String annotation : annotationArray) {
                    if (annotation.contains("viewmode=")) {
                        final String viewmode = annotation.split("viewmode=")[1].split(".class")[0];
                        try {
                            final String key = annotation.split("key=\"")[1].split("\"")[0];
                            parseSubscribe(viewmode, key, name, element);
                        } catch (Exception e) {
                            parseSubscribe(viewmode, "", name, element);
                        }
                    }
                }
            }
        }

        private String getSubscribeMethodName(Element element) {
            final String name;
            final AnnotationMirror named = AnnotationHelp.findAnnotationMirror(element, DaggerClass.Named);
            if (named == null) {
                name = element.getSimpleName().toString();
            } else {
                final String value = (String) AnnotationHelp.findAnnotationValue(named, "value");
                if (value.equals("null")) {
                    name = element.getSimpleName().toString();
                } else {
                    name = value;
                }
            }
            return name;
        }

        private void parseSubscribe(String viewModel, String key, String subjectName, Element element) {
            if (element instanceof ExecutableElement) {
                final ExecutableElement method = (ExecutableElement) element;
                if (method.getParameters().isEmpty()) {
                    final SubscribeElement subscribeElement = SubscribeElement.createInstanceByCompletedElement(element);
                    subscribeElement.viewModel = viewModel;
                    subscribeElement.subjectName = subjectName;
                    subscribeElement.key = key;
                    parseSubscribeElement(subscribeElement);
                } else {
                    final SubscribeElement subscribeElement = SubscribeElement.createInstanceByElement(element);
                    subscribeElement.viewModel = viewModel;
                    subscribeElement.subjectName = subjectName;
                    subscribeElement.key = key;
                    parseSubscribeElement(subscribeElement);
                }
            } else {
                final SubscribeElement subscribeElement = SubscribeElement.createInstanceByElement(element);
                subscribeElement.viewModel = viewModel;
                subscribeElement.subjectName = subjectName;
                subscribeElement.key = key;
                parseSubscribeElement(subscribeElement);
            }
        }

        private void parseSubscribeElement(SubscribeElement subscribeElement) {
            final Element element = subscribeElement.element;
            String disposableFieldName = getDisposableFieldName(subscribeElement);
            if (subscribeElement.viewModel != null) {
                final String key = mapKey(subscribeElement);
                if (subscribeMap.containsKey(key)) {
                    final HashMap<String, SubscribeGroup> map = subscribeMap.get(key);
                    final SubscribeGroup subscribeGroup;
                    if (map.containsKey(disposableFieldName)) {
                        subscribeGroup = map.get(disposableFieldName);
                    } else {
                        subscribeGroup = new SubscribeGroup(key, viewModelName(subscribeElement), subscribeElement.viewModel, subscribeElement.key, subscribeElement.subjectName);
                        map.put(disposableFieldName, subscribeGroup);
                    }
                    if (EVENY_NEXT.equals(subscribeElement.eventType)) {
                        subscribeGroup.nexts.add(subscribeElement);
                    } else if (EVENY_COMPLETED.equals(subscribeElement.eventType)) {
                        subscribeGroup.completeds.add(subscribeElement);
                    }
                } else {
                    final HashMap<String, SubscribeGroup> map = new HashMap<>();
                    subscribeMap.put(key, map);
                    final SubscribeGroup subscribeGroup = new SubscribeGroup(key, viewModelName(subscribeElement), subscribeElement.viewModel, subscribeElement.key, subscribeElement.subjectName);
                    map.put(disposableFieldName, subscribeGroup);
                    if (EVENY_NEXT.equals(subscribeElement.eventType)) {
                        subscribeGroup.nexts.add(subscribeElement);
                    } else if (EVENY_COMPLETED.equals(subscribeElement.eventType)) {
                        subscribeGroup.completeds.add(subscribeElement);
                    }
                }
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
                    .add("this.$N = ", viewModelOf.element.getSimpleName())
                    .add(ViewModelHelp.viewModelProvidersOf(context_from, key, typeName))
                    .addStatement("")
                    .build();
        }

        private boolean isViewModeFieldinSubscribeGroup(SubscribeGroup group) {
            return viewModelOfMap.containsKey(group.mapKey);
        }

        private ViewModelOfElement getViewModelOfSubscribeGroup(SubscribeGroup group) {
            return viewModelOfMap.get(group.mapKey);
        }

        String getDisposableFieldName(SubscribeElement subscribeElement) {
            final Element element = subscribeElement.element;
            final String mName = viewModelName(subscribeElement) + subscribeElement.subjectName;
            final String fName = viewModelName(subscribeElement) + subscribeElement.subjectName;
            if (element.asType() instanceof DeclaredType) {
                return String.format("m%s%s_Disposable", mName.toUpperCase().charAt(0), mName.substring(1));
            } else if (element.asType() instanceof ExecutableType) {
                return String.format("f%s%s_Disposable", fName.toUpperCase().charAt(0), fName.substring(1));
            }
            return "";
        }

        void addDisposableFieldInType(TypeSpec.Builder tb) {
            tb.addField(FieldSpec.builder(RxJavaClass.CompositeDisposable, CompositeDisposable_NAME, Modifier.PRIVATE)
                    .initializer("new $T()", RxJavaClass.CompositeDisposable).build());
        }


        private CodeBlock createSubscribeToViewMode(SubscribeGroup subscribeGroup, String thisClassName, TypeName viewModelType, String viewModelName) {
            final CodeBlock.Builder cb = CodeBlock.builder();
            cb.add("$N.add((($T)$N).$N.subject()", CompositeDisposable_NAME, viewModelType, viewModelName, subscribeGroup.subjectName);
            final CodeBlock nextCodeBlock;
            final CodeBlock errorCodeBlock;
            final CodeBlock completedCodeBlock;
            final CodeBlock.Builder nextAccept = CodeBlock.builder();
            final CodeBlock.Builder errorAccept = CodeBlock.builder();
            for (SubscribeElement subscribeElement : subscribeGroup.nexts) {
                final Element element = subscribeElement.element;
                if (element.asType() instanceof DeclaredType) {
                    TypeName elementType = creator.getClassName(element.asType());
                    nextAccept.beginControlFlow("if(t instanceof $T)", elementType)
                            .addStatement("$N.this.$N = t", thisClassName, element.getSimpleName())
                            .endControlFlow("\n");
                } else {
                    try {
                        final ExecutableElement method = (ExecutableElement) element;
                        final List<? extends VariableElement> parameters = method.getParameters();
                        for (VariableElement parameter : parameters) {
                            final TypeName elementType = creator.getClassName(parameter.asType());
                            if (creator.isInstanceOf(parameter.asType(), ClassName.get(Throwable.class))) {
                                errorAccept.beginControlFlow("if(t instanceof $T)", elementType);
                                final Iterator<? extends VariableElement> iterator = parameters.iterator();
                                errorAccept.add("$N.this.$N(", thisClassName, element.getSimpleName());
                                while (iterator.hasNext()) {
                                    final VariableElement p = iterator.next();
                                    final TypeName pType = creator.getClassName(p.asType());
                                    if (creator.getClassName(parameter.asType()).equals(pType)) {
                                        errorAccept.add("($T)t", pType);
                                    } else {
                                        errorAccept.add(AnnotationHelp.addNullCode(parameter));
                                    }
                                    if (iterator.hasNext()) {
                                        errorAccept.add(",");
                                    }
                                }
                                errorAccept.addStatement(")");
                                errorAccept.endControlFlow();
                            } else  if (creator.isInstanceOf(parameter.asType(), ClassName.get(List.class))){
                                nextAccept.beginControlFlow("if(t instanceof $T)", List.class);
                                final Iterator<? extends VariableElement> iterator = parameters.iterator();
                                nextAccept.beginControlFlow("try");
                                nextAccept.add("$N.this.$N(", thisClassName, element.getSimpleName());
                                while (iterator.hasNext()) {
                                    final VariableElement p = iterator.next();
                                    final TypeName pType = creator.getClassName(p.asType());
                                    if (creator.getClassName(parameter.asType()).equals(pType)) {
                                        nextAccept.add("($T)t", pType);
                                    } else {
                                        nextAccept.add(AnnotationHelp.addNullCode(parameter));
                                    }
                                    if (iterator.hasNext()) {
                                        nextAccept.add(",");
                                    }
                                }
                                nextAccept.addStatement(")");
                                nextAccept.nextControlFlow("catch ($T e)",ClassCastException.class);
                                nextAccept.endControlFlow();
                                nextAccept.endControlFlow();
                            } else  if (creator.isInstanceOf(parameter.asType(), ClassName.get(Set.class))){
                                nextAccept.beginControlFlow("if(t instanceof $T)", Set.class);
                                final Iterator<? extends VariableElement> iterator = parameters.iterator();
                                nextAccept.beginControlFlow("try");
                                nextAccept.add("$N.this.$N(", thisClassName, element.getSimpleName());
                                while (iterator.hasNext()) {
                                    final VariableElement p = iterator.next();
                                    final TypeName pType = creator.getClassName(p.asType());
                                    if (creator.getClassName(parameter.asType()).equals(pType)) {
                                        nextAccept.add("($T)t", pType);
                                    } else {
                                        nextAccept.add(AnnotationHelp.addNullCode(parameter));
                                    }
                                    if (iterator.hasNext()) {
                                        nextAccept.add(",");
                                    }
                                }
                                nextAccept.addStatement(")");
                                nextAccept.nextControlFlow("catch ($T e)",ClassCastException.class);
                                nextAccept.endControlFlow();
                                nextAccept.endControlFlow();
                            } else  if (creator.isInstanceOf(parameter.asType(), ClassName.get(Collection.class))){
                                nextAccept.beginControlFlow("if(t instanceof $T)", Collection.class);
                                final Iterator<? extends VariableElement> iterator = parameters.iterator();
                                nextAccept.beginControlFlow("try");
                                nextAccept.add("$N.this.$N(", thisClassName, element.getSimpleName());
                                while (iterator.hasNext()) {
                                    final VariableElement p = iterator.next();
                                    final TypeName pType = creator.getClassName(p.asType());
                                    if (creator.getClassName(parameter.asType()).equals(pType)) {
                                        nextAccept.add("($T)t", pType);
                                    } else {
                                        nextAccept.add(AnnotationHelp.addNullCode(parameter));
                                    }
                                    if (iterator.hasNext()) {
                                        nextAccept.add(",");
                                    }
                                }
                                nextAccept.addStatement(")");
                                nextAccept.nextControlFlow("catch ($T e)",ClassCastException.class);
                                nextAccept.endControlFlow();
                                nextAccept.endControlFlow();
                            } else {
                                nextAccept.beginControlFlow("if(t instanceof $T)", elementType);
                                final Iterator<? extends VariableElement> iterator = parameters.iterator();
                                nextAccept.add("$N.this.$N(", thisClassName, element.getSimpleName());
                                while (iterator.hasNext()) {
                                    final VariableElement p = iterator.next();
                                    final TypeName pType = creator.getClassName(p.asType());
                                    if (creator.getClassName(parameter.asType()).equals(pType)) {
                                        nextAccept.add("($T)t", pType);
                                    } else {
                                        nextAccept.add(AnnotationHelp.addNullCode(parameter));
                                    }
                                    if (iterator.hasNext()) {
                                        nextAccept.add(",");
                                    }
                                }
                                nextAccept.addStatement(")");
                                nextAccept.endControlFlow();
                            }
                        }
                    } catch (Exception e) {
                        cb.addStatement("//" + e.toString());
                    }
                }
            }
            for (SubscribeElement subscribeElement : subscribeGroup.completeds) {
                final Element element = subscribeElement.element;
                final ExecutableElement method = (ExecutableElement) element;
                final List<? extends VariableElement> parameters = method.getParameters();
                final Iterator<? extends VariableElement> iterator = parameters.iterator();
                final StringBuffer parameterString = new StringBuffer();
                while (iterator.hasNext()) {
                    final VariableElement parameter = iterator.next();
                    parameterString.append(AnnotationHelp.addNullCode(parameter));
                    if (iterator.hasNext()) {
                        parameterString.append(",");
                    }
                }
                nextAccept.addStatement("$N.this.$N($N)", thisClassName, element.getSimpleName(), parameterString.toString());
            }
            nextAccept.add(errorAccept.build());
            nextCodeBlock = ReactiveXHelp.newConsumer(ClassName.get(Object.class), "t", nextAccept.build());
            errorCodeBlock = ReactiveXHelp.newConsumer(ClassName.get(Throwable.class), "t", errorAccept.build());
            completedCodeBlock = CodeBlock.builder().add("$T.EMPTY_ACTION", RxJavaClass.Functions).build();

            cb.add(".subscribe(")
                    .add(nextCodeBlock)
                    .add(",")
                    .add(errorCodeBlock)
                    .add(",")
                    .add(completedCodeBlock)
                    .addStatement("))");
            return cb.build();
        }

        CodeBlock createCodeSubscribeInOnCreate(String thisClassName, String context_from) {
            final CodeBlock.Builder cb = CodeBlock.builder();
            final HashMap<String, String> localViewModelNames = new HashMap<>();
            for (HashMap<String, SubscribeGroup> subscribeGroups : subscribeMap.values()) {
                for (SubscribeGroup subscribeGroup : subscribeGroups.values()) {
                    if (isViewModeFieldinSubscribeGroup(subscribeGroup)) {
                        final ViewModelOfElement viewModelOf = getViewModelOfSubscribeGroup(subscribeGroup);
                        final ClassName typeName = viewModelOf.getProcessorType(creator);
                        final String viewModelName = viewModelOf.element.getSimpleName().toString();
                        cb.add(createSubscribeToViewMode(subscribeGroup, thisClassName, typeName, viewModelName));
                    } else {
                        final ClassName typeName = ClassName.bestGuess(subscribeGroup.viewModel);
                        final ClassName typeName_ = ClassName.bestGuess(subscribeGroup.viewModel + "_");
                        if (!localViewModelNames.containsKey(subscribeGroup.mapKey)) {
                            final String localViewModelName = "lm" + subscribeGroup.viewModelKey + typeName.simpleName();
                            cb.add("$T $N = ", typeName, localViewModelName);
                            cb.add(ViewModelHelp.viewModelProvidersOf(context_from, subscribeGroup.viewModelKey, typeName_));
                            cb.addStatement("");
                            localViewModelNames.put(subscribeGroup.mapKey, localViewModelName);
                            cb.add(createSubscribeToViewMode(subscribeGroup, thisClassName, typeName, localViewModelName));
                        } else {
                            final String localViewModelName = localViewModelNames.get(subscribeGroup.mapKey);
                            cb.add(createSubscribeToViewMode(subscribeGroup, thisClassName, typeName_, localViewModelName));
                        }
                    }
                }
            }
            return cb.build();
        }

        CodeBlock createAddLifecycle(String context_from) {
            final CodeBlock.Builder cb = CodeBlock.builder();
            for (ViewModelOfElement viewModelOf : viewModelOfMap.values()) {
                final ClassName typeName = viewModelOf.getProcessorType(creator);
                cb.add(CodeBlock.builder().addStatement("$N.getLifecycle().addObserver(($T)$N)", context_from, typeName, viewModelOf.element.getSimpleName()).build());
            }
            final HashMap<String, String> localViewModelNames = new HashMap<>();
            for (HashMap<String, SubscribeGroup> subscribeGroups : subscribeMap.values()) {
                for (SubscribeGroup subscribeGroup : subscribeGroups.values()) {
                    if (isViewModeFieldinSubscribeGroup(subscribeGroup)) {
                    } else {
                        final ClassName typeName = ClassName.bestGuess(subscribeGroup.viewModel);
                        final ClassName typeName_ = ClassName.bestGuess(subscribeGroup.viewModel + "_");
                        if (!localViewModelNames.containsKey(subscribeGroup.mapKey)) {
                            final String localViewModelName = "lm" + subscribeGroup.viewModelKey + typeName.simpleName();
                            localViewModelNames.put(subscribeGroup.mapKey, localViewModelName);
                            cb.add(CodeBlock.builder().addStatement("$N.getLifecycle().addObserver(($T)$N)", context_from, typeName_, localViewModelName).build());
                        }
                    }
                }
            }
            return cb.build();
        }

        CodeBlock createDispose() {
            CodeBlock.Builder code = CodeBlock.builder();
            code.addStatement("$N.clear()", CompositeDisposable_NAME);
            return code.build();
        }
    }
}
