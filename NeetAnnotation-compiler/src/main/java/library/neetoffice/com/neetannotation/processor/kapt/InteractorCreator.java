package library.neetoffice.com.neetannotation.processor.kapt;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;
import com.squareup.javapoet.TypeVariableName;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;

import library.neetoffice.com.neetannotation.processor.RxJavaClass;

public class InteractorCreator extends BaseCreator {
    static final String INTERACTOR = "Interactor";
    static final String INTERACTOR_ = INTERACTOR + "_";
    static final String PUBLISHER_ = "Publisher_";
    static final String ENTITY_FIELD_NAME = "entity";
    static final String SUBJECT_FIELD_NAME = "subject";
    static final String UPDATE = "update";
    static final String ENTITY = "entity";
    static final String GET_ENTITY = "getEntity";
    static final String OBSERVABLE = "observable";
    static final String ACCEPT = "accept";
    static final String SUBSCRIBE = "subscribe";
    static final String NOTIFY_DATA_SET_CHANGED = "notifydatasetchanged";
    static final String SET = "set";
    static final String GET = "get";

    static class InteractBuild {
        final List<Setter> setters = new ArrayList<>();
        final List<Getter> getters = new ArrayList<>();
        final String interfaceName;
        final TypeName interactTypeName;
        String packageName;
        String interactorClassName;
        String publisherClassName;

        InteractBuild(TypeName interactTypeName, String interfaceName) {
            this.interactTypeName = interactTypeName;
            this.interfaceName = interfaceName;
        }

        public void setPackageName(String packageName) {
            this.packageName = packageName;
        }

        public void setInteractorClassName(String className) {
            this.interactorClassName = className;
        }
        public void setPublisherClassName(String className) {
            this.publisherClassName = className;
        }
    }

    static class Setter {
        final Element element;
        final String fieldMame;
        final String methodName;
        final TypeName parameterTypeName;
        final String parameterName;

        Setter(Element element, String fieldMame, String methodName, TypeName parameterTypeName, String parameterName) {
            this.element = element;
            this.fieldMame = fieldMame;
            this.methodName = methodName;
            this.parameterTypeName = parameterTypeName;
            this.parameterName = parameterName;
        }
    }

    static class Getter {
        final Element element;
        final String fieldMame;
        final String methodName;
        final TypeName returnTypeName;

        Getter(Element element, String fieldMame, String methodName, TypeName returnTypeName) {
            this.element = element;
            this.fieldMame = fieldMame;
            this.methodName = methodName;
            this.returnTypeName = returnTypeName;
        }
    }

    final HashMap<String, InteractBuild> interactBuilds = new HashMap<>();
    final HashMap<String, Element> interactElements = new HashMap<>();

    public InteractorCreator(MainProcessor processor, ProcessingEnvironment processingEnv) {
        super(processor, processingEnv);
    }

    @Override
    void release() {
        super.release();
        interactBuilds.clear();
        interactElements.clear();
    }

    @Override
    void process(TypeElement interactElement, RoundEnvironment roundEnv) {
        final String packageName = getPackageName(interactElement);
        final ClassName entityType = ClassName.get(interactElement);
        final InteractBuild build = createInterface(this, packageName, entityType, interactBuilds, interactElements, interactElement);
        createImplement(this, packageName, entityType, build, true, interactElement);
        createImplement(this, packageName, entityType, build, false, interactElement);
    }

