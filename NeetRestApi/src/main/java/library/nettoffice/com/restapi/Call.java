package library.nettoffice.com.restapi;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.ResponseEntity;

import java.io.IOException;

/**
 * Created by Deo-chainmeans on 2017/5/19.
 */

public interface Call<Response> {

    Response execute() throws IOException;

    void enqueue(ResponseCallBack<Response> responseCallBack);

}
