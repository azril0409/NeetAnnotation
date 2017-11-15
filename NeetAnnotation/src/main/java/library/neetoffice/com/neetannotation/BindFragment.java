package library.neetoffice.com.neetannotation;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by Deo on 2016/3/17.
 */
abstract class BindFragment {

    static void onCreate(Fragment a, Bundle b) {
        Class<?> c = a.getClass();
        boolean hasMenu = false;
        do {
            final NFragment q = c.getAnnotation(NFragment.class);
            if (q != null) {
                final OptionsMenu y = a.getClass().getAnnotation(OptionsMenu.class);
                hasMenu = hasMenu | y != null;
                a.setHasOptionsMenu(hasMenu);
                final Field[] f = c.getDeclaredFields();
                for (Field g : f) {
                    BindExtra.bindArgument(a, g);
                    BindField.bindSaveInstance(a, g, b);
                }
            }
            c = c.getSuperclass();
        } while (c != null);
    }

    static View onCreateView(Fragment a, ViewGroup b, Bundle w) {
        Class<?> c = a.getClass();
        final NFragment d = c.getAnnotation(NFragment.class);
        final View v;
        if (d != null && d.value() != -1) {
            v = LayoutInflater.from(a.getActivity()).inflate(d.value(), b, false);
        } else {
            v = new View(a.getActivity());
        }
        final ArrayList<Method> j = new ArrayList<>();
        do {
            final NFragment q = c.getAnnotation(NFragment.class);
            if (q != null) {
                final Field[] f = c.getDeclaredFields();
                for (Field g : f) {
                    bindViewById(a, v, g);
                    BindBase.baseFieldBind(a, g, a.getActivity());
                }
                final Method[] h = c.getDeclaredMethods();
                final TouchListener l = new TouchListener(a);
                for (Method i : h) {
                    BindBase.baseViewListenerBind(a, v, i, l, a.getActivity());
                    if (BindMethod.isAfterAnnotationMethod(i)) {
                        j.add(i);
                    }
                }
            }
            c = c.getSuperclass();
        } while (c != null);
        for (int i = j.size() - 1; i >= 0; i--) {
            BindBase.callAfterAnnotationMethod(a, j.get(i), w);
        }
        return v;
    }

    static void bindViewById(Fragment a, View b, Field c) {
        final ViewById d = c.getAnnotation(ViewById.class);
        if (d == null) {
            return;
        }
        try {
            final View f;
            if (d.value() > 0) {
                f = b.findViewById(d.value());
            } else {
                f = b.findViewById(FindResources.id(a.getActivity(), c.getName()));
            }
            if (f != null) {
                AnnotationUtil.set(c, a, f);
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    static void onSaveInstanceState(Fragment a, Bundle b) {
        Class<?> c = a.getClass();
        do {
            final NFragment q = c.getAnnotation(NFragment.class);
            if (q != null) {
                final Field[] f = c.getDeclaredFields();
                for (Field g : f) {
                    final SaveInstance d = g.getAnnotation(SaveInstance.class);
                    if (d == null) {
                        continue;
                    }
                    final String h;
                    if (d.value().length() > 0) {
                        h = d.value();
                    } else {
                        h = "_" + g.getName();
                    }
                    BindBundle.addFieldToBundle(a, g, h, b);
                }
            }
            c = c.getSuperclass();
        } while (c != null);
    }
}
