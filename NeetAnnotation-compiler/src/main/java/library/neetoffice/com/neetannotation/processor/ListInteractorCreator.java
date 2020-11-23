package library.neetoffice.com.neetannotation.processor;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;
import com.squareup.javapoet.TypeVariableName;
import com.squareup.javapoet.WildcardTypeName;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;

public class ListInteractorCreator extends BaseCreator {
    static final String INTERACTOR = "Listinteractor";
    static final String PRESENTER_ = INTERACTOR + "_";
    static final String SUBJECT_FIELD_NAME = "subject";
    static final String ADD = "add";
    static final String ADDALL = "addAll";
    static final String SET = "set";
    static final String REMOVE = "remove";
    static final String CLEAR = "clear";

    final HashMap<String, InteractBuild> interactBuilds = new HashMap<>();
    final HashMap<String, Element> interactElements = new HashMap<>();

    static class InteractBuild {
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

    public ListInteractorCreator(AbstractProcessor processor, ProcessingEnvironment processingEnv) {
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
        final ListInteractorCreator.InteractBuild build = createInterface(interactElement);
        createImplement(interactElement, build);
    }

    private InteractBuild createInterface(TypeElement interactElement) {
        final String interfaceName = interactElement.getSimpleName() + INTERACTOR;
        final String packageName = getPackageName(interactElement);
        final TypeName entityType = getClassName(interactElement.asType());
        final TypeName listType = ParameterizedTypeName.get(ClassName.get(List.class), entityType);
        final ClassName interfaceClassName = ClassName.get(packageName, interfaceName);
        final InteractBuild interactBuild = new InteractBuild(interfaceClassName, interfaceName);
        final TypeSpec.Builder tb = TypeSpec.interfaceBuilder(interfaceName)
                .addModifiers(Modifier.PUBLIC)
                .addSuperinterface(RxJavaClass.Consumer(listType));

        final MethodSpec.Builder add = MethodSpec.methodBuilder(ADD)
                .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
                .addParameter(entityType, interactElement.getSimpleName().toString())
                .returns(void.class);
        tb.addMethod(add.build());

        final MethodSpec.Builder addAll = MethodSpec.methodBuilder(ADDALL)
                .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
                .addParameter(ParameterizedTypeName.get(ClassName.get(Collection.class), entityType), "collection")
                .returns(void.class);
        tb.addMethod(addAll.build());

        final MethodSpec.Builder set = MethodSpec.methodBuilder(SET)
                .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
                .addParameter(int.class, "index")
                .addParameter(entityType, interactElement.getSimpleName().toString())
                .returns(void.class);
        tb.addMethod(set.build());


        final MethodSpec.Builder remove1 = MethodSpec.methodBuilder(REMOVE)
                .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
                .addParameter(entityType, interactElement.getSimpleName().toString())
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


        final MethodSpec.Builder update = MethodSpec.methodBuilder(InteractorCreator.UPDATE)
                .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
                .addParameter(listType, interactElement.getSimpleName().toString())
                .returns(void.class);
        tb.addMethod(update.build());

        final MethodSpec.Builder entity = MethodSpec.methodBuilder(InteractorCreator.ENTITY)
                .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
                .returns(RxJavaClass.Single(listType));
        tb.addMethod(entity.build());

        final MethodSpec.Builder subject = MethodSpec.methodBuilder(InteractorCreator.OBSERVABLE)
                .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
                .returns(RxJavaClass.Observable(listType));
        tb.addMethod(subject.build());
        final MethodSpec.Builder subscribe_0 = MethodSpec.methodBuilder(InteractorCreator.SUBSCRIBE)
                .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
                .returns(RxJavaClass.Disposable);
        tb.addMethod(subscribe_0.build());
        final MethodSpec.Builder subscribe_1 = MethodSpec.methodBuilder(InteractorCreator.SUBSCRIBE)
                .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
                .addParameter(RxJavaClass.Consumer(listType), "onNext")
                .returns(RxJavaClass.Disposable);
        tb.addMethod(subscribe_1.build());
        final MethodSpec.Builder subscribe_2 = MethodSpec.methodBuilder(InteractorCreator.SUBSCRIBE)
                .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
                .addParameter(RxJavaClass.Consumer(listType), "onNext")
                .addParameter(RxJavaClass.Consumer(ClassName.get(Throwable.class)), "onError")
                .returns(RxJavaClass.Disposable);
        tb.addMethod(subscribe_2.build());
        final MethodSpec.Builder subscribe_3 = MethodSpec.methodBuilder(InteractorCreator.SUBSCRIBE)
                .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
                .addParameter(RxJavaClass.Consumer(listType), "onNext")
                .addParameter(RxJavaClass.Consumer(ClassName.get(Throwable.class)), "onError")
                .addParameter(RxJavaClass.Action, "onComplete")
                .returns(RxJavaClass.Disposable);
        tb.addMethod(subscribe_3.build());
        final MethodSpec.Builder subscribe_4 = MethodSpec.methodBuilder(InteractorCreator.SUBSCRIBE)
                .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
                .addParameter(RxJavaClass.Consumer(listType), "onNext")
                .addParameter(RxJavaClass.Consumer(ClassName.get(Throwable.class)), "onError")
                .addParameter(RxJavaClass.Action, "onComplete")
                .addParameter(RxJavaClass.Consumer(RxJavaClass.Disposable), "onSubscribe")
                .returns(RxJavaClass.Disposable);
        tb.addMethod(subscribe_4.build());
        final MethodSpec.Builder subscribe_5 = MethodSpec.methodBuilder(InteractorCreator.SUBSCRIBE)
                .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
                .addParameter(RxJavaClass.Observer(listType), "observer")
                .returns(void.class);
        tb.addMethod(subscribe_5.build());
        final MethodSpec.Builder notifyDataSetChanged = MethodSpec.methodBuilder(InteractorCreator.NOTIFY_DATA_SET_CHANGED)
                .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
                .returns(void.class);
        tb.addMethod(notifyDataSetChanged.build());

        writeTo(packageName, tb.build());
        interactBuilds.put(interfaceClassName.toString(), interactBuild);
        interactBuilds.put(interfaceName, interactBuild);
        interactElements.put(interfaceClassName.toString(), interactElement);
        interactElements.put(interfaceName, interactElement);
        return interactBuild;
    }

