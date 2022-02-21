package library.neetoffice.com.neetannotation;

/**
 * The annotation method will running at specify thread.
 */
public @interface ThreadOn {
    /**
     * @return Thread mode. default: UIThread
     */
    Mode value() default Mode.UIThread;

    /**
     * The annotation method will delaying to run.
     *
     * @return Millis time
     */
    long delayMillis() default 0;

    /**
     * Thread mode
     */
    enum Mode {
        /**
         * Specify this thread will running on ui thread.
         */
        UIThread,
        /**
         * Specify this thread will running on background thread.
         */
        Background
    }
}
