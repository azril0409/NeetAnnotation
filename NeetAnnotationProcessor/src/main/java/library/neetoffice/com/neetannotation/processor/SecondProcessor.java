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
import library.neetoffice.com.neetannotation.NActivity;
import library.neetoffice.com.neetannotation.NDagger;
import library.neetoffice.com.neetannotation.NFragment;
import library.neetoffice.com.neetannotation.NService;
import library.neetoffice.com.neetannotation.NView;
import library.neetoffice.com.neetannotation.NViewModel;

@SupportedAnnotationTypes({
        "library.neetoffice.com.neetannotation.NActivity",
        "library.neetoffice.com.neetannotation.NFragment",
        "library.neetoffice.com.neetannotation.NView",
        "library.neetoffice.com.neetannotation.NService"})
@AutoService(Process.class)
public class SecondProcessor extends AbstractProcessor {
    private static final String APPLY = "apply";
    private static final String APPLICATION = "application";
    private static final String LIBRARY = "library";
    private static final String PACKAGE_NAME = "packageName";
    ActivityCreator activityCreator;
    FragmentCreator fragmentCreator;
    ViewCreator viewCreator;
    ServiceCreator serviceCreator;
    String contextPackageName = "com.neetoffice.neetannotation";
    ClassName contextModule;

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.RELEASE_8;
    }

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        activityCreator = new ActivityCreator(this, processingEnv);
        fragmentCreator = new FragmentCreator(this, processingEnv);
        viewCreator = new ViewCreator(this, processingEnv);
        serviceCreator = new ServiceCreator(this, processingEnv);
        final Map<String, String> options = processingEnv.getOptions();
        if (options.containsKey(APPLY)) {
            final String apply = options.get(APPLY);
        }
        if (options.containsKey(PACKAGE_NAME)) {
            final String packageName = options.get(PACKAGE_NAME);
            contextPackageName = packageName + ".support";
        }
        contextModule = ClassName.get(contextPackageName, AndroidClass.CONTEXT_MODULE_NAME);
    }

    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnv) {
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
