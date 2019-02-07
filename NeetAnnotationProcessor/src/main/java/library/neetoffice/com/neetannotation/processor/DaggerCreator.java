package library.neetoffice.com.neetannotation.processor;

import com.squareup.javapoet.AnnotationSpec;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;

import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.tools.Diagnostic;

import library.neetoffice.com.neetannotation.InjectEntity;
import library.neetoffice.com.neetannotation.NDagger;
import library.neetoffice.com.neetannotation.Subject;

public class DaggerCreator extends BaseCreator {
    static final String MODULES = "modules";

    public DaggerCreator(MainProcessor processor, ProcessingEnvironment processingEnv) {
        super(processor, processingEnv);
    }

    @Override
    void process(TypeElement daggerElement, RoundEnvironment roundEnv) {
        final Object modulesValue = findAnnotationValue(daggerElement, NDagger.class, MODULES);
        final AnnotationSpec.Builder ab = AnnotationSpec.builder(DaggerClass.Component);
        boolean haveContextParameterInConstructor = false;
        boolean haveApplicationParameterInConstructor = false;
        boolean haveActivityParameterInConstructor = false;
        for (Element enclosedElement : daggerElement.getEnclosedElements()) {
            if (enclosedElement.getKind() == ElementKind.CONSTRUCTOR) {
                final ExecutableElement constructor = (ExecutableElement) enclosedElement;
                for (VariableElement parameter : constructor.getParameters()) {
                    if (isInstanceOf(parameter.asType(), AndroidClass.Application)) {
                        haveActivityParameterInConstructor = true;
                    }
                    if (isInstanceOf(parameter.asType(), AndroidClass.Activity)) {
                        haveActivityParameterInConstructor = true;
                    }
                    if (isInstanceOf(parameter.asType(), AndroidClass.Context)) {
                        haveContextParameterInConstructor = true;
                    }
                }
            }
        }
        if (isSubActivity(daggerElement) || isSubFragment(daggerElement) || isSubAndroidViewModel(daggerElement) || isSubService(daggerElement)) {
            ab.addMember(MODULES, "$L", AndroidClass.CONTEXT_MODULE + ".class")
                    .addMember(MODULES, "$L", modulesValue);
        } else if (haveActivityParameterInConstructor || haveApplicationParameterInConstructor || haveContextParameterInConstructor) {
            ab.addMember(MODULES, "$L", AndroidClass.CONTEXT_MODULE + ".class")
                    .addMember(MODULES, "$L", modulesValue);
        } else {
            ab.addMember("modules", "{$L}", modulesValue);
        }
        final String interfaceName = "_" + daggerElement.getSimpleName();
        final TypeSpec.Builder tb = TypeSpec.interfaceBuilder(interfaceName)
                .addModifiers(Modifier.PUBLIC)
                .addAnnotation(ab.build())
                .addAnnotation(DaggerClass.Singleton);
        tb.addMethod(createInjectMethod(daggerElement));

        for (Element element : daggerElement.getEnclosedElements()) {
            if (element.getAnnotation(Subject.class) != null &&
                    element.getAnnotation(InjectEntity.class) != null) {
                tb.addMethod(createGetEntity(element));
            }
        }
        writeTo(getPackageName(daggerElement), tb.build());
    }

    MethodSpec createInjectMethod(TypeElement daggerElement) {
        return MethodSpec.methodBuilder("inject")
                .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
                .addParameter(getClassName(daggerElement.asType()), toModelCase(daggerElement.getSimpleName()))
                .returns(void.class)
                .build();
    }

