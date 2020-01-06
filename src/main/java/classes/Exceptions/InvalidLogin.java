package classes.Exceptions;

/**
 * Error class for when the username/email and password don't match with stored results
 * enough funds to complete the transaction
 *
 * @author (Devansh Kaloti)
 * @version (1.0)
 */
public class InvalidLogin extends Exception {
    private String email; // Email of user (username)

    /**
     * Constructor, initializes
     * @param email of user
     */
    public InvalidLogin(String email) {
        super("Invalid Email/Password");
        this.email = email; // Email
    }

    /**
     * @return email of user
     */
    public String getEmail() {
        return email;
    }

    /**
     * @return toString of this function
     */
    @Override
    public String toString() {
        return "Invalid Email/Password for " + this.email;
    }
}


