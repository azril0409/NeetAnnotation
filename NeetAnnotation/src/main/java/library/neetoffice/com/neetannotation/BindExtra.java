package library.neetoffice.com.neetannotation;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by Deo-chainmeans on 2017/5/21.
 */

abstract class BindExtra {

    static void bindExtra(Activity a, Field b) {
        final Extra c = b.getAnnotation(Extra.class);
        if (c == null) {
            return;
        }
        final Intent e = a.getIntent();
        if (e == null) {
            return;
        }
        final Bundle f = e.getExtras();
        if (f == null) {
            return;
        }
        final String d = getExtra(b, c);
        final Object g = getExtraValue(f, d, b);
        try {
            AnnotationUtil.set(b, a, g);
        } catch (IllegalAccessException e1) {
            e1.printStackTrace();
        }
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    static void bindArgument(android.app.Fragment a, Field b) {
        final Extra c = b.getAnnotation(Extra.class);
        if (c == null) {
            return;
        }
        final Bundle d = a.getArguments();
        final String f = getExtra(b, c);
        final Object g = getExtraValue(d, f, b);
        try {
            AnnotationUtil.set(b, a, g);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    static String getExtra(Field a, Extra b) {
        if ("".equals(b.value())) {
            return a.getName();
        } else {
            return b.value();
        }
    }

    static Object getExtraValue(Bundle f, String d, Field b) {
        if (f == null) {
            return null;
        }
        if (b.getType() == Boolean.TYPE) {
            final DefaultBoolean u = b.getAnnotation(DefaultBoolean.class);
            if (u != null) {
                return f.getBoolean(d, u.value());
            } else {
                return f.getBoolean(d);
            }
        } else if (b.getType() == Float.TYPE) {
            final DefaultFloat u = b.getAnnotation(DefaultFloat.class);
            if (u != null) {
                return f.getFloat(d, u.value());
            } else {
                return f.getFloat(d);
            }
        } else if (b.getType() == Integer.TYPE) {
            final DefaultInt u = b.getAnnotation(DefaultInt.class);
            if (u != null) {
                return f.getInt(d, u.value());
            } else {
                return f.getInt(d);
            }
        } else if (b.getType() == Long.TYPE) {
            final DefaultLong u = b.getAnnotation(DefaultLong.class);
            if (u != null) {
                return f.getLong(d, u.value());
            } else {
                return f.getLong(d);
            }
        } else if (b.getType() == String.class) {
            final DefaultString u = b.getAnnotation(DefaultString.class);
            if (u != null) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR1) {
                    return f.getString(d, u.value());
                } else {
                    return f.getString(d);
                }
            } else {
                return f.getString(d);
            }
        } else {
            final Object o = f.get(d);
            if (o == null) {
                return o;
            } else if (HashSet.class == b.getType()) {
                return new HashSet<>((Collection) o);
            } else if (Set.class == b.getType()) {
                return new HashSet<>((Collection) o);
            } else if (ArrayList.class == b.getType()) {
                return new ArrayList<>((Collection) o);
            } else if (List.class == b.getType()) {
                return new ArrayList<>((Collection) o);
            } else if (Collection.class == b.getType()) {
                return new ArrayList<>((Collection) o);
            } else {
                return o;
            }
        }
    }
}
