package library.neetoffice.com.neetannotation;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toolbar;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

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
        if (b.resId() != -1) {
            c = a.findViewById(b.resId());
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

    static boolean onCreateOptionsMenu(Object a, MenuInflater m, Menu b) {
        final NToolBar c = a.getClass().getAnnotation(NToolBar.class);
        final OptionsMenu d = a.getClass().getAnnotation(OptionsMenu.class);
        final int e;
        if (c != null) {
            e = c.menuId();
        } else if (d != null) {
            e = d.value();
        } else {
            return true;
        }
        if (e > 0) {
            m.inflate(e, b);
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
                A:for (Method g : f) {
                    final OptionsItem h = g.getAnnotation(OptionsItem.class);
                    if (h == null) {
                        continue;
                    }
                    int[] i = BindMethod.findResourcesID(h.value(), g.getName(), "Selected", c);
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
