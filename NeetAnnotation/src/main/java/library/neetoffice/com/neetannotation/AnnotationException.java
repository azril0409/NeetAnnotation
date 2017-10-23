package library.neetoffice.com.neetannotation;

/**
 * Created by Deo on 2016/3/17.
 */
public class AnnotationException extends RuntimeException {

    AnnotationException(String message) {
        super(message);
    }

    AnnotationException(Throwable throwable) {
        super(throwable);
    }
}
