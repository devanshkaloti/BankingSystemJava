package classes;
import DKSheetsAPI.DKSheetsAPI;
import classes.Exceptions.InsufficientFundsException;
import classes.Exceptions.TransactionNotFoundException;

import javax.security.auth.login.AccountNotFoundException;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * Transaction Class
 * Provides code to add, void, and list transactions among other things
 *
 * @author (Devansh Kaloti)
 * @version (1.0)
 */
public class Transaction {

    // Properties
    private static List<List<Object>> data;
    private String id;
    private String postDate;
    private String fromid;
    private String toid;
    private Double amount;
    private Status status;

    public Account toAccount;
    public Account fromAccount;

    /**
     * Status of transaction
     */
    public enum Status {
        Posting,
        Posted,
        Void
    }


    /**
     * Initialize Transaction Class
     *
     * @param accountFrom Sender account
     * @param accountTo Receiveing  Account
     * @param amount Amount of transaction
     */
    public Transaction(Account accountFrom, Account accountTo, Double amount) {
        this.fromid = accountFrom.getId();
        this.toid = accountTo.getId();
        this.amount = amount;

        // Date
        DateFormat df = new SimpleDateFormat("dd/MM/yy");
        Date date = new Date();
        this.postDate = df.format(date);

        this.status = Status.Posting;

        this.fromAccount = accountFrom;
        this.toAccount = accountTo;
    }

    /**
     * Initialize Transaction Class
     *
     * @param id ID of transaction
     * @throws TransactionNotFoundException Transaction could not be found
     * @throws AccountNotFoundException Sender/Receiver Account could not be found
     */
    public Transaction(String id) throws TransactionNotFoundException, AccountNotFoundException {
        getSheetsData();

        // For each row of data
        for (List row: data) {

            // Matching id
            if (String.valueOf(row.get(0)).equals(id)) {

                // Found
                this.id = String.valueOf(row.get(0));
                this.postDate = String.valueOf(row.get(1));
                this.fromid = String.valueOf(row.get(2));
                this.toid = String.valueOf(row.get(3));
                this.amount = Double.parseDouble(String.valueOf(row.get(4)));
                this.status = Status.valueOf(String.valueOf(row.get(5)));

                this.fromAccount = new Account(String.valueOf(row.get(2)));
                this.toAccount = new Account(String.valueOf(row.get(3)));

                return;
            }
        }

        throw new TransactionNotFoundException();
    }

    /**
     * Get data from DB
     */
    protected static void getSheetsData() {
        try {
            data = new DKSheetsAPI("Transactions").readData("A2:P");
        } catch (IOException e) {
            System.out.println("Could not open file - Transactions.java");

        } catch (GeneralSecurityException e) {
            System.out.println("General Security Exception - Transactions.java");
        }
    }

    /**
     * Get list of transactions
     *
     * @return Return list
     * @throws TransactionNotFoundException A transaction could not be found
     * @throws AccountNotFoundException An associated account could not be found
     */
    public static ArrayList<Transaction> getTransactions() throws TransactionNotFoundException, AccountNotFoundException {
        getSheetsData();
        ArrayList<Transaction> transactions = new ArrayList<>();

        // For each transaction
        for (int i = 0; data.size() > i; i++) {
            transactions.add(new Transaction(String.valueOf(data.get(i).get(0))));
        }

        return transactions;
    }


    /**
     * Post the transaction to ledger / db
     * @return Success of operation
     * @throws IOException File could not be opened
     * @throws GeneralSecurityException Security error
     * @throws InsufficientFundsException Not enough account balance to complete transaction
     */
    public boolean post() throws IOException, GeneralSecurityException, InsufficientFundsException {

        // Make sure enough funds exist
        if ((fromAccount.getBalance() - this.amount) < 0) {
            throw new InsufficientFundsException("Insufficient Funds", this.amount, fromAccount.getBalance());
        }

        DKSheetsAPI db = new DKSheetsAPI("Transactions");
        int lastIndex = Integer.parseInt(db.getLastIndex());
        this.status = Status.Posted; // Set status

        // Prepare array
        String[] row = {String.valueOf(lastIndex+1), this.postDate, this.fromid, this.toid, this.amount.toString(), Status.Posted.toString()};

        // Update balances
        fromAccount.updateBalance(-amount);
        toAccount.updateBalance(amount);

        // Add data, refresh, get id
        db.addData(row);
        getSheetsData();
        this.id = db.getLastIndex();

        return true;
    }

    /**
     * Void transaction
     * Not being used currently
     *
     * @return Success of operation
     * @throws IOException File could not be opened
     * @throws GeneralSecurityException Security/Permissions error
     */
    public boolean cancel() throws IOException, GeneralSecurityException {
        DKSheetsAPI db = new DKSheetsAPI("Transactions");
        String cellLocation = "F" + this.id+1;
        db.updateData(cellLocation, Status.Void.toString());

        return true;
    }

    // Getters

    public String getId() {
        return id;
    }

    public String getPostDate() {
        return postDate;
    }

    public String getFromid() {
        return fromid;
    }

    public String getToid() {
        return toid;
    }

    public Double getAmount() {
        return amount;
    }

    public Status getStatus() {
        return status;
    }
}
