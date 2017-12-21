package library.neetoffice.com.neetannotation;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.View;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;

/**
 * Created by Deo on 2016/3/18.
 */
abstract class BindActivity {

    static View onSetContentView(Activity a) {
        final NActivity d = a.getClass().getAnnotation(NActivity.class);
        if (d != null && d.value() != 0) {
            a.setContentView(d.value());
        } else if (d != null && !d.resName().isEmpty()) {
            a.setContentView(FindResources.layout(BindBase.resPath(a,a), d.resName()));
        }
        return a.getWindow().getDecorView();
    }

    static void onCreate(Activity a, View v, Bundle b) {
        final ArrayList<Method> j = new ArrayList<>();
        Class<?> c = a.getClass();
        do {
            final NActivity q = c.getAnnotation(NActivity.class);
            if (q != null) {
                final Field[] f = c.getDeclaredFields();
                for (Field g : f) {
                    BindBase.bindViewById(a, a.getWindow().getDecorView(), g, a);
                    bindFragmentById(a, g);
                    bindFragmentByTag(a, g);
                    BindBase.baseFieldBind(a, g, a);
                    BindExtra.bindExtra(a, g);
                    BindField.bindSaveInstance(a, g, b);
                    BindMenu.bindMenu(a);
                }
                final Method[] h = c.getDeclaredMethods();
                final TouchListener l = new TouchListener(a);
                for (Method i : h) {
                    BindBase.baseViewListenerBind(a, a.getWindow().getDecorView(), i, l, a);
                    if (BindMethod.isAfterAnnotationMethod(i)) {
                        j.add(i);
                    }
                }
            }
            c = c.getSuperclass();
        } while (c != null);
        for (int i = j.size() - 1; i >= 0; i--) {
            BindBase.callAfterAnnotationMethod(a, j.get(i), b);
        }
    }

    private static void bindFragmentById(Activity a, Field b) {
        final FragmentById c = b.getAnnotation(FragmentById.class);
        if (c == null) {
            return;
        }
        final Class<?> f = b.getType();
        try {
            if (Fragment.class.isAssignableFrom(f)) {
                Fragment i;
                if (c.value() != 0) {
                    i = a.getFragmentManager().findFragmentById(c.value());
                } else if (!c.resName().isEmpty()) {
                    i = a.getFragmentManager().findFragmentById(FindResources.id(BindBase.resPath(a,a), c.resName()));
                } else {
                    i = a.getFragmentManager().findFragmentById(FindResources.id(BindBase.resPath(a,a), b.getName()));
                }
                AnnotationUtil.set(b, a, i);
            } else {
                final Class<?> sf = Class.forName("android.support.v4.app.Fragment");
                if (sf.isAssignableFrom(f)) {
                    final Class<?> bsf = Class.forName("library.neetoffice.com.neetannotation.BindSupport");
                    if (bsf == null) {
                        throw new AnnotationException("No compile NeetAnnotationSupport");
                    }
                    final Method m = bsf.getDeclaredMethod("bindFragmentById", new Class[]{Activity.class, Field.class});
                    m.invoke(null, a, b);
                }
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    private static void bindFragmentByTag(Activity a, Field b) {
        final FragmentByTag c = b.getAnnotation(FragmentByTag.class);
        if (c == null) {
            return;
        }
        final Class<?> f = b.getType();
        try {
            if (Fragment.class.isAssignableFrom(f)) {
                String t = c.value();
                if (t == null || t.isEmpty()) {
                    t = b.getName();
                }
                Fragment i = a.getFragmentManager().findFragmentByTag(t);
                AnnotationUtil.set(b, a, i);
            } else {
                final Class<?> sf = Class.forName("android.support.v4.app.Fragment");
                if (sf.isAssignableFrom(f)) {
                    final Class<?> bsf = Class.forName("library.neetoffice.com.neetannotation.BindSupport");
                    if (bsf == null) {
                        throw new AnnotationException("No compile NeetAnnotationSupport");
                    }
                    final Method m = bsf.getDeclaredMethod("bindFragmentByTag", new Class[]{Activity.class, Field.class});
                    m.invoke(null, a, b);
                }
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    static void onSaveInstanceState(Activity a, Bundle b) {
        Class<?> c = a.getClass();
        do {
            final NActivity q = c.getAnnotation(NActivity.class);
            if (q != null) {
                final Field[] f = c.getDeclaredFields();
                for (Field g : f) {
                    final SaveInstance d = g.getAnnotation(SaveInstance.class);
                    if (d == null) {
                        continue;
                    }
                    final String h;
                    if (d.value().length() > 0) {
                        h = d.value();
                    } else {
                        h = "_" + g.getName();
                    }
                    BindBundle.addFieldToBundle(a, g, h, b);
                }
            }
            c = c.getSuperclass();
        } while (c != null);
    }
}
