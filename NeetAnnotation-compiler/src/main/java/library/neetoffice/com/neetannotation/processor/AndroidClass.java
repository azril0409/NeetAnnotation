package library.neetoffice.com.neetannotation.processor;

import com.squareup.javapoet.ClassName;

/***/
public class AndroidClass {
    public static final String CONTEXT_MODULE_NAME = "ContextModule";
    public static final String SYSTEM_MODULE_NAME = "SystemModule";
    /**
     * Class name android.app.Application
     */
    public static final ClassName Application = ClassName.get("android.app", "Application");
    /**
     * Class name android.app.Activity
     */
    public static final ClassName Activity = ClassName.get("android.app", "Activity");
    /**
     * Class name androidx.core.app.ActivityOptionsCompat
     */
    public static final ClassName ActivityOptionsCompat = ClassName.get("androidx.core.app", "ActivityOptionsCompat");
    /**
     * Class name android.view.animation.AnimationUtils
     */
    public static final ClassName AnimationUtils = ClassName.get("android.view.animation", "AnimationUtils");
    /**
     * Class name android.widget.AdapterView
     */
    public static final ClassName AdapterView = ClassName.get("android.widget", "AdapterView");
    /**
     * Class name android.widget.AdapterView
     */
    public static final ClassName AdapterView_OnItemClickListener = ClassName.get("android.widget", "AdapterView", "OnItemClickListener");
    /**
     * Class name android.widget.AdapterView.OnItemLongClickListener
     */
    public static final ClassName AdapterView_OnItemLongClickListener = ClassName.get("android.widget", "AdapterView", "OnItemLongClickListener");
    /**
     * Class name android.widget.AdapterView.OnItemSelectedListener
     */
    public static final ClassName AdapterView_OnItemSelectedListener = ClassName.get("android.widget", "AdapterView", "OnItemSelectedListener");
    /**
     * Class name android.util.AttributeSet
     */
    public static final ClassName AttributeSet = ClassName.get("android.util", "AttributeSet");
    /**
     * Class name android.graphics.Bitmap
     */
    public static final ClassName Bitmap = ClassName.get("android.graphics", "Bitmap");
    /**
     * Class name android.os.Bundle
     */
    public static final ClassName Bundle = ClassName.get("android.os", "Bundle");
    /**
     * Class name android.widget.CompoundButton
     */
    public static final ClassName CompoundButton = ClassName.get("android.widget", "CompoundButton");
    /**
     * Class name android.widget.CompoundButton.OnCheckedChangeListener
     */
    public static final ClassName CompoundButton_OnCheckedChangeListener = ClassName.get("android.widget", "CompoundButton", "OnCheckedChangeListener");
    /**
     * Class name android.content.Context
     */
    public static final ClassName Context = ClassName.get("android.content", "Context");
    /**
     * Class name android.text.Editable
     */
    public static final ClassName Editable = ClassName.get("android.text", "Editable");
    /**
     * Class name androidx.fragment.app.FragmentActivity
     */
    public static final ClassName FragmentActivity = ClassName.get("androidx.fragment.app", "FragmentActivity");
    /**
     * Class name androidx.fragment.app.Fragment
     */
    public static final ClassName Fragment = ClassName.get("androidx.fragment.app", "Fragment");
    /**
     * Class name android.content.Intent
     */
    public static final ClassName Intent = ClassName.get("android.content", "Intent");
    /**
     * Class name android.view.LayoutInflater
     */
    public static final ClassName LayoutInflater = ClassName.get("android.view", "LayoutInflater");
    /**
     * Class name android.view.Menu
     */
    public static final ClassName Menu = ClassName.get("android.view", "Menu");
    /**
     * Class name android.view.MenuInflater
     */
    public static final ClassName MenuInflater = ClassName.get("android.view", "MenuInflater");
    /**
     * Class name android.view.MenuItem
     */
    public static final ClassName MenuItem = ClassName.get("android.view", "MenuItem");
    /**
     * Class name android.view.MotionEvent
     */
    public static final ClassName MotionEvent = ClassName.get("android.view", "MotionEvent");
    /**
     * Class name android.os.Parcelable
     */
    public static final ClassName Parcelable = ClassName.get("android.os", "Parcelable");
    /**
     * Class name android.app.Service
     */
    public static final ClassName Service = ClassName.get("android.app", "Service");
    /**
     * Class name android.util.Size
     */
    public static final ClassName Size = ClassName.get("android.util", "Size");
    /**
     * Class name android.util.SizeF
     */
    public static final ClassName SizeF = ClassName.get("android.util", "SizeF");
    /**
     * Class name android.widget.TextView
     */
    public static final ClassName TextView = ClassName.get("android.widget", "TextView");
    /**
     * Class name android.widget.Toolbar
     */
    public static final ClassName Toolbar = ClassName.get("android.widget", "Toolbar");
    /**
     * Class name androidx.appcompat.widget.Toolbar
     */
    public static final ClassName Toolbar_x = ClassName.get("androidx.appcompat.widget", "Toolbar");
    /**
     * Class name android.net.Uri
     */
    public static final ClassName Uri = ClassName.get("android.net", "Uri");
    /**
     * Class name android.view.View
     */
    public static final ClassName View = ClassName.get("android.view", "View");
    /**
     * Class name android.view.View.OnClickListener
     */
    public static final ClassName View_OnClickListener = ClassName.get("android.view", "View", "OnClickListener");
    /**
     * Class name android.view.View.OnLongClickListener
     */
    public static final ClassName View_OnLongClickListener = ClassName.get("android.view", "View", "OnLongClickListener");
    /**
     * Class name android.view.View.OnTouchListener
     */
    public static final ClassName View_OnTouchListener = ClassName.get("android.view", "View", "OnTouchListener");
    /**
     * Class name android.view.View.OnFocusChangeListener
     */
    public static final ClassName View_OnFocusChangeListener = ClassName.get("android.view", "View", "OnFocusChangeListener");
    /**
     * Class name android.view.ViewGroup
     */
    public static final ClassName ViewGroup = ClassName.get("android.view", "ViewGroup");
    /**
     * Class name android.view.ViewGroup.LayoutParams
     */
    public static final ClassName ViewGroup_LayoutParams = ClassName.get("android.view", "ViewGroup", "LayoutParams");

