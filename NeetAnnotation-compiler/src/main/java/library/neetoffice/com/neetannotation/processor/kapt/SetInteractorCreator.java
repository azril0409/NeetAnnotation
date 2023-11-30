package library.neetoffice.com.neetannotation.processor.kapt;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;
import com.squareup.javapoet.TypeVariableName;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;

import library.neetoffice.com.neetannotation.processor.RxJavaClass;

public class SetInteractorCreator extends BaseCreator {
    static final String INTERACTOR = "Setinteractor";
    static final String INTERACTOR_ = INTERACTOR + "_";
    static final String PUBLISHER_ = "Setpublisher_";
    static final String ENTITY_FIELD_NAME = "entity";
    static final String SUBJECT_FIELD_NAME = "subject";
    static final String ACCEPT = "accept";
    static final String ADD = "add";
    static final String ADDALL = "addAll";
    static final String REMOVE = "remove";
    static final String CLEAR = "clear";
    static final String UPDATE = "update";
    static final String ENTITY = "entity";
    static final String SUBJECT = "subject";
    static final String SUBSCRIBE = "subscribe";
    static final String NOTIFY_DATA_SET_CHANGED = "notifydatasetchanged";

    final HashMap<String, InteractBuild> interactBuilds = new HashMap<>();

    static class InteractBuild {
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

    public SetInteractorCreator(AbstractProcessor processor, ProcessingEnvironment processingEnv) {
        super(processor, processingEnv);
    }

    @Override
    void release() {
        super.release();
        interactBuilds.clear();
    }

    @Override
    void process(TypeElement interactElement, RoundEnvironment roundEnv) {
        final String packageName = getPackageName(interactElement);
        final ClassName entityType = ClassName.get(interactElement);
        final SetInteractorCreator.InteractBuild build = createInterface(this, packageName, entityType, interactBuilds);
        createImplement(this, packageName, entityType, build, false);
        createImplement(this, packageName, entityType, build, true);
    }

