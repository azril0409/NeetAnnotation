package library.neetoffice.com.neetannotation.processor;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;
import com.squareup.javapoet.TypeVariableName;

import java.util.HashMap;

import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;

import static library.neetoffice.com.neetannotation.processor.InteractorCreator.ACCEPT;
import static library.neetoffice.com.neetannotation.processor.InteractorCreator.ENTITY;
import static library.neetoffice.com.neetannotation.processor.InteractorCreator.ENTITY_FIELD_NAME;
import static library.neetoffice.com.neetannotation.processor.InteractorCreator.INTERACTOR;
import static library.neetoffice.com.neetannotation.processor.InteractorCreator.INTERACTOR_;
import static library.neetoffice.com.neetannotation.processor.InteractorCreator.NOTIFY_DATA_SET_CHANGED;
import static library.neetoffice.com.neetannotation.processor.InteractorCreator.OBSERVABLE;
import static library.neetoffice.com.neetannotation.processor.InteractorCreator.SUBJECT_FIELD_NAME;
import static library.neetoffice.com.neetannotation.processor.InteractorCreator.SUBSCRIBE;
import static library.neetoffice.com.neetannotation.processor.InteractorCreator.UPDATE;

public class ContextInteractorCreator extends BaseCreator {

    public ContextInteractorCreator(MainProcessor processor, ProcessingEnvironment processingEnv) {
        super(processor, processingEnv);
    }

    @Override
    void process(TypeElement element, RoundEnvironment roundEnv) {

    }

    void createModule(String packageName) {
        createBaseInteractor(packageName, String.class);
        createBaseInteractor(packageName, Throwable.class);
        createBaseInteractor(packageName, Exception.class);
        createBaseInteractor(packageName, Byte.class);
        createBaseInteractor(packageName, Short.class);
        createBaseInteractor(packageName, Integer.class);
        createBaseInteractor(packageName, Long.class);
        createBaseInteractor(packageName, Float.class);
        createBaseInteractor(packageName, Double.class);
        createBaseInteractor(packageName, Character.class);
        createBaseInteractor(packageName, Boolean.class);
    }

    private void createBaseInteractor(String packageName, Class cls) {
        final ClassName stringClassName = ClassName.get(cls);
        final InteractorCreator.InteractBuild stringBuild = InteractorCreator.createInterface(this, packageName, stringClassName, new HashMap(), new HashMap(), null);
        InteractorCreator.createImplement(this, packageName, stringClassName, stringBuild, false, null);
        InteractorCreator.createImplement(this, packageName, stringClassName, stringBuild, true, null);
        final ListInteractorCreator.InteractBuild stringListBuild = ListInteractorCreator.createInterface(this, packageName, stringClassName, new HashMap());
        ListInteractorCreator.createImplement(this, packageName, stringClassName, stringListBuild, false);
        ListInteractorCreator.createImplement(this, packageName, stringClassName, stringListBuild, true);
        final SetInteractorCreator.InteractBuild stringSetBuild = SetInteractorCreator.createInterface(this, packageName, stringClassName, new HashMap());
        SetInteractorCreator.createImplement(this, packageName, stringClassName, stringSetBuild, false);
        SetInteractorCreator.createImplement(this, packageName, stringClassName, stringSetBuild, true);
    }
}
