package library.neetoffice.com.neetannotation.processor;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.TypeName;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Name;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeMirror;

import library.neetoffice.com.neetannotation.AfterTextChange;
import library.neetoffice.com.neetannotation.BeforeTextChange;
import library.neetoffice.com.neetannotation.CheckedChange;
import library.neetoffice.com.neetannotation.Click;
import library.neetoffice.com.neetannotation.FocusChange;
import library.neetoffice.com.neetannotation.ItemClick;
import library.neetoffice.com.neetannotation.ItemLongClick;
import library.neetoffice.com.neetannotation.ItemSelect;
import library.neetoffice.com.neetannotation.LongClick;
import library.neetoffice.com.neetannotation.TextChange;
import library.neetoffice.com.neetannotation.Touch;
import library.neetoffice.com.neetannotation.TouchDown;
import library.neetoffice.com.neetannotation.TouchMove;
import library.neetoffice.com.neetannotation.TouchUp;

public class ListenerHelp {
    private static final String RETURNS = "returns";
    private final BaseCreator creator;

    public ListenerHelp(BaseCreator creator) {
        this.creator = creator;
    }


    public Builder builder(String className, String findView_from, String context_from, String defPackage) {
        return new Builder(creator, className, findView_from, context_from, defPackage);
    }

    private static class ElementBundle {
        private final Element element;
        private final CodeBlock getIdCode;

        private ElementBundle(Element element, CodeBlock getIdCode) {
            this.element = element;
            this.getIdCode = getIdCode;
        }
    }

    public static class Builder {
        private final BaseCreator creator;
        private final String className;
        private final String findView_from;
        private final String context_from;
        private final String defPackage;
        private final HashMap<String, ElementBundle> clickElements = new HashMap<>();
        private final HashMap<String, ElementBundle> longClickElements = new HashMap<>();
        private final HashMap<String, ElementBundle> touchElements = new HashMap<>();
        private final HashMap<String, ElementBundle> touchDownElements = new HashMap<>();
        private final HashMap<String, ElementBundle> touchMovenElements = new HashMap<>();
        private final HashMap<String, ElementBundle> touchUpElements = new HashMap<>();
        private final HashMap<String, ElementBundle> itemClickElements = new HashMap<>();
        private final HashMap<String, ElementBundle> itemLongClickElements = new HashMap<>();
        private final HashMap<String, ElementBundle> itemSelectElements = new HashMap<>();
        private final HashMap<String, ElementBundle> checkedChangeElements = new HashMap<>();
        private final HashMap<String, ElementBundle> textChangeElements = new HashMap<>();
        private final HashMap<String, ElementBundle> beforeTextChangeElements = new HashMap<>();
        private final HashMap<String, ElementBundle> afterTextChangeElements = new HashMap<>();
        private final HashMap<String, ElementBundle> focusChangElements = new HashMap<>();

        public Builder(BaseCreator creator, String className, String findView_from, String context_from, String defPackage) {
            this.creator = creator;
            this.className = className;
            this.findView_from = findView_from;
            this.context_from = context_from;
            this.defPackage = defPackage;
        }

        public void parseElement(javax.lang.model.element.Element element) {
            if (element.getAnnotation(Click.class) != null) {
                final Click aClick = element.getAnnotation(Click.class);
                addElementsMap(element, aClick.value(), aClick.resName(), "Clicked", clickElements);
            }
            if (element.getAnnotation(LongClick.class) != null) {
                final LongClick aLongClick = element.getAnnotation(LongClick.class);
                addElementsMap(element, aLongClick.value(), aLongClick.resName(), "LongClicked", longClickElements);
            }
            if (element.getAnnotation(Touch.class) != null) {
                final Touch aTouch = element.getAnnotation(Touch.class);
                addElementsMap(element, aTouch.value(), aTouch.resName(), "Touched", touchElements);
            }
            if (element.getAnnotation(TouchDown.class) != null) {
                final TouchDown aTouchDown = element.getAnnotation(TouchDown.class);
                addElementsMap(element, aTouchDown.value(), aTouchDown.resName(), "TouchDowned", touchDownElements);
            }
            if (element.getAnnotation(TouchMove.class) != null) {
                final TouchMove aTouchMove = element.getAnnotation(TouchMove.class);
                addElementsMap(element, aTouchMove.value(), aTouchMove.resName(), "TouchMoved", touchMovenElements);
            }
            if (element.getAnnotation(TouchUp.class) != null) {
                final TouchUp aTouchUp = element.getAnnotation(TouchUp.class);
                addElementsMap(element, aTouchUp.value(), aTouchUp.resName(), "TouchUped", touchUpElements);
            }
            if (element.getAnnotation(ItemClick.class) != null) {
                final ItemClick aItemClick = element.getAnnotation(ItemClick.class);
                addElementsMap(element, aItemClick.value(), aItemClick.resName(), "ItemClicked", itemClickElements);
            }
            if (element.getAnnotation(ItemLongClick.class) != null) {
                final ItemLongClick aItemLongClick = element.getAnnotation(ItemLongClick.class);
                addElementsMap(element, aItemLongClick.value(), aItemLongClick.resName(), "ItemLongClicked", itemLongClickElements);
            }
            if (element.getAnnotation(ItemSelect.class) != null) {
                final ItemSelect aItemSelect = element.getAnnotation(ItemSelect.class);
                addElementsMap(element, aItemSelect.value(), aItemSelect.resName(), "ItemSelected", itemSelectElements);
            }
            if (element.getAnnotation(CheckedChange.class) != null) {
                final CheckedChange aCheckedChange = element.getAnnotation(CheckedChange.class);
                addElementsMap(element, aCheckedChange.value(), aCheckedChange.resName(), "CheckedChanged", checkedChangeElements);
            }
            if (element.getAnnotation(TextChange.class) != null) {
                final TextChange aTextChange = element.getAnnotation(TextChange.class);
                addElementsMap(element, aTextChange.value(), aTextChange.resName(), "TextChanged", textChangeElements);
            }
            if (element.getAnnotation(BeforeTextChange.class) != null) {
                final BeforeTextChange aBeforeTextChange = element.getAnnotation(BeforeTextChange.class);
                addElementsMap(element, aBeforeTextChange.value(), aBeforeTextChange.resName(), "BeforeTextChanged", beforeTextChangeElements);
            }
            if (element.getAnnotation(AfterTextChange.class) != null) {
                final AfterTextChange aAfterTextChange = element.getAnnotation(AfterTextChange.class);
                addElementsMap(element, aAfterTextChange.value(), aAfterTextChange.resName(), "AfterTextChanged", afterTextChangeElements);
            }
            if (element.getAnnotation(FocusChange.class) != null) {
                final FocusChange aFocusChange = element.getAnnotation(FocusChange.class);
                addElementsMap(element, aFocusChange.value(), aFocusChange.resName(), "FocusChanged", focusChangElements);
            }
        }

