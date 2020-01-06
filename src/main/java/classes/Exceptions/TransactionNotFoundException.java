package classes.Exceptions;

/**
 * Error class for when the transaction is not found
 *
 *
 * @author (Devansh Kaloti)
 * @version (1.0)
 */
public class TransactionNotFoundException extends Exception {

    /**
     * Constructor to initialize this exception
     */
    public TransactionNotFoundException() {
        super("Transaction Not Found");
    }

}


