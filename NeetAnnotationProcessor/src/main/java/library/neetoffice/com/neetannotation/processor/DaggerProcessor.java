package library.neetoffice.com.neetannotation.processor;


import com.google.auto.service.AutoService;
import com.squareup.javapoet.AnnotationSpec;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;

import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;

import library.neetoffice.com.neetannotation.NDagger;
import library.neetoffice.com.neetannotation.Subject;
import library.neetoffice.com.neetannotation.NullEntity;

@SupportedAnnotationTypes({
        "library.neetoffice.com.neetannotation.NDagger"})
@AutoService(Process.class)
public class DaggerProcessor extends AbstractProcessor {

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
        createContextModule();
        final Set<? extends Element> elements = roundEnv.getElementsAnnotatedWith(NDagger.class);
        for (Element mainElement : elements) {
            final AnnotationMirror annotationMirror = AnnotationHelp.findAnnotationMirror(mainElement, NDagger.class);
            final Object modulesValue = AnnotationHelp.findAnnotationValue(annotationMirror, "modules");
            if (modulesValue == null) {
                return false;
            }
            final AnnotationSpec.Builder ab = AnnotationSpec.builder(DaggerClass.Component);
            if (processorUtil.isSubActivity((TypeElement) mainElement)) {
                ab.addMember("modules", "$L", AndroidClass.CONTEXT_MODULE + ".class")
                        .addMember("modules", "$L", modulesValue);
            } else if (processorUtil.isSubFragment((TypeElement) mainElement)) {
                ab.addMember("modules", "$L", AndroidClass.CONTEXT_MODULE + ".class")
                        .addMember("modules", "$L", modulesValue);
            } else if (processorUtil.isSubAndroidViewModel((TypeElement) mainElement)) {
                ab.addMember("modules", "$L", AndroidClass.CONTEXT_MODULE + ".class")
                        .addMember("modules", "$L", modulesValue);
            } else {
                ab.addMember("modules", "{$L}", modulesValue);
            }

            final String interfaceName = "_" + mainElement.getSimpleName();
            final TypeSpec.Builder tb = TypeSpec.interfaceBuilder(interfaceName)
                    .addModifiers(Modifier.PUBLIC)
                    .addAnnotation(ab.build())
                    .addAnnotation(DaggerClass.Singleton);

            final MethodSpec.Builder inject = MethodSpec.methodBuilder("inject")
                    .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
                    .addParameter(processorUtil.getClassName(mainElement.asType()), processorUtil.toModelCase(mainElement.getSimpleName()))
                    .returns(void.class);
            tb.addMethod(inject.build());
            for (Element element : mainElement.getEnclosedElements()) {
                if (element.getAnnotation(Subject.class) != null && element.getAnnotation(NullEntity.class) == null) {
                    tb.addMethod(createGetEntity(element));
                }
            }
            final JavaFile javaFile = JavaFile.builder(processorUtil.getPackageName(mainElement), tb.build()).build();
            processorUtil.writeTo(javaFile);
        }
        return true;
    }

    private void createContextModule() {
        final TypeSpec.Builder contextModuleBuilder = TypeSpec.classBuilder(AndroidClass.CONTEXT_MODULE)
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                .addAnnotation(DaggerClass.Module);
        contextModuleBuilder.addField(FieldSpec.
                builder(AndroidClass.Application, "application")
                .addModifiers(Modifier.PRIVATE, Modifier.FINAL)
                .build());
        contextModuleBuilder.addField(FieldSpec.
                builder(AndroidClass.Activity, "activity")
                .addModifiers(Modifier.PRIVATE, Modifier.FINAL)
                .build());
        contextModuleBuilder.addField(FieldSpec.
                builder(AndroidClass.Fragment, "fragment")
                .addModifiers(Modifier.PRIVATE, Modifier.FINAL)
                .build());
        contextModuleBuilder.addField(FieldSpec.
                builder(AndroidClass.Fragment_v4, "fragment_v4")
                .addModifiers(Modifier.PRIVATE, Modifier.FINAL)
                .build());
        contextModuleBuilder.addMethod(MethodSpec.constructorBuilder()
                .addModifiers(Modifier.PUBLIC)
                .addParameter(AndroidClass.Application, "application")
                .addStatement("this.application = application")
                .addStatement("this.activity = null")
                .addStatement("this.fragment = null")
                .addStatement("this.fragment_v4 = null")
                .build());
        contextModuleBuilder.addMethod(MethodSpec.constructorBuilder()
                .addModifiers(Modifier.PUBLIC)
                .addParameter(AndroidClass.Activity, "activity")
                .addStatement("this.application = null")
                .addStatement("this.activity = activity")
                .addStatement("this.fragment = null")
                .addStatement("this.fragment_v4 = null")
                .build());
        contextModuleBuilder.addMethod(MethodSpec.constructorBuilder()
                .addModifiers(Modifier.PUBLIC)
                .addParameter(AndroidClass.Fragment, "fragment")
                .addStatement("this.application = null")
                .addStatement("this.activity = null")
                .addStatement("this.fragment = fragment")
                .addStatement("this.fragment_v4 = null")
                .build());
        contextModuleBuilder.addMethod(MethodSpec.constructorBuilder()
                .addModifiers(Modifier.PUBLIC)
                .addParameter(AndroidClass.Fragment_v4, "fragment_v4")
                .addStatement("this.application = null")
                .addStatement("this.activity = null")
                .addStatement("this.fragment = null")
                .addStatement("this.fragment_v4 = fragment_v4")
                .build());
        contextModuleBuilder.addMethod(MethodSpec.methodBuilder("application")
                .addModifiers(Modifier.PUBLIC)
                .addAnnotation(DaggerClass.Provides)
                .returns(AndroidClass.Application)
                .addStatement("return application")
                .build());
        contextModuleBuilder.addMethod(MethodSpec.methodBuilder("activity")
                .addModifiers(Modifier.PUBLIC)
                .addAnnotation(DaggerClass.Provides)
                .returns(AndroidClass.Activity)
                .addStatement("return activity")
                .build());
        contextModuleBuilder.addMethod(MethodSpec.methodBuilder("fragment")
                .addModifiers(Modifier.PUBLIC)
                .addAnnotation(DaggerClass.Provides)
                .returns(AndroidClass.Fragment)
                .addStatement("return fragment")
                .build());
        contextModuleBuilder.addMethod(MethodSpec.methodBuilder("fragment_v4")
                .addModifiers(Modifier.PUBLIC)
                .addAnnotation(DaggerClass.Provides)
                .returns(AndroidClass.Fragment_v4)
                .addStatement("return fragment_v4")
                .build());
        final JavaFile javaFile = JavaFile.builder("com.neetoffice.neetannotation", contextModuleBuilder.build()).build();
        processorUtil.writeTo(javaFile);
    }

    private MethodSpec createGetEntity(Element element) {
        final AnnotationMirror aNamed = AnnotationHelp.findAnnotationMirror(element, DaggerClass.Named);
        final String methodName = daggerHelp.createDaggerMethodName(element);
        final String classSimpleName = element.asType().toString();
        final String simpleName = classSimpleName.substring(0, classSimpleName.length() - "Interactor".length());
        final TypeName entityTypeName = ClassName.get(processorUtil.getPackageName(element), simpleName);
        final MethodSpec.Builder getEntity = MethodSpec.methodBuilder(methodName)
                .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
                .returns(entityTypeName);
        if (aNamed != null) {
            final Object value = AnnotationHelp.findAnnotationValue(aNamed, "value");
            if (value != null) {
                getEntity.addAnnotation(AnnotationSpec.builder(DaggerClass.Named).addMember("value", "$S", value.toString()).build());
            }
        }
        return getEntity.build();
    }
}