    static InteractBuild createInterface(BaseCreator creator, String packageName, ClassName entityType,
                                         HashMap<String, InteractBuild> interactBuilds) {
        final String interfaceName = entityType.simpleName() + INTERACTOR;
        final TypeName listType = ParameterizedTypeName.get(ClassName.get(Set.class), entityType);
        final ClassName interfaceClassName = ClassName.get(packageName, interfaceName);
        final InteractBuild interactBuild = new InteractBuild(interfaceClassName, interfaceName);
        final TypeSpec.Builder tb = TypeSpec.interfaceBuilder(interfaceName)
                .addModifiers(Modifier.PUBLIC)
                .addSuperinterface(RxJavaClass.Consumer(listType));

        final MethodSpec.Builder add = MethodSpec.methodBuilder(ADD)
                .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
                .addParameter(entityType, entityType.simpleName())
                .returns(void.class);
        tb.addMethod(add.build());

        final MethodSpec.Builder addAll = MethodSpec.methodBuilder(ADDALL)
                .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
                .addParameter(ParameterizedTypeName.get(ClassName.get(Collection.class), entityType), "collection")
                .returns(void.class);
        tb.addMethod(addAll.build());

        final MethodSpec.Builder remove1 = MethodSpec.methodBuilder(REMOVE)
                .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
                .addParameter(entityType, entityType.simpleName())
                .returns(void.class);
        tb.addMethod(remove1.build());

        final MethodSpec.Builder remove2 = MethodSpec.methodBuilder(REMOVE)
                .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
                .addParameter(int.class, "index")
                .returns(void.class);
        tb.addMethod(remove2.build());

        final MethodSpec.Builder clear = MethodSpec.methodBuilder(CLEAR)
                .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
                .returns(void.class);
        tb.addMethod(clear.build());

        final MethodSpec.Builder update = MethodSpec.methodBuilder(UPDATE)
                .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
                .addParameter(listType, entityType.simpleName())
                .returns(void.class);
        tb.addMethod(update.build());

        final MethodSpec.Builder entity = MethodSpec.methodBuilder(ENTITY)
                .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
                .returns(RxJavaClass.Single(listType));
        tb.addMethod(entity.build());

        final MethodSpec.Builder getEntity = MethodSpec.methodBuilder(InteractorCreator.GET_ENTITY)
                .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
                .returns(listType);
        tb.addMethod(getEntity.build());

        final MethodSpec.Builder subject = MethodSpec.methodBuilder(SUBJECT)
                .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
                .returns(RxJavaClass.Subject(listType));
        tb.addMethod(subject.build());
        final MethodSpec.Builder subscribe_0 = MethodSpec.methodBuilder(SUBSCRIBE)
                .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
                .returns(RxJavaClass.Disposable);
        tb.addMethod(subscribe_0.build());
        final MethodSpec.Builder subscribe_1 = MethodSpec.methodBuilder(SUBSCRIBE)
                .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
                .addParameter(RxJavaClass.Consumer(listType), "onNext")
                .returns(RxJavaClass.Disposable);
        tb.addMethod(subscribe_1.build());
        final MethodSpec.Builder subscribe_2 = MethodSpec.methodBuilder(SUBSCRIBE)
                .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
                .addParameter(RxJavaClass.Consumer(listType), "onNext")
                .addParameter(RxJavaClass.Consumer(ClassName.get(Throwable.class)), "onError")
                .returns(RxJavaClass.Disposable);
        tb.addMethod(subscribe_2.build());
        final MethodSpec.Builder subscribe_3 = MethodSpec.methodBuilder(SUBSCRIBE)
                .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
                .addParameter(RxJavaClass.Consumer(listType), "onNext")
                .addParameter(RxJavaClass.Consumer(ClassName.get(Throwable.class)), "onError")
                .addParameter(RxJavaClass.Action, "onComplete")
                .returns(RxJavaClass.Disposable);
        tb.addMethod(subscribe_3.build());
        final MethodSpec.Builder subscribe_4 = MethodSpec.methodBuilder(SUBSCRIBE)
                .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
                .addParameter(RxJavaClass.Consumer(listType), "onNext")
                .addParameter(RxJavaClass.Consumer(ClassName.get(Throwable.class)), "onError")
                .addParameter(RxJavaClass.Action, "onComplete")
                .addParameter(RxJavaClass.Consumer(RxJavaClass.Disposable), "onSubscribe")
                .returns(RxJavaClass.Disposable);
        tb.addMethod(subscribe_4.build());
        final MethodSpec.Builder subscribe_5 = MethodSpec.methodBuilder(SUBSCRIBE)
                .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
                .addParameter(RxJavaClass.Observer(listType), "observer")
                .returns(void.class);
        tb.addMethod(subscribe_5.build());
        final MethodSpec.Builder notifyDataSetChanged = MethodSpec.methodBuilder(NOTIFY_DATA_SET_CHANGED)
                .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
                .returns(void.class);
        tb.addMethod(notifyDataSetChanged.build());
        creator.writeTo(packageName, tb.build());
        interactBuilds.put(interfaceClassName.toString(), interactBuild);
        interactBuilds.put(interfaceName, interactBuild);
        return interactBuild;
    }

