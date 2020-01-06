package userinterface.LoginManagerVC;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Launcher for Manager Login VC
 *
 * @author (Devansh Kaloti)
 * @version (1.0)
 */
public class ManagerLoginLaunch extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception{

        Parent root = FXMLLoader.load(getClass().getResource("../../../resources/ManagerLogin.fxml"));
        primaryStage.setTitle("DKBanking");
        Scene scene = new Scene(root, 800, 600);
        primaryStage.setScene(scene);
        primaryStage.show();

    }

}