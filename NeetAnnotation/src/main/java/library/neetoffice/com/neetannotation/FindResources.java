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

    public static int id(String a, String b) {
        try {
            return getValue(a + ".R$id", b);
        } catch (Exception e) {
            throw new AnnotationException(String.format("No field %s in R.id", b));
        }
    }

    public static int string(String a, ResString b, Field c) {
        try {
            if (b.value() != 0) {
                return b.value();
            }
            if(!b.resName().isEmpty()){
                return getValue(a + ".R$string", b.resName());
            }
            return getValue(a + ".R$string", c.getName());
        } catch (Exception e) {
            throw new AnnotationException(String.format("No field %s in R.string", b));
        }
    }

    public static int bool(String a, ResBoolean b, Field c) {
        try {
            if (b.value() != 0) {
                return b.value();
            }
            if(!b.resName().isEmpty()){
                return getValue(a + ".R$bool", b.resName());
            }
            return getValue(a + ".R$bool", c.getName());
        } catch (Exception e) {
            throw new AnnotationException(String.format("No field %s in R.bool", b));
        }
    }

    public static int dimen(String a, ResDimen b, Field c) {
        try {
            if (b.value() != 0) {
                return b.value();
            }
            if(!b.resName().isEmpty()){
                return getValue(a + ".R$dimen", b.resName());
            }
            return getValue(a + ".R$dimen", c.getName());
        } catch (Exception e) {
            throw new AnnotationException(String.format("No field %s in R.dimen", b));
        }
    }

    public static int integer(String a, ResInt b, Field c) {
        try {
            if (b.value() != 0) {
                return b.value();
            }
            if(!b.resName().isEmpty()){
                return getValue(a + ".R$integer", b.resName());
            }
            return getValue(a + ".R$integer", c.getName());
        } catch (Exception e) {
            throw new AnnotationException(String.format("No field %s in R.integer", b));
        }
    }

    public static int array(String a, ResStringArray b, Field c) {
        try {
            if (b.value() != 0) {
                return b.value();
            }
            if(!b.resName().isEmpty()){
                return getValue(a + ".R$array", b.resName());
            }
            return getValue(a + ".R$array", c.getName());
        } catch (Exception e) {
            throw new AnnotationException(String.format("No field %s in R.array", b));
        }
    }

    public static int array(String a, ResIntArray b, Field c) {
        try {
            if (b.value() != 0) {
                return b.value();
            }
            if(!b.resName().isEmpty()){
                return getValue(a + ".R$array", b.resName());
            }
            return getValue(a + ".R$array", c.getName());
        } catch (Exception e) {
            throw new AnnotationException(String.format("No field %s in R.array", b));
        }
    }

    public static int anim(String a, ResAnimation b, Field c) {
        try {
            if (b.value() != 0) {
                return b.value();
            }
            if(!b.resName().isEmpty()){
                return getValue(a + ".R$anim", b.resName());
            }
            return getValue(a + ".R$anim", c.getName());
        } catch (Exception e) {
            throw new AnnotationException(String.format("No field %s in R.anim", b));
        }
    }

    public static int anim(String a, ResLayoutAnimation b, Field c) {
        try {
            if (b.value() != 0) {
                return b.value();
            }
            if(!b.resName().isEmpty()){
                return getValue(a + ".R$anim", b.resName());
            }
            return getValue(a + ".R$anim", c.getName());
        } catch (Exception e) {
            throw new AnnotationException(String.format("No field %s in R.anim", b));
        }
    }

    public static int color(String a, ResColor b, Field c) {
        try {
            if (b.value() != 0) {
                return b.value();
            }
            if(!b.resName().isEmpty()){
                return getValue(a + ".R$color", b.resName());
            }
            return getValue(a + ".R$color", c.getName());
        } catch (Exception e) {
            throw new AnnotationException(String.format("No field %s in R.color", b));
        }
    }

    public static int drwable(String a, ResDrawable b, Field c) {
        try {
            if (b.value() != 0) {
                return b.value();
            }
            if(!b.resName().isEmpty()){
                return getValue(a + ".R$drawable", b.resName());
            }
            return getValue(a + ".R$drawable", c.getName());
        } catch (Exception e) {
            throw new AnnotationException(String.format("No field %s in R.drawable", b));
        }
    }

    public static int layout(String a, String b) {
        try {
            return getValue(a + ".R$layout", b);
        } catch (Exception e) {
            throw new AnnotationException(String.format("No field %s in R.layout", b));
        }
    }
}
