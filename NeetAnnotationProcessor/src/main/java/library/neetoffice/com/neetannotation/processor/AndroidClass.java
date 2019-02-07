package library.neetoffice.com.neetannotation.processor;

import com.squareup.javapoet.ClassName;

public class AndroidClass {
    public static final String CONTEXT_MODULE_PACKAGE = "com.neetoffice.neetannotation";
    public static final String CONTEXT_MODULE_NAME = "ContextModule";
    public static final ClassName CONTEXT_MODULE = ClassName.get(CONTEXT_MODULE_PACKAGE, CONTEXT_MODULE_NAME);
    public static final ClassName Application = ClassName.get("android.app", "Application");
    public static final ClassName Activity = ClassName.get("android.app", "Activity");
    public static final ClassName AndroidViewModel = ClassName.get("androidx.lifecycle", "AndroidViewModel");
    public static final ClassName ViewModelProviders = ClassName.get("androidx.lifecycle", "ViewModelProviders");
    public static final ClassName AdapterView = ClassName.get("android.widget", "AdapterView");
    public static final ClassName AdapterView_OnItemClickListener = ClassName.get("android.widget", "AdapterView", "OnItemClickListener");
    public static final ClassName AdapterView_OnItemLongClickListener = ClassName.get("android.widget", "AdapterView", "OnItemLongClickListener");
    public static final ClassName AdapterView_OnItemSelectedListener = ClassName.get("android.widget", "AdapterView", "OnItemSelectedListener");
    public static final ClassName AttributeSet = ClassName.get("android.util", "AttributeSet");
    public static final ClassName Bundle = ClassName.get("android.os", "Bundle");
    public static final ClassName CompoundButton = ClassName.get("android.widget", "CompoundButton");
    public static final ClassName CompoundButton_OnCheckedChangeListener = ClassName.get("android.widget", "CompoundButton", "OnCheckedChangeListener");
    public static final ClassName Context = ClassName.get("android.content", "Context");
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
    public static final ClassName RequiresApi = ClassName.get("androidx.annotation", "RequiresApi");
    public static final ClassName Uri = ClassName.get("android.net", "Uri");
    public static final ClassName View = ClassName.get("android.view", "View");
    public static final ClassName View_OnClickListener = ClassName.get("android.view", "View", "OnClickListener");
    public static final ClassName View_OnLongClickListener = ClassName.get("android.view", "View", "OnLongClickListener");
    public static final ClassName View_OnTouchListener = ClassName.get("android.view", "View", "OnTouchListener");
    public static final ClassName ViewGroup = ClassName.get("android.view", "ViewGroup");
    public static final ClassName ViewGroup_LayoutParams = ClassName.get("android.view", "ViewGroup", "LayoutParams");
}
