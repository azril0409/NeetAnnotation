package library.neetoffice.com.neetannotation;

import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by Deo-chainmeans on 2017/11/15.
 */

class ItemSelectedListener implements AdapterView.OnItemSelectedListener {
    private static final String EXCEPTION_MESSAGE = "%s neet  (position(index or object item)) or (View,position(index or object item)) parameter";
    final Object a;
    final Method b;
    final int d;
    final Class<?> f;

    ItemSelectedListener(Object a, Method b) throws AnnotationException {
        this.a = a;
        this.b = b;
        final Class<?>[] c = b.getParameterTypes();
        if (c.length == 1) {
            d = 1;
            f = c[0];
        } else if (c.length == 2) {
            if (View.class.isAssignableFrom(c[0])) {
                d = 2;
                f = c[1];
            } else if (View.class.isAssignableFrom(c[1])) {
                d = 3;
                f = c[0];
            } else {
                throw new AnnotationException(String.format(EXCEPTION_MESSAGE, b.getName()));
            }
        } else {
            throw new AnnotationException(String.format(EXCEPTION_MESSAGE, b.getName()));
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        final Object o = parent.getItemAtPosition(position);
        try {
            switch (d) {
                case 1:
                    if (f == int.class) {
                        AnnotationUtil.invoke(b, a, position);
                    } else if (o != null && f.isAssignableFrom(o.getClass())) {
                        AnnotationUtil.invoke(b, a, o);
                    }
                    break;
                case 2:
                    if (f == int.class) {
                        AnnotationUtil.invoke(b, a, parent, position);
                    } else if (o != null && f.isAssignableFrom(o.getClass())) {
                        AnnotationUtil.invoke(b, a, parent, o);
                    }
                    break;
                case 3:
                    if (f == int.class) {
                        AnnotationUtil.invoke(b, a, position, parent);
                    } else if (o != null && f.isAssignableFrom(o.getClass())) {
                        AnnotationUtil.invoke(b, a, o, parent);
                    }
                    break;
            }
        } catch (IllegalAccessException e) {
        } catch (InvocationTargetException e) {
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