    /**
     * Class name androidx.activity.ComponentActivity
     */
    public static final ClassName ComponentActivity = ClassName.get("androidx.activity", "ComponentActivity");
    /**
     * Class name androidx.activity.result.contract.ActivityResultContract
     */
    public static final ClassName ActivityResultContract = ClassName.get("androidx.activity.result.contract", "ActivityResultContract");
    /**
     * Class name androidx.activity.result.ActivityResultLauncher
     */
    public static final ClassName ActivityResultLauncher = ClassName.get("androidx.activity.result", "ActivityResultLauncher");
    /**
     * Class name androidx.activity.result.ActivityResult
     */
    public static final ClassName ActivityResult = ClassName.get("androidx.activity.result", "ActivityResult");
    /**
     * Class name androidx.activity.result.IntentSenderRequest
     */
    public static final ClassName IntentSenderRequest = ClassName.get("androidx.activity.result", "IntentSenderRequest");
    /**
     * Class name androidx.activity.result.ActivityResultCallback
     */
    public static final ClassName ActivityResultCallback = ClassName.get("androidx.activity.result", "ActivityResultCallback");
    /**
     * Class name androidx.activity.result.contract.StartActivityForResult
     */
    public static final ClassName StartActivityForResult = ClassName.get("androidx.activity.result.contract", "ActivityResultContracts", "StartActivityForResult");
    /**
     * Class name androidx.activity.result.contract.StartIntentSenderForResult
     */
    public static final ClassName StartIntentSenderForResult = ClassName.get("androidx.activity.result.contract", "ActivityResultContracts", "StartIntentSenderForResult");
    /**
     * Class name androidx.activity.result.contract.RequestMultiplePermissions
     */
    public static final ClassName RequestMultiplePermissions = ClassName.get("androidx.activity.result.contract", "ActivityResultContracts", "RequestMultiplePermissions");
    /**
     * Class name androidx.activity.result.contract.RequestPermission
     */
    public static final ClassName RequestPermission = ClassName.get("androidx.activity.result.contract", "ActivityResultContracts", "RequestPermission");
    /**
     * Class name androidx.activity.result.contract.TakePicturePreview
     */
    public static final ClassName TakePicturePreview = ClassName.get("androidx.activity.result.contract", "ActivityResultContracts", "TakePicturePreview");
    /**
     * Class name androidx.activity.result.contract.TakePicture
     */
    public static final ClassName TakePicture = ClassName.get("androidx.activity.result.contract", "ActivityResultContracts", "TakePicture");
    /**
     * Class name androidx.activity.result.contract.CaptureVideo
     */
    public static final ClassName CaptureVideo = ClassName.get("androidx.activity.result.contract", "ActivityResultContracts", "CaptureVideo");
    /**
     * Class name androidx.activity.result.contract.PickContact
     */
    public static final ClassName PickContact = ClassName.get("androidx.activity.result.contract", "ActivityResultContracts", "PickContact");
    /**
     * Class name androidx.activity.result.contract.GetContent
     */
    public static final ClassName GetContent = ClassName.get("androidx.activity.result.contract", "ActivityResultContracts", "GetContent");
    /**
     * Class name androidx.activity.result.contract.GetMultipleContents
     */
    public static final ClassName GetMultipleContents = ClassName.get("androidx.activity.result.contract", "ActivityResultContracts", "GetMultipleContents");
    /**
     * Class name androidx.activity.result.contract.OpenDocument
     */
    public static final ClassName OpenDocument = ClassName.get("androidx.activity.result.contract", "ActivityResultContracts", "OpenDocument");
    /**
     * Class name androidx.activity.result.contract.OpenMultipleDocuments
     */
    public static final ClassName OpenMultipleDocuments = ClassName.get("androidx.activity.result.contract", "ActivityResultContracts", "OpenMultipleDocuments");
    /**
     * Class name androidx.activity.result.contract.OpenDocumentTree
     */
    public static final ClassName OpenDocumentTree = ClassName.get("androidx.activity.result.contract", "ActivityResultContracts", "OpenDocumentTree");
    /**
     * Class name androidx.activity.result.contract.CreateDocument
     */
    public static final ClassName CreateDocument = ClassName.get("androidx.activity.result.contract", "ActivityResultContracts", "CreateDocument");

