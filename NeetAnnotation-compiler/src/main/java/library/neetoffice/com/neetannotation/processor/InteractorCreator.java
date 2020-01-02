package library.neetoffice.com.neetannotation.processor;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;
import com.squareup.javapoet.TypeVariableName;
import com.sun.org.apache.xpath.internal.operations.Bool;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;

public class InteractorCreator extends BaseCreator {
    static final String INTERACTOR = "Interactor";
    static final String PRESENTER_ = INTERACTOR + "_";
    static final String UPDATE = "update";
    static final String ENTITY = "entity";
    static final String SUBJECT = "subject";
    static final String ACCEPT = "accept";
    static final String ENTITY_FIELD_NAME = "entity";
    static final String SUBJECT_FIELD_NAME = "subject";
    static final String SUBSCRIBE = "subscribe";
    static final String SET = "set";
    static final String GET = "get";

    static class InteractBuild {
        final List<Setter> setters = new ArrayList<>();
        final List<Getter> getters = new ArrayList<>();
        final String interfaceName;
        final TypeName interactTypeName;
        String packageName;
        String implementClassName;

        InteractBuild(TypeName interactTypeName, String interfaceName) {
            this.interactTypeName = interactTypeName;
            this.interfaceName = interfaceName;
        }

        public void setPackageName(String packageName) {
            this.packageName = packageName;
        }

