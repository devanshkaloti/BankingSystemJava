package userinterface.AccountHome;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Launcher for Accounts Homepage VC
 *
 * @author (Devansh Kaloti)
 * @version (1.0)
 */
public class AccountsHomeLaunch extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception{

        Parent root = FXMLLoader.load(getClass().getResource("../../../resources/AccountsHome.fxml"));
        primaryStage.setTitle("DKBanking");
        Scene scene = new Scene(root, 560, 600);
        primaryStage.setScene(scene);
        primaryStage.show();

    }

}