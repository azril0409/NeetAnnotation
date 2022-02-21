package library.neetoffice.com.neetannotation;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.SOURCE;

/**
 * Interacted viewmodel to activity and fragment
 * */
@Retention(SOURCE)
@Target(TYPE)
public @interface SetInteractor {
}