    static InteractBuild createInterface(BaseCreator creator, String packageName, ClassName entityType, HashMap<String, InteractBuild> interactBuilds, HashMap<String, Element> interactElements, TypeElement interactElement) {
        final String interfaceName = entityType.simpleName() + INTERACTOR;
        final ClassName interfaceClassName = ClassName.get(packageName, interfaceName);
        final InteractBuild interactBuild = new InteractBuild(interfaceClassName, interfaceName);
        final TypeSpec.Builder tb = TypeSpec.interfaceBuilder(interfaceName)
                .addModifiers(Modifier.PUBLIC)
                .addSuperinterface(RxJavaClass.Consumer(entityType));

        final MethodSpec.Builder update = MethodSpec.methodBuilder(UPDATE)
                .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
                .addParameter(entityType, entityType.simpleName())
                .returns(void.class);

        final MethodSpec.Builder entity = MethodSpec.methodBuilder(ENTITY)
                .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
                .returns(RxJavaClass.Single(entityType));

        final MethodSpec.Builder getEntity =  MethodSpec.methodBuilder(GET_ENTITY)
                .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
                .returns(entityType);

        final MethodSpec.Builder subject = MethodSpec.methodBuilder(OBSERVABLE)
                .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
                .returns(RxJavaClass.Observable(entityType));

        final MethodSpec.Builder subscribe_0 = MethodSpec.methodBuilder(SUBSCRIBE)
                .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
                .returns(RxJavaClass.Disposable);
        final MethodSpec.Builder subscribe_1 = MethodSpec.methodBuilder(SUBSCRIBE)
                .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
                .addParameter(RxJavaClass.Consumer(entityType), "onNext")
                .returns(RxJavaClass.Disposable);
        final MethodSpec.Builder subscribe_2 = MethodSpec.methodBuilder(SUBSCRIBE)
                .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
                .addParameter(RxJavaClass.Consumer(entityType), "onNext")
                .addParameter(RxJavaClass.Consumer(ClassName.get(Throwable.class)), "onError")
                .returns(RxJavaClass.Disposable);
        final MethodSpec.Builder subscribe_3 = MethodSpec.methodBuilder(SUBSCRIBE)
                .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
                .addParameter(RxJavaClass.Consumer(entityType), "onNext")
                .addParameter(RxJavaClass.Consumer(ClassName.get(Throwable.class)), "onError")
                .addParameter(RxJavaClass.Action, "onComplete")
                .returns(RxJavaClass.Disposable);
        final MethodSpec.Builder subscribe_4 = MethodSpec.methodBuilder(SUBSCRIBE)
                .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
                .addParameter(RxJavaClass.Consumer(entityType), "onNext")
                .addParameter(RxJavaClass.Consumer(ClassName.get(Throwable.class)), "onError")
                .addParameter(RxJavaClass.Action, "onComplete")
                .addParameter(RxJavaClass.Consumer(RxJavaClass.Disposable), "onSubscribe")
                .returns(RxJavaClass.Disposable);
        final MethodSpec.Builder subscribe_5 = MethodSpec.methodBuilder(SUBSCRIBE)
                .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
                .addParameter(RxJavaClass.Observer(entityType), "observer")
                .returns(void.class);
        final MethodSpec.Builder notifyDataSetChanged = MethodSpec.methodBuilder(NOTIFY_DATA_SET_CHANGED)
                .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
                .returns(void.class);

        if (interactElement != null) {
            for (Element element : interactElement.getEnclosedElements()) {
                if (element.getModifiers().contains(Modifier.STATIC)) {
                    continue;
                }
                if (element.getKind() == ElementKind.FIELD) {
                    final TypeName fieldType = getClassName(element.asType());
                    final String fieldMame = element.getSimpleName().toString();
                    final String setterName = SET + creator.toUpperCaseFirst(fieldMame);
                    final String getterName = GET + creator.toUpperCaseFirst(fieldMame);
                    final MethodSpec.Builder setter = MethodSpec.methodBuilder(setterName)
                            .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
                            .addParameter(fieldType, fieldMame)
                            .returns(void.class);
                    final MethodSpec.Builder getter = MethodSpec.methodBuilder(getterName)
                            .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
                            .returns(fieldType);
                    if (!element.getModifiers().contains(Modifier.FINAL)) {
                        tb.addMethod(setter.build());
                        interactBuild.setters.add(new Setter(element, fieldMame, setterName, fieldType, fieldMame));
                    }
                    tb.addMethod(getter.build());
                    interactBuild.getters.add(new Getter(element, fieldMame, getterName, fieldType));
                }
            }
        }
        tb.addMethod(update.build());
        tb.addMethod(entity.build());
        tb.addMethod(getEntity.build());
        tb.addMethod(subject.build());
        tb.addMethod(subscribe_0.build());
        tb.addMethod(subscribe_1.build());
        tb.addMethod(subscribe_2.build());
        tb.addMethod(subscribe_3.build());
        tb.addMethod(subscribe_4.build());
        tb.addMethod(subscribe_5.build());
        tb.addMethod(notifyDataSetChanged.build());
        creator.writeTo(packageName, tb.build());
        interactBuilds.put(interfaceClassName.toString(), interactBuild);
        interactBuilds.put(interfaceName, interactBuild);
        interactElements.put(interfaceClassName.toString(), interactElement);
        interactElements.put(interfaceName, interactElement);
        return interactBuild;
    }

