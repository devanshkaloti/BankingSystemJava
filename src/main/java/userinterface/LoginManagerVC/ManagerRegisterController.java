package userinterface.LoginManagerVC;


import classes.Users.Manager;
import com.jfoenix.controls.JFXTextField;
import javafx.fxml.FXML;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Label;

import java.io.IOException;
import java.security.GeneralSecurityException;

/**
 * VC Controller for Manager Register Page
 * This page allows new managers to sign up
 *
 * @author (Devansh Kaloti)
 * @version (1.0)
 */
public class ManagerRegisterController {


    @FXML
    protected JFXTextField email;
    @FXML
    protected JFXTextField password;
    @FXML
    protected JFXTextField name;
    @FXML
    protected JFXTextField salary;

    @FXML
    protected Label errorMessage;


    // Register
    public void registerButton() {
        Manager newManager = new Manager(name.getText(), email.getText(), password.getText(),Double.parseDouble(salary.getText()));

        try {
            newManager.add(); // Add

        } catch (IOException e) {
            this.errorMessage.setText("Database or Register Window could Not Be Opened. Please make sure you are connected to internet");
        } catch (GeneralSecurityException e) {
            this.errorMessage.setText("Security error exception. Please contact devansh@dksources.com");
        }
    }

    // Go back to login screen
    public void goBack() {
        try {
            Parent screen = FXMLLoader.load(getClass().getResource("../../../resources/ManagerLogin.fxml"));;
            email.getScene().setRoot(screen);
        } catch (IOException e) {
            System.out.println("Could not open new view controller ");
        }
    }
}