    //--- Android Annotation
    /**
     * Class name androidx.annotation.RequiresApi
     */
    public static final ClassName RequiresApi = ClassName.get("androidx.annotation", "RequiresApi");
    /**
     * Class name androidx.annotation.Keep
     */
    public static final ClassName Keep = ClassName.get("androidx.annotation", "Keep");
    /**
     * Class name androidx.annotation.NonNull
     */
    public static final ClassName NonNull = ClassName.get("androidx.annotation", "NonNull");

    //--- Android Lifecycle
    /**
     * Class name androidx.lifecycle.AndroidViewModel
     */
    public static final ClassName AndroidViewModel = ClassName.get("androidx.lifecycle", "AndroidViewModel");
    /**
     * Class name androidx.lifecycle.ViewModelProviders
     */
    public static final ClassName ViewModelProviders = ClassName.get("androidx.lifecycle", "ViewModelProviders");
    /**
     * Class name androidx.lifecycle.ViewModelProvider
     */
    public static final ClassName ViewModelProvider = ClassName.get("androidx.lifecycle", "ViewModelProvider");
    /**
     * Class name androidx.lifecycle.ViewModelStoreOwner
     */
    public static final ClassName ViewModelStoreOwner = ClassName.get("androidx.lifecycle", "ViewModelStoreOwner");
    /**
     * Class name androidx.lifecycle.ViewModelStore
     */
    public static final ClassName ViewModelStore = ClassName.get("androidx.lifecycle", "ViewModelStore");
    /**
     * Class name androidx.lifecycle.HasDefaultViewModelProviderFactory
     */
    public static final ClassName HasDefaultViewModelProviderFactory = ClassName.get("androidx.lifecycle", "HasDefaultViewModelProviderFactory");
    /**
     * Class name androidx.lifecycle.ViewModelProvider.Factory
     */
    public static final ClassName Factory = ClassName.get("androidx.lifecycle", "ViewModelProvider", "Factory");
    /**
     * Class name androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory
     */
    public static final ClassName AndroidViewModelFactory = ClassName.get("androidx.lifecycle", "ViewModelProvider", "AndroidViewModelFactory");
    /**
     * Class name androidx.lifecycle.Lifecycle
     */
    public static final ClassName Lifecycle = ClassName.get("androidx.lifecycle", "Lifecycle");
    /**
     * Class name androidx.lifecycle.LifecycleObserver
     */
    public static final ClassName LifecycleObserver = ClassName.get("androidx.lifecycle", "LifecycleObserver");
    /**
     * Class name androidx.lifecycle.LifecycleOwner
     */
    public static final ClassName LifecycleOwner = ClassName.get("androidx.lifecycle", "LifecycleOwner");
    /**
     * Class name androidx.lifecycle.DefaultLifecycleObserver
     */
    public static final ClassName DefaultLifecycleObserver = ClassName.get("androidx.lifecycle", "DefaultLifecycleObserver");

