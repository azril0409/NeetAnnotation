package library.neetoffice.com.neetannotation;

import android.app.Application;
import android.os.Bundle;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;

/**
 * Created by Deo on 2016/4/11.
 */
public class BindApplication {
    static void onCreate(Application a) {
        Class<?> d = a.getClass();
        final ArrayList<Method> j = new ArrayList<>();
        do {
            final NApplication f = d.getAnnotation(NApplication.class);
            if (f != null) {
                final Field[] g = d.getDeclaredFields();
                for (Field h : g) {
                    BindBase.baseFieldBind(a, h, a);
                }
                final Method[] h = d.getDeclaredMethods();
                for (Method i : h) {
                    if (BindMethod.isAfterAnnotationMethod(i)) {
                        j.add(i);
                    }
                }
            }
            d = d.getSuperclass();
        } while (d != null);
        for (int i = j.size() - 1; i >= 0; i--) {
            BindBase.callAfterAnnotationMethod(a, j.get(i), new Bundle());
        }
    }
}
