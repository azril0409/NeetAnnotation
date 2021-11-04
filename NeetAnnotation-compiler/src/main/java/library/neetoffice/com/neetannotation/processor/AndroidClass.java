package library.neetoffice.com.neetannotation.processor;

import com.squareup.javapoet.ClassName;

public class AndroidClass {
    public static final String CONTEXT_MODULE_NAME = "ContextModule";
    public static final String SYSTEM_MODULE_NAME = "SystemModule";
    public static final ClassName Application = ClassName.get("android.app", "Application");
    public static final ClassName Activity = ClassName.get("android.app", "Activity");
    public static final ClassName AnimationUtils = ClassName.get("android.view.animation", "AnimationUtils");
    public static final ClassName AdapterView = ClassName.get("android.widget", "AdapterView");
    public static final ClassName AdapterView_OnItemClickListener = ClassName.get("android.widget", "AdapterView", "OnItemClickListener");
    public static final ClassName AdapterView_OnItemLongClickListener = ClassName.get("android.widget", "AdapterView", "OnItemLongClickListener");
    public static final ClassName AdapterView_OnItemSelectedListener = ClassName.get("android.widget", "AdapterView", "OnItemSelectedListener");
    public static final ClassName AttributeSet = ClassName.get("android.util", "AttributeSet");
    public static final ClassName Bundle = ClassName.get("android.os", "Bundle");
    public static final ClassName CompoundButton = ClassName.get("android.widget", "CompoundButton");
    public static final ClassName CompoundButton_OnCheckedChangeListener = ClassName.get("android.widget", "CompoundButton", "OnCheckedChangeListener");
    public static final ClassName Context = ClassName.get("android.content", "Context");
    public static final ClassName Editable = ClassName.get("android.text", "Editable");
    public static final ClassName FragmentActivity = ClassName.get("androidx.fragment.app", "FragmentActivity");
    public static final ClassName Fragment = ClassName.get("androidx.fragment.app", "Fragment");
    public static final ClassName Intent = ClassName.get("android.content", "Intent");
    public static final ClassName LayoutInflater = ClassName.get("android.view", "LayoutInflater");
    public static final ClassName Menu = ClassName.get("android.view", "Menu");
    public static final ClassName MenuInflater = ClassName.get("android.view", "MenuInflater");
    public static final ClassName MenuItem = ClassName.get("android.view", "MenuItem");
    public static final ClassName MotionEvent = ClassName.get("android.view", "MotionEvent");
    public static final ClassName Parcelable = ClassName.get("android.os", "Parcelable");
    public static final ClassName Service = ClassName.get("android.app", "Service");
    public static final ClassName Size = ClassName.get("android.util", "Size");
    public static final ClassName SizeF = ClassName.get("android.util", "SizeF");
    public static final ClassName TextView = ClassName.get("android.widget", "TextView");
    public static final ClassName Toolbar = ClassName.get("android.widget", "Toolbar");
    public static final ClassName Toolbar_x = ClassName.get("androidx.appcompat.widget", "Toolbar");
    public static final ClassName Uri = ClassName.get("android.net", "Uri");
    public static final ClassName View = ClassName.get("android.view", "View");
    public static final ClassName View_OnClickListener = ClassName.get("android.view", "View", "OnClickListener");
    public static final ClassName View_OnLongClickListener = ClassName.get("android.view", "View", "OnLongClickListener");
    public static final ClassName View_OnTouchListener = ClassName.get("android.view", "View", "OnTouchListener");
    public static final ClassName View_OnFocusChangeListener = ClassName.get("android.view", "View", "OnFocusChangeListener");
    public static final ClassName ViewGroup = ClassName.get("android.view", "ViewGroup");
    public static final ClassName ViewGroup_LayoutParams = ClassName.get("android.view", "ViewGroup", "LayoutParams");

    //--- Android Annotation
    public static final ClassName RequiresApi = ClassName.get("androidx.annotation", "RequiresApi");
    public static final ClassName Keep = ClassName.get("androidx.annotation", "Keep");
    public static final ClassName NonNull = ClassName.get("androidx.annotation", "NonNull");

