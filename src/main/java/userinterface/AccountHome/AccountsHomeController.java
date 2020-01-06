package userinterface.AccountHome;

import classes.Account;
import classes.Exceptions.InsufficientFundsException;
import classes.Exceptions.TransactionNotFoundException;
import classes.Transaction;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXTreeTableColumn;
import com.jfoenix.controls.JFXTreeTableView;
import com.jfoenix.controls.RecursiveTreeItem;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javax.security.auth.login.AccountNotFoundException;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;


/**
 * VC Controller for Accounts Homepage
 * This page allows the user to deposit/withdraw money
 * View past transactions
 *
 * @author (Devansh Kaloti)
 * @version (1.0)
 */
public class AccountsHomeController {

    @FXML
    protected JFXTreeTableView<TableTransaction> tableView;
    @FXML
    protected Label welcome;
    @FXML
    protected Label accountNumber;
    @FXML
    protected Label currentBalance;
    @FXML
    protected JFXTextField withdrawAmount;
    @FXML
    protected JFXTextField depositAmount;
    @FXML
    protected Label errorMessage;
    private Account account;

    // Runs after properties have been initialized
    public void initialize() {

        // Open user's account and sets info
        try {
            this.account = new Account("1");
            this.accountNumber.setText("Account Number: " + this.account.getId());
            this.currentBalance.setText("Current Balance: " + String.valueOf(this.account.getBalance()));

        } catch (AccountNotFoundException e) {
            System.out.println("Account not found");
        }

        // Sets up ledger
        setTransactionsTableView();
    }

    // Send money to another user - open that screen
    public void sendMoney() {
        try {
            Parent scene = FXMLLoader.load(getClass().getResource("../../../resources/AccountsSend.fxml"));
            this.withdrawAmount.getScene().setRoot(scene);
        } catch (IOException e) {
            System.out.println("Could not open new view controller ");
        }
    }

    // Deposit money into account, no record is maintained for this other than balance being updated
    public void depositButton() {

        // Confirm valid input
        if (depositAmount.getText().equals("") || depositAmount.getText() == null) {
            errorMessage.setText("Please specify your deposit amount.");
        }

        try {
            this.account.deposit(Double.parseDouble(depositAmount.getText()));
        } catch (NumberFormatException e) {
            errorMessage.setText("That is not a valid amount. Please try again.");
        } catch (IOException e) {
            errorMessage.setText("Deposit file could not be accessed");
        } catch (GeneralSecurityException e) {
            errorMessage.setText("Security error, deposit file could not be accessed");
        }

        errorMessage.setText("Money has been deposited!");
        this.currentBalance.setText("Current Balance: " + String.valueOf(this.account.getBalance()));
    }

    // Withdraw moeny from account
    public void withdrawButton() {

        // Check for valid input
        if (withdrawAmount.getText().equals("") || withdrawAmount.getText() == null) {
            errorMessage.setText("Please specify your withdraw amount.");
        }

        try {
            this.account.withdraw(Double.parseDouble(withdrawAmount.getText()));
        } catch (NumberFormatException e) {
            errorMessage.setText("That is not a valid amount. Please try again.");
        } catch (IOException e) {
            errorMessage.setText("Deposit file could not be accessed");
        } catch (GeneralSecurityException e) {
            errorMessage.setText("Security error, deposit file could not be accessed");
        } catch (InsufficientFundsException e) {
            errorMessage.setText("Insufficient funds.");
        }

        errorMessage.setText("Money withdrawn successfully! ");
        this.currentBalance.setText("Current Balance: " + String.valueOf(this.account.getBalance()));
    }


