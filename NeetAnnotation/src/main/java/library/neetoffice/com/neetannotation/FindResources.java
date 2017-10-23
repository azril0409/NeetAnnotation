package library.neetoffice.com.neetannotation;

import android.content.Context;

import java.lang.reflect.Field;

/**
 * Created by Deo-chainmeans on 2016/12/21.
 */

public class FindResources {

    private static int getValue(String className,String fieldName) throws ClassNotFoundException, NoSuchFieldException, IllegalAccessException {
        Class clz = Class.forName(className);
        Field field = clz.getDeclaredField(fieldName);
        return (int) field.get(null);
    }

    public static int id(Context a, int b, Field c) throws ClassNotFoundException, NoSuchFieldException, IllegalAccessException {
        if (b > 0) {
            return b;
        }
        return getValue(a.getPackageName() + ".R$id",c.getName());
    }

    public static int string(Context a, ResString b, Field c) throws ClassNotFoundException, NoSuchFieldException, IllegalAccessException {
        final int id = b.value();
        if (id > 0) {
            return id;
        }
        return getValue(a.getPackageName() + ".R$string",c.getName());
    }

    public static int bool(Context a, ResBoolean b, Field c) throws ClassNotFoundException, NoSuchFieldException, IllegalAccessException {
        final int id = b.value();
        if (id > 0) {
            return id;
        }
        return getValue(a.getPackageName() + ".R$bool",c.getName());
    }

    public static int dimen(Context a, ResDimen b, Field c) throws ClassNotFoundException, NoSuchFieldException, IllegalAccessException {
        final int id = b.value();
        if (id > 0) {
            return id;
        }
        return getValue(a.getPackageName() + ".R$dimen",c.getName());
    }

    public static int integer(Context a, ResInt b, Field c) throws ClassNotFoundException, NoSuchFieldException, IllegalAccessException {
        final int id = b.value();
        if (id > 0) {
            return id;
        }
        return getValue(a.getPackageName() + ".R$integer",c.getName());
    }

    public static int array(Context a, ResStringArray b, Field c) throws ClassNotFoundException, NoSuchFieldException, IllegalAccessException {
        final int id = b.value();
        if (id > 0) {
            return id;
        }
        return getValue(a.getPackageName() + ".R$array",c.getName());
    }

    public static int anim(Context a, ResAnimation b, Field c) throws ClassNotFoundException, NoSuchFieldException, IllegalAccessException {
        final int id = b.value();
        if (id > 0) {
            return id;
        }
        return getValue(a.getPackageName() + ".R$anim",c.getName());
    }

    public static int anim(Context a, ResLayoutAnimation b, Field c) throws ClassNotFoundException, NoSuchFieldException, IllegalAccessException {
        final int id = b.value();
        if (id > 0) {
            return id;
        }
        return getValue(a.getPackageName() + ".R$anim",c.getName());
    }

    public static int color(Context a, ResColor b, Field c) throws ClassNotFoundException, NoSuchFieldException, IllegalAccessException {
        final int id = b.value();
        if (id > 0) {
            return id;
        }
        return getValue(a.getPackageName() + ".R$color",c.getName());
    }

    public static int drwable(Context a, ResDrawable b, Field c) throws ClassNotFoundException, NoSuchFieldException, IllegalAccessException {
        final int id = b.value();
        if (id > 0) {
            return id;
        }
        return getValue(a.getPackageName() + ".R$drwable",c.getName());
    }
}
