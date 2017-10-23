package library.nettoffice.com.restapi;

import org.springframework.http.*;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.client.ResponseErrorHandler;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

/**
 * Created by Deo-chainmeans on 2017/3/21.
 */

class RestApiRequester implements RestApiSupport {
    RestTemplate restTemplate = new RestTemplate();
    String rootUrl;

    RestApiRequester(RestApi restApi) {
        final Class<? extends HttpMessageConverter>[] converters = restApi.converters();
        final Class<?> responseErrorHandler = restApi.responseErrorHandler();
        rootUrl = restApi.rootUrl();
        restTemplate.getMessageConverters().clear();
        restTemplate.getMessageConverters().add(new StringHttpMessageConverter());
        for (Class<? extends HttpMessageConverter> converter : converters) {
            try {
                restTemplate.getMessageConverters().add(converter.newInstance());
            } catch (Exception e) {
            }
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