    //--- Android Lifecycle
    public static final ClassName AndroidViewModel = ClassName.get("androidx.lifecycle", "AndroidViewModel");
    public static final ClassName ViewModelProviders = ClassName.get("androidx.lifecycle", "ViewModelProviders");
    public static final ClassName ViewModelProvider = ClassName.get("androidx.lifecycle", "ViewModelProvider");
    public static final ClassName ViewModelStoreOwner = ClassName.get("androidx.lifecycle", "ViewModelStoreOwner");
    public static final ClassName ViewModelStore = ClassName.get("androidx.lifecycle", "ViewModelStore");
    public static final ClassName HasDefaultViewModelProviderFactory = ClassName.get("androidx.lifecycle", "HasDefaultViewModelProviderFactory");
    public static final ClassName Factory = ClassName.get("androidx.lifecycle", "ViewModelProvider", "Factory");
    public static final ClassName AndroidViewModelFactory = ClassName.get("androidx.lifecycle", "ViewModelProvider", "AndroidViewModelFactory");
    public static final ClassName Lifecycle = ClassName.get("androidx.lifecycle", "Lifecycle");
    public static final ClassName LifecycleObserver = ClassName.get("androidx.lifecycle", "LifecycleObserver");
    public static final ClassName LifecycleOwner = ClassName.get("androidx.lifecycle", "LifecycleOwner");
    public static final ClassName DefaultLifecycleObserver = ClassName.get("androidx.lifecycle", "DefaultLifecycleObserver");

    //--- Android Viewbinding
    public static final ClassName ViewBinding = ClassName.get("androidx.viewbinding", "ViewBinding");

    //--- Android System Manager
    public static final ClassName WindowManager = ClassName.get("android.view", "WindowManager");
    public static final ClassName ActivityManager = ClassName.get("android.app", "ActivityManager");
    public static final ClassName PowerManager = ClassName.get("android.os", "PowerManager");
    public static final ClassName AlarmManager = ClassName.get("android.app", "AlarmManager");
    public static final ClassName NotificationManager = ClassName.get("android.app", "NotificationManager");
    public static final ClassName KeyguardManager = ClassName.get("android.app", "KeyguardManager");
    public static final ClassName LocationManager = ClassName.get("android.location", "LocationManager");
    public static final ClassName SearchManager = ClassName.get("android.app", "SearchManager");
    public static final ClassName SensorManager = ClassName.get("android.hardware", "SensorManager");
    public static final ClassName StorageManager = ClassName.get("android.os.storage", "StorageManager");
    public static final ClassName Vibrator = ClassName.get("android.os", "Vibrator");
    public static final ClassName ConnectivityManager = ClassName.get("android.net", "ConnectivityManager");
    public static final ClassName WifiManager = ClassName.get("android.net.wifi", "WifiManager");
    public static final ClassName AudioManager = ClassName.get("android.media", "AudioManager");
    public static final ClassName MediaRouter = ClassName.get("android.media", "MediaRouter");
    public static final ClassName TelephonyManager = ClassName.get("android.telephony", "TelephonyManager");
    public static final ClassName SubscriptionManager = ClassName.get("android.telephony", "SubscriptionManager");
    public static final ClassName CarrierConfigManager = ClassName.get("android.telephony", "CarrierConfigManager");
    public static final ClassName InputMethodManager = ClassName.get("android.view.inputmethod", "InputMethodManager");
    public static final ClassName UiModeManager = ClassName.get("android.app", "UiModeManager");
    public static final ClassName DownloadManager = ClassName.get("android.app", "DownloadManager");
    public static final ClassName BatteryManager = ClassName.get("android.os", "BatteryManager");
    public static final ClassName JobScheduler = ClassName.get("android.app.job", "JobScheduler");
    public static final ClassName NetworkStatsManager = ClassName.get("android.app.usage", "NetworkStatsManager");
    public static final ClassName HardwarePropertiesManager = ClassName.get("android.os", "HardwarePropertiesManager");
    public static final ClassName NfcManager = ClassName.get("android.nfc", "NfcManager");
    public static final ClassName BluetoothManager = ClassName.get("android.bluetooth", "BluetoothManager");
    public static final ClassName UsbManager = ClassName.get("android.hardware.usb", "UsbManager");

}