    static void createImplement(BaseCreator creator, String packageName, ClassName entityType, InteractBuild interactBuild, boolean isBehavior) {
        final TypeName listType = ParameterizedTypeName.get(ClassName.get(Set.class), entityType);
        final TypeName subjectType = RxJavaClass.Subject(listType);
        final String implementClassName = entityType.simpleName() + (isBehavior ? INTERACTOR_ : PUBLISHER_);
        if (isBehavior){
            interactBuild.setInteractorClassName(implementClassName);
        }else{
            interactBuild.setPackageName(implementClassName);
        }
        interactBuild.setPackageName(packageName);

        final TypeSpec.Builder tb = TypeSpec.classBuilder(implementClassName)
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                .addSuperinterface(RxJavaClass.Consumer(listType))
                .addSuperinterface(interactBuild.interactTypeName);

        tb.addField(FieldSpec.builder(listType, ENTITY_FIELD_NAME, Modifier.PRIVATE).build());
        tb.addField(FieldSpec.builder(subjectType, SUBJECT_FIELD_NAME, Modifier.PRIVATE, Modifier.FINAL).build());
        tb.addMethod(createConstructor(entityType, isBehavior));
        tb.addMethod(createNewInstance(ClassName.get(interactBuild.packageName, implementClassName), listType));
        tb.addMethod(createNewInstanceNoParameters(ClassName.get(interactBuild.packageName, implementClassName), listType));

        final MethodSpec.Builder add = MethodSpec.methodBuilder(ADD)
                .addAnnotation(Override.class)
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                .addParameter(entityType, entityType.simpleName())
                .addStatement("$N.add($N)", ENTITY_FIELD_NAME, entityType.simpleName())
                .addStatement("$N.onNext($N)", SUBJECT_FIELD_NAME, ENTITY_FIELD_NAME)
                .returns(void.class);
        tb.addMethod(add.build());

        final MethodSpec.Builder addAll = MethodSpec.methodBuilder(ADDALL)
                .addAnnotation(Override.class)
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                .addParameter(ParameterizedTypeName.get(ClassName.get(Collection.class), entityType), "collection")
                .addStatement("$N.addAll(collection)", ENTITY_FIELD_NAME)
                .addStatement("$N.onNext($N)", SUBJECT_FIELD_NAME, ENTITY_FIELD_NAME)
                .returns(void.class);
        tb.addMethod(addAll.build());

        final MethodSpec.Builder remove1 = MethodSpec.methodBuilder(REMOVE)
                .addAnnotation(Override.class)
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                .addParameter(entityType, entityType.simpleName())
                .addStatement("$N.remove($N)", ENTITY_FIELD_NAME, entityType.simpleName())
                .addStatement("$N.onNext($N)", SUBJECT_FIELD_NAME, ENTITY_FIELD_NAME)
                .returns(void.class);
        tb.addMethod(remove1.build());

        final MethodSpec.Builder remove2 = MethodSpec.methodBuilder(REMOVE)
                .addAnnotation(Override.class)
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                .addParameter(int.class, "index")
                .beginControlFlow("if($N.size() > index)", ENTITY_FIELD_NAME)
                .addStatement("$N.remove(index)", ENTITY_FIELD_NAME)
                .addStatement("$N.onNext($N)", SUBJECT_FIELD_NAME, ENTITY_FIELD_NAME)
                .endControlFlow()
                .returns(void.class);
        tb.addMethod(remove2.build());

        final MethodSpec.Builder clear = MethodSpec.methodBuilder(CLEAR)
                .addAnnotation(Override.class)
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                .addStatement("$N.clear()", ENTITY_FIELD_NAME)
                .addStatement("$N.onNext($N)", SUBJECT_FIELD_NAME, ENTITY_FIELD_NAME)
                .returns(void.class);
        tb.addMethod(clear.build());


        tb.addMethod(createUpdateMethod(listType));
        tb.addMethod(createEntityMethod(implementClassName, listType));
        tb.addMethod(createGetEntityMethod(implementClassName, listType));
        tb.addMethod(createSubjectMethod(listType));
        final MethodSpec.Builder subscribe_0 = MethodSpec.methodBuilder(SUBSCRIBE)
                .addAnnotation(Override.class)
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                .returns(RxJavaClass.Disposable)
                .addStatement("return subject.subscribe()");
        tb.addMethod(subscribe_0.build());
        final MethodSpec.Builder subscribe_1 = MethodSpec.methodBuilder(SUBSCRIBE)
                .addAnnotation(Override.class)
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                .addParameter(RxJavaClass.Consumer(listType), "onNext")
                .returns(RxJavaClass.Disposable)
                .addStatement("return subject.subscribe(onNext)");
        tb.addMethod(subscribe_1.build());
        final MethodSpec.Builder subscribe_2 = MethodSpec.methodBuilder(SUBSCRIBE)
                .addAnnotation(Override.class)
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                .addParameter(RxJavaClass.Consumer(listType), "onNext")
                .addParameter(RxJavaClass.Consumer(ClassName.get(Throwable.class)), "onError")
                .returns(RxJavaClass.Disposable)
                .addStatement("return subject.subscribe(onNext,onError)");
        tb.addMethod(subscribe_2.build());
        final MethodSpec.Builder subscribe_3 = MethodSpec.methodBuilder(SUBSCRIBE)
                .addAnnotation(Override.class)
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                .addParameter(RxJavaClass.Consumer(listType), "onNext")
                .addParameter(RxJavaClass.Consumer(ClassName.get(Throwable.class)), "onError")
                .addParameter(RxJavaClass.Action, "onComplete")
                .returns(RxJavaClass.Disposable)
                .addStatement("return subject.subscribe(onNext,onError,onComplete)");
        tb.addMethod(subscribe_3.build());
        final MethodSpec.Builder subscribe_4 = MethodSpec.methodBuilder(SUBSCRIBE)
                .addAnnotation(Override.class)
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                .addParameter(RxJavaClass.Consumer(listType), "onNext")
                .addParameter(RxJavaClass.Consumer(ClassName.get(Throwable.class)), "onError")
                .addParameter(RxJavaClass.Action, "onComplete")
                .addParameter(RxJavaClass.Consumer(RxJavaClass.Disposable), "onSubscribe")
                .returns(RxJavaClass.Disposable)
                .addStatement("return subject.subscribe(onNext,onError,onComplete,onSubscribe)");
        tb.addMethod(subscribe_4.build());
        final MethodSpec.Builder subscribe_5 = MethodSpec.methodBuilder(SUBSCRIBE)
                .addAnnotation(Override.class)
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                .addParameter(RxJavaClass.Observer(listType), "observer")
                .returns(void.class)
                .addStatement("subject.subscribe(observer)");
        tb.addMethod(subscribe_5.build());
        final MethodSpec.Builder notifyDataSetChanged = MethodSpec.methodBuilder(NOTIFY_DATA_SET_CHANGED)
                .addAnnotation(Override.class)
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                .returns(void.class)
                .addStatement("subject.onNext(entity)");
        tb.addMethod(notifyDataSetChanged.build());
        tb.addMethod(createAcceptMethod(listType));
        creator.writeTo(interactBuild.packageName, tb.build());
    }

