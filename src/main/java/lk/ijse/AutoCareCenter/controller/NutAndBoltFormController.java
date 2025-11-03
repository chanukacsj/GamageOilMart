package lk.ijse.AutoCareCenter.controller;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

public class NutAndBoltFormController {

    @FXML
    private JFXComboBox<?> cmbSupId;

    @FXML
    private TableColumn<?, ?> colCode;

    @FXML
    private TableColumn<?, ?> colDescription;

    @FXML
    private TableColumn<?, ?> colQtyOnHand;

    @FXML
    private TableColumn<?, ?> colSupId;

    @FXML
    private TableColumn<?, ?> colUnitPrice;

    @FXML
    private Label lblId;

    @FXML
    private AnchorPane root;

    @FXML
    private TableView<?> tblMaterial;

    @FXML
    private JFXTextField txtCode;

    @FXML
    private JFXTextField txtDescription;

    @FXML
    private JFXTextField txtQtyOnHand;

    @FXML
    private JFXTextField txtUnitPrice;

    @FXML
    void btnClearOnAction(ActionEvent event) {

    }

    @FXML
    void btnDeleteOnAction(ActionEvent event) {

    }

    @FXML
    void btnSaveOnAction(ActionEvent event) {

    }

    @FXML
    void btnUpdateOnAction(ActionEvent event) {

    }

    @FXML
    void codeSearchOnAction(ActionEvent event) {

    }

    @FXML
    void getMaterials(MouseEvent event) {

    }

    @FXML
    void txtIDOnKeyReleased(KeyEvent event) {

    }

    @FXML
    void txtPriceOnKeyReleased(KeyEvent event) {

    }

    @FXML
    void txtQtyOnKeyReleased(KeyEvent event) {

    }

}
