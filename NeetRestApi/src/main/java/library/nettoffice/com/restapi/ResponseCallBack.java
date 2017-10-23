package library.nettoffice.com.restapi;

import android.app.AlertDialog;
import android.os.Build;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.ResponseEntity;
import org.springframework.util.Assert;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * Created by Deo-chainmeans on 2017/3/22.
 */

public interface ResponseCallBack<Response> {
    void onResponse(Response response);

    void onFailure(Throwable throwable);
}
