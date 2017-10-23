package library.nettoffice.com.restapi;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by Deo-chainmeans on 2017/3/22.
 */

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PARAMETER)
public @interface Path {

    /**
     * Name of the url variable.
     *
     * @return the url variable name
     */
    String value();
}