        private void addElementsMap(Element element, int[] rids, String[] resNames, String end, HashMap<String, ElementBundle> elements) {
            for (int rid : rids) {
                final String viewName = "i_" + rid;
                final ElementBundle bundle = new ElementBundle(element, AndroidResHelp.id(rid, "", "", context_from, defPackage));
                elements.put(viewName, bundle);
            }
            for (String resName : resNames) {
                final String viewName = "n_" + resName;
                final ElementBundle bundle = new ElementBundle(element, AndroidResHelp.id(0, resName, "", context_from, defPackage));
                elements.put(viewName, bundle);
            }
            if (rids.length == 0 && resNames.length == 0) {
                final String elementName = parseMethodName(element.getSimpleName(), end);
                final String viewName = "n_" + elementName;
                final ElementBundle bundle = new ElementBundle(element, AndroidResHelp.id(0, "", elementName, context_from, defPackage));
                elements.put(viewName, bundle);
            }
        }

        public CodeBlock createListenerCode() {
            final CodeBlock.Builder cb = CodeBlock.builder();
            cb.add(addLocalViewCode());
            cb.add(addClick());
            cb.add(addLongClick());
            cb.add(addTouch());
            cb.add(addItemClick());
            cb.add(addItemLongClick());
            cb.add(addItemSelect());
            cb.add(addCheckedChange());
            cb.add(addTextChange());
            cb.add(addFocusChang());
            return cb.build();
        }

        private HashMap<String, CodeBlock> getIdCodeMap(HashMap<String, ElementBundle> map) {
            final HashMap<String, CodeBlock> hashMap = new HashMap<>();
            for (Map.Entry<String, ElementBundle> entry : map.entrySet()) {
                hashMap.put(entry.getKey(), entry.getValue().getIdCode);
            }
            return hashMap;
        }

        private CodeBlock addLocalViewCode() {
            final HashMap<String, CodeBlock> getIdCodes = new HashMap<>();
            getIdCodes.putAll(getIdCodeMap(clickElements));
            getIdCodes.putAll(getIdCodeMap(longClickElements));
            getIdCodes.putAll(getIdCodeMap(touchElements));
            getIdCodes.putAll(getIdCodeMap(touchDownElements));
            getIdCodes.putAll(getIdCodeMap(touchMovenElements));
            getIdCodes.putAll(getIdCodeMap(touchUpElements));
            getIdCodes.putAll(getIdCodeMap(itemClickElements));
            getIdCodes.putAll(getIdCodeMap(itemLongClickElements));
            getIdCodes.putAll(getIdCodeMap(itemSelectElements));
            getIdCodes.putAll(getIdCodeMap(checkedChangeElements));
            getIdCodes.putAll(getIdCodeMap(textChangeElements));
            getIdCodes.putAll(getIdCodeMap(beforeTextChangeElements));
            getIdCodes.putAll(getIdCodeMap(afterTextChangeElements));
            getIdCodes.putAll(getIdCodeMap(focusChangElements));
            final CodeBlock.Builder cb = CodeBlock.builder();
            for (Map.Entry<String, CodeBlock> entry : getIdCodes.entrySet()) {
                cb.add("final $T $N = $N.findViewById(", AndroidClass.View, entry.getKey(), findView_from)
                        .add(entry.getValue())
                        .addStatement(")");
            }
            return cb.build();
        }