    //--- Android Viewbinding
    /**
     * Class name androidx.viewbinding.ViewBinding
     */
    public static final ClassName ViewBinding = ClassName.get("androidx.viewbinding", "ViewBinding");

    //--- Android System Manager
    /**
     * Class name android.view.WindowManager
     */
    public static final ClassName WindowManager = ClassName.get("android.view", "WindowManager");
    /**
     * Class name android.app.ActivityManager
     */
    public static final ClassName ActivityManager = ClassName.get("android.app", "ActivityManager");
    /**
     * Class name android.os.PowerManager
     */
    public static final ClassName PowerManager = ClassName.get("android.os", "PowerManager");
    /**
     * Class name android.app.AlarmManager
     */
    public static final ClassName AlarmManager = ClassName.get("android.app", "AlarmManager");
    /**
     * Class name android.app.NotificationManager
     */
    public static final ClassName NotificationManager = ClassName.get("android.app", "NotificationManager");
    /**
     * Class name android.app.KeyguardManager
     */
    public static final ClassName KeyguardManager = ClassName.get("android.app", "KeyguardManager");
    /**
     * Class name android.location.LocationManager
     */
    public static final ClassName LocationManager = ClassName.get("android.location", "LocationManager");
    /**
     * Class name android.app.SearchManager
     */
    public static final ClassName SearchManager = ClassName.get("android.app", "SearchManager");
    /**
     * Class name android.hardware.SensorManager
     */
    public static final ClassName SensorManager = ClassName.get("android.hardware", "SensorManager");
    /**
     * Class name android.os.storage.StorageManager
     */
    public static final ClassName StorageManager = ClassName.get("android.os.storage", "StorageManager");
    /**
     * Class name android.os.Vibrator
     */
    public static final ClassName Vibrator = ClassName.get("android.os", "Vibrator");
    /**
     * Class name android.net.ConnectivityManager
     */
    public static final ClassName ConnectivityManager = ClassName.get("android.net", "ConnectivityManager");
    /**
     * Class name android.net.wifi.WifiManager
     */
    public static final ClassName WifiManager = ClassName.get("android.net.wifi", "WifiManager");
    /**
     * Class name android.media.AudioManager
     */
    public static final ClassName AudioManager = ClassName.get("android.media", "AudioManager");
    /**
     * Class name android.media.MediaRouter
     */
    public static final ClassName MediaRouter = ClassName.get("android.media", "MediaRouter");
    /**
     * Class name android.telephony.TelephonyManager
     */
    public static final ClassName TelephonyManager = ClassName.get("android.telephony", "TelephonyManager");
    /**
     * Class name android.telephony.SubscriptionManager
     */
    public static final ClassName SubscriptionManager = ClassName.get("android.telephony", "SubscriptionManager");
    /**
     * Class name android.telephony.CarrierConfigManager
     */
    public static final ClassName CarrierConfigManager = ClassName.get("android.telephony", "CarrierConfigManager");
    /**
     * Class name android.view.inputmethod.InputMethodManager
     */
    public static final ClassName InputMethodManager = ClassName.get("android.view.inputmethod", "InputMethodManager");
    /**
     * Class name android.app.UiModeManager
     */
    public static final ClassName UiModeManager = ClassName.get("android.app", "UiModeManager");
    /**
     * Class name android.app.DownloadManager
     */
    public static final ClassName DownloadManager = ClassName.get("android.app", "DownloadManager");
    /**
     * Class name android.app.BatteryManager
     */
    public static final ClassName BatteryManager = ClassName.get("android.os", "BatteryManager");
    /**
     * Class name android.app.job.JobScheduler
     */
    public static final ClassName JobScheduler = ClassName.get("android.app.job", "JobScheduler");
    /**
     * Class name android.app.usage.NetworkStatsManager
     */
    public static final ClassName NetworkStatsManager = ClassName.get("android.app.usage", "NetworkStatsManager");
    /**
     * Class name android.os.HardwarePropertiesManager
     */
    public static final ClassName HardwarePropertiesManager = ClassName.get("android.os", "HardwarePropertiesManager");
    /**
     * Class name android.nfc.NfcManager
     */
    public static final ClassName NfcManager = ClassName.get("android.nfc", "NfcManager");
    /**
     * Class name android.bluetooth.BluetoothManager
     */
    public static final ClassName BluetoothManager = ClassName.get("android.bluetooth", "BluetoothManager");
    /**
     * Class name android.hardware.usb.UsbManager
     */
    public static final ClassName UsbManager = ClassName.get("android.hardware.usb", "UsbManager");

}
