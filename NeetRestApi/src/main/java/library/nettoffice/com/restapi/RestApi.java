package library.nettoffice.com.restapi;

import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by Deo-chainmeans on 2017/5/19.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface RestApi {
    String rootUrl() default "";

    Class<? extends HttpMessageConverter>[] converters() default {FormHttpMessageConverter.class};

    Class<?> responseErrorHandler() default DefaultResponseErrorHandler.class;
}
