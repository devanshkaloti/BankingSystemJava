package classes.Exceptions;

/**
 * Error class for when the bank account sending money does not have
 * enough funds to complete the transaction
 *
 * @author (Devansh Kaloti)
 * @version (1.0)
 */
public class InsufficientFundsException extends Exception {

    // Properties
    private Double transactionAmount;
    private Double currentBalance;

    /**
     * Constructor to initialize this exception
     * Assigns properties
     *
     * @param errorMessage The message for the user, if needed to display
     * @param transactionAmount Amount of transaction which failed
     * @param currentBalance Balance of the sender's account
     */
    public InsufficientFundsException(String errorMessage, Double transactionAmount, Double currentBalance) {
        super(errorMessage); // Constructor for Exception class

        // Set properties
        this.transactionAmount = transactionAmount;
        this.currentBalance = currentBalance;
    }

    /**
     * @return Transaction amount
     */
    public Double getTransactionAmount() {
        return transactionAmount;
    }

    /**
     * @return Balance of sender's account
     */
    public Double getCurrentBalance() {
        return currentBalance;
    }
}


