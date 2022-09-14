package uk.gov.dwp.uc.pairtest.exception;

/**
 * Exception to be thrown when an invalid purchase request is taken
 */
public class InvalidPurchaseException extends RuntimeException {
    /**
     * Thrown on an invalid purchase request
     * @param message The exception message
     */
    public InvalidPurchaseException(final String message) {
        super(message);
    }
}
