package library.neetoffice.com.neetannotation.processor;

import com.google.auto.service.AutoService;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;

import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeMirror;

import library.neetoffice.com.neetannotation.Interact;

@SupportedAnnotationTypes({
        "library.neetoffice.com.neetannotation.Interact"})
@AutoService(Process.class)
public class InteractorProcessor extends AbstractProcessor {
    static class Setter {
        final String fieldMame;
        final String methodName;
        final TypeName parameterTypeName;
        final String parameterName;

        Setter(String fieldMame, String methodName, TypeName parameterTypeName, String parameterName) {
            this.fieldMame = fieldMame;
            this.methodName = methodName;
            this.parameterTypeName = parameterTypeName;
            this.parameterName = parameterName;
        }
    }

    static class Getter {
        final String fieldMame;
        final String methodName;
        final TypeName returnTypeName;

        Getter(String fieldMame, String methodName, TypeName returnTypeName) {
            this.fieldMame = fieldMame;
            this.methodName = methodName;
            this.returnTypeName = returnTypeName;
        }
    }

    static class InteractBuild {
        final List<Setter> setters = new ArrayList<>();
        final List<Getter> getters = new ArrayList<>();
        final TypeName interactTypeName;

        InteractBuild(TypeName interactTypeName) {
            this.interactTypeName = interactTypeName;
        }
    }

