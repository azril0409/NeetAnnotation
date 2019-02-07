package library.neetoffice.com.neetannotation.processor;

import com.google.auto.service.AutoService;

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
import library.neetoffice.com.neetannotation.NActivity;
import library.neetoffice.com.neetannotation.NDagger;
import library.neetoffice.com.neetannotation.NFragment;
import library.neetoffice.com.neetannotation.NService;
import library.neetoffice.com.neetannotation.NView;
import library.neetoffice.com.neetannotation.NViewModel;

@SupportedAnnotationTypes({
        "library.neetoffice.com.neetannotation.Interactor",
        "library.neetoffice.com.neetannotation.NDagger",
        "library.neetoffice.com.neetannotation.NViewModel",
        "library.neetoffice.com.neetannotation.NActivity",
        "library.neetoffice.com.neetannotation.NFragment",
        "library.neetoffice.com.neetannotation.NView",
        "library.neetoffice.com.neetannotation.NService"})
@AutoService(Process.class)
public class MainProcessor extends AbstractProcessor {
    private static final String APPLY = "apply";
    private static final String APPLICATION = "application";
    private static final String LIBRARY = "library";
    InteractorCreator interactorCreator;
    DaggerCreator daggerCreator;
    ViewModelCreator viewModelCreator;
    ActivityCreator activityCreator;
    FragmentCreator fragmentCreator;
    ViewCreator viewCreator;
    ServiceCreator serviceCreator;
    boolean createContextModule = true;

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.RELEASE_8;
    }

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        interactorCreator = new InteractorCreator(this, processingEnv);
        daggerCreator = new DaggerCreator(this, processingEnv);
        viewModelCreator = new ViewModelCreator(this, processingEnv);
        activityCreator = new ActivityCreator(this, processingEnv);
        fragmentCreator = new FragmentCreator(this, processingEnv);
        viewCreator = new ViewCreator(this, processingEnv);
        serviceCreator = new ServiceCreator(this, processingEnv);
        final Map<String, String> options = processingEnv.getOptions();
        if (options.containsKey(APPLY)) {
            final String apply = options.get(APPLY);
            createContextModule = APPLICATION.equals(apply);
        }
    }

    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnv) {
        final Set<? extends Element> interacts = roundEnv.getElementsAnnotatedWith(Interactor.class);
        for (Element interact : interacts) {
            interactorCreator.process((TypeElement) interact, roundEnv);
        }
        if (createContextModule) {
            daggerCreator.createContextModule();
        }
        final Set<? extends Element> nDaggers = roundEnv.getElementsAnnotatedWith(NDagger.class);
        for (Element nDagger : nDaggers) {
            daggerCreator.process((TypeElement) nDagger, roundEnv);
        }
        final Set<? extends Element> nViewModels = roundEnv.getElementsAnnotatedWith(NViewModel.class);
        for (Element nViewModel : nViewModels) {
            viewModelCreator.process((TypeElement) nViewModel, roundEnv);
        }
        final Set<? extends Element> nActivitys = roundEnv.getElementsAnnotatedWith(NActivity.class);
        for (Element nActivity : nActivitys) {
            activityCreator.process((TypeElement) nActivity, roundEnv);
        }
        final Set<? extends Element> nFragments = roundEnv.getElementsAnnotatedWith(NFragment.class);
        for (Element nFragment : nFragments) {
            fragmentCreator.process((TypeElement) nFragment, roundEnv);
        }
        final Set<? extends Element> nViews = roundEnv.getElementsAnnotatedWith(NView.class);
        for (Element nView : nViews) {
            viewCreator.process((TypeElement) nView, roundEnv);
        }
        final Set<? extends Element> nServices = roundEnv.getElementsAnnotatedWith(NService.class);
        for (Element nService : nServices) {
            serviceCreator.process((TypeElement) nService, roundEnv);
        }
        return true;
    }
}
