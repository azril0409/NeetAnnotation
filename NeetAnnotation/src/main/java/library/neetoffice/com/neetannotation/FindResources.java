package library.neetoffice.com.neetannotation;

import android.content.Context;

import java.lang.reflect.Field;

/**
 * Created by Deo-chainmeans on 2016/12/21.
 */

public class FindResources {

    private static int getValue(String a, String b) throws ClassNotFoundException, NoSuchFieldException, IllegalAccessException {
        Class clz = Class.forName(a);
        Field field = clz.getDeclaredField(b);
        return (int) field.get(null);
    }

    public static int id(Context a, String b) {
        try {
            return getValue(a.getPackageName() + ".R$id", b);
        } catch (Exception e) {
            throw new AnnotationException(String.format("No field %s in R.id", b));
        }
    }

    public static int string(Context a, ResString b, Field c) {
        try {
            final int id = b.value();
            if (id > 0) {
                return id;
            }
            return getValue(a.getPackageName() + ".R$string", c.getName());
        } catch (Exception e) {
            throw new AnnotationException(String.format("No field %s in R.string", b));
        }
    }

    public static int bool(Context a, ResBoolean b, Field c) {
        try {
            final int id = b.value();
            if (id > 0) {
                return id;
            }
            return getValue(a.getPackageName() + ".R$bool", c.getName());
        } catch (Exception e) {
            throw new AnnotationException(String.format("No field %s in R.bool", b));
        }
    }

    public static int dimen(Context a, ResDimen b, Field c) {
        try {
            final int id = b.value();
            if (id > 0) {
                return id;
            }
            return getValue(a.getPackageName() + ".R$dimen", c.getName());
        } catch (Exception e) {
            throw new AnnotationException(String.format("No field %s in R.dimen", b));
        }
    }

    public static int integer(Context a, ResInt b, Field c) {
        try {
            final int id = b.value();
            if (id > 0) {
                return id;
            }
            return getValue(a.getPackageName() + ".R$integer", c.getName());
        } catch (Exception e) {
            throw new AnnotationException(String.format("No field %s in R.integer", b));
        }
    }

    public static int array(Context a, ResStringArray b, Field c) {
        try {
            final int id = b.value();
            if (id > 0) {
                return id;
            }
            return getValue(a.getPackageName() + ".R$array", c.getName());
        } catch (Exception e) {
            throw new AnnotationException(String.format("No field %s in R.array", b));
        }
    }

    public static int array(Context a, ResIntArray b, Field c) {
        try {
            final int id = b.value();
            if (id > 0) {
                return id;
            }
            return getValue(a.getPackageName() + ".R$array", c.getName());
        } catch (Exception e) {
            throw new AnnotationException(String.format("No field %s in R.array", b));
        }
    }

    public static int anim(Context a, ResAnimation b, Field c) {
        try {
            final int id = b.value();
            if (id > 0) {
                return id;
            }
            return getValue(a.getPackageName() + ".R$anim", c.getName());
        } catch (Exception e) {
            throw new AnnotationException(String.format("No field %s in R.anim", b));
        }
    }

    public static int anim(Context a, ResLayoutAnimation b, Field c) {
        try {
            final int id = b.value();
            if (id > 0) {
                return id;
            }
            return getValue(a.getPackageName() + ".R$anim", c.getName());
        } catch (Exception e) {
            throw new AnnotationException(String.format("No field %s in R.anim", b));
        }
    }

    public static int color(Context a, ResColor b, Field c) {
        try {
            final int id = b.value();
            if (id > 0) {
                return id;
            }
            return getValue(a.getPackageName() + ".R$color", c.getName());
        } catch (Exception e) {
            throw new AnnotationException(String.format("No field %s in R.color", b));
        }
    }

    public static int drwable(Context a, ResDrawable b, Field c) {
        try {
            final int id = b.value();
            if (id > 0) {
                return id;
            }
            return getValue(a.getPackageName() + ".R$drwable", c.getName());
        } catch (Exception e) {
            throw new AnnotationException(String.format("No field %s in R.drwable", b));
        }
    }
}