    static void createImplement(BaseCreator creator, String packageName, ClassName entityType, InteractBuild interactBuild, boolean isBehavior, TypeElement interactElement) {
        final HashMap<String, VariableElement> fieldElements = parseInteractFieldElement(interactElement);
        final HashMap<String, ExecutableElement> methodElements = parseInteractMethodElement(interactElement);
        final TypeName subjectType = RxJavaClass.Subject(entityType);
        final String implementClassName = entityType.simpleName() + (isBehavior?INTERACTOR_:PUBLISHER_);
        if(isBehavior){
            interactBuild.setInteractorClassName(implementClassName);
        }else{
            interactBuild.setPublisherClassName(implementClassName);
        }
        interactBuild.setPackageName(packageName);

        final TypeSpec.Builder tb = TypeSpec.classBuilder(implementClassName)
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                .addSuperinterface(RxJavaClass.Consumer(entityType))
                .addSuperinterface(interactBuild.interactTypeName);

        final FieldSpec entityField = FieldSpec.builder(entityType, ENTITY_FIELD_NAME, Modifier.PRIVATE).build();
        final FieldSpec subjectField = FieldSpec.builder(subjectType, SUBJECT_FIELD_NAME, Modifier.PRIVATE, Modifier.FINAL)
                .build();
        final MethodSpec constructor = createConstructor(entityType, isBehavior);
        final MethodSpec entity = createEntityMethod(implementClassName, entityType);
        final MethodSpec getEntity = createGetEntityMethod(implementClassName, entityType);
        final MethodSpec subject = createSubjectMethod(entityType);
        final MethodSpec update = createUpdateMethod(entityType);
        final MethodSpec accept = createAcceptMethod(entityType);
        final MethodSpec.Builder subscribe_0 = MethodSpec.methodBuilder(SUBSCRIBE)
                .addAnnotation(Override.class)
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                .returns(RxJavaClass.Disposable)
                .addStatement("return subject.subscribe()");
        final MethodSpec.Builder subscribe_1 = MethodSpec.methodBuilder(SUBSCRIBE)
                .addAnnotation(Override.class)
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                .addParameter(RxJavaClass.Consumer(entityType), "onNext")
                .returns(RxJavaClass.Disposable)
                .addStatement("return subject.subscribe(onNext)");
        final MethodSpec.Builder subscribe_2 = MethodSpec.methodBuilder(SUBSCRIBE)
                .addAnnotation(Override.class)
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                .addParameter(RxJavaClass.Consumer(entityType), "onNext")
                .addParameter(RxJavaClass.Consumer(ClassName.get(Throwable.class)), "onError")
                .returns(RxJavaClass.Disposable)
                .addStatement("return subject.subscribe(onNext,onError)");
        final MethodSpec.Builder subscribe_3 = MethodSpec.methodBuilder(SUBSCRIBE)
                .addAnnotation(Override.class)
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                .addParameter(RxJavaClass.Consumer(entityType), "onNext")
                .addParameter(RxJavaClass.Consumer(ClassName.get(Throwable.class)), "onError")
                .addParameter(RxJavaClass.Action, "onComplete")
                .returns(RxJavaClass.Disposable)
                .addStatement("return subject.subscribe(onNext,onError,onComplete)");
        final MethodSpec.Builder subscribe_4 = MethodSpec.methodBuilder(SUBSCRIBE)
                .addAnnotation(Override.class)
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                .addParameter(RxJavaClass.Consumer(entityType), "onNext")
                .addParameter(RxJavaClass.Consumer(ClassName.get(Throwable.class)), "onError")
                .addParameter(RxJavaClass.Action, "onComplete")
                .addParameter(RxJavaClass.Consumer(RxJavaClass.Disposable), "onSubscribe")
                .returns(RxJavaClass.Disposable)
                .addStatement("return subject.subscribe(onNext,onError,onComplete,onSubscribe)");
        final MethodSpec.Builder subscribe_5 = MethodSpec.methodBuilder(SUBSCRIBE)
                .addAnnotation(Override.class)
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                .addParameter(RxJavaClass.Observer(entityType), "observer")
                .returns(void.class)
                .addStatement("subject.subscribe(observer)");
        final MethodSpec.Builder notifyDataSetChanged = MethodSpec.methodBuilder(NOTIFY_DATA_SET_CHANGED)
                .addAnnotation(Override.class)
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                .returns(void.class)
                .beginControlFlow("if(entity != null)")
                .addStatement("subject.onNext(entity)")
                .endControlFlow();

        for (Setter setter : interactBuild.setters) {
            tb.addMethod(createSetterMethod(setter, implementClassName, fieldElements, methodElements));
        }
        for (Getter getter : interactBuild.getters) {
            tb.addMethod(createGetterMethod(getter, implementClassName, fieldElements, methodElements));
        }
        tb.addMethod(createNewInstance(ClassName.get(interactBuild.packageName, implementClassName), entityType));
        tb.addMethod(createNewInstanceNoParameters(ClassName.get(interactBuild.packageName, implementClassName), entityType));
        tb.addField(entityField);
        tb.addField(subjectField);
        tb.addMethod(constructor);
        tb.addMethod(update);
        tb.addMethod(entity);
        tb.addMethod(getEntity);
        tb.addMethod(subject);
        tb.addMethod(subscribe_0.build());
        tb.addMethod(subscribe_1.build());
        tb.addMethod(subscribe_2.build());
        tb.addMethod(subscribe_3.build());
        tb.addMethod(subscribe_4.build());
        tb.addMethod(subscribe_5.build());
        tb.addMethod(notifyDataSetChanged.build());
        tb.addMethod(accept);
        creator.writeTo(interactBuild.packageName, tb.build());
    }

