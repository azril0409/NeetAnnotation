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
import com.squareup.javapoet.TypeVariableName;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;

import library.neetoffice.com.neetannotation.NViewModel;
import library.neetoffice.com.neetannotation.Subject;
import library.neetoffice.com.neetannotation.NullEntity;

@SupportedAnnotationTypes({
        "library.neetoffice.com.neetannotation.NViewModel"})
@AutoService(Process.class)
public class ViewModelProcessor extends AbstractProcessor {
    private static final String findSubjectByName_parameterName = "name";
    private ProcessorUtil processorUtil;
    private DaggerHelp daggerHelp;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        processorUtil = new ProcessorUtil(processingEnv);
        daggerHelp = new DaggerHelp(processorUtil);
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.RELEASE_8;
    }

    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnv) {
        final Set<? extends Element> elements = roundEnv.getElementsAnnotatedWith(NViewModel.class);
        for (Element mainElement : elements) {
            final TypeElement typeMainElement = (TypeElement) mainElement;
            final String className = mainElement.getSimpleName() + "_";
            final TypeSpec.Builder tb = TypeSpec.classBuilder(className)
                    .superclass(ParameterizedTypeName.get(typeMainElement.asType()))
                    .addModifiers(Modifier.PUBLIC, Modifier.FINAL);
            final MethodSpec.Builder onCleared = MethodSpec.methodBuilder("onCleared")
                    .addAnnotation(Override.class)
                    .addModifiers(Modifier.PROTECTED)
                    .returns(void.class)
                    .addStatement("super.onCleared()");
            final MethodSpec.Builder findSubjectByName = MethodSpec.methodBuilder("findSubjectByName")
                    .addTypeVariable(TypeVariableName.get("T"))
                    .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                    .addParameter(String.class, findSubjectByName_parameterName)
                    .returns(RxJavaClass.Subject(TypeVariableName.get("T")))
                    .beginControlFlow("switch (name)")
                    .addStatement("default: return PublishSubject.create()");

            final List<? extends Element> enclosedElements = mainElement.getEnclosedElements();
            for (Element element : enclosedElements) {
                if (element.getKind() == ElementKind.CONSTRUCTOR) {
                    ExecutableElement constructor = (ExecutableElement) element;
                    final MethodSpec.Builder cb = MethodSpec.constructorBuilder()
                            .addModifiers(Modifier.PUBLIC);
                    final Iterator<? extends VariableElement> iterator = constructor.getParameters().iterator();
                    final CodeBlock.Builder code = CodeBlock.builder()
                            .add("super(");
                    while (iterator.hasNext()) {
                        final VariableElement parameter = iterator.next();
                        cb.addParameter(processorUtil.getClassName(parameter.asType()), parameter.getSimpleName().toString());
                        code.add("$N", parameter.getSimpleName());
                        if (iterator.hasNext()) {
                            code.add(",");
                        }
                    }
                    code.addStatement(")");
                    tb.addMethod(cb.addCode(code.build()).build());
                }
                if (element.getAnnotation(Subject.class) != null) {
                    final FieldSpec subjectField = createSubjectFieldSpec(element);
                    tb.addField(subjectField);
                    final CodeBlock instanceInteractor = createCodeBlockInstanceInteractorByDagger(typeMainElement, element, subjectField);
                    onCleared.addCode(instanceInteractor);
                    final CodeBlock findSubjectByNameCode = createFindSubjectByNameCode(element);
                    findSubjectByName.addCode(findSubjectByNameCode);
                }
            }
            findSubjectByName.endControlFlow();

            onCleared.addCode(createInject(typeMainElement));
            tb.addMethod(onCleared.build());
            tb.addMethod(findSubjectByName.build());
            final JavaFile javaFile = JavaFile.builder(processorUtil.getPackageName(typeMainElement), tb.build()).build();
            processorUtil.writeTo(javaFile);
        }
        return true;
    }

    FieldSpec createSubjectFieldSpec(Element element) {
        final String classSimpleName = element.asType().toString();
        final TypeName entityTypeName = ClassName.get(processorUtil.getPackageName(element), classSimpleName.substring(0, classSimpleName.length() - "Interactor".length()));
        final String fieldName = element.getSimpleName().toString() + "_Subject";
        final TypeName subjectTypeName = RxJavaClass.Subject(entityTypeName);
        final FieldSpec.Builder fb = FieldSpec.builder(subjectTypeName, fieldName, Modifier.PUBLIC)
                .initializer("$T.create()", RxJavaClass.PublishSubject);
        return fb.build();
    }

    CodeBlock createCodeBlockInstanceInteractorByDagger(TypeElement mainElement, Element element, FieldSpec subjectField) {
        final boolean isSubAndroidViewModel = processorUtil.isSubAndroidViewModel(mainElement);
        final String packageName = processorUtil.getPackageName(element);
        final String daggerMethodName = daggerHelp.findNameFromDagger(element);
        final String classSimpleName = element.asType().toString() + "_";
        final TypeName interactorImpl = ClassName.get(packageName, classSimpleName);
        final String elementName = element.getSimpleName().toString();
        final NullEntity aNullEntity = element.getAnnotation(NullEntity.class);
        if (aNullEntity != null) {
            return CodeBlock.builder()
                    .addStatement("$N = new $T(null,$N)", elementName, interactorImpl, subjectField.name)
                    .build();
        }
        if (isSubAndroidViewModel) {
            return CodeBlock.builder()
                    .add("$N = new $T(", elementName, interactorImpl)
                    .add("Dagger_$N.builder()", mainElement.getSimpleName())
                    .add(".$N(new $T(getApplication()))", AndroidClass.CONTEXT_MODULE_NAME.toLowerCase().charAt(0) + AndroidClass.CONTEXT_MODULE_NAME.substring(1), AndroidClass.CONTEXT_MODULE)
                    .add(".build().$N()", daggerMethodName)
                    .addStatement(",$N)", subjectField.name)
                    .build();
        }
        return CodeBlock.builder()
                .addStatement("$N = new $T(Dagger_$N.create().$N(),$N)", elementName, interactorImpl, mainElement.getSimpleName(), daggerMethodName, subjectField.name)
                .build();
    }

    CodeBlock createInject(TypeElement mainElement) {
        final boolean isSubAndroidViewModel = processorUtil.isSubAndroidViewModel(mainElement);
        if (isSubAndroidViewModel) {
            return CodeBlock.builder()
                    .add("Dagger_$N.builder()", mainElement.getSimpleName())
                    .add(".$N(new $T(getApplication()))", AndroidClass.CONTEXT_MODULE_NAME.toLowerCase().charAt(0) + AndroidClass.CONTEXT_MODULE_NAME.substring(1), AndroidClass.CONTEXT_MODULE)
                    .add(".build()")
                    .addStatement(".inject(this)")
                    .build();
        }
        return CodeBlock.builder()
                .addStatement("Dagger_$N.create().inject(this)", mainElement.getSimpleName())
                .build();
    }

    CodeBlock createFindSubjectByNameCode(Element element) {
        final CodeBlock.Builder code = CodeBlock.builder();
        final String fieldName = element.getSimpleName().toString() + "_Subject";
        final AnnotationMirror named = AnnotationHelp.findAnnotationMirror(element, DaggerClass.Named);
        if (named != null) {
            final Object value = AnnotationHelp.findAnnotationValue(named, "value");
            if (value == null) {
                final String name = "n_" + value.toString();
                code.addStatement("case $S: return (Subject<T>) $N", name, fieldName).build();
            } else {
                code.addStatement("case $S: return (Subject<T>) $N", "n_" + element.getSimpleName().toString(), fieldName).build();
            }
        } else {
            code.addStatement("case $S: return (Subject<T>) $N", "n_" + element.getSimpleName().toString(), fieldName).build();
        }
        code.addStatement("case $S: return (Subject<T>) $N", element.getSimpleName().toString(), fieldName).build();
        return code.build();
    }
}
