package library.nettoffice.com.restapi;

import org.springframework.web.client.RestTemplate;

/**
 * Created by Deo-chainmeans on 2017/5/23.
 */

public interface RestApiSupport {
    RestTemplate getRestTemplate();

    void setRestTemplate(RestTemplate restTemplate);

    void setRootUrl(String rootUrl);
}