    // Setup table view / Tree
    public void setTransactionsTableView() {

        // Columns
        // ID
        JFXTreeTableColumn<TableTransaction, String> id = new JFXTreeTableColumn<>("Id");
        id.setCellValueFactory((TreeTableColumn.CellDataFeatures<TableTransaction, String> param) -> {
            if (id.validateValue(param)) {
                return param.getValue().getValue().id;
            } else {
                return id.getComputedValue(param);
            }
        });

        // SENDER
        JFXTreeTableColumn<TableTransaction, String> fromAccount = new JFXTreeTableColumn<>("Sender");
        fromAccount.setCellValueFactory((TreeTableColumn.CellDataFeatures<TableTransaction, String> param) -> {
            if (fromAccount.validateValue(param)) {
                return param.getValue().getValue().fromAccount;
            } else {
                return fromAccount.getComputedValue(param);
            }
        });

        // RECIPIENT
        JFXTreeTableColumn<TableTransaction, String> toAccount = new JFXTreeTableColumn<>("Recipient");
        toAccount.setCellValueFactory((TreeTableColumn.CellDataFeatures<TableTransaction, String> param) -> {
            if (toAccount.validateValue(param)) {
                return param.getValue().getValue().toAccount;
            } else {
                return toAccount.getComputedValue(param);
            }
        });

        // DATE
        JFXTreeTableColumn<TableTransaction, String> postDate = new JFXTreeTableColumn<>("Date");
        postDate.setCellValueFactory((TreeTableColumn.CellDataFeatures<TableTransaction, String> param) -> {
            if (postDate.validateValue(param)) {
                return param.getValue().getValue().postDate;
            } else {
                return postDate.getComputedValue(param);
            }
        });

        // AMOUNT
        JFXTreeTableColumn<TableTransaction, String> amount = new JFXTreeTableColumn<>("Amount");
        amount.setCellValueFactory((TreeTableColumn.CellDataFeatures<TableTransaction, String> param) -> {
            if (amount.validateValue(param)) {
                return param.getValue().getValue().amount;
            } else {
                return amount.getComputedValue(param);
            }
        });

        // Add data
        final TreeItem<TableTransaction> root = new RecursiveTreeItem<>(getTransactions(), RecursiveTreeObject::getChildren);
        this.tableView.setRoot(root);
        this.tableView.setShowRoot(false);

        // Add Columns
        this.tableView.getColumns().addAll(id, postDate, fromAccount, toAccount, amount);

    }

    // get all transactions for table view
    public ObservableList<TableTransaction> getTransactions() {
        ObservableList<TableTransaction> transactions = FXCollections.observableArrayList();
        try {
            ArrayList<Transaction> userTransactions = this.account.listTransactions();

            // For all transactions to be shown
            for (Transaction trans: userTransactions) {
                TableTransaction tableTrans = new TableTransaction(
                        trans.getId(),
                        trans.getFromid(),
                        trans.getToid(),
                        trans.getPostDate(),
                        String.valueOf(trans.getAmount())
                        );
                transactions.add(tableTrans);
            }

           return transactions;
        } catch (TransactionNotFoundException e) {
            System.out.println("Transaction not found");
        } catch (AccountNotFoundException e) {
            System.out.println("Account not found");
        }
        return transactions;
    }

    /**
     * Class designed to output on Table Tree
     */
    private static final class TableTransaction extends RecursiveTreeObject<TableTransaction> {
        final StringProperty id;
        StringProperty fromAccount;
        StringProperty toAccount;
        final StringProperty postDate;
        final StringProperty amount;

        // Constructor
        TableTransaction(String id, String fromAccount, String toAccount, String postDate, String amount) {
            this.id = new SimpleStringProperty(id);
            this.postDate = new SimpleStringProperty(postDate);
            this.amount = new SimpleStringProperty(amount);


//            try {
//                String customerIdFromAccount = new Account(fromAccount).getCustomerid();
//                String customerIdToAccount = new Account(toAccount).getCustomerid();
//
////                this.fromAccount = new SimpleStringProperty(new Customer(customerIdFromAccount).getName() + ": " + fromAccount);
////                this.toAccount = new SimpleStringProperty(new Customer(customerIdToAccount).getName() + ": " + toAccount);
//            } catch (AccountNotFoundException e) {
//                System.out.println("Account not found. ");
//
//
//            }
//
            this.fromAccount = new SimpleStringProperty(fromAccount);
            this.toAccount = new SimpleStringProperty(toAccount);
        }
    }
}

