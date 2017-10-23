package library.neetoffice.com.neetannotation;

import android.content.Context;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by Deo-chainmeans on 2017/5/19.
 */

public class BindRestService {

    static void bind(Object a, Field b) {
        final RestService d = b.getAnnotation(RestService.class);
        if (d == null) {
            return;
        }
        try {
            final Class<?> cls = Class.forName("library.nettoffice.com.restapi.RestApiHelp");
            final Method method = cls.getMethod("create", new Class[]{Class.class});
            final Object f = method.invoke(null, b.getType());
            if (f != null) {
                AnnotationUtil.set(b, a, f);
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

    }
}