        private CodeBlock addInstanceOfCode(String viewName, TypeName cls, CodeBlock callMethod) {
            final CodeBlock.Builder cb = CodeBlock.builder();
            if (!cls.equals(AndroidClass.View)) {
                return cb.beginControlFlow("if($N instanceof $T)", viewName, cls)
                        .add("(($T)$N).", cls, viewName)
                        .add(callMethod)
                        .endControlFlow().build();
            } else {
                return cb.add("$N.", viewName).add(callMethod).build();
            }
        }

        private CodeBlock addClick() {
            final CodeBlock.Builder cb = CodeBlock.builder();
            for (Map.Entry<String, ElementBundle> entry : clickElements.entrySet()) {
                final ExecutableElement method = (ExecutableElement) entry.getValue().element;
                final Iterator<? extends VariableElement> parameters = method.getParameters().iterator();
                final CodeBlock.Builder code = CodeBlock.builder()
                        .add("$N.this.$N(", className, method.getSimpleName());
                while (parameters.hasNext()) {
                    final VariableElement parameter = parameters.next();
                    if (creator.isInstanceOf(parameter.asType(), AndroidClass.View)) {
                        if (AndroidClass.View.equals(ClassName.get(parameter.asType()))) {
                            code.add("$N", "view");
                        } else {
                            code.add("($T)$N", parameter.asType(), "view");
                        }
                    } else {
                        code.add(AnnotationHelp.addNullCode(parameter));
                    }
                    if (parameters.hasNext()) {
                        code.add(",");
                    }
                }
                code.addStatement(")");
                final CodeBlock.Builder callMethod = CodeBlock.builder()
                        .add("setOnClickListener(")
                        .beginControlFlow("new $T()", AndroidClass.View_OnClickListener)
                        .beginControlFlow("@$T\npublic void onClick($T view)", Override.class, AndroidClass.View)
                        .add(code.build())
                        .endControlFlow()
                        .endControlFlow(")");
                cb.add(addInstanceOfCode(entry.getKey(), AndroidClass.View, callMethod.build()));
            }
            return cb.build();
        }

        private CodeBlock addLongClick() {
            final CodeBlock.Builder cb = CodeBlock.builder();
            for (Map.Entry<String, ElementBundle> entry : longClickElements.entrySet()) {
                final ExecutableElement method = (ExecutableElement) entry.getValue().element;
                final Iterator<? extends VariableElement> parameters = method.getParameters().iterator();
                final CodeBlock.Builder code = CodeBlock.builder()
                        .addStatement("boolean returns = false");
                if (creator.getClassName(method.getReturnType()).equals(ClassName.get(Boolean.class))) {
                    code.add("returns = returns|$N.this.$N(", className, method.getSimpleName());
                } else {
                    code.add("$N.this.$N(", className, method.getSimpleName());
                }
                while (parameters.hasNext()) {
                    final VariableElement parameter = parameters.next();
                    if (creator.isInstanceOf(parameter.asType(), AndroidClass.View)) {
                        if (AndroidClass.View.equals(ClassName.get(parameter.asType()))) {
                            code.add("$N", "view");
                        } else {
                            code.add("($T)$N", parameter.asType(), "view");
                        }
                    } else {
                        code.add(AnnotationHelp.addNullCode(parameter));
                    }
                    if (parameters.hasNext()) {
                        code.add(",");
                    }
                }
                code.addStatement(")")
                        .addStatement("return returns");
                final CodeBlock.Builder callMethod = CodeBlock.builder()
                        .add("setOnLongClickListener(")
                        .beginControlFlow("new $T()", AndroidClass.View_OnLongClickListener)
                        .beginControlFlow("@$T\npublic boolean onLongClick($T view)", Override.class, AndroidClass.View)
                        .add(code.build())
                        .endControlFlow()
                        .endControlFlow(")");
                cb.add(addInstanceOfCode(entry.getKey(), AndroidClass.View, callMethod.build()));
            }
            return cb.build();
        }

        private CodeBlock addTouch() {
            final CodeBlock.Builder cb = CodeBlock.builder();
            final HashMap<String, CodeBlock.Builder> onTouchListenerMap = new HashMap<>();
            for (Map.Entry<String, ElementBundle> entry : touchElements.entrySet()) {
                onTouchListenerMap.put(entry.getKey(), createSetOnTouchListener());
            }
            for (Map.Entry<String, ElementBundle> entry : touchDownElements.entrySet()) {
                onTouchListenerMap.put(entry.getKey(), createSetOnTouchListener());
            }
            for (Map.Entry<String, ElementBundle> entry : touchMovenElements.entrySet()) {
                onTouchListenerMap.put(entry.getKey(), createSetOnTouchListener());
            }
            for (Map.Entry<String, ElementBundle> entry : touchUpElements.entrySet()) {
                onTouchListenerMap.put(entry.getKey(), createSetOnTouchListener());
            }

            for (Map.Entry<String, CodeBlock.Builder> entry : onTouchListenerMap.entrySet()) {
                final CodeBlock.Builder onTouchListenerCode = entry.getValue();
                if (touchElements.containsKey(entry.getKey())) {
                    final ElementBundle touch = touchElements.get(entry.getKey());
                    onTouchListenerCode.add(createTouchCode(touch, creator, className));
                }
                if (touchDownElements.containsKey(entry.getKey())) {
                    final ElementBundle touchDown = touchDownElements.get(entry.getKey());
                    onTouchListenerCode.add(createTouchDownCode(touchDown, creator, className));
                }
                if (touchMovenElements.containsKey(entry.getKey())) {
                    final ElementBundle touchMoven = touchMovenElements.get(entry.getKey());
                    onTouchListenerCode.add(createTouchMovenCode(touchMoven, creator, className));
                }
                if (touchUpElements.containsKey(entry.getKey())) {
                    final ElementBundle touchUp = touchUpElements.get(entry.getKey());
                    onTouchListenerCode.add(createTouchUpCode(touchUp, creator, className));
                }
                onTouchListenerCode.addStatement("return $N", RETURNS)
                        .endControlFlow()
                        .endControlFlow(")");
                cb.add(addInstanceOfCode(entry.getKey(), AndroidClass.View, onTouchListenerCode.build()));
            }
            return cb.build();
        }