    private static HashMap<String, VariableElement> parseInteractFieldElement(TypeElement interactElement) {
        final HashMap<String, VariableElement> map = new HashMap<>();
        if (interactElement != null) {
            for (Element fieldElement : interactElement.getEnclosedElements()) {
                if (fieldElement.getKind() == ElementKind.FIELD &&
                        !fieldElement.getModifiers().contains(Modifier.PRIVATE) &&
                        !fieldElement.getModifiers().contains(Modifier.FINAL)) {
                    if (fieldElement.getModifiers().contains(Modifier.PUBLIC) && !fieldElement.getModifiers().contains(Modifier.FINAL)) {
                        map.put(fieldElement.getSimpleName().toString().toLowerCase(), (VariableElement) fieldElement);
                    }
                }
            }
        }
        return map;
    }

    private static HashMap<String, ExecutableElement> parseInteractMethodElement(TypeElement interactElement) {
        final HashMap<String, ExecutableElement> map = new HashMap<>();
        if (interactElement != null) {
            for (Element element : interactElement.getEnclosedElements()) {
                if (element.getKind() == ElementKind.METHOD &&
                        !element.getModifiers().contains(Modifier.PRIVATE)) {
                    map.put(element.getSimpleName().toString().toLowerCase(), (ExecutableElement) element);
                }
            }
        }
        return map;
    }

