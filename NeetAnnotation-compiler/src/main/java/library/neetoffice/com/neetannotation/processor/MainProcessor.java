package library.neetoffice.com.neetannotation.processor;

import com.google.auto.service.AutoService;
import com.squareup.javapoet.ClassName;

import java.util.Map;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;

import library.neetoffice.com.neetannotation.Interactor;
import library.neetoffice.com.neetannotation.ListInteractor;
import library.neetoffice.com.neetannotation.NActivity;
import library.neetoffice.com.neetannotation.NApplication;
import library.neetoffice.com.neetannotation.NDagger;
import library.neetoffice.com.neetannotation.NFragment;
import library.neetoffice.com.neetannotation.NService;
import library.neetoffice.com.neetannotation.NView;
import library.neetoffice.com.neetannotation.NViewModel;
import library.neetoffice.com.neetannotation.SetInteractor;

@SupportedAnnotationTypes({
        "library.neetoffice.com.neetannotation.Interactor",
        "library.neetoffice.com.neetannotation.NDagger",
        "library.neetoffice.com.neetannotation.NViewModel"})
@AutoService(Process.class)
public class MainProcessor extends AbstractProcessor {
    private static final String APPLY = "apply";
    private static final String APPLICATION = "application";
    private static final String LIBRARY = "library";
    private static final String PACKAGE_NAME = "packageName";
    ContextInteractorCreator contextInteractorCreator;
    InteractorCreator interactorCreator;
    ListInteractorCreator listInteractorCreator;
    SetInteractorCreator setInteractorCreator;
    ViewModelStoreOwnerCreator viewModelStoreOwnerCreator;
    DaggerCreator daggerCreator;
    ViewModelCreator viewModelCreator;
    String contextPackageName = "com.neetoffice.neetannotation";
    ClassName contextModule;
    ClassName systemModule;

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.RELEASE_8;
    }

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        contextInteractorCreator = new ContextInteractorCreator(this, processingEnv);
        interactorCreator = new InteractorCreator(this, processingEnv);
        listInteractorCreator = new ListInteractorCreator(this, processingEnv);
        setInteractorCreator = new SetInteractorCreator(this, processingEnv);
        viewModelStoreOwnerCreator = new ViewModelStoreOwnerCreator(this, processingEnv);
        daggerCreator = new DaggerCreator(this, processingEnv);
        viewModelCreator = new ViewModelCreator(this, processingEnv);
        final Map<String, String> options = processingEnv.getOptions();
        if (options.containsKey(APPLY)) {
            final String apply = options.get(APPLY);
        }
        if (options.containsKey(PACKAGE_NAME)) {
            final String packageName = options.get(PACKAGE_NAME);
            contextPackageName = packageName + ".support";
        }
        contextModule = ClassName.get(contextPackageName, AndroidClass.CONTEXT_MODULE_NAME);
        systemModule = ClassName.get(contextPackageName, AndroidClass.SYSTEM_MODULE_NAME);
    }

    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnv) {
        contextInteractorCreator.createModule(contextPackageName);
        daggerCreator.createContextModule(contextPackageName);
        viewModelCreator.createContextModule(contextPackageName);
        daggerCreator.createSystemModule(contextPackageName);
        viewModelStoreOwnerCreator.createSimpleViewModelStoreOwner(contextPackageName);
        viewModelStoreOwnerCreator.createApplaction(contextPackageName);

        final Set<? extends Element> applications = roundEnv.getElementsAnnotatedWith(NApplication.class);
        for (Element application : applications) {
            viewModelStoreOwnerCreator.process((TypeElement) application, roundEnv);
        }
        final Set<? extends Element> interacts = roundEnv.getElementsAnnotatedWith(Interactor.class);
        for (Element interact : interacts) {
            interactorCreator.process((TypeElement) interact, roundEnv);
        }
        final Set<? extends Element> listInteracts = roundEnv.getElementsAnnotatedWith(ListInteractor.class);
        for (Element interact : listInteracts) {
            listInteractorCreator.process((TypeElement) interact, roundEnv);
        }
        final Set<? extends Element> setInteracts = roundEnv.getElementsAnnotatedWith(SetInteractor.class);
        for (Element interact : setInteracts) {
            setInteractorCreator.process((TypeElement) interact, roundEnv);
        }

        final Set<? extends Element> nDaggers = roundEnv.getElementsAnnotatedWith(NDagger.class);
        for (Element nDagger : nDaggers) {
            daggerCreator.process((TypeElement) nDagger, roundEnv);
        }
        final Set<? extends Element> nViewModels = roundEnv.getElementsAnnotatedWith(NViewModel.class);
        for (Element nViewModel : nViewModels) {
            viewModelCreator.process((TypeElement) nViewModel, roundEnv);
        }
        return true;
    }
}