package library.neetoffice.com.neetannotation;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;

/**
 * Created by Deo on 2016/3/18.
 */
@NotProguard
public class BindSupport {

    @NotProguard
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
                    final SaveInstance d = g.getAnnotation(SaveInstance.class);
                    if (d == null) {
                        continue;
                    }
                    final String h;
                    if (d.value().length() > 0) {
                        h = d.value();
                    } else {
                        h = "_" + c.getName();
                    }
                    if (b != null) {
                        final Object i = b.get(h);
                        try {
                            AnnotationUtil.set(g, a, i);
                        } catch (IllegalAccessException e) {
                        }
                    }
                }
            }
            c = c.getSuperclass();
        } while (c != null);
    }

    @NotProguard
    static View onCreateView(Fragment a, ViewGroup b, Bundle w) {
        final NFragment d = a.getClass().getAnnotation(NFragment.class);
        final View v;
        if (d != null && d.value() != 0) {
            v = LayoutInflater.from(a.getActivity()).inflate(d.value(), b, false);
        } else if (d != null && !d.resName().isEmpty()) {
            v = LayoutInflater.from(a.getActivity()).inflate(FindResources.layout(BindBase.resPath(a, a.getActivity()), d.resName()), b, false);
        } else if (d != null) {
            v = LayoutInflater.from(a.getActivity()).inflate(FindResources.layout(BindBase.resPath(a, a.getActivity()), a.getClass().getSimpleName()), b, false);
        } else {
            v = new View(a.getActivity());
        }
        return v;
    }

    @NotProguard
    static void onBeforeAnnotation(Fragment a, View v, Bundle w) {
        Class<?> c = a.getClass();
        final ArrayList<Method> j = new ArrayList<>();
        do {
            final NFragment q = c.getAnnotation(NFragment.class);
            if (q != null) {
                final Field[] f = c.getDeclaredFields();
                for (Field g : f) {
                    bindViewById(a, v, g);
                    BindBase.baseFieldBind(a, g, a.getActivity());
                    bindArgument(a, g);
                    BindField.bindSaveInstance(a, g, w);
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

    @NotProguard
    static void setSupportActionBar(AppCompatActivity a, Toolbar b) {
        a.setSupportActionBar(b);
    }

    @NotProguard
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
                f = b.findViewById(FindResources.id(BindBase.resPath(a, a.getActivity()), c.getName()));
            }
            if (f != null) {
                AnnotationUtil.set(c, a, f);
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    @NotProguard
    static void bindFragmentById(Activity a, Field b) {
        final FragmentById c = b.getAnnotation(FragmentById.class);
        if (c == null) {
            return;
        }
        try {
            if (a instanceof FragmentActivity) {
                final Fragment i;
                if (c.value() != 0) {
                    i = ((FragmentActivity) a).getSupportFragmentManager().findFragmentById(c.value());
                } else if (!c.resName().isEmpty()) {
                    i = ((FragmentActivity) a).getSupportFragmentManager().findFragmentById(FindResources.id(BindBase.resPath(a, a), c.resName()));
                } else {
                    i = ((FragmentActivity) a).getSupportFragmentManager().findFragmentById(FindResources.id(BindBase.resPath(a, a), b.getName()));
                }
                AnnotationUtil.set(b, a, i);
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    @NotProguard
    static void bindFragmentByTag(Activity a, Field b) {
        final FragmentByTag c = b.getAnnotation(FragmentByTag.class);
        if (c == null) {
            return;
        }
        try {
            if (a instanceof FragmentActivity) {
                String t = c.value();
                if (t == null || t.isEmpty()) {
                    t = b.getName();
                }
                final Fragment i = ((FragmentActivity) a).getSupportFragmentManager().findFragmentByTag(t);
                AnnotationUtil.set(b, a, i);
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    @NotProguard
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

    @NotProguard
    static void bindArgument(android.support.v4.app.Fragment a, Field b) {
        final Extra c = b.getAnnotation(Extra.class);
        if (c == null) {
            return;
        }
        final Bundle d = a.getArguments();
        final String f = BindExtra.getExtra(b, c);
        final Object g = BindExtra.getExtraValue(d, f, b);
        try {
            AnnotationUtil.set(b, a, g);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    @NotProguard
    static Context getActivity(Fragment a) {
        return a.getActivity();
    }

    @NotProguard
    static void startActivity(Fragment a, Intent b) {
        a.startActivity(b);
    }

    @NotProguard
    static void startActivityForResult(Fragment a, Intent b, int c) {
        a.startActivityForResult(b, c);
    }
}
