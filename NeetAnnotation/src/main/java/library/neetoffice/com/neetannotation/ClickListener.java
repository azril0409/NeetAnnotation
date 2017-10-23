package library.neetoffice.com.neetannotation;

import android.view.View;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

/**
 * Created by Deo on 2016/3/17.
 */
class ClickListener implements View.OnClickListener {
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
                throw new AnnotationException(b.getName() + " neet  () or (View) parameter");
            }
        } else {
            throw new AnnotationException(b.getName() + " neet  () or (View) parameter");
        }
    }

    @Override
    public void onClick(View v) {
        if (d == 0) {
            try {
                AnnotationUtil.invoke(b, a);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        } else if (d == 1) {
            try {
                AnnotationUtil.invoke(b, a, v);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }
    }
}
