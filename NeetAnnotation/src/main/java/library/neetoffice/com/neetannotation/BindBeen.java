package library.neetoffice.com.neetannotation;

import android.app.Application;
import android.content.Context;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;

/**
 * Created by Deo on 2016/4/11.
 */
public class BindBeen {
    static void onCreate(Context c,Object h) {
        Class<?> f = h.getClass();
        final ArrayList<Method> b = new ArrayList<>();
        do {
            final Field[] i = f.getDeclaredFields();
            for (Field j : i) {
                BindField.bindBean(h, j, c);
                BindField.bindRootContext(h, j, c);
                BindField.bindApp(h, j, c);
                BindField.bindResString(h, j, c);
                BindField.bindResStringArray(h, j, c);
                BindField.bindResBoolean(h, j, c);
                BindField.bindResDimen(h, j, c);
                BindField.bindResInteger(h, j, c);
                BindField.bindResColor(h, j, c, c.getTheme());
                BindField.bindResDrawable(h, j, c, c.getTheme());
                BindField.bindSharedPreferences(h, j, c);
                BindField.bindHandler(h, j, c);
                BindField.bindSystemService(h, j, c);
                BindRestService.bind(h, j);
            }
            final Method[] g = f.getDeclaredMethods();
            for (Method l : g) {
                if (BindMethod.isAfterAnnotationMethod(l)) {
                    b.add(l);
                }
            }
            f = f.getSuperclass();
        } while (f != null);
        for (int i = b.size() - 1; i >= 0; i--) {
            try {
                final Method k = b.get(i);
                AnnotationUtil.invoke(k, h);
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }
}
