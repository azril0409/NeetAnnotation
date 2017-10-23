package library.neetoffice.com.neetannotation;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by Deo-chainmeans on 2017/1/3.
 */

public abstract class AnnotationUtil {
    public static void set(Field a, Object object, Object value) throws IllegalAccessException {
        try {
            if (value == null) {
                return;
            }
            a.setAccessible(true);
            a.set(object, value);
        } catch (IllegalAccessException e) {
            throw e;
        } finally {
            a.setAccessible(false);
        }
    }

    public static Object get(Field a, Object object) throws IllegalAccessException {
        try {
            a.setAccessible(true);
            return a.get(object);
        } catch (IllegalAccessException e) {
            throw e;
        } finally {
            a.setAccessible(false);
        }
    }

    public static Object invoke(Method a, Object object, Object... value) throws InvocationTargetException, IllegalAccessException {
        try {
            a.setAccessible(true);
            return a.invoke(object, value);
        } catch (InvocationTargetException e) {
            throw e;
        } catch (IllegalAccessException e) {
            throw e;
        } finally {
            a.setAccessible(false);
        }
    }
}
