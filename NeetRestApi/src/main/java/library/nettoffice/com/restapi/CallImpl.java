package library.nettoffice.com.restapi;

import android.util.Log;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * Created by Deo-chainmeans on 2017/5/19.
 */

final class CallImpl<Response> implements Call<Response> {
    private final RestApiRequester restApiHelp;
    private final RestBuild build;
    private final Type type;

    CallImpl(RestApiRequester restApiHelp, RestBuild build, Type type) {
        this.restApiHelp = restApiHelp;
        this.build = build;
        this.type = type;
    }

    @Override
    public Response execute() throws IOException {
        if (ParameterizedType.class.isInstance(type) && ParameterizedType.class.cast(type).getRawType() == ResponseEntity.class) {
            return (Response) restApiHelp.request(build, new Reference(((ParameterizedType) type).getActualTypeArguments()[0]));
        }
        return (Response) restApiHelp.request(build, new Reference(type)).getBody();
    }

    @Override
    public void enqueue(ResponseCallBack<Response> responseCallBack) {
        new Task(restApiHelp, responseCallBack, type).execute(build);
    }
}
