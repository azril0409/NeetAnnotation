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
    static void onCreate(View a) {
        Class<?> c = a.getClass();
        final NView d = c.getAnnotation(NView.class);
        if (d != null && d.value() != -1 &&  a instanceof ViewGroup) {
            ViewGroup.inflate(a.getContext(), d.value(), (ViewGroup)a);
        }
        final ArrayList<Method> j = new ArrayList<>();
        do {
            final NView q = c.getAnnotation(NView.class);
            if (q != null) {
                final Field[] f = c.getDeclaredFields();
                for (Field g : f) {
                    bindViewById(a, g);
                    BindBase.baseFieldBind(a, g, a.getContext());
                }
                final Method[] h = c.getDeclaredMethods();
                final TouchListener l = new TouchListener(a);
                for (Method i : h) {
                    BindBase.baseViewListenerBind(a, a, i, l, a.getContext());
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

    private static void bindViewById(View a, Field b) {
        final ViewById c = b.getAnnotation(ViewById.class);
        if (c == null) {
            return;
        }
        try {
            final View d;
            if (c.value() > 0) {
                d = a.findViewById(c.value());
            } else {
                d = a.findViewById(FindResources.id(a.getContext(), b.getName()));
            }
            if (d != null) {
                AnnotationUtil.set(b, a, d);
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}
