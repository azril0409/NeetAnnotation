package library.neetoffice.com.neetannotation.processor.kapt;

import com.squareup.javapoet.ClassName;

import java.util.HashMap;

import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.TypeElement;

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
        final InteractorCreator.InteractBuild stringBuild = InteractorCreator.createInterface(this, packageName, stringClassName, new HashMap<>(), new HashMap<>(), null);
        InteractorCreator.createImplement(this, packageName, stringClassName, stringBuild, false, null);
        InteractorCreator.createImplement(this, packageName, stringClassName, stringBuild, true, null);
        final ListInteractorCreator.InteractBuild stringListBuild = ListInteractorCreator.createInterface(this, packageName, stringClassName, new HashMap<>());
        ListInteractorCreator.createImplement(this, packageName, stringClassName, stringListBuild, false);
        ListInteractorCreator.createImplement(this, packageName, stringClassName, stringListBuild, true);
        final SetInteractorCreator.InteractBuild stringSetBuild = SetInteractorCreator.createInterface(this, packageName, stringClassName, new HashMap<>());
        SetInteractorCreator.createImplement(this, packageName, stringClassName, stringSetBuild, false);
        SetInteractorCreator.createImplement(this, packageName, stringClassName, stringSetBuild, true);
    }
}