    private void createImplement(TypeElement interactElement, InteractBuild interactBuild) {
        final TypeName entityType = getClassName(interactElement.asType());
        final TypeName listType = ParameterizedTypeName.get(ClassName.get(List.class), entityType);
        //final TypeName subjectType = RxJavaClass.Subject(WildcardTypeName.subtypeOf(entityType)); List<? extends entityType>
        final TypeName subjectType = RxJavaClass.Subject(listType);
        final String implementClassName = interactElement.getSimpleName().toString() + PRESENTER_;
        interactBuild.setImplementClassName(implementClassName);
        interactBuild.setPackageName(getPackageName(interactElement));

        final TypeSpec.Builder tb = TypeSpec.classBuilder(implementClassName)
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                .addSuperinterface(RxJavaClass.Consumer(listType))
                .addSuperinterface(interactBuild.interactTypeName);

        tb.addField(FieldSpec.builder(listType, InteractorCreator.ENTITY_FIELD_NAME, Modifier.PRIVATE).build());
        tb.addField(FieldSpec.builder(subjectType, SUBJECT_FIELD_NAME, Modifier.PRIVATE, Modifier.FINAL).build());
        tb.addMethod(createConstructor(entityType));
        tb.addMethod(createNewInstance(ClassName.get(interactBuild.packageName, implementClassName), listType));
        tb.addMethod(createNewInstanceNoParameters(ClassName.get(interactBuild.packageName, implementClassName), listType));

        final MethodSpec.Builder add = MethodSpec.methodBuilder(ADD)
                .addAnnotation(Override.class)
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                .addParameter(entityType, interactElement.getSimpleName().toString())
                .addStatement("$N.add($N)", InteractorCreator.ENTITY_FIELD_NAME, interactElement.getSimpleName().toString())
                .addStatement("$N.onNext($N)", SUBJECT_FIELD_NAME, InteractorCreator.ENTITY_FIELD_NAME)
                .returns(void.class);
        tb.addMethod(add.build());

        final MethodSpec.Builder addAll = MethodSpec.methodBuilder(ADDALL)
                .addAnnotation(Override.class)
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                .addParameter(ParameterizedTypeName.get(ClassName.get(Collection.class), entityType), "collection")
                .addStatement("$N.addAll(collection)", InteractorCreator.ENTITY_FIELD_NAME)
                .addStatement("$N.onNext($N)", SUBJECT_FIELD_NAME, InteractorCreator.ENTITY_FIELD_NAME)
                .returns(void.class);
        tb.addMethod(addAll.build());

        final MethodSpec.Builder set = MethodSpec.methodBuilder(SET)
                .addAnnotation(Override.class)
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                .addParameter(int.class, "index")
                .addParameter(entityType, interactElement.getSimpleName().toString())
                .addStatement("$N.set(index,$N)", InteractorCreator.ENTITY_FIELD_NAME, interactElement.getSimpleName().toString())
                .addStatement("$N.onNext($N)", SUBJECT_FIELD_NAME, InteractorCreator.ENTITY_FIELD_NAME)
                .returns(void.class);
        tb.addMethod(set.build());


        final MethodSpec.Builder remove1 = MethodSpec.methodBuilder(REMOVE)
                .addAnnotation(Override.class)
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                .addParameter(entityType, interactElement.getSimpleName().toString())
                .addStatement("$N.remove($N)", InteractorCreator.ENTITY_FIELD_NAME, interactElement.getSimpleName().toString())
                .addStatement("$N.onNext($N)", SUBJECT_FIELD_NAME, InteractorCreator.ENTITY_FIELD_NAME)
                .returns(void.class);
        tb.addMethod(remove1.build());

        final MethodSpec.Builder remove2 = MethodSpec.methodBuilder(REMOVE)
                .addAnnotation(Override.class)
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                .addParameter(int.class, "index")
                .beginControlFlow("if($N.size() > index)", InteractorCreator.ENTITY_FIELD_NAME)
                .addStatement("$N.remove(index)", InteractorCreator.ENTITY_FIELD_NAME)
                .addStatement("$N.onNext($N)", SUBJECT_FIELD_NAME, InteractorCreator.ENTITY_FIELD_NAME)
                .endControlFlow()
                .returns(void.class);
        tb.addMethod(remove2.build());

        final MethodSpec.Builder clear = MethodSpec.methodBuilder(CLEAR)
                .addAnnotation(Override.class)
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                .addStatement("$N.clear()", InteractorCreator.ENTITY_FIELD_NAME)
                .addStatement("$N.onNext($N)", SUBJECT_FIELD_NAME, InteractorCreator.ENTITY_FIELD_NAME)
                .returns(void.class);
        tb.addMethod(clear.build());


        tb.addMethod(createUpdateMethod(listType));
        tb.addMethod(createEntityMethod(implementClassName, listType));
        tb.addMethod(createSubjectMethod(listType));
        final MethodSpec.Builder subscribe_0 = MethodSpec.methodBuilder(InteractorCreator.SUBSCRIBE)
                .addAnnotation(Override.class)
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                .returns(RxJavaClass.Disposable)
                .addStatement("return subject.subscribe()");
        tb.addMethod(subscribe_0.build());
        final MethodSpec.Builder subscribe_1 = MethodSpec.methodBuilder(InteractorCreator.SUBSCRIBE)
                .addAnnotation(Override.class)
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                .addParameter(RxJavaClass.Consumer(listType), "onNext")
                .returns(RxJavaClass.Disposable)
                .addStatement("return subject.subscribe(onNext)");
        tb.addMethod(subscribe_1.build());
        final MethodSpec.Builder subscribe_2 = MethodSpec.methodBuilder(InteractorCreator.SUBSCRIBE)
                .addAnnotation(Override.class)
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                .addParameter(RxJavaClass.Consumer(listType), "onNext")
                .addParameter(RxJavaClass.Consumer(ClassName.get(Throwable.class)), "onError")
                .returns(RxJavaClass.Disposable)
                .addStatement("return subject.subscribe(onNext,onError)");
        tb.addMethod(subscribe_2.build());
        final MethodSpec.Builder subscribe_3 = MethodSpec.methodBuilder(InteractorCreator.SUBSCRIBE)
                .addAnnotation(Override.class)
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                .addParameter(RxJavaClass.Consumer(listType), "onNext")
                .addParameter(RxJavaClass.Consumer(ClassName.get(Throwable.class)), "onError")
                .addParameter(RxJavaClass.Action, "onComplete")
                .returns(RxJavaClass.Disposable)
                .addStatement("return subject.subscribe(onNext,onError,onComplete)");
        tb.addMethod(subscribe_3.build());
        final MethodSpec.Builder subscribe_4 = MethodSpec.methodBuilder(InteractorCreator.SUBSCRIBE)
                .addAnnotation(Override.class)
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                .addParameter(RxJavaClass.Consumer(listType), "onNext")
                .addParameter(RxJavaClass.Consumer(ClassName.get(Throwable.class)), "onError")
                .addParameter(RxJavaClass.Action, "onComplete")
                .addParameter(RxJavaClass.Consumer(RxJavaClass.Disposable), "onSubscribe")
                .returns(RxJavaClass.Disposable)
                .addStatement("return subject.subscribe(onNext,onError,onComplete,onSubscribe)");
        tb.addMethod(subscribe_4.build());
        final MethodSpec.Builder subscribe_5 = MethodSpec.methodBuilder(InteractorCreator.SUBSCRIBE)
                .addAnnotation(Override.class)
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                .addParameter(RxJavaClass.Observer(listType), "observer")
                .returns(void.class)
                .addStatement("subject.subscribe(observer)");
        tb.addMethod(subscribe_5.build());
        final MethodSpec.Builder notifyDataSetChanged = MethodSpec.methodBuilder(InteractorCreator.NOTIFY_DATA_SET_CHANGED)
                .addAnnotation(Override.class)
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                .returns(void.class)
                .addStatement("subject.onNext(entity)");
        tb.addMethod(notifyDataSetChanged.build());
        tb.addMethod(createAcceptMethod(listType));
        writeTo(interactBuild.packageName, tb.build());
    }