        private CodeBlock addItemClick() {
            final CodeBlock.Builder cb = CodeBlock.builder();
            for (Map.Entry<String, ElementBundle> entry : itemClickElements.entrySet()) {
                final ExecutableElement method = (ExecutableElement) entry.getValue().element;
                final Iterator<? extends VariableElement> parameters = method.getParameters().iterator();
                final CodeBlock.Builder code = CodeBlock.builder()
                        .add("$N.this.$N(", className, method.getSimpleName());
                TypeMirror itemType = null;
                while (parameters.hasNext()) {
                    final VariableElement parameter = parameters.next();
                    if (creator.isInstanceOf(parameter.asType(), AndroidClass.View)) {
                        if (AndroidClass.View.equals(ClassName.get(parameter.asType()))) {
                            code.add("$N", "view");
                        } else {
                            code.add("($T)$N", parameter.asType(), "view");
                        }
                    } else if (creator.getClassName(parameter.asType()).equals(ClassName.get(Integer.class))) {
                        code.add("position");
                    } else if (creator.getClassName(parameter.asType()).equals(ClassName.get(Long.class))) {
                        code.add("id");
                    } else {
                        if (itemType == null) {
                            itemType = parameter.asType();
                            code.add("($T)parent.getItemAtPosition(position)", itemType);
                        } else {
                            code.add(AnnotationHelp.addNullCode(parameter));
                        }
                    }
                    if (parameters.hasNext()) {
                        code.add(",");
                    }
                }
                code.addStatement(")");
                final CodeBlock.Builder callMethod = CodeBlock.builder()
                        .add("setOnItemClickListener(")
                        .beginControlFlow("new $T()", AndroidClass.AdapterView_OnItemClickListener)
                        .beginControlFlow("@$T\npublic void onItemClick($T<?> parent, $T view, int position, long id)", Override.class, AndroidClass.AdapterView, AndroidClass.View)
                        .add(code.build())
                        .endControlFlow()
                        .endControlFlow(")");

                cb.add(addInstanceOfCode(entry.getKey(), AndroidClass.AdapterView, callMethod.build()));
            }
            return cb.build();
        }

        private CodeBlock addItemLongClick() {
            final CodeBlock.Builder cb = CodeBlock.builder();
            for (Map.Entry<String, ElementBundle> entry : itemLongClickElements.entrySet()) {
                final ExecutableElement method = (ExecutableElement) entry.getValue().element;
                final Iterator<? extends VariableElement> parameters = method.getParameters().iterator();
                final CodeBlock.Builder code = CodeBlock.builder()
                        .addStatement("boolean returns = false");
                if (creator.getClassName(method.getReturnType()).equals(ClassName.get(Boolean.class))) {
                    code.add("returns = $N.this.$N(", className, method.getSimpleName());
                } else {
                    code.add("$N.this.$N(", className, method.getSimpleName());
                }
                TypeMirror itemType = null;
                while (parameters.hasNext()) {
                    final VariableElement parameter = parameters.next();
                    if (creator.isInstanceOf(parameter.asType(), AndroidClass.View)) {
                        if (AndroidClass.View.equals(ClassName.get(parameter.asType()))) {
                            code.add("$N", "view");
                        } else {
                            code.add("($T)$N", parameter.asType(), "view");
                        }
                    } else if (creator.getClassName(parameter.asType()).equals(ClassName.get(Integer.class))) {
                        code.add("position");
                    } else if (creator.getClassName(parameter.asType()).equals(ClassName.get(Long.class))) {
                        code.add("id");
                    } else {
                        if (itemType == null) {
                            itemType = parameter.asType();
                            code.add("($T)parent.getItemAtPosition(position)", itemType);
                        } else {
                            code.add(AnnotationHelp.addNullCode(parameter));
                        }
                    }
                    if (parameters.hasNext()) {
                        code.add(",");
                    }
                }
                code.addStatement(")")
                        .addStatement("return returns");
                final CodeBlock.Builder callMethod = CodeBlock.builder()
                        .add("setOnItemLongClickListener(")
                        .beginControlFlow("new $T()", AndroidClass.AdapterView_OnItemLongClickListener)
                        .beginControlFlow("@$T\npublic boolean onItemLongClick($T<?> parent, $T view, int position, long id)", Override.class, AndroidClass.AdapterView, AndroidClass.View)
                        .add(code.build())
                        .endControlFlow()
                        .endControlFlow(")");

                cb.add(addInstanceOfCode(entry.getKey(), AndroidClass.AdapterView, callMethod.build()));
            }
            return cb.build();
        }

