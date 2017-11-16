package library.neetoffice.com.neetannotation;

import android.view.View;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by Deo on 2016/3/17.
 */
class ClickListener implements View.OnClickListener {
    private static final String EXCEPTION_MESSAGE = "%s need  () or (View) parameter";
    final Object a;
    final Method b;
    final int d;

    ClickListener(Object a, Method b) throws AnnotationException {
        this.a = a;
        this.b = b;
        final Class<?>[] c = b.getParameterTypes();
        if (c.length == 0) {
            d = 0;
        } else if (c.length == 1) {
            if (View.class.isAssignableFrom(c[0])) {
                d = 1;
            } else {
                throw new AnnotationException(String.format(EXCEPTION_MESSAGE, b.getName()));
            }
        } else {
            throw new AnnotationException(String.format(EXCEPTION_MESSAGE, b.getName()));
        }
    }

    @Override
    public void onClick(View v) {
        try {
            if (d == 0) {
                AnnotationUtil.invoke(b, a);
            } else if (d == 1) {
                AnnotationUtil.invoke(b, a, v);
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }
}
