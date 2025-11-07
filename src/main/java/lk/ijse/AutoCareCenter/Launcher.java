package lk.ijse.AutoCareCenter;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Launcher extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        stage.setScene(new Scene(
                FXMLLoader.load(this.getClass().getResource("/view/dashboard_form.fxml")),
                1240, // width
                700   // height
        ));
        stage.setTitle("Login Form");
        stage.centerOnScreen();
        stage.show();
    }
}
