package library.neetoffice.com.neetannotation;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by Deo on 2016/4/6.
 */
abstract class BindBroadcastReceiver {
    static void onReceive(BroadcastReceiver a, Context b, Intent c) {
        Class<?> d = a.getClass();
        do {
            final NReceiver f = d.getAnnotation(NReceiver.class);
            if (f != null) {
                final Field[] g = d.getDeclaredFields();
                for (Field h : g) {
                    BindBase.baseFieldBind(a, h, b);
                }
                if (c != null) {
                    final Method[] i = d.getDeclaredMethods();
                    for (Method j : i) {
                        bindReceiverAction(a, b, j, c);
                    }
                }
            }
            d = d.getSuperclass();
        } while (d != null);
    }

    private static void bindReceiverAction(BroadcastReceiver a, Context p, Method j, Intent c) {
        final ReceiverAction b = j.getAnnotation(ReceiverAction.class);
        if (b == null) {
            return;
        }
        final String action = c.getAction();
        if (!action.equals(b.value())) {
            return;
        }
        final Annotation[][] v = j.getParameterAnnotations();
        final Class<?>[] d = j.getParameterTypes();
        final Object[] t = new Object[d.length];
        for (int i = 0; i < d.length; i++) {
            final Class<?> f = d[i];
            final Extra g = f.getAnnotation(Extra.class);
            if (f == Context.class) {
                t[i] = a;
            } else if (f == Intent.class) {
                t[i] = c;
            } else if (f == Bundle.class && c.getExtras() != null) {
                t[i] = c.getExtras();
            } else {
                final ReceiverAction.Extra u = BindMethod.findParameterAnnotation(v[i], ReceiverAction.Extra.class);
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
