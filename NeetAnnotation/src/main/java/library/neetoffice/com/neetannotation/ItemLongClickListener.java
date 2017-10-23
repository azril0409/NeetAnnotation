package library.neetoffice.com.neetannotation;

import android.view.View;
import android.widget.AdapterView;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by Deo on 2016/3/18.
 */
class ItemLongClickListener implements AdapterView.OnItemLongClickListener {
    final Object a;
    final Method b;
    final int d;
    final Class<?> f;

    ItemLongClickListener(Object a, Method b) throws AnnotationException {
        this.a = a;
        this.b = b;
        final Class<?>[] c = b.getParameterTypes();
        if (c.length == 1) {
            d = 0;
            f = c[0];
        } else if (c.length == 2) {
            if (View.class.isAssignableFrom(c[0])) {
                d = 1;
                f = c[1];
            } else if (View.class.isAssignableFrom(c[1])) {
                d = 2;
                f = c[0];
            } else {
                throw new AnnotationException(b.getName() + " neet  (position(index or object item)) or (View,position(index or object item)) parameter");
            }
        } else {
            throw new AnnotationException(b.getName() + " neet  (position(index or object item)) or (View,position(index or object item)) parameter");
        }
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        final Object o = parent.getItemAtPosition(position);
        if (d == 0) {
            if (f == int.class) {
                try {
                    AnnotationUtil.invoke(b, a, position);
                    return true;
                } catch (IllegalAccessException e) {
                } catch (InvocationTargetException e) {
                }
            } else if (o != null && f.isAssignableFrom(o.getClass())) {
                try {
                    AnnotationUtil.invoke(b, a, o);
                    return true;
                } catch (IllegalAccessException e) {
                } catch (InvocationTargetException e) {
                }
            }
        } else if (d == 1) {
            if (f == int.class) {
                try {
                    AnnotationUtil.invoke(b, a, parent, position);
                    return true;
                } catch (IllegalAccessException e) {
                } catch (InvocationTargetException e) {
                }
            } else if (o != null && f.isAssignableFrom(o.getClass())) {
                try {
                    AnnotationUtil.invoke(b, a, parent, o);
                    return true;
                } catch (IllegalAccessException e) {
                } catch (InvocationTargetException e) {
                }
            }
        } else if (d == 2) {
            if (f == int.class) {
                try {
                    AnnotationUtil.invoke(b, a, position, parent);
                    return true;
                } catch (IllegalAccessException e) {
                } catch (InvocationTargetException e) {
                }
            } else if (o != null && f.isAssignableFrom(o.getClass())) {
                try {
                    AnnotationUtil.invoke(b, a, o, parent);
                    return true;
                } catch (IllegalAccessException e) {
                } catch (InvocationTargetException e) {
                }
            }
        }
        return false;
    }
}
