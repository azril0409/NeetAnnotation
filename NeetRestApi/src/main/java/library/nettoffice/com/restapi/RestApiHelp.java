package library.nettoffice.com.restapi;

import android.content.Context;

import java.lang.reflect.Proxy;

/**
 * Created by Deo-chainmeans on 2017/5/19.
 */

public class RestApiHelp {

    public static <T> T create(final Context context, final Class<T> service) {
        final RestApi restApi = service.getAnnotation(RestApi.class);
        if (restApi == null) {
            return null;
        }
        final RestApiRequester restApiHelp = new RestApiRequester(context, restApi);
        return (T) Proxy.newProxyInstance(service.getClassLoader(), new Class<?>[]{service}, new RestInvocationHandler(restApiHelp));
    }
}
