package library.nettoffice.com.restapi;

import org.springframework.core.ParameterizedTypeReference;

import java.lang.reflect.Type;

/**
 * Created by Deo-chainmeans on 2017/3/22.
 */

class Reference extends ParameterizedTypeReference<Object> {
    private final Type type;

    Reference(Type type) {
        this.type = type;
    }

    @Override
    public Type getType() {
        return type;
    }
}
