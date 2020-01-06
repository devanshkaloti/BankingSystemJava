package userinterface.LoginVC;

import classes.Users.Customer;
import com.jfoenix.controls.JFXTextField;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Label;

import java.io.IOException;
import java.security.GeneralSecurityException;

/**
 * VC Controller for Customer Register Page
 * This page allows the customer to sign up
 *
 * @author (Devansh Kaloti)
 * @version (1.0)
 */
public class CustomerRegisterController {


    @FXML
    protected JFXTextField email;
    @FXML
    protected JFXTextField password;
    @FXML
    protected JFXTextField name;
    @FXML
    protected JFXTextField occupation;

    @FXML
    protected Label errorMessage;

    // register customer
    public void registerButton() {
        try {

            // Add
            Customer newCustomer = new Customer(name.getText(), email.getText(), password.getText(), occupation.getText(), "0");
            newCustomer.add();
            LoginLaunch.setCustomer(newCustomer);

            moveToAccountsHome();

        } catch (IOException e) {
            this.errorMessage.setText("Database or Register Window could Not Be Opened. Please make sure you are connected to internet");
        } catch (GeneralSecurityException e) {
            this.errorMessage.setText("Security error exception. Please contact devansh@dksources.com");
        }
    }

    // Successful - move to home
    public void moveToAccountsHome() {
        try {
            Parent screen = FXMLLoader.load(getClass().getResource("../../../resources/AccountsHome.fxml"));;
            email.getScene().setRoot(screen);
        } catch (IOException e) {
            System.out.println("Could not open new view controller ");
        }
    }

    // Go back to login page
    public void goBack() {
        try {
            Parent screen = FXMLLoader.load(getClass().getResource("../../../resources/Login.fxml"));;
            email.getScene().setRoot(screen);
        } catch (IOException e) {
            System.out.println("Could not open new view controller ");
        }
    }
}
