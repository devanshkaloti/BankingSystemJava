package userinterface.LoginManagerVC;

import classes.*;
import classes.Exceptions.InvalidLogin;
import classes.Users.Customer;
import classes.Users.Manager;
import classes.Users.User;
import com.jfoenix.controls.JFXTextField;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;
import java.security.GeneralSecurityException;

/**
 * VC Controller for Manager Login
 * This page allows the Manager to login
 *
 * @author (Devansh Kaloti)
 * @version (1.0)
 */
public class ManagerLoginController {


    @FXML
    protected JFXTextField email;
    @FXML
    protected JFXTextField password;
    @FXML
    protected JFXTextField name;
    @FXML
    protected Label errorMessage;


    // Login
    public void loginButton() {
        try {
            Manager manager = new Manager(this.email.getText(), this.password.getText());
        } catch (InvalidLogin e) {
            this.errorMessage.setText(e.toString());
        }
    }

    // Register screen
    public void moveToRegisterScreen() {
        try {
            Parent screen = FXMLLoader.load(getClass().getResource("../../../resources/ManagerRegister.fxml"));
            email.getScene().setRoot(screen);
        } catch (IOException e) {
            System.out.println("Could not open new view controller ");
        }
    }

    // Go to customer portal
    public void customerPortal() {
        try {
            Parent screen = FXMLLoader.load(getClass().getResource("../../../resources/Login.fxml"));;
            email.getScene().setRoot(screen);
        } catch (IOException e) {
            System.out.println("Could not open new view controller ");
        }
    }

}
