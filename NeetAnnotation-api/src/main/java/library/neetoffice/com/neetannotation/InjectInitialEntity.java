package library.neetoffice.com.neetannotation;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.SOURCE;

/**
 * Inject initial entity to interactor ({@link Interactor},{@link ListInteractor},{@link SetInteractor}).
 */
@Retention(SOURCE)
@Target(FIELD)
public @interface InjectInitialEntity {
}
