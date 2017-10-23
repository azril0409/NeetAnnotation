package library.nettoffice.com.restapi;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.lang.annotation.Annotation;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashMap;

/**
 * Created by Deo-chainmeans on 2017/5/19.
 */

class RestInvocationHandler implements InvocationHandler {
    private final RestApiRequester restApiHelp;

    RestInvocationHandler(RestApiRequester restApiHelp) {
        this.restApiHelp = restApiHelp;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        final Class<?> clz = method.getDeclaringClass();
        if (clz == Object.class) {
            return method.invoke(this, args);
        }
        final String name = method.getName();
        if ("getRestTemplate".equals(name) && method.getReturnType() == RestTemplate.class) {
            return restApiHelp.getRestTemplate();
        } else if ("setRestTemplate".equals(name) && args.length == 1 && args[0] instanceof RestTemplate) {
            restApiHelp.setRestTemplate((RestTemplate) args[0]);
            return null;
        } else if ("setRootUrl".equals(name) && args.length == 1 && args[0] instanceof String) {
            restApiHelp.setRootUrl((String) args[0]);
            return null;
        }


        final HttpHeaders httpHeaders = new HttpHeaders();
        final Header header = method.getAnnotation(Header.class);
        if (header != null) {
            httpHeaders.set(header.name(), header.value());
        }
        final Headers headers = method.getAnnotation(Headers.class);
        if (headers != null) {
            for (Header value : headers.value()) {
                httpHeaders.set(value.name(), value.value());
            }
        }
        final HashMap<String, String> cookies = new HashMap<>();
        final HashMap<String, String> pathMap = new HashMap<>();
        Object body = null;
        if (args != null) {
            final Annotation[][] t = method.getParameterAnnotations();
            for (int i = 0; i < t.length; i++) {
                final Object arg = args[i];
                for (Annotation a : t[i]) {
                    if (a.annotationType() == Header.class) {
                        httpHeaders.set(((Header) a).name(), arg.toString());
                    }
                    if (a.annotationType() == Cookie.class) {
                        cookies.put(((Cookie) a).value(), arg.toString());
                    }
                    if (a.annotationType() == Path.class) {
                        pathMap.put(((Path) a).value(), arg.toString());
                    }
                    if (a.annotationType() == Body.class) {
                        body = arg;
                    }
                }
            }
        }

        final Accept accept = method.getAnnotation(Accept.class);
        final ContentType contentType = method.getAnnotation(ContentType.class);

        final Get get = method.getAnnotation(Get.class);
        final Post post = method.getAnnotation(Post.class);
        final Put put = method.getAnnotation(Put.class);
        final Delete delete = method.getAnnotation(Delete.class);
        final Patch patch = method.getAnnotation(Patch.class);
        RestBuild build = null;
        if (get != null) {
            build = RestBuildMaker.with(restApiHelp.rootUrl, get, httpHeaders, cookies, accept, contentType, pathMap, body);
        } else if (post != null) {
            build = RestBuildMaker.with(restApiHelp.rootUrl, post, httpHeaders, cookies, accept, contentType, pathMap, body);
        } else if (put != null) {
            build = RestBuildMaker.with(restApiHelp.rootUrl, put, httpHeaders, cookies, accept, contentType, pathMap, body);
        } else if (delete != null) {
            build = RestBuildMaker.with(restApiHelp.rootUrl, delete, httpHeaders, cookies, accept, contentType, pathMap, body);
        } else if (patch != null) {
            build = RestBuildMaker.with(restApiHelp.rootUrl, patch, httpHeaders, cookies, accept, contentType, pathMap, body);
        }

        if (build == null) {
            return null;
        }

        final Type returnType = method.getReturnType();
        if (returnType == void.class) {
            restApiHelp.request(build, new Reference(null));
        } else if (returnType == Call.class) {
            final Type type = ParameterizedType.class.cast(method.getGenericReturnType()).getActualTypeArguments()[0];
            Call call = new CallImpl(restApiHelp, build, type);
            return call;
        } else if (returnType == ResponseEntity.class) {
            final Type type = ParameterizedType.class.cast(method.getGenericReturnType()).getActualTypeArguments()[0];
            return restApiHelp.request(build, new Reference(type));
        } else {
            return restApiHelp.request(build, new Reference(returnType)).getBody();
        }
        return null;
    }
}
