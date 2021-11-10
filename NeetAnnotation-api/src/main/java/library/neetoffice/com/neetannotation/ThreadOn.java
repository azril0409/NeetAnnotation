package library.neetoffice.com.neetannotation;

public @interface ThreadOn {
    Mode value() default Mode.UIThread;

    long delayMillis() default 0;

    enum  Mode{
        UIThread,Background
    }
}