    private static final String entityName = "entity";
    private static final String subjectFieldName = "subject";
    private ProcessorUtil processorUtil;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        processorUtil = new ProcessorUtil(processingEnv);
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.RELEASE_8;
    }

    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnv) {
        final Set<? extends Element> elements = roundEnv.getElementsAnnotatedWith(Interact.class);
        for (Element entityElement : elements) {
            final InteractBuild interactBuild = createInteractor(entityElement);
            createInteractorClass(entityElement, interactBuild);
        }
        return true;
    }

    private InteractBuild createInteractor(Element entityElement) {
        final String interactClassName = entityElement.getSimpleName() + "Interactor";
        final InteractBuild interactBuild = new InteractBuild(ClassName.get(processorUtil.getPackageName(entityElement), interactClassName));
        final TypeSpec.Builder tb = TypeSpec.interfaceBuilder(interactClassName)
                .addModifiers(Modifier.PUBLIC)
                .addSuperinterface(RxJavaClass.Consumer(processorUtil.getClassName(entityElement.asType())));

        final MethodSpec.Builder entity = MethodSpec.methodBuilder("entity")
                .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
                .returns(processorUtil.getClassName(entityElement.asType()));

        final MethodSpec.Builder update = MethodSpec.methodBuilder("update")
                .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
                .addParameter(processorUtil.getClassName(entityElement.asType()), entityElement.getSimpleName().toString())
                .returns(void.class);

        for (Element element : entityElement.getEnclosedElements()) {
            if (element.getKind() == ElementKind.FIELD) {
                final String fieldMame = element.getSimpleName().toString();
                final String setterName = "set" + fieldMame.toUpperCase().charAt(0) + fieldMame.substring(1);
                final String getterName = "get" + fieldMame.toUpperCase().charAt(0) + fieldMame.substring(1);
                final MethodSpec.Builder setter = MethodSpec.methodBuilder(setterName)
                        .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
                        .addParameter(processorUtil.getClassName(element.asType()), fieldMame)
                        .returns(void.class);
                final MethodSpec.Builder getter = MethodSpec.methodBuilder(getterName)
                        .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
                        .returns(processorUtil.getClassName(element.asType()));
                tb.addMethod(setter.build());
                tb.addMethod(getter.build());
                interactBuild.setters.add(new Setter(fieldMame, setterName, processorUtil.getClassName(element.asType()), fieldMame));
                interactBuild.getters.add(new Getter(fieldMame, getterName, processorUtil.getClassName(element.asType())));
            }
        }
        tb.addMethod(update.build());
        tb.addMethod(entity.build());
        final JavaFile javaFile = JavaFile.builder(processorUtil.getPackageName(entityElement), tb.build()).build();
        processorUtil.writeTo(javaFile);
        return interactBuild;
    }


    private void createInteractorClass(Element entityElement, InteractBuild interactBuild) {
        final TypeName subjectTypeName = RxJavaClass.Subject(processorUtil.getClassName(entityElement.asType()));
        createInteractorClass(entityElement, interactBuild, subjectTypeName);
    }

    private void createInteractorClass(Element entityElement, InteractBuild interactBuild, TypeName subjectTypeName) {
        final List<ExecutableElement> interactMethods = new ArrayList<>();
        for (Element method : entityElement.getEnclosedElements()) {
            if (method.getKind() == ElementKind.METHOD) {
                interactMethods.add((ExecutableElement) method);
            }
        }
        final HashMap<String, VariableElement> entityVars = new HashMap<>();
        final HashMap<String, ExecutableElement> entityMethods = new HashMap<>();
        for (Element element : entityElement.getEnclosedElements()) {
            if (element.getKind() == ElementKind.FIELD && element.getModifiers().contains(Modifier.PUBLIC) && !element.getModifiers().contains(Modifier.FINAL)) {
                entityVars.put(element.getSimpleName().toString().toLowerCase(), (VariableElement) element);
            }
            if (element.getKind() == ElementKind.METHOD && !element.getModifiers().contains(Modifier.PRIVATE)) {
                entityMethods.put(element.getSimpleName().toString().toLowerCase(), (ExecutableElement) element);
            }
        }
        final String className = entityElement.getSimpleName().toString() + "Interactor_";

        final TypeSpec.Builder tb = TypeSpec.classBuilder(className)
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                .addSuperinterface(RxJavaClass.Consumer(processorUtil.getClassName(entityElement.asType())))
                .addSuperinterface(interactBuild.interactTypeName);

        final FieldSpec.Builder entityField = FieldSpec.builder(ClassName.get(entityElement.asType()), entityName, Modifier.PRIVATE);

        final FieldSpec.Builder subjectField = FieldSpec.builder(subjectTypeName, subjectFieldName, Modifier.PRIVATE, Modifier.FINAL);

        final MethodSpec.Builder constructor = MethodSpec.constructorBuilder()
                .addModifiers(Modifier.PUBLIC)
                .addParameter(ClassName.get(entityElement.asType()), entityName)
                .addParameter(subjectTypeName, subjectFieldName)
                .addStatement("this.$N = $N", entityName, entityName)
                .addStatement("this.$N = $N", subjectFieldName, subjectFieldName);

        final MethodSpec.Builder entity = MethodSpec.methodBuilder("entity")
                .addAnnotation(Override.class)
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                .returns(ClassName.get(entityElement.asType()))
                .addStatement("return $N", entityName);

        final MethodSpec.Builder update = MethodSpec.methodBuilder("update")
                .addAnnotation(Override.class)
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                .addParameter(processorUtil.getClassName(entityElement.asType()), entityName)
                .returns(void.class)
                .addCode(createUpdateCodeBlock(className, processorUtil.getClassName(entityElement.asType())));

        final MethodSpec.Builder accept = MethodSpec.methodBuilder("accept")
                .addAnnotation(Override.class)
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                .addParameter(processorUtil.getClassName(entityElement.asType()), entityName)
                .addException(Exception.class)
                .returns(void.class)
                .addStatement("update($N)", entityName);


        for (Setter setter : interactBuild.setters) {
            final MethodSpec.Builder method = MethodSpec.methodBuilder(setter.methodName)
                    .addAnnotation(Override.class)
                    .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                    .addParameter(setter.parameterTypeName, setter.parameterName)
                    .returns(void.class);
            method.addCode(createSetterCodeBlock(className, setter, entityElement.asType(), entityVars, entityMethods));
            tb.addMethod(method.build());
        }
        for (Getter getter : interactBuild.getters) {
            final MethodSpec.Builder method = MethodSpec.methodBuilder(getter.methodName)
                    .addAnnotation(Override.class)
                    .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                    .returns(getter.returnTypeName);
            method.addCode(createGetterCodeBlock(className, getter, entityVars, entityMethods));
            tb.addMethod(method.build());
        }
        tb.addField(entityField.build());
        tb.addField(subjectField.build());
        tb.addMethod(constructor.build());
        tb.addMethod(update.build());
        tb.addMethod(entity.build());
        tb.addMethod(accept.build());
        final JavaFile javaFile = JavaFile.builder(processorUtil.getPackageName(entityElement), tb.build()).build();
        processorUtil.writeTo(javaFile);
    }

    private CodeBlock createUpdateCodeBlock(String thisClassName, TypeName entityType) {
        return CodeBlock.builder()
                .add(ReactiveXHelp.just(entityName))
                .add(ReactiveXHelp.map(entityType, entityType, entityName,
                        CodeBlock.builder()
                                .addStatement("$N.this.$N = $N", thisClassName, entityName, entityName)
                                .addStatement("return $N", entityName)
                                .build()))
                .add(ReactiveXHelp.subscribe(subjectFieldName))
                .build();
    }

    private CodeBlock createSetterCodeBlock(String thisClassName, Setter setter, TypeMirror entityType, HashMap<String, VariableElement> entityVars, HashMap<String, ExecutableElement> entityMethods) {
        final TypeName parameterType = setter.parameterTypeName;
        final String parameterName = setter.parameterName;
        final CodeBlock.Builder codeBlock = CodeBlock.builder();
        final String interactMethodName = setter.methodName.toLowerCase();
        if (entityMethods.containsKey(interactMethodName)) {
            final ExecutableElement entityMethod = entityMethods.get(interactMethodName);
            codeBlock.addStatement("$N.this.$N.$N(value)", thisClassName, entityName, entityMethod.getSimpleName());
        } else if (entityVars.containsKey(interactMethodName.substring(3))) {
            final VariableElement entityVar = entityVars.get(interactMethodName.substring(3));
            codeBlock.addStatement("$N.this.$N.$N = value", thisClassName, entityName, entityVar.getSimpleName());
        }
        codeBlock.addStatement("return $N.this.$N", thisClassName, entityName);
        return CodeBlock.builder()
                .add(ReactiveXHelp.just(parameterName))
                .add(ReactiveXHelp.map(parameterType, processorUtil.getClassName(entityType), "value", codeBlock.build()))
                .add(ReactiveXHelp.subscribe(subjectFieldName))
                .build();
    }

    private CodeBlock createGetterCodeBlock(String thisClassName, Getter getter, HashMap<String, VariableElement> entityVars, HashMap<String, ExecutableElement> entityMethods) {
        final String interactMethodName = getter.methodName.toLowerCase();
        final CodeBlock.Builder codeBlock = CodeBlock.builder();
        if (entityMethods.containsKey(interactMethodName)) {
            final ExecutableElement entityMethod = entityMethods.get(interactMethodName);
            codeBlock.addStatement("return $N.this.$N.$N()", thisClassName, entityName, entityMethod.getSimpleName().toString());
        } else if (entityVars.containsKey(interactMethodName.substring(3))) {
            final VariableElement entityVar = entityVars.get(interactMethodName.substring(3));
            codeBlock.addStatement("return $N.this.$N.$N", thisClassName, entityName, entityVar.getSimpleName().toString());
        }
        return codeBlock.build();
    }
}
