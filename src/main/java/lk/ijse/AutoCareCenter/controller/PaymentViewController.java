package lk.ijse.AutoCareCenter.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
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
import java.time.LocalDate;
import java.util.List;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;


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
    private Label lblDailyIncome;

    @FXML
    private Label lblMonthlyIncome;

    @FXML
    private Label lblYearlyIncome;

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

    @FXML
    private JFXComboBox<String> cmbDateFilter;

    private ObservableList<PaymentTm> masterList = FXCollections.observableArrayList();

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

        cmbDateFilter.setItems(FXCollections.observableArrayList(
                "Today",
                "Last 7 Days",
                "This Month",
                "This Year",
                "All"
        ));

        cmbDateFilter.setValue("All");
        loadAllPayments();
    }

    private void loadAllPayments() {
        try {
            List<Payment> list = purchaseOrderBO.loadAll();
            masterList.clear();

            for (Payment dto : list) {
                String description = purchaseOrderBO.getDescriptionByCode(dto.getCode());

                masterList.add(new PaymentTm(
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

            tblPayments.setItems(masterList);
            calculateIncome();


        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @FXML
    void cmbDateFilterOnAction(ActionEvent event) {

        String filter = cmbDateFilter.getValue();
        ObservableList<PaymentTm> filteredList = FXCollections.observableArrayList();

        LocalDate today = LocalDate.now();

        for (PaymentTm tm : masterList) {

            // "2025-12-27 14:56:13" → "2025-12-27"
            String dateOnly = tm.getDate().split(" ")[0];
            LocalDate paymentDate = LocalDate.parse(dateOnly);

            switch (filter) {

                case "Today":
                    if (paymentDate.equals(today)) {
                        filteredList.add(tm);
                    }
                    break;

                case "Last 7 Days":
                    if (!paymentDate.isBefore(today.minusDays(7))) {
                        filteredList.add(tm);
                    }
                    break;

                case "This Month":
                    if (paymentDate.getMonth() == today.getMonth()
                            && paymentDate.getYear() == today.getYear()) {
                        filteredList.add(tm);
                    }
                    break;

                case "This Year":
                    if (paymentDate.getYear() == today.getYear()) {
                        filteredList.add(tm);
                    }
                    break;

                case "All":
                default:
                    filteredList.add(tm);
            }
        }

        tblPayments.setItems(filteredList);
    }

    private void calculateIncome() {

        double dailyIncome = 0;
        double monthlyIncome = 0;
        double yearlyIncome = 0;

        LocalDate today = LocalDate.now();

        for (PaymentTm tm : masterList) {

            // "2025-12-27 14:56:13" → "2025-12-27"
            String dateOnly = tm.getDate().split(" ")[0];
            LocalDate paymentDate = LocalDate.parse(dateOnly);

            double amount = tm.getTotal();

            // Daily
            if (paymentDate.equals(today)) {
                dailyIncome += amount;
            }

            // Monthly
            if (paymentDate.getMonth() == today.getMonth()
                    && paymentDate.getYear() == today.getYear()) {
                monthlyIncome += amount;
            }

            // Yearly
            if (paymentDate.getYear() == today.getYear()) {
                yearlyIncome += amount;
            }
        }

        lblDailyIncome.setText(String.format("%.2f", dailyIncome));
        lblMonthlyIncome.setText(String.format("%.2f", monthlyIncome));
        lblYearlyIncome.setText(String.format("%.2f", yearlyIncome));
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
