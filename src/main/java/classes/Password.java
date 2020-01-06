package classes;

/**
 * Password class
 * This class can assist in encrypting passwords in the future
 *
 * @author (Devansh Kaloti)
 * @version (1.0)
 */
public class Password {

    private String hashedPassword;

    /**
     * Initialize
     * @param password Password of user, plain text
     */
    public Password(String password) {
        this.hashedPassword = password;
    }

    /**
     * Return hashed password
     * A hashed password is a set of alpha-numerical charectors which encrypts the password
     * When user enters same password, the same key is used, and a comparison between hashes is done
     *
     * Currently not complete implementation
     *
     * @param password Plain-text password
     * @return Hashed password
     */
    public String hashPassword(String password) {
        return password;
    }
    /**
     * Getter
     *
     * @return Get password
     */
    public String getHashedPassword(){
        return this.hashedPassword;
    }
}
