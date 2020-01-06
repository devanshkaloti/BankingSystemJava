package userinterface.LoginVC;

import classes.Account;
import classes.Users.Customer;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Launcher for Customer Login VC
 *
 * @author (Devansh Kaloti)
 * @version (1.0)
 */
public class LoginLaunch extends Application {
    private static Customer customer;
    private static Account account;


    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("../../../resources/Login.fxml"));
        primaryStage.setTitle("DKBanking");
        Scene scene = new Scene(root, 580, 600);
        primaryStage.setScene(scene);
        primaryStage.show();

    }

    public static Customer getCustomer() {
        return customer;
    }

    public static void setCustomer(Customer customer) {
        LoginLaunch.customer = customer;
    }

    public static Account getAccount() {
        return account;
    }

    public static void setAccount(Account account) {
        LoginLaunch.account = account;
    }
}