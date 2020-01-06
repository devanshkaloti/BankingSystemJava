package classes.Users;

import DKSheetsAPI.DKSheetsAPI;
import classes.Account;
import classes.Exceptions.InvalidLogin;
import java.io.IOException;
import java.security.GeneralSecurityException;

/**
 * Customer class
 * Contains code for Customers
 * Inherits from user which provides the base code for a user
 *
 * @author (Devansh Kaloti)
 * @version (1.0)
 */
public class Customer extends User {

    // Properties
    private String occupation;
    private String managerid;
    private Account account;


    /**
     * Initialize Customer Object
     *
     * @param name Name of customer
     * @param email Email of customer
     * @param password Password of customer
     * @param occupation occupation of customer
     * @param managerId ID of customer's manager
     * @throws IOException Google Sheets File could not be opened
     * @throws GeneralSecurityException Security error / permissions error
     */
    public Customer(String name, String email, String password, String occupation, String managerId) throws IOException, GeneralSecurityException {
        super(name, email, password, Type.CUSTOMER);
        this.occupation = occupation;
        this.managerid = managerId;

        // Create Account
        // Although this app has provision to open multiple accounts / user,
        // for the time being this is the only place to open a new account
        // NOTE - Starting Balance $500
        Account account = new Account(this.id, 500.00, Account.Currency.CAD);
        account.open();

        this.account = account;
    }

    /**
     * Initialize Customer Object
     *
     * @param email Email of user
     * @param password Password of User
     * @throws InvalidLogin
     */
    public Customer(String email, String password) throws InvalidLogin{
        super(email, password, Type.CUSTOMER);
        // InvalidLogin Exception will be thrown from super class if authentication fails

        // Leftover properties pending initialization
        this.occupation = this.childData[0];
        this.managerid = this.childData[1];

    }

    /**
     * Initialize Customer Object
     *
     * @param id ID of customer
     */
    public Customer(String id) {
        super(id);

        // Leftover properties
        this.occupation = this.childData[0];
        this.managerid = this.childData[1];
    }


    /**
     * Add the user to the DB
     *
     * @return status of update
     * @throws IOException Could not open DB
     * @throws GeneralSecurityException Security/Permissions error while opening DB
     */
    public boolean add() throws IOException, GeneralSecurityException {
        DKSheetsAPI db = new DKSheetsAPI("Users");
        int lastIndex = Integer.parseInt(db.getLastIndex());

        // Setup Array
        String[] row = {String.valueOf(lastIndex+1),
                this.name, this.email, this.encryptedPassword, this.dateJoined,
                String.valueOf(this.branchid), type.toString(), "N/A", occupation, String.valueOf(this.managerid)
        };

        // Add data and refresh
        db.addData(row);
        getSheetsData();

        this.id = db.getLastIndex(); // Set id
        return true;
    }

    /**
     * @return Get Account
     */
    public Account getAccount() {
        return account;
    }
}
