package library.neetoffice.com.neetannotation;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;

/**
 * Created by Deo on 2016/4/1.
 */
abstract class BindView {
    static void onCreate(ViewGroup a) {
        Class<?> c = a.getClass();
        final NViewGroup d = c.getAnnotation(NViewGroup.class);
        if (d != null && d.value() != -1) {
            ViewGroup.inflate(a.getContext(), d.value(), a);
        }
        final ArrayList<Method> j = new ArrayList<>();
        do {
            final NViewGroup q = c.getAnnotation(NViewGroup.class);
            if (q != null) {
                final Field[] f = c.getDeclaredFields();
                for (Field g : f) {
                    bindViewById(a, g);
                    BindBase.baseFieldBind(a, g, a.getContext());
                }
                final Method[] h = c.getDeclaredMethods();
                final TouchListener l = new TouchListener(a);
                for (Method i : h) {
                    BindBase.baseViewListenerBind(a, a, i, l);
                    if (BindMethod.isAfterAnnotationMethod(i)) {
                        j.add(i);
                    }
                }
            }
            c = c.getSuperclass();
        } while (c != null);
        for (int i = j.size() - 1; i >= 0; i--) {
            BindBase.callAfterAnnotationMethod(a, j.get(i), new Bundle());
        }
    }

    private static void bindViewById(ViewGroup a, Field b) {
        final ViewById c = b.getAnnotation(ViewById.class);
        if (c == null) {
            return;
        }
        try {
            final View d = a.findViewById(FindResources.id(a.getContext(), c.value(), b));
            if (d != null) {
                AnnotationUtil.set(b, a, d);
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}
