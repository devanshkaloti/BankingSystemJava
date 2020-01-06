package classes.Users;

import DKSheetsAPI.DKSheetsAPI;
import classes.Exceptions.InvalidLogin;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.List;

/**
 * Manager class
 * Contains code for Managers
 * Inherits from user which provides the base code for a user
 *
 * @author (Devansh Kaloti)
 * @version (1.0)
 */
public class Manager extends User {

    // Properties
    private double salary;

    /**
     * Initialize Manager Object
     *
     * @param name Name of manager
     * @param email Email of manager
     * @param password Password of manager
     * @param salary Salary of manager
     */
    public Manager(String name, String email, String password, double salary) {
        super(name, email, password, Type.MANAGER);
        this.salary = salary;
    }

    /**
     * Initialize Manager Object
     *
     * @param email Email of manager
     * @param password Password of manager
     * @throws InvalidLogin Login credentials didn't match
     */
    public Manager(String email, String password) throws InvalidLogin {
        super(email, password, Type.MANAGER);
        this.salary = Double.parseDouble(this.childData[0]);
    }

    /**
     * Initialize Manager Object
     *
     * @param id ID of user
     */
    public Manager(String id) {
        super(id);
        this.salary = Double.parseDouble(this.childData[0]);
    }

    /**
     * Add manager to DB
     *
     * @return success of operation
     * @throws IOException Could not open DB file
     * @throws GeneralSecurityException Security/Permissions error while opening file
     */
    public boolean add() throws IOException, GeneralSecurityException {
        DKSheetsAPI db = new DKSheetsAPI("Users");
        int lastIndex = Integer.parseInt(db.getLastIndex());

        // Id, Name, Email, Password, Date, Branch-id, Type, Salary
        String[] row = {String.valueOf(lastIndex+1), this.name, this.email, this.encryptedPassword, this.dateJoined, String.valueOf(this.branchid), type.toString(), String.valueOf(this.salary)};

        db.addData(row); // Add to DB
        getSheetsData(); // Refresh

        this.id = db.getLastIndex(); // Get ID to set in object
        return true;
    }

    /**
     * List customers managed by the manager
     *
     * @return List of customers
     */
    public Customer[] listMyCustomers() {
        ArrayList<Customer> customers = new ArrayList<>(); // Empty

        // For each user in the sheet
        for (List row: User.data) {

            // Find user that is a) customer, b) id matches
            if (Type.valueOf(String.valueOf(row.get(6))) == Type.CUSTOMER && String.valueOf(row.get(9)).equals(this.id)) {
                // Customer found, adding to array
                customers.add(new Customer(String.valueOf(row.get(0))));
            }
        }

        // Convert arraylist to array
        Customer[] customersArray = new Customer[customers.size()];
        for (int i = 0; i < customers.size(); i++) {
            customersArray[i] = customers.get(i);
        }

        return customersArray;
    }

}
