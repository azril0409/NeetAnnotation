package library.neetoffice.com.neetannotation.processor;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;
import com.squareup.javapoet.TypeVariableName;

import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;

import static library.neetoffice.com.neetannotation.processor.InteractorCreator.ACCEPT;
import static library.neetoffice.com.neetannotation.processor.InteractorCreator.ENTITY;
import static library.neetoffice.com.neetannotation.processor.InteractorCreator.ENTITY_FIELD_NAME;
import static library.neetoffice.com.neetannotation.processor.InteractorCreator.INTERACTOR;
import static library.neetoffice.com.neetannotation.processor.InteractorCreator.NOTIFY_DATA_SET_CHANGED;
import static library.neetoffice.com.neetannotation.processor.InteractorCreator.PRESENTER_;
import static library.neetoffice.com.neetannotation.processor.InteractorCreator.SUBJECT;
import static library.neetoffice.com.neetannotation.processor.InteractorCreator.SUBJECT_FIELD_NAME;
import static library.neetoffice.com.neetannotation.processor.InteractorCreator.SUBSCRIBE;
import static library.neetoffice.com.neetannotation.processor.InteractorCreator.UPDATE;

public class ContextInteractorCreator extends BaseCreator{

    public ContextInteractorCreator(MainProcessor processor, ProcessingEnvironment processingEnv) {
        super(processor, processingEnv);
    }

    @Override
    void process(TypeElement element, RoundEnvironment roundEnv) {

    }

    void createModule(String packageName) {
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
        final MethodSpec.Builder notifyDataSetChanged = MethodSpec.methodBuilder(NOTIFY_DATA_SET_CHANGED)
                .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
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
        interfaceTb.addMethod(notifyDataSetChanged.build());
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
        final MethodSpec.Builder notifyDataSetChanged = MethodSpec.methodBuilder(NOTIFY_DATA_SET_CHANGED)
                .addAnnotation(Override.class)
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                .returns(void.class)
                .addStatement("subject.onNext(entity)");

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
        tb.addMethod(notifyDataSetChanged.build());
        writeTo(packageName, tb.build());
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

}