    private static MethodSpec createNewInstance(TypeName instanceType, TypeName entityType, TypeVariableName... typeVariableNames) {
        final MethodSpec.Builder builder = MethodSpec.methodBuilder("newInstance");
        builder.addModifiers(Modifier.PUBLIC, Modifier.STATIC);
        builder.addParameter(entityType, ENTITY_FIELD_NAME);
        for (TypeVariableName typeVariableName : typeVariableNames) {
            builder.addTypeVariable(typeVariableName);
        }
        builder.returns(instanceType);
        builder.addStatement("return new $T($N)", instanceType, ENTITY_FIELD_NAME);
        return builder.build();
    }

    private static MethodSpec createNewInstanceNoParameters(TypeName instanceType, TypeName entityType) {
        final MethodSpec.Builder builder = MethodSpec.methodBuilder("newInstance");
        builder.addModifiers(Modifier.PUBLIC, Modifier.STATIC);
        builder.returns(instanceType);
        builder.addCode("return new $T(", instanceType)
                .addCode(addNullCode(entityType))
                .addStatement(")");
        return builder.build();
    }

    private static MethodSpec createConstructor(TypeName entityType, boolean isBehavior) {
        final MethodSpec.Builder builder = MethodSpec.constructorBuilder()
                .addModifiers(Modifier.PUBLIC)
                .addParameter(entityType, ENTITY_FIELD_NAME)
                .addStatement("this.$N = $N", ENTITY_FIELD_NAME, ENTITY_FIELD_NAME);
        if (isBehavior) {
            return builder.beginControlFlow("if($N == null)", ENTITY_FIELD_NAME)
                    .addStatement("$N = $T.create()", SUBJECT_FIELD_NAME, RxJavaClass.BehaviorSubject)
                    .nextControlFlow("else")
                    .addStatement("$N = $T.createDefault($N)", SUBJECT_FIELD_NAME, RxJavaClass.BehaviorSubject, ENTITY_FIELD_NAME)
                    .endControlFlow()
                    .build();
        } else {
            return builder.addStatement("$N = $T.create()", SUBJECT_FIELD_NAME, RxJavaClass.PublishSubject).build();
        }
    }

    private static MethodSpec createEntityMethod(String elementClass, TypeName entityType) {
        return MethodSpec.methodBuilder(ENTITY)
                .addAnnotation(Override.class)
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                .returns(RxJavaClass.Single(entityType))
                .beginControlFlow("if(entity == null)")
                .addStatement("return $T.never()", RxJavaClass.Single)
                .endControlFlow()
                .addStatement("return $T.just($N)", RxJavaClass.Single, ENTITY_FIELD_NAME)
                .build();
    }

    private static MethodSpec createGetEntityMethod(String elementClass, TypeName entityType) {
        return MethodSpec.methodBuilder(GET_ENTITY)
                .addAnnotation(Override.class)
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                .returns(entityType)
                .addStatement("return $N", ENTITY_FIELD_NAME)
                .build();
    }

    private static MethodSpec createSubjectMethod(TypeName entityType) {
        return MethodSpec.methodBuilder(OBSERVABLE)
                .addAnnotation(Override.class)
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                .returns(RxJavaClass.Observable(entityType))
                .addStatement("return $N", SUBJECT_FIELD_NAME).build();
    }


    private static MethodSpec createUpdateMethod(TypeName entityType) {
        return MethodSpec.methodBuilder(UPDATE)
                .addAnnotation(Override.class)
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                .addParameter(entityType, ENTITY_FIELD_NAME)
                .returns(void.class)
                .beginControlFlow("if($N == null)", ENTITY_FIELD_NAME)
                .addStatement("return")
                .endControlFlow()
                .addStatement("this.$N = $N", ENTITY_FIELD_NAME, ENTITY_FIELD_NAME)
                .addStatement("$N.onNext($N)", SUBJECT_FIELD_NAME, ENTITY_FIELD_NAME)
                .build();
    }

    private static MethodSpec createAcceptMethod(TypeName entityType) {
        return MethodSpec.methodBuilder(ACCEPT)
                .addAnnotation(Override.class)
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                .addParameter(entityType, ENTITY_FIELD_NAME)
                .addException(Exception.class)
                .returns(void.class)
                .addStatement("$N($N)", UPDATE, ENTITY_FIELD_NAME)
                .build();
    }