    private MethodSpec createGetEntity(Element subjectElement) {
        final Object aNamedValue = findAnnotationValue(subjectElement, DaggerClass.Named, "value");
        final Element entityElement = processor.interactorCreator.interactElements.get(subjectElement.asType().toString());
        final TypeName entityType;
        if (entityElement == null) {
            final String typeString = subjectElement.asType().toString();
            entityType = ClassName.bestGuess(typeString.substring(0, typeString.length() - processor.interactorCreator.INTERACTOR.length()));
        } else {
            entityType = getClassName(entityElement.asType());
        }
        final String methodName = DaggerHelp.findNameFromDagger(this, subjectElement);
        final MethodSpec.Builder getEntity = MethodSpec.methodBuilder(methodName)
                .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
                .returns(entityType);
        if (aNamedValue != null) {
            getEntity.addAnnotation(AnnotationSpec.builder(DaggerClass.Named)
                    .addMember("value", "$S", aNamedValue.toString())
                    .build());
        }
        return getEntity.build();
    }

    void createContextModule() {
        final TypeSpec.Builder contextModuleBuilder = TypeSpec.classBuilder(AndroidClass.CONTEXT_MODULE)
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                .addAnnotation(DaggerClass.Module);
        contextModuleBuilder.addField(FieldSpec.
                builder(AndroidClass.Context, "context")
                .addModifiers(Modifier.PRIVATE, Modifier.FINAL)
                .build());
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
                builder(AndroidClass.Service, "service")
                .addModifiers(Modifier.PRIVATE, Modifier.FINAL)
                .build());
        contextModuleBuilder.addMethod(MethodSpec.constructorBuilder()
                .addModifiers(Modifier.PUBLIC)
                .addParameter(AndroidClass.Context, "context")
                .addStatement("this.context = context")
                .addStatement("this.application = null")
                .addStatement("this.activity = null")
                .addStatement("this.fragment = null")
                .addStatement("this.service = null")
                .build());
        contextModuleBuilder.addMethod(MethodSpec.constructorBuilder()
                .addModifiers(Modifier.PUBLIC)
                .addParameter(AndroidClass.Application, "application")
                .addStatement("this.context = application")
                .addStatement("this.application = application")
                .addStatement("this.activity = null")
                .addStatement("this.fragment = null")
                .addStatement("this.service = null")
                .build());
        contextModuleBuilder.addMethod(MethodSpec.constructorBuilder()
                .addModifiers(Modifier.PUBLIC)
                .addParameter(AndroidClass.Activity, "activity")
                .addStatement("this.context = activity")
                .addStatement("this.application = activity.getApplication()")
                .addStatement("this.activity = activity")
                .addStatement("this.fragment = null")
                .addStatement("this.service = null")
                .build());
        contextModuleBuilder.addMethod(MethodSpec.constructorBuilder()
                .addModifiers(Modifier.PUBLIC)
                .addParameter(AndroidClass.Fragment, "fragment")
                .addStatement("this.context = fragment.getActivity()")
                .addStatement("this.application = fragment.getActivity().getApplication()")
                .addStatement("this.activity = fragment.getActivity()")
                .addStatement("this.fragment = fragment")
                .addStatement("this.service = null")
                .build());
        contextModuleBuilder.addMethod(MethodSpec.constructorBuilder()
                .addModifiers(Modifier.PUBLIC)
                .addParameter(AndroidClass.Service, "service")
                .addStatement("this.context = service")
                .addStatement("this.application = service.getApplication()")
                .addStatement("this.activity = null")
                .addStatement("this.fragment = null")
                .addStatement("this.service = service")
                .build());
        //
        contextModuleBuilder.addMethod(MethodSpec.methodBuilder("context")
                .addModifiers(Modifier.PUBLIC)
                .addAnnotation(DaggerClass.Provides)
                .returns(AndroidClass.Context)
                .addStatement("return context")
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
        contextModuleBuilder.addMethod(MethodSpec.methodBuilder("service")
                .addModifiers(Modifier.PUBLIC)
                .addAnnotation(DaggerClass.Provides)
                .returns(AndroidClass.Service)
                .addStatement("return service")
                .build());
        writeTo("com.neetoffice.neetannotation", contextModuleBuilder.build());
    }
}
