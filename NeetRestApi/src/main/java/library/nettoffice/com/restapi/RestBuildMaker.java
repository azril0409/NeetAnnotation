package library.nettoffice.com.restapi;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;

import java.nio.charset.Charset;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by Deo-chainmeans on 2017/5/19.
 */

abstract class RestBuildMaker {
    static RestBuild with(final String rootUrl, final Get get, final HttpHeaders httpHeaders, final HashMap<String, String> cookies, final Accept accept, final ContentType contentType, final HashMap<String, String> pathMap, final Object body) {
        final String url = rootUrl + get.value();
        if(!cookies.isEmpty()){
            httpHeaders.add("Cookie", getCookie(cookies));
        }
        httpHeaders.setContentType(getContentType(contentType));
        httpHeaders.setAccept(getAccepts(accept));
        return new RestBuild(HttpMethod.GET, url, httpHeaders, pathMap, body);
    }

    static RestBuild with(final String rootUrl, final Post post, final HttpHeaders httpHeaders, final HashMap<String, String> cookies, final Accept accept, final ContentType contentType, final HashMap<String, String> pathMap, final Object body) {
        final String url = rootUrl + post.value();
        if(!cookies.isEmpty()){
            httpHeaders.add("Cookie", getCookie(cookies));
        }
        httpHeaders.setContentType(getContentType(contentType));
        httpHeaders.setAccept(getAccepts(accept));
        return new RestBuild(HttpMethod.POST, url, httpHeaders, pathMap, body);
    }

    static RestBuild with(final String rootUrl, final Put put, final HttpHeaders httpHeaders, final HashMap<String, String> cookies, final Accept accept, final ContentType contentType, final HashMap<String, String> pathMap, final Object body) {
        final String url = rootUrl + put.value();
        if(!cookies.isEmpty()){
            httpHeaders.add("Cookie", getCookie(cookies));
        }
        httpHeaders.setContentType(getContentType(contentType));
        httpHeaders.setAccept(getAccepts(accept));
        return new RestBuild(HttpMethod.PUT, url, httpHeaders, pathMap, body);
    }

    static RestBuild with(final String rootUrl, final Delete delete, final HttpHeaders httpHeaders, final HashMap<String, String> cookies, final Accept accept, final ContentType contentType, final HashMap<String, String> pathMap, final Object body) {
        final String url = rootUrl + delete.value();
        if(!cookies.isEmpty()){
            httpHeaders.add("Cookie", getCookie(cookies));
        }
        httpHeaders.setContentType(getContentType(contentType));
        httpHeaders.setAccept(getAccepts(accept));
        return new RestBuild(HttpMethod.DELETE, url, httpHeaders, pathMap, body);
    }

    static RestBuild with(final String rootUrl, final Patch patch, final HttpHeaders httpHeaders, final HashMap<String, String> cookies, final Accept accept, final ContentType contentType, final HashMap<String, String> pathMap, final Object body) {
        final String url = rootUrl + patch.value();
        if(!cookies.isEmpty()){
            httpHeaders.add("Cookie", getCookie(cookies));
        }
        httpHeaders.setContentType(getContentType(contentType));
        httpHeaders.setAccept(getAccepts(accept));
        return new RestBuild(HttpMethod.PATCH, url, httpHeaders, pathMap, body);
    }

    private static org.springframework.http.MediaType getMediaType(String accept, String charset) {
        final org.springframework.http.MediaType mediaType = org.springframework.http.MediaType.parseMediaType(accept);
        return new org.springframework.http.MediaType(mediaType.getType(), mediaType.getSubtype(), Charset.forName(charset));
    }

    private static org.springframework.http.MediaType getContentType(ContentType contentType) {
        if (contentType == null) {
            return getMediaType(MediaType.APPLICATION_FORM_URLENCODED, "UTF-8");
        } else {
            return getMediaType(contentType.value(), contentType.charset());
        }
    }

    private static List<org.springframework.http.MediaType> getAccepts(Accept accept) {
        if (accept == null) {
            return Collections.singletonList(getMediaType(library.nettoffice.com.restapi.MediaType.ALL, "UTF-8"));
        } else {
            return Collections.singletonList(getMediaType(accept.contentType(), accept.charset()));
        }
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
