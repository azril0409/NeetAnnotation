package library.nettoffice.com.restapi;

import android.os.AsyncTask;

import org.springframework.http.ResponseEntity;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * Created by Deo-chainmeans on 2017/3/22.
 */

class Task extends AsyncTask<RestBuild, Throwable, Object> {
    private final RestApiRequester restApiRequester;
    private final ResponseCallBack responseCallBack;
    private final Type type;

    Task(RestApiRequester restApiRequester, ResponseCallBack responseCallBack, Type type) {
        this.restApiRequester = restApiRequester;
        this.responseCallBack = responseCallBack;
        this.type = type;
    }

    @Override
    protected Object doInBackground(RestBuild... params) {
        try {
            if (ParameterizedType.class.isInstance(type) && ParameterizedType.class.cast(type).getRawType() == ResponseEntity.class) {
                return restApiRequester.request(params[0], new Reference(((ParameterizedType) type).getActualTypeArguments()[0]));
            }
            return restApiRequester.request(params[0], new Reference(type)).getBody();
        } catch (Exception e) {
            publishProgress(e);
            return null;
        }
    }

    @Override
    protected void onProgressUpdate(Throwable... values) {
        super.onProgressUpdate(values);
        try {
            if (responseCallBack != null) {
                responseCallBack.onFailure(values[0]);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onPostExecute(Object responseEntity) {
        try {
            if (responseCallBack != null && responseEntity != null) {
                responseCallBack.onResponse(responseEntity);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
