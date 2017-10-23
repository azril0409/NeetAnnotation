package library.neetoffice.com.neetannotation;

import java.lang.reflect.Proxy;

/**
 * Created by Deo-chainmeans on 2017/9/5.
 */

public class HandlerHelp {
    public static <T> T newInstance(Class<T> interfaces, T object) {
        return (T) Proxy.newProxyInstance(interfaces.getClassLoader(), new Class<?>[]{interfaces}, new HandlerInvocationHandler(object));
    }
}
