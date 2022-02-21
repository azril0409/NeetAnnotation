package library.neetoffice.com.neetannotation;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.SOURCE;

/**
 * Annotation interactor ({@link Interactor},{@link ListInteractor},{@link SetInteractor}) can be subscribe.
 * */
@Retention(SOURCE)
@Target(FIELD)
public @interface Published {
    /**
     * The interactor ({@link Interactor},{@link ListInteractor},{@link SetInteractor}) will be send last entity on subscribed.
     *
     * @return Boolean
     * */
    boolean recordLastEntity() default true;
}
