package library.neetoffice.com.neetannotation;

import android.view.View;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

/**
 * Created by Deo on 2016/3/17.
 */
class LongClickListener implements View.OnLongClickListener {
    private static final String EXCEPTION_MESSAGE = "%s need  () or (View) parameter";
    final Object a;
    final Method b;
    final int d;

    LongClickListener(Object a, Method b) throws AnnotationException {
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
    public boolean onLongClick(View v) {
        try {
            if (d == 0) {
                if (b.getReturnType() == void.class) {
                    AnnotationUtil.invoke(b, a);
                } else if (b.getReturnType() == boolean.class) {
                    final Object e = AnnotationUtil.invoke(b, a);
                    return (boolean) e;
                } else if (b.getReturnType() == Boolean.class) {
                    final Object e = AnnotationUtil.invoke(b, a);
                    return (boolean) e;
                }
            } else if (d == 1) {
                if (b.getReturnType() == void.class) {
                    AnnotationUtil.invoke(b, a, v);
                } else if (b.getReturnType() == boolean.class) {
                    final Object e = AnnotationUtil.invoke(b, a, v);
                    return (boolean) e;
                } else if (b.getReturnType() == Boolean.class) {
                    final Object e = AnnotationUtil.invoke(b, a, v);
                    return (boolean) e;
                }
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return false;
    }

}