        public void setImplementClassName(String implementClassName) {
            this.implementClassName = implementClassName;
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
    void process(TypeElement interactElement, RoundEnvironment roundEnv) {
        final InteractBuild build = createInterface(interactElement);
        createImplement(interactElement, build);
    }

    private InteractBuild createInterface(TypeElement interactElement) {
        final String interfaceName = interactElement.getSimpleName() + INTERACTOR;
        final String packageName = getPackageName(interactElement);
        final TypeName entityType = getClassName(interactElement.asType());
        final ClassName interfaceClassName = ClassName.get(packageName, interfaceName);
        final InteractBuild interactBuild = new InteractBuild(interfaceClassName, interfaceName);
        final TypeSpec.Builder tb = TypeSpec.interfaceBuilder(interfaceName)
                .addModifiers(Modifier.PUBLIC)
                .addSuperinterface(RxJavaClass.Consumer(entityType));


        final MethodSpec.Builder update = MethodSpec.methodBuilder(UPDATE)
                .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
                .addParameter(entityType, interactElement.getSimpleName().toString())
                .returns(void.class);

        final MethodSpec.Builder entity = MethodSpec.methodBuilder(ENTITY)
                .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
                .returns(RxJavaClass.Maybe(entityType));

        final MethodSpec.Builder subject = MethodSpec.methodBuilder(SUBJECT)
                .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
                .returns(RxJavaClass.Subject(entityType));

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

        for (Element element : interactElement.getEnclosedElements()) {
            if (element.getKind() == ElementKind.FIELD) {
                final TypeName fieldType = getClassName(element.asType());
                final String fieldMame = element.getSimpleName().toString();
                final String setterName = SET + toUpperCaseFirst(fieldMame);
                final String getterName = GET + toUpperCaseFirst(fieldMame);
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
        tb.addMethod(update.build());
        tb.addMethod(entity.build());
        tb.addMethod(subject.build());
        tb.addMethod(subscribe_0.build());
        tb.addMethod(subscribe_1.build());
        tb.addMethod(subscribe_2.build());
        tb.addMethod(subscribe_3.build());
        tb.addMethod(subscribe_4.build());
        tb.addMethod(subscribe_5.build());
        writeTo(packageName, tb.build());
        interactBuilds.put(interfaceClassName.toString(), interactBuild);
        interactBuilds.put(interfaceName, interactBuild);
        interactElements.put(interfaceClassName.toString(), interactElement);
        interactElements.put(interfaceName, interactElement);
        return interactBuild;
    }

    private void createImplement(TypeElement interactElement, InteractBuild interactBuild) {
        final HashMap<String, VariableElement> fieldElements = parseInteractFieldElement(interactElement);
        final HashMap<String, ExecutableElement> methodElements = parseInteractMethodElement(interactElement);
        final TypeName entityType = getClassName(interactElement.asType());
        final TypeName subjectType = RxJavaClass.Subject(entityType);
        final String implementClassName = interactElement.getSimpleName().toString() + PRESENTER_;
        interactBuild.setImplementClassName(implementClassName);
        interactBuild.setPackageName(getPackageName(interactElement));

        final TypeSpec.Builder tb = TypeSpec.classBuilder(implementClassName)
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                .addSuperinterface(RxJavaClass.Consumer(entityType))
                .addSuperinterface(interactBuild.interactTypeName);

        final FieldSpec entityField = FieldSpec.builder(entityType, ENTITY_FIELD_NAME, Modifier.PRIVATE).build();
        final FieldSpec subjectField = FieldSpec.builder(subjectType, SUBJECT_FIELD_NAME, Modifier.PRIVATE, Modifier.FINAL)
                .build();
        final MethodSpec constructor = createConstructor(entityType);
        final MethodSpec entity = createEntityMethod(implementClassName, entityType);
        final MethodSpec subject = createSubjectMethod(entityType);
        final MethodSpec update = createUpdateMethod(entityType);
        final MethodSpec accept = createAcceptMethod(entityType);
        //final MethodSpec onNext = createOnNextMethod(entityType);
        //final MethodSpec onError = createOnErrorMethod();
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
        tb.addMethod(subject);
        tb.addMethod(subscribe_0.build());
        tb.addMethod(subscribe_1.build());
        tb.addMethod(subscribe_2.build());
        tb.addMethod(subscribe_3.build());
        tb.addMethod(subscribe_4.build());
        tb.addMethod(subscribe_5.build());
        tb.addMethod(accept);
        writeTo(interactBuild.packageName, tb.build());
    }

    private HashMap<String, VariableElement> parseInteractFieldElement(TypeElement interactElement) {
        final HashMap<String, VariableElement> map = new HashMap<>();
        for (Element fieldElement : interactElement.getEnclosedElements()) {
            if (fieldElement.getKind() == ElementKind.FIELD &&
                    !fieldElement.getModifiers().contains(Modifier.PRIVATE) &&
                    !fieldElement.getModifiers().contains(Modifier.FINAL)) {
                if (fieldElement.getModifiers().contains(Modifier.PUBLIC) && !fieldElement.getModifiers().contains(Modifier.FINAL)) {
                    map.put(fieldElement.getSimpleName().toString().toLowerCase(), (VariableElement) fieldElement);
                }
            }
        }
        return map;
    }

    private HashMap<String, ExecutableElement> parseInteractMethodElement(TypeElement interactElement) {
        final HashMap<String, ExecutableElement> map = new HashMap<>();
        for (Element element : interactElement.getEnclosedElements()) {
            if (element.getKind() == ElementKind.METHOD &&
                    !element.getModifiers().contains(Modifier.PRIVATE)) {
                map.put(element.getSimpleName().toString().toLowerCase(), (ExecutableElement) element);
            }
        }
        return map;
    }

    private MethodSpec createNewInstance(TypeName instanceType, TypeName entityType, TypeVariableName... typeVariableNames) {
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

    private MethodSpec createNewInstanceNoParameters(TypeName instanceType, TypeName entityType) {
        final MethodSpec.Builder builder = MethodSpec.methodBuilder("newInstance");
        builder.addModifiers(Modifier.PUBLIC, Modifier.STATIC);
        builder.returns(instanceType);
        builder.addCode("return new $T(", instanceType)
                .addCode(addNullCode(entityType))
                .addStatement(")");
        return builder.build();
    }

    private MethodSpec createConstructor(TypeName entityType) {
        return MethodSpec.constructorBuilder()
                .addModifiers(Modifier.PUBLIC)
                .addParameter(entityType, ENTITY_FIELD_NAME)
                .addStatement("this.$N = $N", ENTITY_FIELD_NAME, ENTITY_FIELD_NAME)
                .beginControlFlow("if($N == null)", ENTITY_FIELD_NAME)
                .addStatement("$N = $T.create()", SUBJECT_FIELD_NAME, RxJavaClass.BehaviorSubject)
                .nextControlFlow("else")
                .addStatement("$N = $T.createDefault($N)", SUBJECT_FIELD_NAME, RxJavaClass.BehaviorSubject, ENTITY_FIELD_NAME)
                .endControlFlow()
                .build();
    }

    private MethodSpec createEntityMethod(String elementClass, TypeName entityType) {
        return MethodSpec.methodBuilder(ENTITY)
                .addAnnotation(Override.class)
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                .returns(RxJavaClass.Maybe(entityType))
                .addCode("return $T.just($N != null)", RxJavaClass.Maybe, ENTITY_FIELD_NAME)
                .beginControlFlow(".filter(new $T()", RxJavaClass.Predicate(ClassName.get(Boolean.class)))
                .beginControlFlow("@$T public boolean test($T aBoolean) throws $T ", Override.class, ClassName.get(Boolean.class), Exception.class)
                .addStatement("return aBoolean")
                .endControlFlow()
                .endControlFlow()
                .beginControlFlow(").map(new $T()", RxJavaClass.Function(ClassName.get(Boolean.class), entityType))
                .beginControlFlow("@$T public $T apply($T aBoolean) throws $T", Override.class, entityType, ClassName.get(Boolean.class), Exception.class)
                .addStatement("return $N", ENTITY_FIELD_NAME)
                .endControlFlow()
                .endControlFlow(")")
                .build();
    }

    /*
     Maybe.just(entity == null).filter(new Predicate<Boolean>() {
            @Override
            public boolean test(Boolean aBoolean) throws Exception {
                return aBoolean;
            }
        }).map(new Function<Boolean, Record>() {
            @Override
            public Record apply(Boolean aBoolean) throws Exception {
                return entity;
            }
        });
    * **/

    private MethodSpec createSubjectMethod(TypeName entityType) {
        return MethodSpec.methodBuilder(SUBJECT)
                .addAnnotation(Override.class)
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                .returns(RxJavaClass.Subject(entityType))
                .addStatement("return $N", SUBJECT_FIELD_NAME).build();
    }


    private MethodSpec createUpdateMethod(TypeName entityType) {
        return MethodSpec.methodBuilder(UPDATE)
                .addAnnotation(Override.class)
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                .addParameter(entityType, ENTITY_FIELD_NAME)
                .returns(void.class)
                .addStatement("this.$N = $N", ENTITY_FIELD_NAME, ENTITY_FIELD_NAME)
                .addStatement("$N.onNext($N)", SUBJECT_FIELD_NAME, ENTITY_FIELD_NAME)
                .build();
    }

    private MethodSpec createAcceptMethod(TypeName entityType) {
        return MethodSpec.methodBuilder(ACCEPT)
                .addAnnotation(Override.class)
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                .addParameter(entityType, ENTITY_FIELD_NAME)
                .addException(Exception.class)
                .returns(void.class)
                .addStatement("$N($N)", UPDATE, ENTITY_FIELD_NAME)
                .build();
    }

    private MethodSpec createSetterMethod(Setter setter, String implementClassName, HashMap<String, VariableElement> fieldElements, HashMap<String, ExecutableElement> methodElements) {
        return MethodSpec.methodBuilder(setter.methodName)
                .addAnnotation(Override.class)
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                .addParameter(setter.parameterTypeName, setter.parameterName)
                .returns(void.class)
                .addCode(createSetterCodeBlock(setter, implementClassName, fieldElements, methodElements))
                .build();
    }

    private CodeBlock createSetterCodeBlock(Setter setter, String implementClassName, HashMap<String, VariableElement> fieldElements, HashMap<String, ExecutableElement> methodElements) {
        final String parameterName = setter.parameterName;
        final String interactMethodName = setter.methodName.toLowerCase();
        final CodeBlock.Builder codeBlock = CodeBlock.builder();
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

    private MethodSpec createGetterMethod(Getter getter, String implementClassName, HashMap<String, VariableElement> fieldElements, HashMap<String, ExecutableElement> methodElements) {
        return MethodSpec.methodBuilder(getter.methodName)
                .addAnnotation(Override.class)
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                .returns(getter.returnTypeName)
                .addCode(createGetterCodeBlock(getter, implementClassName, fieldElements, methodElements))
                .build();
    }

    private CodeBlock createGetterCodeBlock(Getter getter, String implementClassName, HashMap<String, VariableElement> fieldElements, HashMap<String, ExecutableElement> methodElements) {
        final String interactMethodName = getter.methodName.toLowerCase();
        final CodeBlock.Builder codeBlock = CodeBlock.builder();
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


    void createContextModule(String packageName) {
        createClassInteractor(packageName, String.class);
        createClassInteractor_(packageName, String.class);
        createClassInteractor(packageName, Throwable.class);
        createClassInteractor_(packageName, Throwable.class);
        createClassInteractor(packageName, Exception.class);
        createClassInteractor_(packageName, Exception.class);
        createClassInteractor(packageName, Byte.class);
        createClassInteractor_(packageName, Byte.class);
        createClassInteractor(packageName, Short.class);
        createClassInteractor_(packageName, Short.class);
        createClassInteractor(packageName, Integer.class);
        createClassInteractor_(packageName, Integer.class);
        createClassInteractor(packageName, Long.class);
        createClassInteractor_(packageName, Long.class);
        createClassInteractor(packageName, Float.class);
        createClassInteractor_(packageName, Float.class);
        createClassInteractor(packageName, Double.class);
        createClassInteractor_(packageName, Double.class);
        createClassInteractor(packageName, Character.class);
        createClassInteractor_(packageName, Character.class);
        createClassInteractor(packageName, Boolean.class);
        createClassInteractor_(packageName, Boolean.class);
    }

    private void createClassInteractor(String packageName, Class clz) {
        final String name = clz.getSimpleName();
        final String interfaceName = name + INTERACTOR;
        final TypeName entityType = ClassName.get(clz);
        final String parameter = "entity";

        final TypeSpec.Builder interfaceTb = TypeSpec.interfaceBuilder(interfaceName)
                .addModifiers(Modifier.PUBLIC)
                .addSuperinterface(RxJavaClass.Consumer(entityType));

        final MethodSpec.Builder abstractUpdate = MethodSpec.methodBuilder(UPDATE)
                .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
                .addParameter(entityType, parameter)
                .returns(void.class);

        final MethodSpec.Builder abstractEntity = MethodSpec.methodBuilder(ENTITY)
                .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
                .returns(RxJavaClass.Maybe(entityType));

        final MethodSpec.Builder abstractSubject = MethodSpec.methodBuilder(SUBJECT)
                .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
                .returns(RxJavaClass.Subject(entityType));

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

        interfaceTb.addMethod(abstractUpdate.build());
        interfaceTb.addMethod(abstractEntity.build());
        interfaceTb.addMethod(abstractSubject.build());
        interfaceTb.addMethod(subscribe_0.build());
        interfaceTb.addMethod(subscribe_1.build());
        interfaceTb.addMethod(subscribe_2.build());
        interfaceTb.addMethod(subscribe_3.build());
        interfaceTb.addMethod(subscribe_4.build());
        interfaceTb.addMethod(subscribe_5.build());
        writeTo(packageName, interfaceTb.build());
    }

    private void createClassInteractor_(String packageName, Class clz) {
        final String name = clz.getSimpleName();
        final String interfaceName = name + INTERACTOR;
        final TypeName entityType = ClassName.get(clz);

        final ClassName interfaceClassName = ClassName.get(packageName, interfaceName);
        final String implementClassName = name + PRESENTER_;
        final TypeName subjectType = RxJavaClass.Subject(entityType);

        final TypeSpec.Builder tb = TypeSpec.classBuilder(implementClassName)
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                .addSuperinterface(RxJavaClass.Consumer(entityType))
                .addSuperinterface(interfaceClassName);

        final FieldSpec entityField = FieldSpec.builder(entityType, ENTITY_FIELD_NAME, Modifier.PRIVATE)
                .initializer(addNullCode(entityType)).build();
        final FieldSpec subjectField = FieldSpec.builder(subjectType, SUBJECT_FIELD_NAME, Modifier.PRIVATE, Modifier.FINAL).build();
        final MethodSpec constructor = createConstructor(entityType);
        final MethodSpec entity = createEntityMethod(interfaceName, entityType);
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
                .addCode("return subscribe(onNext,")
                .beginControlFlow("new $T()", RxJavaClass.Consumer(ClassName.get(Throwable.class)))
                .addStatement("@$T public void accept($T throwable) throws $T {}", Override.class, Throwable.class, Exception.class)
                .endControlFlow(")");
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

        tb.addField(entityField);
        tb.addField(subjectField);
        tb.addMethod(createNewInstance(ClassName.get(packageName, implementClassName), entityType));
        tb.addMethod(createNewInstanceNoParameters(ClassName.get(packageName, implementClassName), entityType));
        tb.addMethod(constructor);
        tb.addMethod(update);
        tb.addMethod(entity);
        tb.addMethod(subject);
        tb.addMethod(accept);
        tb.addMethod(subscribe_0.build());
        tb.addMethod(subscribe_1.build());
        tb.addMethod(subscribe_2.build());
        tb.addMethod(subscribe_3.build());
        tb.addMethod(subscribe_4.build());
        tb.addMethod(subscribe_5.build());
        writeTo(packageName, tb.build());
    }

    private TypeSpec.Builder createCollectionInteractor(Class clz) {
        final String name = clz.getSimpleName();
        final String interfaceName = name + INTERACTOR;
        final String packageName = "com.neetoffice.neetannotation";
        final TypeName entityType = ParameterizedTypeName.get(ClassName.get(clz), TypeVariableName.get("E"));
        final String parameter = "entity";

        final TypeSpec.Builder interfaceTb = TypeSpec.interfaceBuilder(interfaceName)
                .addModifiers(Modifier.PUBLIC)
                .addTypeVariable(TypeVariableName.get("E"))
                .addSuperinterface(RxJavaClass.Consumer(entityType));


        final MethodSpec.Builder abstractUpdate = MethodSpec.methodBuilder(UPDATE)
                .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
                .addParameter(entityType, parameter)
                .returns(void.class);

        final MethodSpec.Builder abstractEntity = MethodSpec.methodBuilder(ENTITY)
                .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
                .returns(entityType);

        final MethodSpec.Builder abstractSubject = MethodSpec.methodBuilder(SUBJECT)
                .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
                .returns(RxJavaClass.Subject(entityType));

        interfaceTb.addMethod(abstractUpdate.build());
        interfaceTb.addMethod(abstractEntity.build());
        interfaceTb.addMethod(abstractSubject.build());
        return interfaceTb;
    }

    private TypeSpec.Builder createCollectionInteractor_(String packageName, Class clz) {
        final String name = clz.getSimpleName();
        final String interfaceName = name + INTERACTOR;
        final String implementClassName = name + PRESENTER_;
        final TypeName entityType = ParameterizedTypeName.get(ClassName.get(clz), TypeVariableName.get("E"));

        final ParameterizedTypeName interfaceClassName = ParameterizedTypeName.get(ClassName.get(packageName, interfaceName), TypeVariableName.get("E"));
        final ParameterizedTypeName classClassName = ParameterizedTypeName.get(ClassName.get(packageName, implementClassName), TypeVariableName.get("E"));
        final TypeName subjectType = RxJavaClass.Subject(entityType);

        final TypeSpec.Builder tb = TypeSpec.classBuilder(implementClassName)
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                .addTypeVariable(TypeVariableName.get("E"))
                .addSuperinterface(RxJavaClass.Consumer(entityType))
                .addSuperinterface(interfaceClassName);

        final FieldSpec entityField = FieldSpec.builder(entityType, ENTITY_FIELD_NAME, Modifier.PRIVATE).build();
        final FieldSpec subjectField = FieldSpec.builder(subjectType, SUBJECT_FIELD_NAME, Modifier.PRIVATE, Modifier.FINAL)
                .initializer("$T.create()", RxJavaClass.BehaviorSubject)
                .build();
        final MethodSpec constructor = createConstructor(entityType);
        final MethodSpec entity = createEntityMethod(interfaceName, entityType);
        final MethodSpec subject = createSubjectMethod(entityType);
        final MethodSpec update = createUpdateMethod(entityType);
        final MethodSpec accept = createAcceptMethod(entityType);

        tb.addMethod(createNewInstance(classClassName, entityType, TypeVariableName.get("E")));
        tb.addMethod(createNewInstanceNoParameters(classClassName, entityType));
        tb.addField(entityField);
        tb.addField(subjectField);
        tb.addMethod(constructor);
        tb.addMethod(update);
        tb.addMethod(entity);
        tb.addMethod(subject);
        tb.addMethod(accept);
        return tb;
    }

    private MethodSpec createAbstractAddMethod() {
        return MethodSpec.methodBuilder("add")
                .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
                .addParameter(TypeVariableName.get("E"), "entity")
                .returns(void.class)
                .build();
    }

    private MethodSpec createAddMethod() {
        final CodeBlock codeBlock = CodeBlock.builder()
                .add(CodeBlock.builder().addStatement("this.entity.add(entity)").build())
                .addStatement("$N.onNext(this.entity)", SUBJECT_FIELD_NAME)
                .build();
        return MethodSpec.methodBuilder("add")
                .addAnnotation(Override.class)
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                .addParameter(TypeVariableName.get("E"), "entity")
                .returns(void.class)
                .addCode(codeBlock)
                .build();
    }

    private MethodSpec createAbstractAddAllMethod() {
        final TypeName entityType = ParameterizedTypeName.get(ClassName.get(Collection.class), TypeVariableName.get("E"));
        return MethodSpec.methodBuilder("addAll")
                .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
                .addParameter(entityType, "entity")
                .returns(void.class)
                .build();
    }

    private MethodSpec createAddAllMethod() {
        final TypeName entityType = ParameterizedTypeName.get(ClassName.get(Collection.class), TypeVariableName.get("E"));
        final CodeBlock codeBlock = CodeBlock.builder()
                .add(CodeBlock.builder().addStatement("this.entity.addAll(entity)").build())
                .addStatement("$N.onNext(this.entity)", SUBJECT_FIELD_NAME)
                .build();
        return MethodSpec.methodBuilder("addAll")
                .addAnnotation(Override.class)
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                .addParameter(entityType, "entity")
                .returns(void.class)
                .addCode(codeBlock)
                .build();
    }

    private MethodSpec createAbstractSetMethod() {
        return MethodSpec.methodBuilder("set")
                .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
                .addParameter(TypeVariableName.INT, "index")
                .addParameter(TypeVariableName.get("E"), "entity")
                .returns(void.class)
                .build();
    }

    private MethodSpec createSetMethod() {
        final CodeBlock codeBlock = CodeBlock.builder()
                .add(CodeBlock.builder().addStatement("this.entity.set(index,entity)").build())
                .addStatement("$N.onNext(this.entity)", SUBJECT_FIELD_NAME)
                .build();
        return MethodSpec.methodBuilder("set")
                .addAnnotation(Override.class)
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                .addParameter(TypeVariableName.INT, "index")
                .addParameter(TypeVariableName.get("E"), "entity")
                .returns(void.class)
                .addCode(codeBlock)
                .build();
    }

    private MethodSpec createAbstractRemoveMethod(TypeName typeName, String name) {
        return MethodSpec.methodBuilder("remove")
                .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
                .addParameter(typeName, name)
                .returns(void.class)
                .build();
    }

    private MethodSpec createRemoveMethod(TypeName typeName, String name) {
        final CodeBlock codeBlock = CodeBlock.builder()
                .add(CodeBlock.builder().addStatement("this.entity.remove($N)", name).build())
                .addStatement("$N.onNext(this.entity)", SUBJECT_FIELD_NAME)
                .build();
        return MethodSpec.methodBuilder("remove")
                .addAnnotation(Override.class)
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                .addParameter(typeName, name)
                .returns(void.class)
                .addCode(codeBlock)
                .build();
    }

    private MethodSpec createAbstractClearMethod() {
        return MethodSpec.methodBuilder("clear")
                .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
                .returns(void.class)
                .build();
    }

    private MethodSpec createClearMethod() {
        final CodeBlock codeBlock = CodeBlock.builder()
                .add(CodeBlock.builder().addStatement("this.entity.clear()").build())
                .addStatement("$N.onNext(this.entity)", SUBJECT_FIELD_NAME)
                .build();
        return MethodSpec.methodBuilder("clear")
                .addAnnotation(Override.class)
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                .returns(void.class)
                .addCode(codeBlock)
                .build();
    }

    private TypeSpec.Builder createMapInteractor(Class clz) {
        final String name = clz.getSimpleName();
        final String interfaceName = name + INTERACTOR;
        final String packageName = "com.neetoffice.neetannotation";
        final TypeName entityType = ParameterizedTypeName.get(ClassName.get(clz), TypeVariableName.get("K"), TypeVariableName.get("E"));
        final String parameter = "entity";

        final TypeSpec.Builder interfaceTb = TypeSpec.interfaceBuilder(interfaceName)
                .addModifiers(Modifier.PUBLIC)
                .addTypeVariable(TypeVariableName.get("K"))
                .addTypeVariable(TypeVariableName.get("E"))
                .addSuperinterface(RxJavaClass.Consumer(entityType));


        final MethodSpec.Builder abstractUpdate = MethodSpec.methodBuilder(UPDATE)
                .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
                .addParameter(entityType, parameter)
                .returns(void.class);

        final MethodSpec.Builder abstractEntity = MethodSpec.methodBuilder(ENTITY)
                .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
                .returns(entityType);

        final MethodSpec.Builder abstractSubject = MethodSpec.methodBuilder(SUBJECT)
                .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
                .returns(RxJavaClass.Subject(entityType));

        interfaceTb.addMethod(abstractUpdate.build());
        interfaceTb.addMethod(abstractEntity.build());
        interfaceTb.addMethod(abstractSubject.build());
        return interfaceTb;
    }

    private TypeSpec.Builder createMapInteractor_(String packageName, Class clz) {
        final String name = clz.getSimpleName();
        final String interfaceName = name + INTERACTOR;
        final TypeName entityType = ParameterizedTypeName.get(ClassName.get(clz), TypeVariableName.get("K"), TypeVariableName.get("E"));
        final ParameterizedTypeName interfaceClassName = ParameterizedTypeName.get(ClassName.get(packageName, interfaceName), TypeVariableName.get("K"), TypeVariableName.get("E"));
        final String implementClassName = name + PRESENTER_;
        final TypeName subjectType = RxJavaClass.Subject(entityType);

        final TypeSpec.Builder tb = TypeSpec.classBuilder(implementClassName)
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                .addTypeVariable(TypeVariableName.get("K"))
                .addTypeVariable(TypeVariableName.get("E"))
                .addSuperinterface(RxJavaClass.Consumer(entityType))
                .addSuperinterface(interfaceClassName);

        final FieldSpec entityField = FieldSpec.builder(entityType, ENTITY_FIELD_NAME, Modifier.PRIVATE).build();
        final FieldSpec subjectField = FieldSpec.builder(subjectType, SUBJECT_FIELD_NAME, Modifier.PRIVATE, Modifier.FINAL)
                .initializer("$T.create()", RxJavaClass.BehaviorSubject)
                .build();
        final MethodSpec constructor = createConstructor(entityType);
        final MethodSpec entity = createEntityMethod(implementClassName, entityType);
        final MethodSpec subject = createSubjectMethod(entityType);
        final MethodSpec update = createUpdateMethod(entityType);
        final MethodSpec accept = createAcceptMethod(entityType);

        tb.addField(entityField);
        tb.addField(subjectField);
        tb.addMethod(constructor);
        tb.addMethod(update);
        tb.addMethod(entity);
        tb.addMethod(subject);
        tb.addMethod(accept);
        return tb;
    }

    private MethodSpec createAbstractPutMethod() {
        return MethodSpec.methodBuilder("put")
                .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
                .addParameter(TypeVariableName.get("K"), "key")
                .addParameter(TypeVariableName.get("E"), "entity")
                .returns(void.class)
                .build();
    }

    private MethodSpec createPutMethod() {
        final CodeBlock codeBlock = CodeBlock.builder()
                .add(CodeBlock.builder().addStatement("this.entity.put(key,entity)").build())
                .addStatement("$N.onNext(this.entity)", SUBJECT_FIELD_NAME)
                .build();
        return MethodSpec.methodBuilder("put")
                .addAnnotation(Override.class)
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                .addParameter(TypeVariableName.get("K"), "key")
                .addParameter(TypeVariableName.get("E"), "entity")
                .returns(void.class)
                .addCode(codeBlock)
                .build();
    }
}