package lk.ijse.AutoCareCenter.controller;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import lk.ijse.AutoCareCenter.Util.Regex;
import lk.ijse.AutoCareCenter.bo.BOFactory;
import lk.ijse.AutoCareCenter.bo.SuperBO;
import lk.ijse.AutoCareCenter.bo.custom.CustomerBO;
import lk.ijse.AutoCareCenter.bo.custom.MaterialBO;
import lk.ijse.AutoCareCenter.bo.custom.MaterialDetailBO;
import lk.ijse.AutoCareCenter.bo.custom.SupplierBO;
import lk.ijse.AutoCareCenter.dao.custom.Impl.MaterialDAOImpl;
import lk.ijse.AutoCareCenter.dao.custom.Impl.MaterialDetailsDAOImpl;
import lk.ijse.AutoCareCenter.dao.custom.Impl.SupplierDAOImpl;
import lk.ijse.AutoCareCenter.entity.MaterialDetails;
import lk.ijse.AutoCareCenter.model.*;

import lk.ijse.AutoCareCenter.model.tm.MaterialsTm;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class MaterialFormController {
    @FXML
    private TableView<MaterialsTm> tblMaterial;
    @FXML
    private JFXComboBox<String> cmbSupId;


    @FXML
    private TableColumn<?, ?> colCode;

    @FXML
    private TableColumn<?, ?> colDescription;

    @FXML
    private TableColumn<?, ?> colUnitPrice;

    @FXML
    private TableColumn<?, ?> colQtyOnHand;
    @FXML
    private TableColumn<?, ?> colSupId;

    @FXML
    private AnchorPane root;

    @FXML
    private TextField txtDescription;

    @FXML
    private TextField txtQtyOnHand;

    @FXML
    private TextField txtUnitPrice;
    @FXML
    private Label lblId;


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
    void btnOilOnAction(ActionEvent event) throws IOException {
        setUi("oils_form.fxml");
    }

    @FXML
    void btnTyreOnAction(ActionEvent event) throws IOException {
        setUi("tyres_form.fxml");
    }

    public void btnBackOnAction(ActionEvent actionEvent) throws IOException {
        setUi("dashboard_form.fxml");
    }
}