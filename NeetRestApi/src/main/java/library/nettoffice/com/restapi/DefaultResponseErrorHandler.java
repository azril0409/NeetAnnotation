package library.nettoffice.com.restapi;

import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.ResponseErrorHandler;

import java.io.IOException;

/**
 * Created by Deo-chainmeans on 2017/10/15.
 */

public class DefaultResponseErrorHandler implements ResponseErrorHandler {
    @Override
    public boolean hasError(ClientHttpResponse response) throws IOException {
        return response.getRawStatusCode() != 200;
    }

    @Override
    public void handleError(ClientHttpResponse response) throws IOException {
        throw new IOException(response.getStatusText());
    }
}