        private CodeBlock addItemSelect() {
            final CodeBlock.Builder cb = CodeBlock.builder();
            for (Map.Entry<String, ElementBundle> entry : itemSelectElements.entrySet()) {
                final ExecutableElement method = (ExecutableElement) entry.getValue().element;
                final Iterator<? extends VariableElement> parameters = method.getParameters().iterator();
                final CodeBlock.Builder code = CodeBlock.builder();
                code.add("$N.this.$N(", className, method.getSimpleName());
                TypeMirror itemType = null;
                while (parameters.hasNext()) {
                    final VariableElement parameter = parameters.next();
                    if (creator.isInstanceOf(parameter.asType(), AndroidClass.View)) {
                        if (AndroidClass.View.equals(ClassName.get(parameter.asType()))) {
                            code.add("$N", "view");
                        } else {
                            code.add("($T)$N", parameter.asType(), "view");
                        }
                    } else if (creator.getClassName(parameter.asType()).equals(ClassName.get(Integer.class))) {
                        code.add("position");
                    } else if (creator.getClassName(parameter.asType()).equals(ClassName.get(Long.class))) {
                        code.add("id");
                    } else {
                        if (itemType == null) {
                            itemType = parameter.asType();
                            code.add("($T)parent.getItemAtPosition(position)", itemType);
                        } else {
                            code.add(AnnotationHelp.addNullCode(parameter));
                        }
                    }
                    if (parameters.hasNext()) {
                        code.add(",");
                    }
                }
                code.addStatement(")");
                final CodeBlock.Builder callMethod = CodeBlock.builder()
                        .add("setOnItemSelectedListener(")
                        .beginControlFlow("new $T()", AndroidClass.AdapterView_OnItemSelectedListener)
                        .beginControlFlow("@$T\npublic void onItemSelected($T<?> parent, $T view, int position, long id)", Override.class, AndroidClass.AdapterView, AndroidClass.View)
                        .add(code.build())
                        .endControlFlow()
                        .beginControlFlow("public void onNothingSelected($T<?> parent)", AndroidClass.AdapterView)
                        .endControlFlow()
                        .endControlFlow(")");

                cb.add(addInstanceOfCode(entry.getKey(), AndroidClass.AdapterView, callMethod.build()));
            }
            return cb.build();
        }

        private CodeBlock addCheckedChange() {
            final CodeBlock.Builder cb = CodeBlock.builder();
            for (Map.Entry<String, ElementBundle> entry : checkedChangeElements.entrySet()) {
                final ExecutableElement method = (ExecutableElement) entry.getValue().element;
                final Iterator<? extends VariableElement> parameters = method.getParameters().iterator();
                final CodeBlock.Builder code = CodeBlock.builder()
                        .add("$N.this.$N(", className, method.getSimpleName());
                while (parameters.hasNext()) {
                    final VariableElement parameter = parameters.next();
                    if (creator.isInstanceOf(parameter.asType(), AndroidClass.View)) {
                        if (AndroidClass.CompoundButton.equals(ClassName.get(parameter.asType()))) {
                            code.add("$N", "view");
                        } else {
                            code.add("($T)$N", parameter.asType(), "view");
                        }
                    } else if (creator.getClassName(parameter.asType()).equals(ClassName.get(Boolean.class))) {
                        code.add("isChecked");
                    } else {
                        code.add(AnnotationHelp.addNullCode(parameter));
                    }
                    if (parameters.hasNext()) {
                        code.add(",");
                    }
                }
                code.addStatement(")");
                final CodeBlock.Builder callMethod = CodeBlock.builder()
                        .add("setOnCheckedChangeListener(")
                        .beginControlFlow("new $T()", AndroidClass.CompoundButton_OnCheckedChangeListener)
                        .beginControlFlow("@$T\npublic void onCheckedChanged($T view,boolean isChecked)", Override.class, AndroidClass.CompoundButton)
                        .add(code.build())
                        .endControlFlow()
                        .endControlFlow(")");
                cb.add(addInstanceOfCode(entry.getKey(), AndroidClass.CompoundButton, callMethod.build()));
            }
            return cb.build();
        }

