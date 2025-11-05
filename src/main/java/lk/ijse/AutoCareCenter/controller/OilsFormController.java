package lk.ijse.AutoCareCenter.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;

import java.io.IOException;

public class OilsFormController {

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
    void btnFilterOnAction(ActionEvent event) throws IOException {
        setUi("filter_form.fxml");
    }

    @FXML
    void btnNutOnAction(ActionEvent event) throws IOException {
        setUi("nut_and_bolt.fxml");

    }

    @FXML
    void btnOilOnAction(ActionEvent event) throws IOException {
        setUi("oil_form.fxml");
    }

    @FXML
    void btnOtherOnAction(ActionEvent event) throws IOException {
        setUi("other_form.fxml");
    }

}
