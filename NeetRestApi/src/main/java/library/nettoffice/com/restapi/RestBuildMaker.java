package library.nettoffice.com.restapi;

import android.util.Log;

import org.springframework.http.HttpAuthentication;
import org.springframework.http.HttpBasicAuthentication;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by Deo-chainmeans on 2017/5/19.
 */

abstract class RestBuildMaker {
    static RestBuild with(final String url, HttpMethod method, final HttpHeaders httpHeaders, final HashMap<String, String> cookies, final Accept accept, final ContentType contentType, final HttpAuthentication authentication, final HashMap<String, String> pathMap, final Object body) {
        if (!cookies.isEmpty()) {
            if (httpHeaders.containsKey("Cookie")) {
                final List<String> cookieList = httpHeaders.get("Cookie");
                if (cookieList.size() > 0) {
                    final String cookie = String.format("%s;%s", cookieList.get(0), getCookie(cookies));
                    cookieList.set(0, cookie);
                } else {
                    httpHeaders.add("Cookie", getCookie(cookies));
                }
            } else {
                httpHeaders.add("Cookie", getCookie(cookies));
            }
        }
        if (contentType != null) {
            httpHeaders.setContentType(getContentType(contentType));
        }
        if (accept != null) {
            httpHeaders.setAccept(getAccepts(accept));
        }
        if (authentication != null) {
            httpHeaders.setAuthorization(authentication);
        }
        Log.d(RestBuildMaker.class.getSimpleName(), "url = " + url);
        return new RestBuild(method, url, httpHeaders, pathMap, body);
    }

    static RestBuild with(final String rootUrl, final Get get, final HttpHeaders httpHeaders, final HashMap<String, String> cookies, final Accept accept, final ContentType contentType, final HttpAuthentication authentication, final HashMap<String, String> pathMap, final HashMap<String, Object> fieldMap, final Object body) {
        String url = rootUrl + get.value();
        final Set<Map.Entry<String, Object>> set = fieldMap.entrySet();
        for (Map.Entry<String, Object> entry : set) {
            final String value = entry.getValue().toString();
            if (url.contains("?")) {
                url += String.format("&%s=%s", entry.getKey(), value);
            } else {
                url += String.format("?%s=%s", entry.getKey(), value);
            }
        }
        return with(url, HttpMethod.GET, httpHeaders, cookies, accept, contentType, authentication, pathMap, null);
    }

    static RestBuild with(final String rootUrl, final Post post, final HttpHeaders httpHeaders, final HashMap<String, String> cookies, final Accept accept, final ContentType contentType, final HttpAuthentication authentication, final HashMap<String, String> pathMap, final HashMap<String, Object> fieldMap, Object body) {
        final String url = rootUrl + post.value();
        if (body == null) {
            body = new HashMap<>();
        }
        if (body instanceof Map) {
            for (Map.Entry<String, Object> e : fieldMap.entrySet()) {
                ((Map) body).put(e.getKey(), e.getValue());
            }
        }
        return with(url, HttpMethod.POST, httpHeaders, cookies, accept, contentType, authentication, pathMap, body);
    }

    static RestBuild with(final String rootUrl, final Put put, final HttpHeaders httpHeaders, final HashMap<String, String> cookies, final Accept accept, final ContentType contentType, final HttpAuthentication authentication, final HashMap<String, String> pathMap, final HashMap<String, Object> fieldMap, Object body) {
        final String url = rootUrl + put.value();
        if (body == null) {
            body = new HashMap<>();
        }
        if (body instanceof Map) {
            for (Map.Entry<String, Object> e : fieldMap.entrySet()) {
                ((Map) body).put(e.getKey(), e.getValue());
            }
        }
        return with(url, HttpMethod.PUT, httpHeaders, cookies, accept, contentType, authentication, pathMap, body);
    }

    static RestBuild with(final String rootUrl, final Delete delete, final HttpHeaders httpHeaders, final HashMap<String, String> cookies, final Accept accept, final ContentType contentType, final HttpAuthentication authentication, final HashMap<String, String> pathMap, final HashMap<String, Object> fieldMap, Object body) {
        final String url = rootUrl + delete.value();
        if (body == null) {
            body = new HashMap<>();
        }
        if (body instanceof Map) {
            for (Map.Entry<String, Object> e : fieldMap.entrySet()) {
                ((Map) body).put(e.getKey(), e.getValue());
            }
        }
        return with(url, HttpMethod.DELETE, httpHeaders, cookies, accept, contentType, authentication, pathMap, body);
    }

    static RestBuild with(final String rootUrl, final Patch patch, final HttpHeaders httpHeaders, final HashMap<String, String> cookies, final Accept accept, final ContentType contentType, final HttpAuthentication authentication, final HashMap<String, String> pathMap, final HashMap<String, Object> fieldMap, Object body) {
        final String url = rootUrl + patch.value();
        if (body == null) {
            body = new HashMap<>();
        }
        if (body instanceof Map) {
            for (Map.Entry<String, Object> e : fieldMap.entrySet()) {
                ((Map) body).put(e.getKey(), e.getValue());
            }
        }
        return with(url, HttpMethod.PATCH, httpHeaders, cookies, accept, contentType, authentication, pathMap, body);
    }

    private static org.springframework.http.MediaType getMediaType(String accept, String charset) {
        final org.springframework.http.MediaType mediaType = org.springframework.http.MediaType.parseMediaType(accept);
        return new org.springframework.http.MediaType(mediaType.getType(), mediaType.getSubtype(), Charset.forName(charset));
    }

    private static org.springframework.http.MediaType getContentType(ContentType contentType) {
        return getMediaType(contentType.value(), contentType.charset());
    }

    private static List<org.springframework.http.MediaType> getAccepts(Accept accept) {
        return Collections.singletonList(getMediaType(accept.contentType(), accept.charset()));
    }

    private static String getCookie(HashMap<String, String> cookies) {
        final StringBuffer cookiesValue = new StringBuffer();
        final Iterator<Map.Entry<String, String>> iterator = cookies.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, String> entry = iterator.next();
            cookiesValue.append(String.format("%s=%s;", entry.getKey(), entry.getValue()));
        }
        return cookiesValue.toString();
    }
}
