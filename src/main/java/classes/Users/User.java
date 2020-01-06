package classes.Users;

import DKSheetsAPI.DKSheetsAPI;
import classes.Exceptions.InvalidLogin;
import classes.Password;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Construct, add and authenticate users of the system
 * Contains the properties and methods for both Manager and User
 * An abstract user class
 *
 * @author (Devansh Kaloti)
 * @version (1.0)
 */
public abstract class User {

    // Properties
    protected String id;
    protected int branchid;

    protected String name;
    protected String email;
    protected String encryptedPassword;
    protected String dateJoined;
    protected static List<List<Object>> data; // data pulled from sheet
    protected Type type; // type of user, enum
    protected String[] childData; // unique data depending on type of user



    /**
     * Type of user
     * Can be customer or manager.
     */
    public enum Type {
        // FYI - Can throw IllegalArgumentException if name doesn't match
        CUSTOMER,
        MANAGER,
    }


    /**
     * Initialize a new user from given parameters
     *
     * @param name Name of user
     * @param email Email of user
     * @param password Plain password of user
     * @param type Type of user, manager/customer
     */
    protected User(String name, String email, String password, Type type) {
        getSheetsData(); // Get data from sheets

        // Set properties
        this.name = name;
        this.email = email;
        this.encryptedPassword = new Password(password).getHashedPassword(); // Generate encrypted password
        this.type = type;

        // Date
        DateFormat df = new SimpleDateFormat("dd/MM/yy");
        Date date = new Date();

        this.dateJoined = df.format(date);
    }

    /**
     * Initialize object based on email/password
     * *Does not set all the variables
     * That happens in authenticate method
     *
     * @param email Email of user
     * @param password Raw [assword of user
     */
    protected User(String email, String password, Type type) throws InvalidLogin {
        getSheetsData(); // Get data from sheets
        this.email = email;
        this.encryptedPassword = new Password(password).getHashedPassword(); // Generate encrypted password

        this.authenticate(type);
    }

    /**
     * Initialize object based on just id
     * @param id id of user
     */
    protected User(String id) {
        getSheetsData(); // Get data from sheets

        // Run for each row in sheet
        for (List row: data) {
            // Comparing the id obtained from the sheet to the Id of the supplied userid
            if (Integer.parseInt(String.valueOf(row.get(0))) == Integer.parseInt(id)) {

                // User found, initialize variables
                this.id = String.valueOf(row.get(0));
                this.name = (String) row.get(1);
                this.dateJoined = (String) row.get(4);
                this.branchid = Integer.parseInt(String.valueOf(row.get(5)));
                this.type = Type.valueOf(String.valueOf(row.get(6)));

                // Determine type of user
                switch (this.type) {
                    case MANAGER: // Manager: append salary
                        this.childData = new String[] {String.valueOf(row.get(7))};
                        break;
                    case CUSTOMER: // Customer: append occupation and managerid
                        this.childData = new String[] {String.valueOf(row.get(8)), String.valueOf(row.get(9))};
                        break;
                }
            }
        }
    }

    /**
     * Get data from google sheets
     * Update instance variable data
     *
     * Protected access level limits scope to within package
     * This helps inheritance
     */
    protected static void getSheetsData() {
        try {
            data = new DKSheetsAPI("Users").readData("A2:P");
        } catch (IOException e) {
            System.out.println("Could not open file - Users.java");

        } catch (GeneralSecurityException e) {
            System.out.println("General Security Exception - Users.java");
        }
    }

    /**
     * Authenticate current user
     *
     * @return Manager or customer object
     * @throws InvalidLogin If the username password is incorrect
     */
    private void authenticate(Type type2) throws InvalidLogin {
        // For each row
        boolean match = false;
        loginCheck:
        for (List row: data) {
            // Check if row contains email and password
            if (row.get(2).equals(this.email) && row.get(3).equals(this.encryptedPassword) && row.get(6).equals(type2.toString())) {
                // Match found

                this.id = String.valueOf(row.get(0));
                this.name = (String) row.get(1);
                this.dateJoined = (String) row.get(4);
                this.branchid = Integer.parseInt(String.valueOf(row.get(5)));
                this.type = Type.valueOf(String.valueOf(row.get(6)));
                match = true;
                // Determine type of user
                switch (this.type) {
                    case MANAGER: // Manager: append salary
                        this.childData = new String[] {String.valueOf(row.get(7))};
                        break loginCheck;
                    case CUSTOMER: // Customer: append occupation and managerid
                        this.childData = new String[] {String.valueOf(row.get(8)), String.valueOf(row.get(9))};
                        break loginCheck;
                }
            }
        }

        if (!match) throw new InvalidLogin(this.email); // If credentials dont match
    }

    // Abstract Classes

    /**
     * Add a User when called
     * @return
     * @throws IOException Google sheets failed to open/read
     * @throws GeneralSecurityException
     */
    abstract public boolean add() throws IOException, GeneralSecurityException;
//    abstract public boolean update() throws IOException, GeneralSecurityException;


    // Getters

    /**
     * @return userid
     */
    public String getId() {
        return id;
    }

    /**
     * @return branch id
     */
    public int getBranchid() {
        return branchid;
    }

    /**
     * @return Name of user
     */
    public String getName() {
        return name;
    }

    /**
     * @return Email of user
     */
    public String getEmail() {
        return email;
    }

    /**
     * @return Get date of joining
     */
    public String getDateJoined() {
        return dateJoined;
    }


}
