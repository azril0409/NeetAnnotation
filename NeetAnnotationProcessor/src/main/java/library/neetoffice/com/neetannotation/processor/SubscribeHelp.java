package library.neetoffice.com.neetannotation.processor;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.ExecutableType;
import javax.lang.model.type.TypeMirror;

import library.neetoffice.com.neetannotation.Background;
import library.neetoffice.com.neetannotation.Subscribe;
import library.neetoffice.com.neetannotation.UIThread;
import library.neetoffice.com.neetannotation.ViewModelOf;

public class SubscribeHelp {
    static class SubscribeElement {
        final Element element;
        Object viewModel;
        String key = "";
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

        ClassName getProcessorType(ProcessorUtil processorUtil) {
            return ClassName.get(processorUtil.getPackageName(element), element.asType().toString() + "_");
        }
    }

    private final ProcessorUtil processorUtil;
    private final DaggerHelp daggerHelp;
    private final HashMap<String, List<SubscribeElement>> subscribeMap = new HashMap<>();
    private final HashMap<String, ViewModelOfElement> viewModelOfMap = new HashMap<>();
    private final List<String> disposableFieldNames = new ArrayList<>();

    public SubscribeHelp(ProcessorUtil processorUtil) {
        this.processorUtil = processorUtil;
        daggerHelp = new DaggerHelp(processorUtil);
    }

    private String mapKey(SubscribeElement subscribeElement) {
        return subscribeElement.named + "_" + subscribeElement.viewModel.toString();
    }

    private String mapKey(ViewModelOfElement viewModelOfElement) {
        return viewModelOfElement.key + "_" + ClassName.get(viewModelOfElement.element.asType()).toString();
    }

    public void parseElement(Element element) {
        if (element.getAnnotation(Subscribe.class) != null) {
            final SubscribeElement subscribeElement = new SubscribeElement(element);
            subscribeElement.viewModel = processorUtil.findAnnotationValue(element, Subscribe.class, "value");
            final Object named = processorUtil.findAnnotationValue(element, DaggerClass.Named, "value");
            subscribeElement.named = named != null ? named.toString() : element.getSimpleName().toString();
            final Object subscribe_key = processorUtil.findAnnotationValue(element, Subscribe.class, "key");
            subscribeElement.key = subscribe_key != null ? subscribe_key.toString() : "";
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
            final ViewModelOfElement viewModelOfElement = new ViewModelOfElement(element);
            final Object v = processorUtil.findAnnotationValue(element, ViewModelOf.class, "key");
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
        final ClassName viewModelProviders = ClassName.get("android.arch.lifecycle", "ViewModelProviders");
        final ClassName typeName = viewModelOf.getProcessorType(processorUtil);
        final String key = viewModelOf.key;
        if (key.isEmpty()) {
            return CodeBlock.builder()
                    .addStatement("this.$N = $T.of($N).get($T.class)", viewModelOf.element.getSimpleName(), viewModelProviders, context_from, typeName)
                    .build();
        }
        return CodeBlock.builder()
                .addStatement("this.$N = $T.of($N).get($S,$T.class)", viewModelOf.element.getSimpleName(), viewModelProviders, context_from, key, typeName)
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
            final String subjectName = daggerHelp.findNameFromDagger(element);
            cb.add("$N = (($T)$N).<$T>findSubjectByName($S)", disposableFieldName, viewModelType, viewModelName, declaredType, subjectName);
            if (element.getAnnotation(Background.class) != null) {
                final Background aBackground = element.getAnnotation(Background.class);
                if (aBackground.delayMillis() > 0) {
                    cb.add(ReactiveXHelp.delay(aBackground.delayMillis()));
                }
                cb.add(ReactiveXHelp.observeOnThread());
            } else if (element.getAnnotation(UIThread.class) != null) {
                final UIThread aUIThread = element.getAnnotation(UIThread.class);
                if (aUIThread.delayMillis() > 0) {
                    cb.add(ReactiveXHelp.delay(aUIThread.delayMillis()));
                }
                cb.add(ReactiveXHelp.observeOnMain());
            } else {
                cb.add(ReactiveXHelp.observeOnMain());
            }
            cb.add(ReactiveXHelp.subscribeConsumer(ClassName.get(declaredType), "t",
                    CodeBlock.builder().addStatement("$N.this.$N = t", thisClassName, element.getSimpleName()).build()));
        } else if (element instanceof ExecutableElement) {
            final ExecutableElement method = (ExecutableElement) element;
            final String subjectName = daggerHelp.findNameFromDagger(element);
            final List<? extends VariableElement> parameters = method.getParameters();
            final TypeName parameterType;
            if (parameters.size() > 0) {
                parameterType = processorUtil.getClassName(parameters.get(0).asType());
            } else {
                parameterType = ClassName.get(Object.class);
            }
            final Iterator<? extends VariableElement> iterator = parameters.iterator();
            final StringBuffer parameterString = new StringBuffer();
            while (iterator.hasNext()) {
                final VariableElement parameter = iterator.next();
                if(processorUtil.getClassName(parameter.asType()).equals(parameterType)){
                    parameterString.append("t");
                }else {
                    parameterString.append(AnnotationHelp.addNullCode(parameter));
                }
                if (iterator.hasNext()) {
                    parameterString.append(",");
                }
            }
            cb.add("$N = (($T)$N).<$T>findSubjectByName($S)", disposableFieldName, viewModelType, viewModelName, parameterType, subjectName);
            if (element.getAnnotation(Background.class) != null) {
                cb.add(ReactiveXHelp.observeOnThread());
            } else {
                cb.add(ReactiveXHelp.observeOnMain());
            }
            cb.add(ReactiveXHelp.subscribeConsumer(parameterType, "t",
                    CodeBlock.builder().addStatement("$N.this.$N($N)", thisClassName, element.getSimpleName(),parameterString.toString()).build()));
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
                    final ClassName typeName = viewModelOf.getProcessorType(processorUtil);
                    final String viewModelName = viewModelOf.element.getSimpleName().toString();
                    final String disposableFieldName = getDisposableFieldName(subscribeElement);
                    cb.add(createSubscribeToViewMode(subscribeElement, thisClassName, typeName, viewModelName, disposableFieldName));
                } else {
                    final ClassName typeName = ClassName.bestGuess(subscribeElement.viewModel.toString() + "_");
                    if (!localViewModelNames.containsKey(mapKey(subscribeElement))) {
                        final String localViewModelName = "lm" + typeName.simpleName();
                        final ClassName viewModelProviders = ClassName.get("android.arch.lifecycle", "ViewModelProviders");
                        if (subscribeElement.key.isEmpty()) {
                            cb.addStatement("$T $N = $T.of($N).get($T.class)", typeName, localViewModelName, viewModelProviders, context_from, typeName);
                        } else {
                            cb.addStatement("$T $N = $T.of($N).get($S,$T.class)", typeName, localViewModelName, viewModelProviders, context_from, subscribeElement.key, typeName);
                        }
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

    public void addStatementSubscribeInOnDestroy(MethodSpec.Builder onDestroy) {
        for (String fieldName : disposableFieldNames) {
            onDestroy.addStatement("$N.dispose()", fieldName);
        }
    }
}