        private CodeBlock addTextChange() {
            final CodeBlock.Builder cb = CodeBlock.builder();
            final HashMap<String, CodeBlock.Builder> textWatcherMap = new HashMap<>();
            for (Map.Entry<String, ElementBundle> entry : textChangeElements.entrySet()) {
                textWatcherMap.put(entry.getKey(), createAddTextChangedListener());
            }
            for (Map.Entry<String, ElementBundle> entry : beforeTextChangeElements.entrySet()) {
                textWatcherMap.put(entry.getKey(), createAddTextChangedListener());
            }
            for (Map.Entry<String, ElementBundle> entry : afterTextChangeElements.entrySet()) {
                textWatcherMap.put(entry.getKey(), createAddTextChangedListener());
            }
            for (Map.Entry<String, CodeBlock.Builder> entry : textWatcherMap.entrySet()) {
                final CodeBlock.Builder addTextChangedListener = entry.getValue();
                if (beforeTextChangeElements.containsKey(entry.getKey())) {
                    final ExecutableElement method = (ExecutableElement) beforeTextChangeElements.get(entry.getKey()).element;
                    final Iterator<? extends VariableElement> parameters = method.getParameters().iterator();
                    final CodeBlock.Builder code = CodeBlock.builder()
                            .add("$N.this.$N(", className, method.getSimpleName());
                    while (parameters.hasNext()) {
                        final VariableElement parameter = parameters.next();
                        if (creator.isInstanceOf(parameter.asType(), AndroidClass.View)) {
                            if (AndroidClass.TextView.equals(ClassName.get(parameter.asType()))) {
                                code.add("$N", entry.getKey());
                            } else {
                                code.add("($T)$N", parameter.asType(), entry.getKey());
                            }
                        } else if (creator.getClassName(parameter.asType()).equals(ClassName.get(String.class))) {
                            code.add("s.toString()");
                        } else if (creator.getClassName(parameter.asType()).equals(ClassName.get(CharSequence.class))) {
                            code.add("s");
                        } else {
                            code.add(AnnotationHelp.addNullCode(parameter));
                        }
                        if (parameters.hasNext()) {
                            code.add(",");
                        }
                    }
                    code.addStatement(")");
                    addTextChangedListener.add(code.build());
                }
                addTextChangedListener.add(nextTextChangedListener().build());
                if (afterTextChangeElements.containsKey(entry.getKey())) {
                    final ExecutableElement method = (ExecutableElement) afterTextChangeElements.get(entry.getKey()).element;
                    final Iterator<? extends VariableElement> parameters = method.getParameters().iterator();
                    final CodeBlock.Builder code = CodeBlock.builder()
                            .add("$N.this.$N(", className, method.getSimpleName());
                    while (parameters.hasNext()) {
                        final VariableElement parameter = parameters.next();
                        if (creator.isInstanceOf(parameter.asType(), AndroidClass.View)) {
                            if (AndroidClass.TextView.equals(ClassName.get(parameter.asType()))) {
                                code.add("$N", entry.getKey());
                            } else {
                                code.add("($T)$N", parameter.asType(), entry.getKey());
                            }
                        } else if (creator.getClassName(parameter.asType()).equals(ClassName.get(String.class))) {
                            code.add("s.toString()");
                        } else if (creator.isInstanceOf(parameter.asType(),AndroidClass.Editable)) {
                            code.add("s");
                        } else {
                            code.add(AnnotationHelp.addNullCode(parameter));
                        }
                        if (parameters.hasNext()) {
                            code.add(",");
                        }
                    }
                    code.addStatement(")");
                    addTextChangedListener.add(code.build());
                }
                if (textChangeElements.containsKey(entry.getKey())) {
                    final ExecutableElement method = (ExecutableElement) textChangeElements.get(entry.getKey()).element;
                    final Iterator<? extends VariableElement> parameters = method.getParameters().iterator();
                    final CodeBlock.Builder code = CodeBlock.builder()
                            .add("$N.this.$N(", className, method.getSimpleName());
                    int countText = 0;
                    boolean before = false;
                    boolean after = false;
                    while (parameters.hasNext()) {
                        final VariableElement parameter = parameters.next();
                        if (creator.isInstanceOf(parameter.asType(), AndroidClass.View)) {
                            if (AndroidClass.TextView.equals(ClassName.get(parameter.asType()))) {
                                code.add("$N", entry.getKey());
                            } else {
                                code.add("($T)$N", parameter.asType(), entry.getKey());
                            }
                        } else if (creator.isInstanceOf(parameter.asType(),AndroidClass.Editable)) {
                            code.add("s");
                        } else if (countText < 2 && creator.getClassName(parameter.asType()).equals(ClassName.get(String.class))) {
                            if (parameter.getAnnotation(TextChange.Before.class) != null) {
                                code.add("old");
                                before = true;
                                countText++;
                            } else if (parameter.getAnnotation(TextChange.After.class) != null) {
                                code.add("s.toString()");
                                after = true;
                                countText++;
                            } else if (!after) {
                                code.add("s.toString()");
                                after = true;
                                countText++;
                            } else if (!before) {
                                code.add("old");
                                before = true;
                                countText++;
                            } else {
                                code.add("$S", "");
                                countText++;
                            }
                        } else if (countText < 2 && creator.getClassName(parameter.asType()).equals(ClassName.get(CharSequence.class))) {
                            if (parameter.getAnnotation(TextChange.Before.class) != null) {
                                code.add("old");
                                before = true;
                                countText++;
                            } else if (parameter.getAnnotation(TextChange.After.class) != null) {
                                code.add("s.toString()");
                                after = true;
                                countText++;
                            } else if (!after) {
                                code.add("s.toString()");
                                after = true;
                                countText++;
                            } else if (!before) {
                                code.add("old");
                                before = true;
                                countText++;
                            } else {
                                code.add("$S", "");
                                countText++;
                            }
                        } else {
                            code.add(AnnotationHelp.addNullCode(parameter));
                        }
                        if (parameters.hasNext()) {
                            code.add(",");
                        }
                    }
                    code.addStatement(")");
                    addTextChangedListener.add(code.build());
                }
                addTextChangedListener.endControlFlow()
                        .endControlFlow(")");
                cb.add(addInstanceOfCode(entry.getKey(), AndroidClass.TextView, addTextChangedListener.build()));
            }
            return cb.build();
        }