    private MethodSpec createConstructor(TypeName entityType) {
        return MethodSpec.constructorBuilder()
                .addModifiers(Modifier.PUBLIC)
                .addParameter(ParameterizedTypeName.get(ClassName.get(Collection.class), entityType), "collection")
                .beginControlFlow("if(collection == null)")
                .addStatement("$N = new $T()", InteractorCreator.ENTITY_FIELD_NAME, ParameterizedTypeName.get(ClassName.get(ArrayList.class), entityType))
                .nextControlFlow("else")
                .addStatement("$N = new $T(collection)", InteractorCreator.ENTITY_FIELD_NAME, ParameterizedTypeName.get(ClassName.get(ArrayList.class), entityType))
                .endControlFlow()
                .addStatement("$N = $T.createDefault($N)", SUBJECT_FIELD_NAME, RxJavaClass.BehaviorSubject, InteractorCreator.ENTITY_FIELD_NAME)
                .build();
    }

    private MethodSpec createUpdateMethod(TypeName entityType) {
        return MethodSpec.methodBuilder(InteractorCreator.UPDATE)
                .addAnnotation(Override.class)
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                .addParameter(entityType, InteractorCreator.ENTITY_FIELD_NAME)
                .returns(void.class)
                .beginControlFlow("if($N == null)",InteractorCreator.ENTITY_FIELD_NAME)
                .addStatement("return")
                .endControlFlow()
                .addStatement("this.$N.clear()", InteractorCreator.ENTITY_FIELD_NAME)
                .addStatement("this.$N.addAll($N)", InteractorCreator.ENTITY_FIELD_NAME, InteractorCreator.ENTITY_FIELD_NAME)
                .addStatement("$N.onNext(this.$N)", SUBJECT_FIELD_NAME, InteractorCreator.ENTITY_FIELD_NAME)
                .build();
    }

