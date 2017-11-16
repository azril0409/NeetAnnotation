package library.nettoffice.com.restapi;

import android.content.Context;
import android.util.Log;

import org.springframework.http.*;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.HttpComponentsAndroidClientHttpRequestFactory;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.client.ResponseErrorHandler;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.lang.reflect.Method;
import java.util.ArrayList;

/**
 * Created by Deo-chainmeans on 2017/3/21.
 */
class RestApiRequester implements RestApiSupport {
    RestTemplate restTemplate = new RestTemplate();
    String rootUrl;

    RestApiRequester(Context context, RestApi restApi) {
        final Class<? extends HttpMessageConverter>[] converters = restApi.converters();
        final Class<?> responseErrorHandler = restApi.responseErrorHandler();
        rootUrl = restApi.rootUrl();
        restTemplate.getMessageConverters().clear();
        for (Class<? extends HttpMessageConverter> converter : converters) {
            restTemplate.getMessageConverters().add(newInstance(context, converter));
        }
        ArrayList<ClientHttpRequestInterceptor> interceptors = new ArrayList<>();
        for (Class<? extends ClientHttpRequestInterceptor> interceptor : restApi.interceptors()) {
            interceptors.add(newInstance(context, interceptor));
        }
        restTemplate.setInterceptors(interceptors);
        if (restApi.requestFactory() != HttpComponentsAndroidClientHttpRequestFactory.class) {
            restTemplate.setRequestFactory(newInstance(context, restApi.requestFactory()));
        }
        try {
            final Object errorHandler = responseErrorHandler.newInstance();
            if (errorHandler instanceof ResponseErrorHandler) {
                restTemplate.setErrorHandler((ResponseErrorHandler) errorHandler);
            }
        } catch (Exception e) {
        }
    }

    <Response> ResponseEntity<Response> request(RestBuild build, Reference reference) throws RestClientException {
        final HttpHeaders httpHeaders = build.httpHeaders;
        final HttpEntity<Object> requestEntity = new HttpEntity<>(build.body, httpHeaders);
        final ResponseEntity responseEntity = restTemplate.exchange(build.url, build.method, requestEntity, reference, build.pathMap);
        return responseEntity;
    }

    private <T> T newInstance(Context context, Class<T> tClass) {
        try {
            final Class<?> bhc = Class.forName("library.neetoffice.com.neetannotation.BeanHelp");
            final Method m = bhc.getDeclaredMethod("newInstance", new Class[]{Context.class, Class.class});
            return (T) m.invoke(null, context, tClass);
        } catch (Exception e) {
            try {
                return tClass.newInstance();
            } catch (Exception e1) {
                throw new RuntimeException(e1);
            }
        }
    }

    @Override
    public RestTemplate getRestTemplate() {
        return restTemplate;
    }

    @Override
    public void setRestTemplate(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public void setRootUrl(String rootUrl) {
        this.rootUrl = rootUrl;
    }
}