        private CodeBlock addFocusChang() {
            final CodeBlock.Builder cb = CodeBlock.builder();
            for (Map.Entry<String, ElementBundle> entry : focusChangElements.entrySet()) {
                final ExecutableElement method = (ExecutableElement) entry.getValue().element;
                final Iterator<? extends VariableElement> parameters = method.getParameters().iterator();
                final CodeBlock.Builder code = CodeBlock.builder()
                        .add("$N.this.$N(", className, method.getSimpleName());
                while (parameters.hasNext()) {
                    final VariableElement parameter = parameters.next();
                    if (creator.isInstanceOf(parameter.asType(), AndroidClass.View)) {
                        if (AndroidClass.View.equals(ClassName.get(parameter.asType()))) {
                            code.add("$N", "view");
                        } else {
                            code.add("($T)$N", parameter.asType(), "view");
                        }
                    } else if (creator.getClassName(parameter.asType()).equals(ClassName.get(Boolean.class))) {
                        code.add("hasFocus");
                    } else {
                        code.add(AnnotationHelp.addNullCode(parameter));
                    }
                    if (parameters.hasNext()) {
                        code.add(",");
                    }
                }
                code.addStatement(")");
                final CodeBlock.Builder callMethod = CodeBlock.builder()
                        .add("setOnFocusChangeListener(")
                        .beginControlFlow("new $T()", AndroidClass.View_OnFocusChangeListener)
                        .beginControlFlow("@$T\npublic void onFocusChange($T view,boolean hasFocus)",Override.class,AndroidClass.View)
                        .add(code.build())
                        .endControlFlow()
                        .endControlFlow(")");
                cb.add(addInstanceOfCode(entry.getKey(), AndroidClass.View, callMethod.build()));

            }
            return cb.build();
        }
    }

    private static CodeBlock.Builder createAddTextChangedListener() {
        final ClassName type = ClassName.get("android.text", "TextWatcher");
        return CodeBlock.builder()
                .add("addTextChangedListener(")
                .beginControlFlow("new $T()", type)
                .addStatement("String old = $S", "")
                .beginControlFlow("@$T\npublic void beforeTextChanged($T s, int start, int count, int after)", Override.class, CharSequence.class)
                .addStatement("old = s.toString()");
    }

    private static CodeBlock.Builder nextTextChangedListener() {
        return CodeBlock.builder()
                .endControlFlow()
                .beginControlFlow("@$T\npublic void onTextChanged($T s, int start, int before, int count)", Override.class, CharSequence.class)
                .endControlFlow()
                .beginControlFlow("@$T\npublic void afterTextChanged($T s) ", Override.class, ClassName.get("android.text", "Editable"));
    }


    private static CodeBlock.Builder createSetOnTouchListener() {
        return CodeBlock.builder()
                .add("setOnTouchListener(")
                .beginControlFlow("new $T()", AndroidClass.View_OnTouchListener)
                .beginControlFlow("@$T\npublic boolean onTouch($T view,$T event)", Override.class, AndroidClass.View, AndroidClass.MotionEvent)
                .addStatement("boolean $N = false", RETURNS);
    }

    private static CodeBlock createTouchCode(ElementBundle touch, BaseCreator creator, String className) {
        final ExecutableElement method = (ExecutableElement) touch.element;
        final Iterator<? extends VariableElement> iterator = method.getParameters().iterator();
        final CodeBlock.Builder code = CodeBlock.builder();
        if (creator.getClassName(method.getReturnType()).equals(ClassName.get(Boolean.class))) {
            code.add("$N = $N|$N.this.$N(", RETURNS, RETURNS, className, method.getSimpleName());
        } else {
            code.add("$N.this.$N(", className, method.getSimpleName());
        }
        int countInt = 0;
        boolean action = false;
        boolean index = false;
        while (iterator.hasNext()) {
            final VariableElement parameter = iterator.next();
            if (creator.isInstanceOf(parameter.asType(), AndroidClass.View)) {
                if (AndroidClass.View.equals(creator.getClassName(parameter.asType()))) {
                    code.add("$N", "view");
                } else {
                    code.add("($T)$N", parameter.asType(), "view");
                }
            } else if (creator.getClassName(parameter.asType()).equals(ClassName.get(Integer.class))) {
                if (parameter.getSimpleName().toString().toLowerCase().equals("action")) {
                    code.add("event.getAction()");
                    countInt++;
                    action = true;
                } else if (parameter.getAnnotation(Touch.Action.class) != null) {
                    code.add("event.getAction()");
                    countInt++;
                    action = true;
                } else if (parameter.getSimpleName().toString().toLowerCase().equals("actionindex")) {
                    code.add("event.getActionIndex()");
                    countInt++;
                    index = true;
                } else if (parameter.getSimpleName().toString().toLowerCase().equals("index")) {
                    code.add("event.getActionIndex()");
                    index = true;
                } else if (parameter.getAnnotation(Touch.Index.class) != null) {
                    code.add("event.getActionIndex()");
                    countInt++;
                    index = true;
                } else if (countInt < 2) {
                    if (!action) {
                        code.add("event.getAction()");
                        countInt++;
                        action = true;
                    } else if (!index) {
                        code.add("event.getActionIndex()");
                        countInt++;
                        index = true;
                    } else {
                        code.add("0");
                    }
                } else {
                    code.add("0");
                }
            } else if (ClassName.get("android.view", "MotionEvent").equals(creator.getClassName(parameter.asType()))) {
                code.add("event");
            } else {
                code.add(AnnotationHelp.addNullCode(parameter));
            }
            if (iterator.hasNext()) {
                code.add(",");
            }
        }
        code.addStatement(")");
        return code.build();
    }

