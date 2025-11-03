package lk.ijse.AutoCareCenter.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;

import java.io.IOException;

public class tyresFormController {

    @FXML
    private Label lblId;

    @FXML
    private AnchorPane root;


    private void setUi(String fileName) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/" + fileName));
        Pane Newroot = fxmlLoader.load();
        try {
            root.getChildren().clear();
            root.getChildren().setAll(Newroot);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    void btnBatteryOnAction(ActionEvent event) throws IOException {
        setUi("battery_form.fxml");
    }

    @FXML
    void btnSpareOnAction(ActionEvent event) throws IOException {
        setUi("spare_form.fxml");
    }

    @FXML
    void btnTubeOnAction(ActionEvent event) throws IOException {
        setUi("tube_form.fxml");
    }

    @FXML
    void btnTyreOnAction(ActionEvent event) throws IOException {
        setUi("tyre_form.fxml");
    }

}
