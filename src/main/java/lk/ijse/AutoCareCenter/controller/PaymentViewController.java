package lk.ijse.AutoCareCenter.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import lk.ijse.AutoCareCenter.bo.BOFactory;
import lk.ijse.AutoCareCenter.bo.custom.PurchaseOrderBO;
import lk.ijse.AutoCareCenter.entity.Employee;
import lk.ijse.AutoCareCenter.entity.OrderDetails;
import lk.ijse.AutoCareCenter.entity.Payment;
import lk.ijse.AutoCareCenter.model.OrderDetailsDTO;
import lk.ijse.AutoCareCenter.model.tm.MaterialsTm;
import lk.ijse.AutoCareCenter.model.tm.OrdersTm;
import lk.ijse.AutoCareCenter.model.tm.PaymentTm;

import java.sql.SQLException;
import java.util.List;

public class PaymentViewController {

    @FXML
    private TableColumn<?, ?> ColDescription;

    @FXML
    private TableColumn<?, ?> ColDate;

    @FXML
    private TableColumn<?, ?> ColId;

    @FXML
    private TableColumn<?, ?> ColOrderId;

    @FXML
    private TableColumn<?, ?> colCharge;

    @FXML
    private TableColumn<?, ?> colCode;

    @FXML
    private TableColumn<?, ?> colQty;

    @FXML
    private TableColumn<?, ?> colTotal;

    @FXML
    private TableColumn<?, ?> colUnitPrice;

    @FXML
    private AnchorPane root;

    @FXML
    private JFXTextField txtSearchDescription;

    @FXML
    private TableView<PaymentTm> tblPayments;

    PurchaseOrderBO purchaseOrderBO = (PurchaseOrderBO) BOFactory.getBoFactory().getBO(BOFactory.BOTypes.PO);

    public void initialize() {
        ColId.setCellValueFactory(new PropertyValueFactory<>("id"));
        ColDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
        colQty.setCellValueFactory(new PropertyValueFactory<>("qty"));
        colUnitPrice.setCellValueFactory(new PropertyValueFactory<>("unitPrice"));
        colCharge.setCellValueFactory(new PropertyValueFactory<>("service_charge"));
        colTotal.setCellValueFactory(new PropertyValueFactory<>("Total"));
        colCode.setCellValueFactory(new PropertyValueFactory<>("code"));
        ColOrderId.setCellValueFactory(new PropertyValueFactory<>("OrderId"));
        ColDate.setCellValueFactory(new PropertyValueFactory<>("date"));

        loadAllPayments();
    }

    private void loadAllPayments() {
        try {
            List<Payment> list = purchaseOrderBO.loadAll();
            ObservableList<PaymentTm> tmList = FXCollections.observableArrayList();

            for (Payment dto : list) {

                String description = purchaseOrderBO.getDescriptionByCode(dto.getCode());

                tmList.add(new PaymentTm(
                        dto.getId(),
                        dto.getOrderId(),
                        description,
                        dto.getQty(),
                        dto.getUnitPrice(),
                        dto.getService_charge(),
                        dto.getTotal(),
                        dto.getDescription(),
                        dto.getDate()
                ));
            }


            tblPayments.setItems(tmList);
            System.out.println("tblPayments = " + tblPayments);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void btnDeleteOnAction(ActionEvent actionEvent) {
    }

    public void btnBackOnAction(ActionEvent actionEvent) {
    }

    public void getMaterials(MouseEvent mouseEvent) {
    }

    public void txtDescriptionOnKeyReleased(KeyEvent keyEvent) {
        String searchText = txtSearchDescription.getText().trim().toLowerCase();

        if (searchText.isEmpty()) {
            loadAllPayments();
            return;
        }

        ObservableList<PaymentTm> filteredList = FXCollections.observableArrayList();
        for (PaymentTm tm : tblPayments.getItems()) {
            if (tm.getCode().toLowerCase().contains(searchText)) {
                filteredList.add(tm);
            }
        }
        tblPayments.setItems(filteredList);
    }

    public void btnSearchOnAction(ActionEvent actionEvent) {
        String searchText = txtSearchDescription.getText().trim().toLowerCase();

        if (searchText.isEmpty()) {
            loadAllPayments();
            return;
        }

        ObservableList<PaymentTm> filteredList = FXCollections.observableArrayList();

        for (PaymentTm tm : tblPayments.getItems()) {
            if (tm.getCode().toLowerCase().contains(searchText)) {
                filteredList.add(tm);
            }
        }

        tblPayments.setItems(filteredList);

        if (filteredList.isEmpty()) {
            new Alert(Alert.AlertType.INFORMATION, "No results found for '" + searchText + "'").show();
        }
    }

    public void btnResetOnAction(ActionEvent actionEvent) {
        txtSearchDescription.clear();
        loadAllPayments();
    }
}
