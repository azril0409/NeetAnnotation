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
        final NFragment d = a.getClass().getAnnotation(NFragment.class);
        final View v;
        if (d != null && d.value() != 0) {
            v = LayoutInflater.from(a.getActivity()).inflate(d.value(), b, false);
        } else if (d != null && !d.resName().isEmpty()) {
            v = LayoutInflater.from(a.getActivity()).inflate(FindResources.layout(BindBase.resPath(a, a.getActivity()), d.resName()), b, false);
        } else {
            v = new View(a.getActivity());
        }
        return v;
    }

    static void onAfterCreateView(Fragment a, View v, Bundle w) {
        Class<?> c = a.getClass();
        final ArrayList<Method> j = new ArrayList<>();
        do {
            final NFragment q = c.getAnnotation(NFragment.class);
            if (q != null) {
                final Field[] f = c.getDeclaredFields();
                for (Field g : f) {
                    BindBase.bindViewById(a, v, g, a.getActivity());
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
