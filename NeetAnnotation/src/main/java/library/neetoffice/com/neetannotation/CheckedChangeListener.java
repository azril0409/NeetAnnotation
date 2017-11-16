package library.neetoffice.com.neetannotation;

import android.view.View;
import android.widget.CompoundButton;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by Deo on 2016/4/1.
 */
class CheckedChangeListener implements CompoundButton.OnCheckedChangeListener {
    private static final String EXCEPTION_MESSAGE = "%s need  (boolean) or (View,boolean) or (boolean,View) parameter";
    final Object a;
    final Method b;
    final int d;

    CheckedChangeListener(Object a, Method b) {
        this.a = a;
        this.b = b;
        final Class<?>[] c = b.getParameterTypes();
        if (c.length == 1) {
            if (c[0] == boolean.class) {
                d = 0;
            } else if (c[0] == Boolean.class) {
                d = 0;
            } else {
                throw new AnnotationException(String.format(EXCEPTION_MESSAGE, b.getName()));
            }
        } else if (c.length == 2) {
            if (View.class.isAssignableFrom(c[0])) {
                d = 1;
            } else if (View.class.isAssignableFrom(c[1])) {
                d = 2;
            } else {
                throw new AnnotationException(String.format(EXCEPTION_MESSAGE, b.getName()));
            }
        } else {
            throw new AnnotationException(String.format(EXCEPTION_MESSAGE, b.getName()));
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        try {
            if (d == 0) {
                AnnotationUtil.invoke(b, a, isChecked);
            } else if (d == 1) {
                AnnotationUtil.invoke(b, a, buttonView, isChecked);
            } else if (d == 2) {
                AnnotationUtil.invoke(b, a, isChecked, buttonView);
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }
}
