package library.neetoffice.com.neetannotation.processor;

import com.squareup.javapoet.ClassName;

public class AndroidClass {
    public static final String CONTEXT_MODULE_PACKAGE = "com.neetoffice.neetannotation";
    public static final String CONTEXT_MODULE_NAME = "ContextModule";
    public static final ClassName CONTEXT_MODULE = ClassName.get(CONTEXT_MODULE_PACKAGE, CONTEXT_MODULE_NAME);
    public static final ClassName Application = ClassName.get("android.app", "Application");
    public static final ClassName Activity = ClassName.get("android.app", "Activity");
    public static final ClassName AndroidViewModel = ClassName.get("android.arch.lifecycle", "AndroidViewModel");
    public static final ClassName AdapterView = ClassName.get("android.widget", "AdapterView");
    public static final ClassName AdapterView_OnItemClickListener = ClassName.get("android.widget", "AdapterView", "OnItemClickListener");
    public static final ClassName AdapterView_OnItemLongClickListener = ClassName.get("android.widget", "AdapterView", "OnItemLongClickListener");
    public static final ClassName AdapterView_OnItemSelectedListener = ClassName.get("android.widget", "AdapterView", "OnItemSelectedListener");
    public static final ClassName Bundle = ClassName.get("android.os", "Bundle");
    public static final ClassName CompoundButton = ClassName.get("android.widget", "CompoundButton");
    public static final ClassName CompoundButton_OnCheckedChangeListener = ClassName.get("android.widget", "CompoundButton", "OnCheckedChangeListener");
    public static final ClassName Fragment = ClassName.get("android.app", "Fragment");
    public static final ClassName Fragment_v4 = ClassName.get("android.support.v4.app", "Fragment");
    public static final ClassName MotionEvent = ClassName.get("android.view", "MotionEvent");
    public static final ClassName TextView = ClassName.get("android.widget", "TextView");
    public static final ClassName View = ClassName.get("android.view", "View");
    public static final ClassName View_OnClickListener = ClassName.get("android.view", "View", "OnClickListener");
    public static final ClassName View_OnLongClickListener = ClassName.get("android.view", "View", "OnLongClickListener");
    public static final ClassName View_OnTouchListener = ClassName.get("android.view", "View", "OnTouchListener");
}