    private static CodeBlock createTouchDownCode(ElementBundle touchDown, BaseCreator creator, String className) {
        final ExecutableElement method = (ExecutableElement) touchDown.element;
        final Iterator<? extends VariableElement> parameters = method.getParameters().iterator();
        final CodeBlock.Builder code = CodeBlock.builder();
        code.beginControlFlow("if(event.getAction()==MotionEvent.ACTION_DOWN)");
        if (creator.getClassName(method.getReturnType()).equals(ClassName.get(Boolean.class))) {
            code.add("$N = $N|$N.this.$N(", RETURNS, RETURNS, className, method.getSimpleName());
        } else {
            code.add("$N.this.$N(", className, method.getSimpleName());
        }
        while (parameters.hasNext()) {
            final VariableElement parameter = parameters.next();
            if (creator.isInstanceOf(parameter.asType(), AndroidClass.View)) {
                if (AndroidClass.View.equals(creator.getClassName(parameter.asType()))) {
                    code.add("$N", "view");
                } else {
                    code.add("($T)$N", parameter.asType(), "view");
                }
            } else if (creator.getClassName(parameter.asType()).equals(ClassName.get(Integer.class))) {
                code.add("event.getActionIndex()");
            } else {
                code.add(AnnotationHelp.addNullCode(parameter));
            }
            if (parameters.hasNext()) {
                code.add(",");
            }
        }
        code.addStatement(")");
        return code.endControlFlow().build();
    }


    private static CodeBlock createTouchMovenCode(ElementBundle touchMoven, BaseCreator creator, String className) {
        final ExecutableElement method = (ExecutableElement) touchMoven.element;
        final Iterator<? extends VariableElement> parameters = method.getParameters().iterator();
        final CodeBlock.Builder code = CodeBlock.builder();
        code.beginControlFlow("if(event.getAction()==MotionEvent.ACTION_MOVE)");
        if (creator.getClassName(method.getReturnType()).equals(ClassName.get(Boolean.class))) {
            code.add("$N = $N|$N.this.$N(", RETURNS, RETURNS, className, method.getSimpleName());
        } else {
            code.add("$N.this.$N(", className, method.getSimpleName());
        }
        while (parameters.hasNext()) {
            final VariableElement parameter = parameters.next();
            if (creator.isInstanceOf(parameter.asType(), AndroidClass.View)) {
                if (AndroidClass.View.equals(creator.getClassName(parameter.asType()))) {
                    code.add("$N", "view");
                } else {
                    code.add("($T)$N", parameter.asType(), "view");
                }
            } else if (creator.getClassName(parameter.asType()).equals(ClassName.get(Integer.class))) {
                code.add("event.getActionIndex()");
            } else {
                code.add(AnnotationHelp.addNullCode(parameter));
            }
            if (parameters.hasNext()) {
                code.add(",");
            }
        }
        code.addStatement(")");
        return code.endControlFlow().build();
    }

    private static CodeBlock createTouchUpCode(ElementBundle touchUp, BaseCreator creator, String className) {
        final ExecutableElement method = (ExecutableElement) touchUp.element;
        final Iterator<? extends VariableElement> parameters = method.getParameters().iterator();
        final CodeBlock.Builder code = CodeBlock.builder();
        code.beginControlFlow("if(event.getAction()==MotionEvent.ACTION_UP)");
        if (creator.getClassName(method.getReturnType()).equals(ClassName.get(Boolean.class))) {
            code.add("$N = $N|$N.this.$N(", RETURNS, RETURNS, className, method.getSimpleName());
        } else {
            code.add("$N.this.$N(", className, method.getSimpleName());
        }
        while (parameters.hasNext()) {
            final VariableElement parameter = parameters.next();
            if (creator.isInstanceOf(parameter.asType(), AndroidClass.View)) {
                if (AndroidClass.View.equals(creator.getClassName(parameter.asType()))) {
                    code.add("$N", "view");
                } else {
                    code.add("($T)$N", parameter.asType(), "view");
                }
            } else if (creator.getClassName(parameter.asType()).equals(ClassName.get(Integer.class))) {
                code.add("event.getActionIndex()");
            } else {
                code.add(AnnotationHelp.addNullCode(parameter));
            }
            if (parameters.hasNext()) {
                code.add(",");
            }
        }
        code.addStatement(")");
        return code.endControlFlow().build();
    }

    private static String parseMethodName(Name name, String end) {
        String string = name.toString();
        if (string.toLowerCase().startsWith("on")) {
            string = String.valueOf(string.charAt(2)).toLowerCase()+string.substring(3);
        }
        if (string.toLowerCase().endsWith(end.toLowerCase())) {
            string = string.substring(0, string.length() - end.length());
        }
        return string;
    }
}
