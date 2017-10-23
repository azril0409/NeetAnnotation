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
                throw new AnnotationException(b.getName() + " neet  () or (View) or (String) or (View,String) or (String,String)or (View,String,String) parameter");
            }
        } else if (c.length == 2) {
            if (View.class.isAssignableFrom(c[0]) && String.class.isAssignableFrom(c[1])) {
                d = 3;
            } else if (String.class.isAssignableFrom(c[0]) && View.class.isAssignableFrom(c[1])) {
                d = 4;
            } else if (String.class.isAssignableFrom(c[0]) && String.class.isAssignableFrom(c[1])) {
                d = 5;
            } else {
                throw new AnnotationException(b.getName() + " neet  () or (View) or (String) or (View,String) or (String,String)or (View,String,String) parameter");
            }
        } else if (c.length == 3) {
            if (View.class.isAssignableFrom(c[0]) && String.class.isAssignableFrom(c[1]) && String.class.isAssignableFrom(c[2])) {
                d = 6;
            } else if (String.class.isAssignableFrom(c[0]) && View.class.isAssignableFrom(c[1]) && String.class.isAssignableFrom(c[2])) {
                d = 7;
            } else if (String.class.isAssignableFrom(c[0]) && String.class.isAssignableFrom(c[1]) && View.class.isAssignableFrom(c[2])) {
                d = 8;
            } else {
                throw new AnnotationException(b.getName() + " neet  () or (View) or (String) or (View,String) or (String,String)or (View,String,String) parameter");
            }
        } else {
            throw new AnnotationException(b.getName() + " neet  () or (View) or (String) or (View,String) or (String,String)or (View,String,String) parameter");
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
        if (d == 0) {
            try {
                AnnotationUtil.invoke(b,a);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        } else if (d == 1) {
            try {
                AnnotationUtil.invoke(b,a, l);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        } else if (d == 2) {
            try {
                AnnotationUtil.invoke(b,a, s.toString());
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        } else if (d == 3) {
            try {
                AnnotationUtil.invoke(b,a, l, s.toString());
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        } else if (d == 4) {
            try {
                AnnotationUtil.invoke(b,a, s.toString(), l);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        } else if (d == 5) {
            try {
                AnnotationUtil.invoke(b,a, old, s.toString());
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        } else if (d == 6) {
            try {
                AnnotationUtil.invoke(b,a, l, old, s.toString());
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        } else if (d == 7) {
            try {
                AnnotationUtil.invoke(b,a, old, l, s.toString());
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        } else if (d == 8) {
            try {
                AnnotationUtil.invoke(b,a, old, s.toString(), l);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }
    }
}
