package library.neetoffice.com.neetannotation;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Fragment;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Size;
import android.util.SizeF;
import android.util.SparseArray;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by Mac on 2016/04/04.
 */
public abstract class IntentBuilder {
    private final Bundle bundle = new Bundle();
    private int mFlags;

    public final Bundle bundle() {
        Class<?> a = getClass();
        final HashMap<String, Field> m = new HashMap<>();
        while (!IntentBuilder.class.equals(a)) {
            final Field[] b = a.getDeclaredFields();
            for (Field c : b) {
                final Extra d = c.getAnnotation(Extra.class);
                if (d != null) {
                    String f;
                    if ("".equals(d.value())) {
                        f = c.getName();
                    } else {
                        f = d.value();
                    }
                    if (!m.containsKey(f)) {
                        m.put(f, c);
                    }
                }
            }
            a = a.getSuperclass();
        }

        final Set<Map.Entry<String, Field>> s = m.entrySet();
        for (Map.Entry<String, Field> p : s) {
            final String n = p.getKey();
            final Field f = p.getValue();
            if (bundle.containsKey(n)) {
                continue;
            }
            final Object o;
            try {
                o = AnnotationUtil.get(f, this);
            } catch (IllegalAccessException e) {
                continue;
            }
            if (o == null) {
                continue;
            }
            if (o instanceof Boolean) {
                putBoolean(n, (Boolean) o);
            } else if (o instanceof Byte) {
                putByte(n, (Byte) o);
            } else if (o instanceof Character) {
                putChar(n, (Character) o);
            } else if (o instanceof Short) {
                putShort(n, (Short) o);
            } else if (o instanceof Integer) {
                putInt(n, (Integer) o);
            } else if (o instanceof Long) {
                putLong(n, (Long) o);
            } else if (o instanceof Float) {
                putFloat(n, (Float) o);
            } else if (o instanceof Double) {
                putDouble(n, (Double) o);
            } else if (o instanceof String) {
                putString(n, (String) o);
            } else if (o instanceof CharSequence) {
                putCharSequence(n, (CharSequence) o);
            } else if (o instanceof Size) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    putSize(n, (Size) o);
                }
            } else if (o instanceof SizeF) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    putSizeF(n, (SizeF) o);
                }
            } else if (o instanceof Parcelable) {
                putParcelable(n, (Parcelable) o);
            } else if (o instanceof Parcelable[]) {
                putParcelableArray(n, (Parcelable[]) o);
            } else if (o instanceof boolean[]) {
                putBooleanArray(n, (boolean[]) o);
            } else if (o instanceof byte[]) {
                putByteArray(n, (byte[]) o);
            } else if (o instanceof short[]) {
                putShortArray(n, (short[]) o);
            } else if (o instanceof char[]) {
                putCharArray(n, (char[]) o);
            } else if (o instanceof int[]) {
                putIntArray(n, (int[]) o);
            } else if (o instanceof long[]) {
                putLongArray(n, (long[]) o);
            } else if (o instanceof float[]) {
                putFloatArray(n, (float[]) o);
            } else if (o instanceof double[]) {
                putDoubleArray(n, (double[]) o);
            } else if (o instanceof String[]) {
                putStringArray(n, (String[]) o);
            } else if (o instanceof CharSequence[]) {
                putCharSequenceArray(n, (CharSequence[]) o);
            } else if (o instanceof Collection) {
                final Class<?> t = getParameterizedType(f);
                if (Parcelable.class.isAssignableFrom(t)) {
                    final ArrayList<? extends Parcelable> q = new ArrayList<>((Collection) o);
                    putParcelableArrayList(n, q);
                } else if (Integer.class.isAssignableFrom(t)) {
                    final ArrayList<Integer> q = new ArrayList<>((Collection) o);
                    putIntegerArrayList(n, q);
                } else if (String.class.isAssignableFrom(t)) {
                    final ArrayList<String> q = new ArrayList<>((Collection) o);
                    putStringArrayList(n, q);
                } else if (CharSequence.class.isAssignableFrom(t)) {
                    final ArrayList<CharSequence> q = new ArrayList<>((Collection) o);
                    putCharSequenceArrayList(n, q);
                }
            } else if (o instanceof SparseArray) {
                final Class<?> t = getParameterizedType(f);
                if (Parcelable.class.isAssignableFrom(t)) {
                    putSparseParcelableArray(n, (SparseArray<? extends Parcelable>) o);
                }
            } else if (o instanceof Serializable) {
                putSerializable(n, (Serializable) o);
            }
        }
        return bundle;
    }

    public IntentBuilder putBoolean(String key, boolean value) {
        bundle.putBoolean(key, value);
        return this;
    }

    public IntentBuilder putByte(String key, byte value) {
        bundle.putByte(key, value);
        return this;
    }

    public IntentBuilder putChar(String key, char value) {
        bundle.putChar(key, value);
        return this;
    }

    public IntentBuilder putShort(String key, short value) {
        bundle.putShort(key, value);
        return this;
    }

    public IntentBuilder putInt(String key, int value) {
        bundle.putInt(key, value);
        return this;
    }

    public IntentBuilder putLong(String key, long value) {
        bundle.putLong(key, value);
        return this;
    }

    public IntentBuilder putFloat(String key, float value) {
        bundle.putFloat(key, value);
        return this;
    }

    public IntentBuilder putDouble(String key, double value) {
        bundle.putDouble(key, value);
        return this;
    }

    public IntentBuilder putString(String key, String value) {
        bundle.putString(key, value);
        return this;
    }

    public IntentBuilder putCharSequence(String key, CharSequence value) {
        bundle.putCharSequence(key, value);
        return this;
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public IntentBuilder putSize(String key, Size value) {
        bundle.putSize(key, value);
        return this;
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public IntentBuilder putSizeF(String key, SizeF value) {
        bundle.putSizeF(key, value);
        return this;
    }

    public IntentBuilder putParcelable(String key, Parcelable value) {
        bundle.putParcelable(key, value);
        return this;
    }

    public IntentBuilder putParcelableArray(String key, Parcelable[] value) {
        bundle.putParcelableArray(key, value);
        return this;
    }

    public IntentBuilder putBooleanArray(String key, boolean[] value) {
        bundle.putBooleanArray(key, value);
        return this;
    }

    public IntentBuilder putByteArray(String key, byte[] value) {
        bundle.putByteArray(key, value);
        return this;
    }

    public IntentBuilder putShortArray(String key, short[] value) {
        bundle.putShortArray(key, value);
        return this;
    }

    public IntentBuilder putCharArray(String key, char[] value) {
        bundle.putCharArray(key, value);
        return this;
    }

    public IntentBuilder putIntArray(String key, int[] value) {
        bundle.putIntArray(key, value);
        return this;
    }

    public IntentBuilder putLongArray(String key, long[] value) {
        bundle.putLongArray(key, value);
        return this;
    }

    public IntentBuilder putFloatArray(String key, float[] value) {
        bundle.putFloatArray(key, value);
        return this;
    }

    public IntentBuilder putDoubleArray(String key, double[] value) {
        bundle.putDoubleArray(key, value);
        return this;
    }

    public IntentBuilder putStringArray(String key, String[] value) {
        bundle.putStringArray(key, value);
        return this;
    }

    public IntentBuilder putCharSequenceArray(String key, CharSequence[] value) {
        bundle.putCharSequenceArray(key, value);
        return this;
    }

    public IntentBuilder putParcelableArrayList(String key, ArrayList<? extends Parcelable> value) {
        bundle.putParcelableArrayList(key, value);
        return this;
    }

    public IntentBuilder putIntegerArrayList(String key, ArrayList<Integer> value) {
        bundle.putIntegerArrayList(key, value);
        return this;
    }

    public IntentBuilder putStringArrayList(String key, ArrayList<String> value) {
        bundle.putStringArrayList(key, value);
        return this;
    }

    public IntentBuilder putCharSequenceArrayList(String key, ArrayList<CharSequence> value) {
        bundle.putCharSequenceArrayList(key, value);
        return this;
    }

    public IntentBuilder putSparseParcelableArray(String key, SparseArray<? extends Parcelable> value) {
        bundle.putSparseParcelableArray(key, value);
        return this;
    }

    public IntentBuilder putSerializable(String key, Serializable value) {
        bundle.putSerializable(key, value);
        return this;
    }

    public IntentBuilder putAll(Bundle extras) {
        bundle.putAll(extras);
        return this;
    }

    public IntentBuilder setFlags(int flags) {
        mFlags = flags;
        return this;
    }

    public IntentBuilder addFlags(int flags) {
        mFlags = flags;
        return this;
    }

    /**
     * {@hide}
     */
    private final static Class<?> getParameterizedType(Field f) {
        final Type fc = f.getGenericType();
        if (fc != null && fc instanceof ParameterizedType) {
            final ParameterizedType pt = (ParameterizedType) fc;
            final Type[] types = pt.getActualTypeArguments();
            if (types != null && types.length > 0) {
                return (Class<?>) types[0];
            }
        }
        return null;
    }

    public static IntentBuilder newIntent() {
        return new IntentBuilder() {
        };
    }

    public void startActivity(final Object context, final Class<? extends Activity> cls) {
        final Intent intent = new Intent();
        intent.putExtras(bundle());
        intent.setFlags(mFlags);
        if (context instanceof Context) {
            intent.setClass((Context) context, cls);
            ((Context) context).startActivity(intent);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context instanceof Fragment) {
            intent.setClass(((Fragment) context).getActivity(), cls);
            ((Fragment) context).startActivity(intent);
        } else {
            try {
                final Class<?> sf = Class.forName("android.support.v4.app.Fragment");
                if (sf.isAssignableFrom(context.getClass())) {
                    final Class<?> bsf = Class.forName("library.neetoffice.com.neetannotation.BindSupport");
                    if (bsf == null) {
                        throw new AnnotationException("No compile NeetAnnotationSupport");
                    }
                    final Method m = bsf.getDeclaredMethod("getActivity", new Class[]{sf});
                    final Context c = (Context) m.invoke(null, context);
                    intent.setClass(c, cls);
                    final Method n = bsf.getDeclaredMethod("startActivity", new Class[]{sf, Intent.class});
                    n.invoke(null, context, intent);
                }
            } catch (Exception e) {
                throw new AnnotationException(e);
            }
        }
    }

    public void startActivity(final Object context, final String action) {
        final Intent intent = new Intent(action);
        intent.putExtras(bundle());
        intent.setFlags(mFlags);
        if (context instanceof Context) {
            ((Context) context).startActivity(intent);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context instanceof Fragment) {
            ((Fragment) context).startActivity(intent);
        } else {
            try {
                final Class<?> sf = Class.forName("android.support.v4.app.Fragment");
                if (sf.isAssignableFrom(context.getClass())) {
                    final Class<?> bsf = Class.forName("library.neetoffice.com.neetannotation.BindSupport");
                    if (bsf == null) {
                        throw new AnnotationException("No compile NeetAnnotationSupport");
                    }
                    final Method n = bsf.getDeclaredMethod("startActivityForResult", new Class[]{sf, Intent.class});
                    n.invoke(null, context, intent);
                }
            } catch (Exception e) {
                throw new AnnotationException(e);
            }
        }
    }

    public void startActivityForResult(Object context, Class<? extends Activity> cls, int requestCode) {
        final Intent intent = new Intent();
        intent.putExtras(bundle());
        intent.setFlags(mFlags);
        if (context instanceof Activity) {
            intent.setClass((Activity) context, cls);
            ((Activity) context).startActivityForResult(intent, requestCode);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context instanceof Fragment) {
            intent.setClass(((Fragment) context).getActivity(), cls);
            ((Fragment) context).startActivityForResult(intent, requestCode);
        } else {
            try {
                final Class<?> sf = Class.forName("android.support.v4.app.Fragment");
                if (sf.isAssignableFrom(context.getClass())) {
                    final Class<?> bsf = Class.forName("library.neetoffice.com.neetannotation.BindSupport");
                    if (bsf == null) {
                        throw new AnnotationException("No compile NeetAnnotationSupport");
                    }
                    final Method m = bsf.getDeclaredMethod("getActivity", new Class[]{sf});
                    final Context c = (Context) m.invoke(null, context);
                    intent.setClass(c, cls);
                    final Method n = bsf.getDeclaredMethod("startActivityForResult", new Class[]{sf, Intent.class, int.class});
                    n.invoke(null, context, intent, requestCode);
                }
            } catch (Exception e) {
                throw new AnnotationException(e);
            }
        }
    }

    public void startActivityForResult(Object context, String action, int requestCode) {
        final Intent intent = new Intent(action);
        intent.putExtras(bundle());
        intent.setFlags(mFlags);
        if (context instanceof Activity) {
            ((Activity) context).startActivityForResult(intent, requestCode);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context instanceof Fragment) {
            ((Fragment) context).startActivityForResult(intent, requestCode);
        } else {
            try {
                final Class<?> sf = Class.forName("android.support.v4.app.Fragment");
                if (sf.isAssignableFrom(context.getClass())) {
                    final Class<?> bsf = Class.forName("library.neetoffice.com.neetannotation.BindSupport");
                    if (bsf == null) {
                        throw new AnnotationException("No compile NeetAnnotationSupport");
                    }
                    final Method n = bsf.getDeclaredMethod("startActivityForResult", new Class[]{sf, Intent.class, int.class});
                    n.invoke(null, context, intent, requestCode);
                }
            } catch (Exception e) {
                throw new AnnotationException(e);
            }
        }
    }

    public void setResult(Activity activity) {
        final Intent intent = new Intent();
        intent.putExtras(bundle());
        intent.setFlags(mFlags);
        activity.setResult(Activity.RESULT_OK, intent);
    }

    public void setResult(Activity activity, int resultCode) {
        final Intent intent = new Intent();
        intent.putExtras(bundle());
        intent.setFlags(mFlags);
        activity.setResult(resultCode, intent);
    }

    public void startService(final Context context, Class<? extends Service> cls) {
        final Intent intent = new Intent(context, cls);
        intent.putExtras(bundle());
        intent.setFlags(mFlags);
        context.startService(intent);
    }

    public void stopService(final Context context, Class<? extends Service> cls) {
        final Intent intent = new Intent(context, cls);
        intent.putExtras(bundle());
        intent.setFlags(mFlags);
        context.stopService(intent);
    }

    public void startService(final Context context, String action) {
        final Intent intent = new Intent(action);
        intent.putExtras(bundle());
        intent.setFlags(mFlags);
        context.startService(intent);
    }

    public void stopService(final Context context, String action) {
        final Intent intent = new Intent(action);
        intent.putExtras(bundle());
        intent.setFlags(mFlags);
        context.stopService(intent);
    }

    public void bindService(final Context context, Class<? extends Service> cls, ServiceConnection serviceConnection, int flags) {
        final Intent intent = new Intent(context, cls);
        intent.putExtras(bundle());
        intent.setFlags(mFlags);
        context.bindService(intent, serviceConnection, flags);
    }

    public void sendBroadcast(final Context context, Class<? extends BroadcastReceiver> cls) {
        final Intent intent = new Intent(context, cls);
        intent.putExtras(bundle());
        intent.setFlags(mFlags);
        context.sendBroadcast(intent);
    }

    public void sendBroadcast(final Context context, String action) {
        final Intent intent = new Intent(action);
        intent.putExtras(bundle());
        intent.setFlags(mFlags);
        context.sendBroadcast(intent);
    }

    public void sendBroadcast(final Context context, Class<? extends BroadcastReceiver> cls, String receiverPermission) {
        final Intent intent = new Intent(context, cls);
        intent.putExtras(bundle());
        intent.setFlags(mFlags);
        context.sendBroadcast(intent, receiverPermission);
    }

    public void sendBroadcast(final Context context, String action, String receiverPermission) {
        final Intent intent = new Intent(action);
        intent.putExtras(bundle());
        intent.setFlags(mFlags);
        context.sendBroadcast(intent, receiverPermission);
    }
}
