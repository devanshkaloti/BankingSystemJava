package userinterface.LoginVC;

import classes.Exceptions.InvalidLogin;
import classes.Users.Customer;
import com.jfoenix.controls.JFXTextField;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Label;

import java.io.IOException;


/**
 * VC Controller for Customer Login Page
 * This page allows the customer to login
 *
 * @author (Devansh Kaloti)
 * @version (1.0)
 */
public class LoginController {


    @FXML
    protected JFXTextField email;
    @FXML
    protected JFXTextField password;
    @FXML
    protected JFXTextField name;
    @FXML
    protected Label errorMessage;


    public void loginButton() {
        try {
            Customer customer = new Customer(this.email.getText(), this.password.getText());
            LoginLaunch.setCustomer(customer);
            LoginLaunch.setAccount(customer.getAccount());
            moveToAccountsHome();
        } catch (InvalidLogin e) {
            this.errorMessage.setText(e.toString());
        }
    }


    public void moveToRegisterScreen() {
        try {
            Parent screen = FXMLLoader.load(getClass().getResource("../../../resources/Register.fxml"));;
            email.getScene().setRoot(screen);
        } catch (IOException e) {
            System.out.println("Could not open new view controller ");
        }
    }

    public void moveToAccountsHome() {
        try {
            Parent customerRegisterScreen = FXMLLoader.load(getClass().getResource("../../../resources/AccountsHome.fxml"));;
            email.getScene().setRoot(customerRegisterScreen);
        } catch (IOException e) {
            System.out.println("Could not open new view controller ");
        }
    }


    public void managerPortal() {
        try {
            Parent screen = FXMLLoader.load(getClass().getResource("../../../resources/ManagerLogin.fxml"));;
            email.getScene().setRoot(screen);
        } catch (IOException e) {
            System.out.println("Could not open new view controller ");
        }
    }
}
