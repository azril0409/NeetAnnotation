package library.neetoffice.com.neetannotation;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toolbar;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;

/**
 * Created by Deo-chainmeans on 2017/3/22.
 */

abstract class BindMenu {
    static void bindMenu(final Activity a) {
        final NToolBar b = a.getClass().getAnnotation(NToolBar.class);
        if (b == null) {
            return;
        }
        View c = null;
        if (b.viewId() != 0) {
            c = a.findViewById(b.viewId());
        } else if (!b.viewResName().isEmpty()) {
            c = a.findViewById(FindResources.id(BindBase.resPath(a, a), b.viewResName()));
        }
        if (c != null && c instanceof Toolbar) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                a.setActionBar((Toolbar) c);
            }
        } else {
            try {
                final Class aca = Class.forName("android.support.v7.app.AppCompatActivity");
                final Class tb = Class.forName("android.support.v7.widget.Toolbar");
                if (aca.isAssignableFrom(a.getClass()) && c != null && tb.isAssignableFrom(c.getClass())) {
                    final Class<?> bsf = Class.forName("library.neetoffice.com.neetannotation.BindSupport");
                    if (bsf == null) {
                        throw new AnnotationException("No compile NeetAnnotationSupport");
                    }
                    final Method m = bsf.getDeclaredMethod("setSupportActionBar", new Class[]{aca, tb});
                    m.invoke(null, a, c);
                }
            } catch (Exception e) {
                throw new AnnotationException(e);
            }
        }
    }

    static boolean onCreateOptionsMenu(Object a, MenuInflater m, Menu b, Context c) {
        final NToolBar p = a.getClass().getAnnotation(NToolBar.class);
        final OptionsMenu d = a.getClass().getAnnotation(OptionsMenu.class);
        if (p != null && p.menuId() != 0) {
            m.inflate(p.menuId(), b);
        } else if (p != null && !p.menuResName().isEmpty()) {
            m.inflate(FindResources.id(BindBase.resPath(a, c), p.menuResName()), b);
        } else if (d != null && d.value() != 0) {
            m.inflate(d.value(), b);
        } else if (d != null && !d.resName().isEmpty()) {
            m.inflate(FindResources.id(BindBase.resPath(a, c), d.resName()), b);
        } else {
            return true;
        }
        Class<?> g = a.getClass();
        while (g != null && (g.isAnnotationPresent(NActivity.class) || g.isAnnotationPresent(NFragment.class))) {
            for (Field h : a.getClass().getDeclaredFields()) {
                final OptionsMenuItem i = h.getAnnotation(OptionsMenuItem.class);
                if (i == null) {
                    continue;
                }
                final Object j;
                if (i.value() != 0) {
                    j = b.findItem(i.value());
                } else if (!i.resName().isEmpty()) {
                    j = b.findItem(FindResources.id(BindBase.resPath(a, c), i.resName()));
                } else {
                    j = b.findItem(FindResources.id(BindBase.resPath(a, c), h.getName()));
                }
                try {
                    AnnotationUtil.set(h, a, j);
                } catch (IllegalAccessException k) {
                    k.printStackTrace();
                }
            }
            g = g.getSuperclass();
        }
        return true;
    }

    static boolean onOptionsItemSelected(Object a, MenuItem b, Context c) {
        Class<?> d = a.getClass();
        Method j = null;
        do {
            final NActivity q = d.getAnnotation(NActivity.class);
            final NFragment r = d.getAnnotation(NFragment.class);
            if (q != null || r != null) {
                final Method[] f = d.getDeclaredMethods();
                A:
                for (Method g : f) {
                    final OptionsItemSelected h = g.getAnnotation(OptionsItemSelected.class);
                    if (h == null) {
                        continue;
                    }
                    int[] i = BindMethod.findResourcesID(h.value(), h.resName(), g.getName(), "Selected", BindBase.resPath(a, c));
                    for (int k : i) {
                        if (k == b.getItemId()) {
                            j = g;
                            break A;
                        }
                    }
                }
            }
            d = d.getSuperclass();
        } while (d != null);
        if (j != null) {
            final Class<?>[] k = j.getParameterTypes();
            if (k.length == 0) {
                try {
                    AnnotationUtil.invoke(j, a);
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
                return true;
            } else if (k.length == 1 && MenuItem.class.isAssignableFrom(k[0])) {
                try {
                    AnnotationUtil.invoke(j, a, b);
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
                return true;
            }
        }
        return false;
    }
}
