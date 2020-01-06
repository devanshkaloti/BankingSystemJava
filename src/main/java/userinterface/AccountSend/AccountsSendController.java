package userinterface.AccountSend;

import classes.Account;
import classes.Exceptions.InsufficientFundsException;
import classes.Transaction;
import classes.Users.Customer;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

import javafx.scene.control.Label;

import javax.security.auth.login.AccountNotFoundException;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;

/**
 * VC Controller for Accounts Sending Page
 * This page allows the user to send money to other users
 *
 * @author (Devansh Kaloti)
 * @version (1.0)
 */
public class AccountsSendController {

    @FXML
    protected JFXComboBox<Label> accountTo;
    @FXML
    protected Label accountNumber;
    @FXML
    protected Label currentBalance;
    @FXML
    protected JFXTextField amount;
    @FXML
    protected Label errorMessage;

    private Account account;
    ArrayList<Account> listAccounts;

    // Runs after constructor
    public void initialize() {
         try {
             this.listAccounts = Account.getAccounts(); // Get all acccounts

             // For each account
             for (int i = 0; listAccounts.size() > i; i++) {

                 // Get customer
                 Customer customer = new Customer(String.valueOf(listAccounts.get(i).getCustomerid()));

                 // Format to add to dropdown
                 accountTo.getItems().add(new Label(customer.getName() + ": "+ listAccounts.get(i).getId()));
             }

             // Setup/Display Account Properties
             this.account = new Account("1");
             this.accountNumber.setText("Account Number: " + String.valueOf(this.account.getId()));
             this.currentBalance.setText("Current Balance: $" + String.valueOf(this.account.getBalance()));

         } catch (AccountNotFoundException e) {
             System.out.println("account could not be located 1");
         }


    }

    // Send money from account
    public void sendMoney() {

        int accountToId = accountTo.getSelectionModel().getSelectedIndex(); // selected receiver

        // Validations
        if (accountToId == 0) this.errorMessage.setText("Please select a recipient account.");
        if (this.amount == null) this.errorMessage.setText("Please specify the transfer amount");

        try {
            Transaction transaction = new Transaction(this.account, listAccounts.get(accountToId), Double.parseDouble(this.amount.getText()));
            transaction.post();
        } catch (NumberFormatException e) {
            errorMessage.setText("That is not a valid amount. Please try again.");
        } catch (IOException e) {
            errorMessage.setText("Deposit file could not be accessed");
        } catch (GeneralSecurityException e) {
            errorMessage.setText("Security error, deposit file could not be accessed");
        } catch (InsufficientFundsException e) {
            errorMessage.setText("Insufficient funds, your account balance is only " + e.getCurrentBalance());
        }

        this.currentBalance.setText("Current Balance: $" + String.valueOf(this.account.getBalance()));
    }

    // Go back
    public void goBack() {
        try {
            Parent customerRegisterScreen = FXMLLoader.load(getClass().getResource("../../../resources/AccountsHome.fxml"));;
            amount.getScene().setRoot(customerRegisterScreen);
        } catch (IOException e) {
            System.out.println("Could not open new view controller ");
        }
    }

}
