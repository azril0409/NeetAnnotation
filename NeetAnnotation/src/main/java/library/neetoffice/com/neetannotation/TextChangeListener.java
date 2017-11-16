package library.neetoffice.com.neetannotation;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

/**
 * Created by Deo on 2016/4/6.
 */
class TextChangeListener implements TextWatcher {
    private static final String EXCEPTION_MESSAGE = "%s need  () or (View) or (String) or (View,String) or (String,String)or (View,String,String) parameter";
    final Object a;
    final Method b;
    final View l;
    final int d;
    String old;

    public TextChangeListener(View l, Object a, Method b) {
        this.a = a;
        this.b = b;
        this.l = l;
        final Class<?>[] c = b.getParameterTypes();
        if (c.length == 0) {
            d = 0;
        } else if (c.length == 1) {
            if (View.class.isAssignableFrom(c[0])) {
                d = 1;
            } else if (String.class.isAssignableFrom(c[0])) {
                d = 2;
            } else {
                throw new AnnotationException(String.format(EXCEPTION_MESSAGE, b.getName()));
            }
        } else if (c.length == 2) {
            if (View.class.isAssignableFrom(c[0]) && String.class.isAssignableFrom(c[1])) {
                d = 3;
            } else if (String.class.isAssignableFrom(c[0]) && View.class.isAssignableFrom(c[1])) {
                d = 4;
            } else if (String.class.isAssignableFrom(c[0]) && String.class.isAssignableFrom(c[1])) {
                d = 5;
            } else {
                throw new AnnotationException(String.format(EXCEPTION_MESSAGE, b.getName()));
            }
        } else if (c.length == 3) {
            if (View.class.isAssignableFrom(c[0]) && String.class.isAssignableFrom(c[1]) && String.class.isAssignableFrom(c[2])) {
                d = 6;
            } else if (String.class.isAssignableFrom(c[0]) && View.class.isAssignableFrom(c[1]) && String.class.isAssignableFrom(c[2])) {
                d = 7;
            } else if (String.class.isAssignableFrom(c[0]) && String.class.isAssignableFrom(c[1]) && View.class.isAssignableFrom(c[2])) {
                d = 8;
            } else {
                throw new AnnotationException(String.format(EXCEPTION_MESSAGE, b.getName()));
            }
        } else {
            throw new AnnotationException(String.format(EXCEPTION_MESSAGE, b.getName()));
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        old = s.toString();
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        try {
            switch (d) {
                case 0:
                    AnnotationUtil.invoke(b, a);
                    break;
                case 1:
                    AnnotationUtil.invoke(b, a, l);
                    break;
                case 2:
                    AnnotationUtil.invoke(b, a, s.toString());
                    break;
                case 3:
                    AnnotationUtil.invoke(b, a, l, s.toString());
                    break;
                case 4:
                    AnnotationUtil.invoke(b, a, s.toString(), l);
                    break;
                case 5:
                    AnnotationUtil.invoke(b, a, old, s.toString());
                    break;
                case 6:
                    AnnotationUtil.invoke(b, a, l, old, s.toString());
                    break;
                case 7:
                    AnnotationUtil.invoke(b, a, old, l, s.toString());
                    break;
                case 8:
                    AnnotationUtil.invoke(b, a, old, s.toString(), l);
                    break;
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }
}
