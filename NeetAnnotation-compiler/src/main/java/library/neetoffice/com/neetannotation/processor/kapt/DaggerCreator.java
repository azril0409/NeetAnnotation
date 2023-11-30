package library.neetoffice.com.neetannotation.processor.kapt;

import com.squareup.javapoet.AnnotationSpec;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;

import java.util.HashSet;
import java.util.Set;

import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;

import library.neetoffice.com.neetannotation.InjectInitialEntity;
import library.neetoffice.com.neetannotation.Published;
import library.neetoffice.com.neetannotation.processor.AndroidClass;
import library.neetoffice.com.neetannotation.processor.DaggerClass;

public class DaggerCreator extends BaseCreator {
    static final String MODULES = "modules";
    private final MainProcessor mainProcessor;
    private final Set<TypeName> typenames;

    public DaggerCreator(MainProcessor processor, ProcessingEnvironment processingEnv) {
        super(processor, processingEnv);
        mainProcessor = processor;
        typenames = new HashSet<>();
    }

    void addLocalModule(TypeElement moduleElement) {
        TypeName typename = getClassName(moduleElement.asType());
        typenames.add(typename);
    }

    @Override
    void process(TypeElement nElement, RoundEnvironment roundEnv) {
        typenames.add(mainProcessor.contextModule);
        typenames.add(mainProcessor.systemModule);
        if (DaggerHelp.process(this, nElement)) {
            createComponent(nElement, roundEnv);
        }
    }