    private MethodSpec createEntityMethod(String elementClass, TypeName entityType) {
        return MethodSpec.methodBuilder(InteractorCreator.ENTITY)
                .addAnnotation(Override.class)
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                .returns(RxJavaClass.Single(entityType))
                .beginControlFlow("if(entity == null)")
                .addStatement("return $T.never()", RxJavaClass.Single)
                .endControlFlow()
                .addStatement("return $T.just($N)", RxJavaClass.Single, InteractorCreator.ENTITY_FIELD_NAME)
                .build();
    }

    private MethodSpec createSubjectMethod(TypeName entityType) {
        return MethodSpec.methodBuilder(InteractorCreator.OBSERVABLE)
                .addAnnotation(Override.class)
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                .returns(RxJavaClass.Observable(entityType))
                .addStatement("return $N", SUBJECT_FIELD_NAME).build();
    }

    private MethodSpec createAcceptMethod(TypeName entityType) {
        return MethodSpec.methodBuilder(InteractorCreator.ACCEPT)
                .addAnnotation(Override.class)
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                .addParameter(entityType, InteractorCreator.ENTITY_FIELD_NAME)
                .addException(Exception.class)
                .returns(void.class)
                .addStatement("$N($N)", InteractorCreator.UPDATE, InteractorCreator.ENTITY_FIELD_NAME)
                .build();
    }

    private MethodSpec createNewInstance(TypeName instanceType, TypeName entityType, TypeVariableName... typeVariableNames) {
        final MethodSpec.Builder builder = MethodSpec.methodBuilder("newInstance");
        builder.addModifiers(Modifier.PUBLIC, Modifier.STATIC);
        builder.addParameter(entityType, InteractorCreator.ENTITY_FIELD_NAME);
        for (TypeVariableName typeVariableName : typeVariableNames) {
            builder.addTypeVariable(typeVariableName);
        }
        builder.returns(instanceType);
        builder.addStatement("return new $T($N)", instanceType, InteractorCreator.ENTITY_FIELD_NAME);
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
}
