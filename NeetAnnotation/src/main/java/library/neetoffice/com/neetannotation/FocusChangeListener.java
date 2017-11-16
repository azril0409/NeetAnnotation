package library.neetoffice.com.neetannotation;

import android.view.View;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

/**
 * Created by Deo on 2016/4/6.
 */
public class FocusChangeListener implements View.OnFocusChangeListener {
    private static final String EXCEPTION_MESSAGE = "%s need  () or (View) or (boolean) or (View,boolean) or (boolean,View) parameter";
    final Object a;
    final Method b;
    final int d;

    public FocusChangeListener(Object a, Method b) {
        this.a = a;
        this.b = b;
        final Class<?>[] c = b.getParameterTypes();
        if (c.length == 0) {
            d = 0;
        } else if (c.length == 1) {
            if (View.class.isAssignableFrom(c[0])) {
                d = 1;
            } else if (c[0] == boolean.class) {
                d = 2;
            } else if (c[0] == Boolean.class) {
                d = 2;
            } else {
                throw new AnnotationException(String.format(EXCEPTION_MESSAGE, b.getName()));
            }
        } else if (c.length == 2) {
            if (View.class.isAssignableFrom(c[0]) && (c[1] == boolean.class || c[1] == Boolean.class)) {
                d = 3;
            } else if (View.class.isAssignableFrom(c[1]) && (c[0] == boolean.class || c[0] == Boolean.class)) {
                d = 4;
            } else {
                throw new AnnotationException(String.format(EXCEPTION_MESSAGE, b.getName()));
            }
        } else {
            throw new AnnotationException(String.format(EXCEPTION_MESSAGE, b.getName()));
        }
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        try {
            if (d == 0) {
                AnnotationUtil.invoke(b, a);
            } else if (d == 1) {
                AnnotationUtil.invoke(b, a, v);
            } else if (d == 2) {
                AnnotationUtil.invoke(b, a, hasFocus);
            } else if (d == 3) {
                AnnotationUtil.invoke(b, a, v, hasFocus);
            } else if (d == 4) {
                AnnotationUtil.invoke(b, a, hasFocus, v);
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }
}
