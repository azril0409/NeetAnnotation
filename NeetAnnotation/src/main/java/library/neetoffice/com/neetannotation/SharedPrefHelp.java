package library.neetoffice.com.neetannotation;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import java.lang.reflect.Proxy;

/**
 * Created by Deo-chainmeans on 2017/9/8.
 */

public class SharedPrefHelp {

    public static <T> T newInstance(Context context, Class<T> SharedPrefInterface) {
        final SharedPref g = SharedPrefInterface.getAnnotation(SharedPref.class);
        if (g == null) {
            return null;
        }
        final ResPath r = SharedPrefInterface.getAnnotation(ResPath.class);
        final String f;
        if (r != null && !r.value().isEmpty()) {
            f = r.value();
        } else {
            f = context.getPackageName();
        }
        final SharedPreferences h;
        if (g.value() == SharedPref.Scope.Default) {
            h = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());
        } else {
            h = context.getSharedPreferences(context.getClass().getSimpleName() + "_preferences", Context.MODE_PRIVATE);
        }
        final Object i = Proxy.newProxyInstance(SharedPrefInterface.getClassLoader(), new Class<?>[]{SharedPrefInterface}, new SharedPrefInvocationHandler(context, f, h));
        return (T) i;
    }

}
