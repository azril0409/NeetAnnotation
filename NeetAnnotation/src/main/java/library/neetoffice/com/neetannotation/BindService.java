package library.neetoffice.com.neetannotation;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;

/**
 * Created by Deo on 2016/4/11.
 */
public class BindService {
    static void onCreate(Service a) {
        Class<?> d = a.getClass();
        final ArrayList<Method> j = new ArrayList<>();
        do {
            final NService f = d.getAnnotation(NService.class);
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
            try {
                final Method k = j.get(i);
                AnnotationUtil.invoke(k, a);
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    static void onStartCommand(Service a, Intent b) {
        Class<?> d = a.getClass();
        do {
            final NService f = d.getAnnotation(NService.class);
            if (f != null) {
                if (b != null) {
                    final Method[] i = d.getDeclaredMethods();
                    for (Method j : i) {
                        bindStartAction(a, j, b);
                    }
                }
            }
            d = d.getSuperclass();
        } while (d != null);
    }

    private static void bindStartAction(Service a, Method j, Intent c) {
        final StartAction b = j.getAnnotation(StartAction.class);
        if (b == null) {
            return;
        }
        final String action = c.getAction();
        final String methodAction = b.value();
        if ((action == null || action.isEmpty()) && methodAction.isEmpty()) {
        } else if (!action.isEmpty() && action.equals(methodAction)) {
        } else {
            return;
        }
        final Annotation[][] v = j.getParameterAnnotations();
        final Class<?>[] d = j.getParameterTypes();
        final Object[] t = new Object[d.length];
        for (int i = 0; i < d.length; i++) {
            final Class<?> f = d[i];
            if (f == Context.class) {
                t[i] = a;
            } else if (f == Intent.class) {
                t[i] = c;
            } else if (f == Bundle.class && c.getExtras() != null) {
                t[i] = c.getExtras();
            } else if (c.getExtras() != null) {
                final StartAction.Extra u = BindMethod.findParameterAnnotation(v[i], StartAction.Extra.class);
                if (u != null) {
                    t[i] = c.getExtras().get(u.value());
                }
            }
        }
        try {
            AnnotationUtil.invoke(j, a, t);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }
}
