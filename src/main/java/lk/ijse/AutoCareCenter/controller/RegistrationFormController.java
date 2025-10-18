package lk.ijse.AutoCareCenter.controller;



import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import lk.ijse.AutoCareCenter.Util.Regex;
import lk.ijse.AutoCareCenter.db.DbConnection;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class RegistrationFormController {

    @FXML
    private TextField txtPw;
    @FXML
    private TextField txtUserName;
    public AnchorPane rootNode;

    @FXML
    void btnRegisterOnAction(ActionEvent event) {
        if (isValid()) {
            String user_id = txtUserName.getText();
            String pw = txtPw.getText();
            System.out.println("" + user_id + " " + pw);
            saveUser(user_id, pw);
        }
    }

    private void saveUser(String user_Name, String pw) {
        try {
            String sql = "INSERT INTO users (username, password) VALUES (?, ?)";

            Connection connection = DbConnection.getInstance().getConnection();
            PreparedStatement pstm = connection.prepareStatement(sql);
            pstm.setString(1, user_Name);
            pstm.setString(2, pw);

            if (pstm.executeUpdate() > 0) {
                new Alert(Alert.AlertType.CONFIRMATION, "User saved!").show();
                navigateToTheDashboard();
            }

        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, "Error saving user: " + e.getMessage()).show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    private void navigateToTheDashboard() throws IOException {
        AnchorPane rootNode = FXMLLoader.load(this.getClass().getResource("/view/dashboard_form.fxml"));

        Scene scene = new Scene(rootNode);

        Stage stage = (Stage)this.rootNode.getScene().getWindow();
        stage.setScene(scene);
        stage.centerOnScreen();
        stage.setTitle("Dashboard Form");
    }

    public void LinkOnBackButton(ActionEvent actionEvent) throws IOException {
        AnchorPane rootNode = FXMLLoader.load(this.getClass().getResource("/view/login_form.fxml"));

        Scene scene = new Scene(rootNode);

        Stage stage = (Stage)this.rootNode.getScene().getWindow();
        stage.setScene(scene);
        stage.centerOnScreen();
        stage.setTitle("Login Form");
    }

    public boolean isValid() {
        if (!Regex.setTextColor(lk.ijse.AutoCareCenter.Util.TextField.USERNAME, (JFXTextField) txtUserName)) return false;
        if (!Regex.setTextColor(lk.ijse.AutoCareCenter.Util.TextField.PASSWORD, (JFXTextField) txtPw)) return false;
        return true;
    }

    public void userNameKey(KeyEvent keyEvent) {
        Regex.setTextColor(lk.ijse.AutoCareCenter.Util.TextField.USERNAME, (JFXTextField) txtUserName);
    }

    public void passwordKey(KeyEvent keyEvent) {
        Regex.setTextColor(lk.ijse.AutoCareCenter.Util.TextField.PASSWORD, (JFXTextField) txtPw);
    }
}