    void createComponent(TypeElement daggerElement, RoundEnvironment roundEnv) {
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
        if (isSubApplication(daggerElement) ||
                isSubActivity(daggerElement) ||
                isSubFragment(daggerElement) ||
                isSubAndroidViewModel(daggerElement) ||
                isSubService(daggerElement)) {
            for (TypeName typeName : typenames) {
                ab.addMember(MODULES, "$L", typeName + ".class");
            }
        } else if (haveActivityParameterInConstructor || haveApplicationParameterInConstructor || haveContextParameterInConstructor) {
            for (TypeName typeName : typenames) {
                ab.addMember(MODULES, "$L", typeName + ".class");
            }
        } else {
            for (TypeName typeName : typenames) {
                ab.addMember(MODULES, "$L", typeName + ".class");
            }
        }
        final String interfaceName = "_" + daggerElement.getSimpleName();
        final TypeSpec.Builder tb = TypeSpec.interfaceBuilder(interfaceName)
                .addModifiers(Modifier.PUBLIC)
                .addAnnotation(ab.build())
                .addAnnotation(DaggerClass.Singleton);
        tb.addMethod(createInjectMethod(daggerElement));

        for (Element element : roundEnv.getElementsAnnotatedWith(InjectInitialEntity.class)) {
            if (isInstanceOf(daggerElement.asType(), TypeName.get(element.getEnclosingElement().asType()))) {
                if (element.getAnnotation(Published.class) != null) {
                    tb.addMethod(createGetEntity(element));
                }
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
        final Element entityElement = mainProcessor.interactorCreator.interactElements.get(subjectElement.asType().toString());
        final TypeName entityType;
        if (entityElement == null) {
            final String typeString = subjectElement.asType().toString();
            entityType = ClassName.bestGuess(typeString.substring(0, typeString.length() - mainProcessor.interactorCreator.INTERACTOR.length()));
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

    void createContextModule(String packageName) {
        final TypeSpec.Builder contextModuleBuilder = TypeSpec.classBuilder(ClassName.get(packageName, AndroidClass.CONTEXT_MODULE_NAME))
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
                .beginControlFlow("if(context instanceof $T)", AndroidClass.Application)
                .addStatement("this.application = ($T)context", AndroidClass.Application)
                .nextControlFlow("else")
                .addStatement("this.application = null")
                .endControlFlow()
                .beginControlFlow("if(context instanceof $T)", AndroidClass.Activity)
                .addStatement("this.activity = ($T)context", AndroidClass.Activity)
                .nextControlFlow("else")
                .addStatement("this.activity = null")
                .endControlFlow()
                .addStatement("this.fragment = null")
                .beginControlFlow("if(context instanceof $T)", AndroidClass.Service)
                .addStatement("this.service = ($T)context", AndroidClass.Service)
                .nextControlFlow("else")
                .addStatement("this.service = null")
                .endControlFlow()
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
        writeTo(packageName, contextModuleBuilder.build());
    }

    void createSystemModule(String packageName) {
        final TypeSpec.Builder contextModuleBuilder = TypeSpec.classBuilder(ClassName.get(packageName, AndroidClass.SYSTEM_MODULE_NAME))
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                .addAnnotation(DaggerClass.Module);
        contextModuleBuilder.addMethod(MethodSpec.methodBuilder("window")
                .addModifiers(Modifier.PUBLIC)
                .addParameter(AndroidClass.Context, "context")
                .addAnnotation(DaggerClass.Provides)
                .returns(AndroidClass.WindowManager)
                .addStatement("return ($T)context.getSystemService(Context.WINDOW_SERVICE)", AndroidClass.WindowManager)
                .build());

        contextModuleBuilder.addMethod(MethodSpec.methodBuilder("layout_inflater")
                .addModifiers(Modifier.PUBLIC)
                .addParameter(AndroidClass.Context, "context")
                .addAnnotation(DaggerClass.Provides)
                .returns(AndroidClass.LayoutInflater)
                .addStatement("return ($T)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)", AndroidClass.LayoutInflater)
                .build());

        contextModuleBuilder.addMethod(MethodSpec.methodBuilder("activity")
                .addModifiers(Modifier.PUBLIC)
                .addParameter(AndroidClass.Context, "context")
                .addAnnotation(DaggerClass.Provides)
                .returns(AndroidClass.ActivityManager)
                .addStatement("return ($T)context.getSystemService(Context.ACTIVITY_SERVICE)", AndroidClass.ActivityManager)
                .build());

        contextModuleBuilder.addMethod(MethodSpec.methodBuilder("power")
                .addModifiers(Modifier.PUBLIC)
                .addParameter(AndroidClass.Context, "context")
                .addAnnotation(DaggerClass.Provides)
                .returns(AndroidClass.PowerManager)
                .addStatement("return ($T)context.getSystemService(Context.POWER_SERVICE)", AndroidClass.PowerManager)
                .build());

        contextModuleBuilder.addMethod(MethodSpec.methodBuilder("alarm")
                .addModifiers(Modifier.PUBLIC)
                .addParameter(AndroidClass.Context, "context")
                .addAnnotation(DaggerClass.Provides)
                .returns(AndroidClass.AlarmManager)
                .addStatement("return ($T)context.getSystemService(Context.ALARM_SERVICE)", AndroidClass.AlarmManager)
                .build());

        contextModuleBuilder.addMethod(MethodSpec.methodBuilder("notification")
                .addModifiers(Modifier.PUBLIC)
                .addParameter(AndroidClass.Context, "context")
                .addAnnotation(DaggerClass.Provides)
                .returns(AndroidClass.NotificationManager)
                .addStatement("return ($T)context.getSystemService(Context.NOTIFICATION_SERVICE)", AndroidClass.NotificationManager)
                .build());

        contextModuleBuilder.addMethod(MethodSpec.methodBuilder("keyguard")
                .addModifiers(Modifier.PUBLIC)
                .addParameter(AndroidClass.Context, "context")
                .addAnnotation(DaggerClass.Provides)
                .returns(AndroidClass.KeyguardManager)
                .addStatement("return ($T)context.getSystemService(Context.KEYGUARD_SERVICE)", AndroidClass.KeyguardManager)
                .build());

        contextModuleBuilder.addMethod(MethodSpec.methodBuilder("location")
                .addModifiers(Modifier.PUBLIC)
                .addParameter(AndroidClass.Context, "context")
                .addAnnotation(DaggerClass.Provides)
                .returns(AndroidClass.LocationManager)
                .addStatement("return ($T)context.getSystemService(Context.LOCATION_SERVICE)", AndroidClass.LocationManager)
                .build());

        contextModuleBuilder.addMethod(MethodSpec.methodBuilder("search")
                .addModifiers(Modifier.PUBLIC)
                .addParameter(AndroidClass.Context, "context")
                .addAnnotation(DaggerClass.Provides)
                .returns(AndroidClass.SearchManager)
                .addStatement("return ($T)context.getSystemService(Context.SEARCH_SERVICE)", AndroidClass.SearchManager)
                .build());

        contextModuleBuilder.addMethod(MethodSpec.methodBuilder("sensor")
                .addModifiers(Modifier.PUBLIC)
                .addParameter(AndroidClass.Context, "context")
                .addAnnotation(DaggerClass.Provides)
                .returns(AndroidClass.SensorManager)
                .addStatement("return ($T)context.getSystemService(Context.SENSOR_SERVICE)", AndroidClass.SensorManager)
                .build());

        contextModuleBuilder.addMethod(MethodSpec.methodBuilder("storage")
                .addModifiers(Modifier.PUBLIC)
                .addParameter(AndroidClass.Context, "context")
                .addAnnotation(DaggerClass.Provides)
                .returns(AndroidClass.StorageManager)
                .addStatement("return ($T)context.getSystemService(Context.STORAGE_SERVICE)", AndroidClass.StorageManager)
                .build());

        contextModuleBuilder.addMethod(MethodSpec.methodBuilder("vibrator")
                .addModifiers(Modifier.PUBLIC)
                .addParameter(AndroidClass.Context, "context")
                .addAnnotation(DaggerClass.Provides)
                .returns(AndroidClass.Vibrator)
                .addStatement("return ($T)context.getSystemService(Context.VIBRATOR_SERVICE)", AndroidClass.Vibrator)
                .build());

        contextModuleBuilder.addMethod(MethodSpec.methodBuilder("connectivity")
                .addModifiers(Modifier.PUBLIC)
                .addParameter(AndroidClass.Context, "context")
                .addAnnotation(DaggerClass.Provides)
                .returns(AndroidClass.ConnectivityManager)
                .addStatement("return ($T)context.getSystemService(Context.CONNECTIVITY_SERVICE)", AndroidClass.ConnectivityManager)
                .build());

        contextModuleBuilder.addMethod(MethodSpec.methodBuilder("wifi")
                .addModifiers(Modifier.PUBLIC)
                .addParameter(AndroidClass.Context, "context")
                .addAnnotation(DaggerClass.Provides)
                .returns(AndroidClass.WifiManager)
                .addStatement("return ($T)context.getSystemService(Context.WIFI_SERVICE)", AndroidClass.WifiManager)
                .build());

        contextModuleBuilder.addMethod(MethodSpec.methodBuilder("audio")
                .addModifiers(Modifier.PUBLIC)
                .addParameter(AndroidClass.Context, "context")
                .addAnnotation(DaggerClass.Provides)
                .returns(AndroidClass.AudioManager)
                .addStatement("return ($T)context.getSystemService(Context.AUDIO_SERVICE)", AndroidClass.AudioManager)
                .build());

        contextModuleBuilder.addMethod(MethodSpec.methodBuilder("media_router")
                .addModifiers(Modifier.PUBLIC)
                .addParameter(AndroidClass.Context, "context")
                .addAnnotation(DaggerClass.Provides)
                .returns(AndroidClass.MediaRouter)
                .addStatement("return ($T)context.getSystemService(Context.MEDIA_ROUTER_SERVICE)", AndroidClass.MediaRouter)
                .build());

        contextModuleBuilder.addMethod(MethodSpec.methodBuilder("phone")
                .addModifiers(Modifier.PUBLIC)
                .addParameter(AndroidClass.Context, "context")
                .addAnnotation(DaggerClass.Provides)
                .returns(AndroidClass.TelephonyManager)
                .addStatement("return ($T)context.getSystemService(Context.TELEPHONY_SERVICE)", AndroidClass.TelephonyManager)
                .build());

        contextModuleBuilder.addMethod(MethodSpec.methodBuilder("telephony_subscription_service")
                .addModifiers(Modifier.PUBLIC)
                .addParameter(AndroidClass.Context, "context")
                .addAnnotation(DaggerClass.Provides)
                .returns(AndroidClass.SubscriptionManager)
                .addStatement("return ($T)context.getSystemService(Context.TELEPHONY_SUBSCRIPTION_SERVICE)", AndroidClass.SubscriptionManager)
                .build());

        contextModuleBuilder.addMethod(MethodSpec.methodBuilder("carrier_config")
                .addModifiers(Modifier.PUBLIC)
                .addParameter(AndroidClass.Context, "context")
                .addAnnotation(DaggerClass.Provides)
                .returns(AndroidClass.CarrierConfigManager)
                .addStatement("return ($T)context.getSystemService(Context.CARRIER_CONFIG_SERVICE)", AndroidClass.CarrierConfigManager)
                .build());

        contextModuleBuilder.addMethod(MethodSpec.methodBuilder("input_method")
                .addModifiers(Modifier.PUBLIC)
                .addParameter(AndroidClass.Context, "context")
                .addAnnotation(DaggerClass.Provides)
                .returns(AndroidClass.InputMethodManager)
                .addStatement("return ($T)context.getSystemService(Context.INPUT_METHOD_SERVICE)", AndroidClass.InputMethodManager)
                .build());

        contextModuleBuilder.addMethod(MethodSpec.methodBuilder("uimode")
                .addModifiers(Modifier.PUBLIC)
                .addParameter(AndroidClass.Context, "context")
                .addAnnotation(DaggerClass.Provides)
                .returns(AndroidClass.UiModeManager)
                .addStatement("return ($T)context.getSystemService(Context.UI_MODE_SERVICE)", AndroidClass.UiModeManager)
                .build());

        contextModuleBuilder.addMethod(MethodSpec.methodBuilder("download")
                .addModifiers(Modifier.PUBLIC)
                .addParameter(AndroidClass.Context, "context")
                .addAnnotation(DaggerClass.Provides)
                .returns(AndroidClass.DownloadManager)
                .addStatement("return ($T)context.getSystemService(Context.DOWNLOAD_SERVICE)", AndroidClass.DownloadManager)
                .build());

        contextModuleBuilder.addMethod(MethodSpec.methodBuilder("batterymanager")
                .addModifiers(Modifier.PUBLIC)
                .addParameter(AndroidClass.Context, "context")
                .addAnnotation(DaggerClass.Provides)
                .returns(AndroidClass.BatteryManager)
                .addStatement("return ($T)context.getSystemService(Context.BATTERY_SERVICE)", AndroidClass.BatteryManager)
                .build());

        contextModuleBuilder.addMethod(MethodSpec.methodBuilder("jobscheduler")
                .addModifiers(Modifier.PUBLIC)
                .addParameter(AndroidClass.Context, "context")
                .addAnnotation(DaggerClass.Provides)
                .returns(AndroidClass.JobScheduler)
                .addStatement("return ($T)context.getSystemService(Context.JOB_SCHEDULER_SERVICE)", AndroidClass.JobScheduler)
                .build());

        contextModuleBuilder.addMethod(MethodSpec.methodBuilder("netstats")
                .addModifiers(Modifier.PUBLIC)
                .addParameter(AndroidClass.Context, "context")
                .addAnnotation(DaggerClass.Provides)
                .returns(AndroidClass.NetworkStatsManager)
                .addStatement("return ($T)context.getSystemService(Context.NETWORK_STATS_SERVICE)", AndroidClass.NetworkStatsManager)
                .build());

        contextModuleBuilder.addMethod(MethodSpec.methodBuilder("hardware_properties")
                .addModifiers(Modifier.PUBLIC)
                .addParameter(AndroidClass.Context, "context")
                .addAnnotation(DaggerClass.Provides)
                .returns(AndroidClass.HardwarePropertiesManager)
                .addStatement("return ($T)context.getSystemService(Context.HARDWARE_PROPERTIES_SERVICE)", AndroidClass.HardwarePropertiesManager)
                .build());

        contextModuleBuilder.addMethod(MethodSpec.methodBuilder("nfc")
                .addModifiers(Modifier.PUBLIC)
                .addParameter(AndroidClass.Context, "context")
                .addAnnotation(DaggerClass.Provides)
                .returns(AndroidClass.NfcManager)
                .addStatement("return ($T)context.getSystemService(Context.NFC_SERVICE)", AndroidClass.NfcManager)
                .build());

        contextModuleBuilder.addMethod(MethodSpec.methodBuilder("bluetooth")
                .addModifiers(Modifier.PUBLIC)
                .addParameter(AndroidClass.Context, "context")
                .addAnnotation(DaggerClass.Provides)
                .returns(AndroidClass.BluetoothManager)
                .addStatement("return ($T)context.getSystemService(Context.BLUETOOTH_SERVICE)", AndroidClass.BluetoothManager)
                .build());

        contextModuleBuilder.addMethod(MethodSpec.methodBuilder("usb")
                .addModifiers(Modifier.PUBLIC)
                .addParameter(AndroidClass.Context, "context")
                .addAnnotation(DaggerClass.Provides)
                .returns(AndroidClass.UsbManager)
                .addStatement("return ($T)context.getSystemService(Context.USB_SERVICE)", AndroidClass.UsbManager)
                .build());

        writeTo(packageName, contextModuleBuilder.build());
    }
}
