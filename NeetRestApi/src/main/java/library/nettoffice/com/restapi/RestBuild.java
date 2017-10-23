package library.nettoffice.com.restapi;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.converter.HttpMessageConverter;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Deo-chainmeans on 2017/5/19.
 */

public class RestBuild {
    final HttpMethod method;
    final String url;
    final HttpHeaders httpHeaders;
    final HashMap<String, String> pathMap;
    final Object body;

    public RestBuild(HttpMethod method, String url, HttpHeaders httpHeaders, HashMap<String, String> pathMap, Object body) {
        this.method = method;
        this.url = url;
        this.httpHeaders = httpHeaders;
        this.pathMap = pathMap;
        this.body = body;
    }
}
