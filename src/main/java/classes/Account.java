package classes;
import DKSheetsAPI.DKSheetsAPI;
import classes.Exceptions.InsufficientFundsException;
import classes.Exceptions.TransactionNotFoundException;
import classes.Users.User;

import javax.security.auth.login.AccountNotFoundException;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Accounts Class
 * Provides code related to accounts, opening, closing, listing
 *
 * @author (Devansh Kaloti)
 * @version (1.0)
 */
public class Account {

    // Properties
    private static List<List<Object>> data;
    private String id;
    private String customerid;
    private Double balance;
    private Currency currency;

    /**
     * Currency used by this account
     */
    public enum Currency {
        USD, // American
        CAD, // Canadian
        INR, // Indian
        AUD // Australian
    }

    /**
     * Initialize Account Object
     *
     * @param customerid ID of customer owner of account
     * @param balance Balance on account
     * @param currency Currency used by account
     */
    public Account(String customerid, Double balance, Currency currency) {
        this.customerid = customerid;
        this.balance = balance;
        this.currency = currency;

        // ID can't be set until account is opened.
    }

    /**
     * Initialize Account Object
     *
     * @param id ID of account
     * @throws AccountNotFoundException When the account could not located
     */
    public Account(String id) throws AccountNotFoundException {
        getSheetsData();

        // For data in sheet
        for (List row: data) {

            // Match Account iD
            if (String.valueOf(row.get(0)).equals(id)) {

                // Account found, set properties
                this.id = String.valueOf(row.get(0));
                this.customerid = String.valueOf(row.get(1));
                this.balance = Double.parseDouble(String.valueOf(row.get(4)));
                this.currency = Currency.valueOf(String.valueOf(row.get(3)));
                return;
            }
        }

        throw new AccountNotFoundException(); // Account not found
    }

    /**
     * Get data from sheets API
     */
    protected static void getSheetsData() {

        try {
            data = new DKSheetsAPI("Accounts").readData("A2:P");
        } catch (IOException e) {
            System.out.println("Could not open file - Accounts.java");
        } catch (GeneralSecurityException e) {
            System.out.println("General Security Exception - Accounts.java");
        }
    }

    /**
     * Get a list of the transactions from DB
     *
     * @return list of transactions
     * @throws TransactionNotFoundException A specific transaction could not be found
     * @throws AccountNotFoundException A specific account could not be found
     *
     * Errors can occur if DB was modified without this program
     */
    public ArrayList<Transaction> listTransactions() throws TransactionNotFoundException, AccountNotFoundException {
        ArrayList<Transaction> allTransactions = Transaction.getTransactions(); // All transactions
        ArrayList<Transaction> myTransactions = new ArrayList<>(); // Those that only belong to this account

        // Sorting Transactions
        for (Transaction transaction: allTransactions) {

            // If transaction is sent or received by this account
            if (transaction.fromAccount.getId().equals(this.id) || transaction.toAccount.getId().equals(this.id)) {
                myTransactions.add(transaction); // Add
            }
        }
        return myTransactions;
    }


    /**
     * Get all accounts stored in DB
     *
     * @return List of accounts
     * @throws AccountNotFoundException A specific account could not be read when tried using id
     */
    public static ArrayList<Account> getAccounts() throws AccountNotFoundException {
        getSheetsData();
        ArrayList<Account> accounts = new ArrayList<>();

        // For each account, initialize and add to arraylist
        for (int i = 0; data.size() > i; i++) {
            accounts.add(new Account(String.valueOf(data.get(i).get(0))));
        }

        return accounts;
    }

    /**
     * Open a new bank account
     * Called following an object is initialized via constrcutor
     *
     * @return Success of operation
     * @throws IOException DB File could not be read
     * @throws GeneralSecurityException Security/Permissions error occured
     */
    public boolean open() throws IOException, GeneralSecurityException {

        // Get data
        DKSheetsAPI db = new DKSheetsAPI("Accounts");
        int lastIndex = Integer.parseInt(db.getLastIndex());

        // Format Date
        DateFormat df = new SimpleDateFormat("dd/MM/yy");
        Date date = new Date();
        String dateFormatted = df.format(date);

        // Prepare
        String[] row = {String.valueOf(lastIndex+1), this.customerid, dateFormatted, this.currency.toString(), String.valueOf(this.balance)};

        // Add, Refresh
        db.addData(row);
        getSheetsData();

        // Get ID
        this.id = db.getLastIndex();

        return true;
    }

    /**
     * Update the balance of an account
     * Can occur after deposit, withdraw, or sending/receiving money
     *
     * @param change The change in account balance to record
     *               Negative if money was withdrawed/sent
     *               Positive if money was deposited/received
     * @throws IOException DB file could not be opened
     * @throws GeneralSecurityException Permissions/Security error on file
     */
    protected void updateBalance(Double change) throws IOException, GeneralSecurityException {
        DKSheetsAPI db = new DKSheetsAPI("Accounts");

        /*
           Cell location to make update
           Accounts! is sheet name, E is column, and the following is row number
           First cell is header so +1 makes up for that
           */
        String cellLocation = "Accounts!E" + (Integer.parseInt(this.id)+1);

        // New Balance
        Double newBalance = this.balance + change;
        this.balance = newBalance;

        // Update
        db.updateData(cellLocation, String.valueOf(newBalance));

    }

    /**
     * Deposit money into account
     *
     * @param amount Amount to be deposited
     * @throws IOException DB file could not be opened
     * @throws GeneralSecurityException Security/Permissions error
     * @throws NumberFormatException Invalid amount given
     */
    public void deposit(Double amount) throws IOException, GeneralSecurityException, NumberFormatException {

        // If invalid amount given
        if (amount <= 0) throw new NumberFormatException();

        // Update balance
        this.updateBalance(amount);
    }

    /**
     * Withdraw money from account
     *
     * @param amount Amount to be taken
     * @throws IOException DB file could not be opened
     * @throws GeneralSecurityException Security/Permissions error
     * @throws InsufficientFundsException Not enough account balance to make transaction
     * @throws NumberFormatException Invalid amount given
     */
    public void withdraw(Double amount) throws IOException, GeneralSecurityException, InsufficientFundsException, NumberFormatException {
        // Check validity
        if (amount <= 0) throw new NumberFormatException();

        // Confirm sure sufficient funds
        if ((this.balance - amount) < 0) throw new InsufficientFundsException("Insufficient Funds", amount, this.balance);


        // Update
        this.updateBalance(-amount);
    }

    // Getters
    public String getId() {
        return id;
    }

    public String getCustomerid() {
        return customerid;
    }

    public Double getBalance() {
        return balance;
    }

    public Currency getCurrency() {
        return currency;
    }
}