    private static MethodSpec createConstructor(TypeName entityType, boolean isBehavior) {
        final MethodSpec.Builder builder = MethodSpec.constructorBuilder()
                .addModifiers(Modifier.PUBLIC)
                .addParameter(ParameterizedTypeName.get(ClassName.get(Collection.class), entityType), "collection")
                .beginControlFlow("if(collection == null)")
                .addStatement("$N = new $T()", ENTITY_FIELD_NAME, ParameterizedTypeName.get(ClassName.get(HashSet.class), entityType))
                .nextControlFlow("else")
                .addStatement("$N = new $T(collection)", ENTITY_FIELD_NAME, ParameterizedTypeName.get(ClassName.get(HashSet.class), entityType))
                .endControlFlow();
        if (isBehavior) {
            return builder.addStatement("$N = $T.createDefault($N)", SUBJECT_FIELD_NAME, RxJavaClass.BehaviorSubject, ENTITY_FIELD_NAME).build();
        } else {
            return builder.addStatement("$N = $T.create()", SUBJECT_FIELD_NAME, RxJavaClass.PublishSubject).build();
        }
    }

    private static MethodSpec createUpdateMethod(TypeName entityType) {
        return MethodSpec.methodBuilder(UPDATE)
                .addAnnotation(Override.class)
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                .addParameter(entityType, ENTITY_FIELD_NAME)
                .returns(void.class)
                .addStatement("this.$N.clear()", ENTITY_FIELD_NAME)
                .addStatement("this.$N.addAll($N)", ENTITY_FIELD_NAME, ENTITY_FIELD_NAME)
                .addStatement("$N.onNext(this.$N)", SUBJECT_FIELD_NAME, ENTITY_FIELD_NAME)
                .build();
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
        return MethodSpec.methodBuilder(InteractorCreator.GET_ENTITY)
                .addAnnotation(Override.class)
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                .returns(entityType)
                .addStatement("return $N", InteractorCreator.ENTITY_FIELD_NAME)
                .build();
    }

    private static MethodSpec createSubjectMethod(TypeName entityType) {
        return MethodSpec.methodBuilder(SUBJECT)
                .addAnnotation(Override.class)
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                .returns(RxJavaClass.Subject(entityType))
                .addStatement("return $N", SUBJECT_FIELD_NAME).build();
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
}