    private static MethodSpec createSetterMethod(Setter setter, String implementClassName, HashMap<String, VariableElement> fieldElements, HashMap<String, ExecutableElement> methodElements) {
        return MethodSpec.methodBuilder(setter.methodName)
                .addAnnotation(Override.class)
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                .addParameter(setter.parameterTypeName, setter.parameterName)
                .returns(void.class)
                .addCode(createSetterCodeBlock(setter, implementClassName, fieldElements, methodElements))
                .build();
    }

    private static CodeBlock createSetterCodeBlock(Setter setter, String implementClassName, HashMap<String, VariableElement> fieldElements, HashMap<String, ExecutableElement> methodElements) {
        final String parameterName = setter.parameterName;
        final String interactMethodName = setter.methodName.toLowerCase();
        final CodeBlock.Builder codeBlock = CodeBlock.builder();
        codeBlock.beginControlFlow("if($N.this.$N == null)", implementClassName, ENTITY_FIELD_NAME)
                .addStatement("return")
                .endControlFlow();
        if (methodElements.containsKey(interactMethodName)) {
            final ExecutableElement methodElement = methodElements.get(interactMethodName);
            codeBlock.addStatement("$N.this.$N.$N($N)", implementClassName, ENTITY_FIELD_NAME, methodElement.getSimpleName(), parameterName);
        } else if (fieldElements.containsKey(interactMethodName.substring(SET.length()))) {
            final VariableElement fieldElement = fieldElements.get(interactMethodName.substring(SET.length()));
            codeBlock.addStatement("$N.this.$N.$N = $N", implementClassName, ENTITY_FIELD_NAME, fieldElement.getSimpleName(), parameterName);
        } else if (setter.element.getModifiers().contains(Modifier.STATIC)) {
            codeBlock.addStatement("$T = $N", setter.parameterTypeName, parameterName);
        }
        return CodeBlock.builder()
                .add(codeBlock.build())
                .addStatement("$N.onNext($N)", SUBJECT_FIELD_NAME, ENTITY_FIELD_NAME)
                .build();
    }

    private static MethodSpec createGetterMethod(Getter getter, String implementClassName, HashMap<String, VariableElement> fieldElements, HashMap<String, ExecutableElement> methodElements) {
        return MethodSpec.methodBuilder(getter.methodName)
                .addAnnotation(Override.class)
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                .returns(getter.returnTypeName)
                .addCode(createGetterCodeBlock(getter, implementClassName, fieldElements, methodElements))
                .build();
    }

    private static CodeBlock createGetterCodeBlock(Getter getter, String implementClassName, HashMap<String, VariableElement> fieldElements, HashMap<String, ExecutableElement> methodElements) {
        final String interactMethodName = getter.methodName.toLowerCase();
        final CodeBlock.Builder codeBlock = CodeBlock.builder();
        codeBlock.beginControlFlow("if($N.this.$N == null)", implementClassName, ENTITY_FIELD_NAME)
                .add("return ")
                .addStatement(addNullCode(getClassName(getter.element.asType())))
                .endControlFlow();
        if (methodElements.containsKey(interactMethodName)) {
            final ExecutableElement methodElement = methodElements.get(interactMethodName);
            codeBlock.addStatement("return $N.this.$N.$N()", implementClassName, ENTITY_FIELD_NAME, methodElement.getSimpleName());
        } else if (fieldElements.containsKey(interactMethodName.substring(GET.length()))) {
            final VariableElement fieldElement = fieldElements.get(interactMethodName.substring(GET.length()));
            codeBlock.addStatement("return $N.this.$N.$N", implementClassName, ENTITY_FIELD_NAME, fieldElement.getSimpleName());
        } else if (getter.element.getModifiers().contains(Modifier.STATIC)) {
            codeBlock.addStatement("return $T", getter.returnTypeName);
        } else {
            codeBlock.add("return ")
                    .add(addNullCode(getClassName(getter.element.asType())))
                    .addStatement("");
        }
        return codeBlock.build();
    }
}