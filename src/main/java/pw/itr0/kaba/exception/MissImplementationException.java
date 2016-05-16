package pw.itr0.kaba.exception;

/**
 * The {@code MissImplementationException} class is a generic exception class to wrap checked Exception which cannot occur.
 *
 * @author ryotan
 * @since 1.0.0
 */
public class MissImplementationException extends RuntimeException {

    /**
     * serial version UID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Constructs a new exception with the specified detail message.
     *
     * @param message the detail message.
     */
    public MissImplementationException(String message) {
        super(message);
    }

    /**
     * Constructs a new exception with the specified detail message and cause.
     *
     * @param message the detail message.
     * @param cause   the cause.
     */
    public MissImplementationException(String message, Throwable cause) {
        super(message, cause);
    }


    /**
     * Constructs a new exception with the specified cause.
     *
     * @param cause the cause.
     */
    public MissImplementationException(Throwable cause) {
        super(cause);
    }

    /**
     * Constructs a new runtime exception with the specified detail message, cause, suppression enabled or disabled,
     * and writable stack trace enabled or disabled.
     *
     * @param message            the detail message.
     * @param cause              the cause.
     * @param enableSuppression  whether or not suppression is enabled or disabled
     * @param writableStackTrace whether or not the stack trace should be writable
     */
    protected MissImplementationException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
