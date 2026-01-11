package lk.ijse.AutoCareCenter.controller;

import com.jfoenix.controls.JFXTextField;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import lk.ijse.AutoCareCenter.bo.BOFactory;
import lk.ijse.AutoCareCenter.bo.custom.ChequeBO;
import lk.ijse.AutoCareCenter.entity.ChequePayment;
import lk.ijse.AutoCareCenter.model.tm.ChequePaymentTm;

import java.util.List;

public class ChequeFormController {

    @FXML
    private TableView<ChequePaymentTm> tblCheque;

    @FXML
    private TableColumn<ChequePaymentTm, String> ColId;
    @FXML
    private TableColumn<ChequePaymentTm, String> ColPayId;
    @FXML
    private TableColumn<ChequePaymentTm, String> colCustomerName;
    @FXML
    private TableColumn<ChequePaymentTm, String> colCustomerPhone;
    @FXML
    private TableColumn<ChequePaymentTm, String> colChequeNo;
    @FXML
    private TableColumn<ChequePaymentTm, String> colBankName;
    @FXML
    private TableColumn<ChequePaymentTm, String> colBranch;
    @FXML
    private TableColumn<ChequePaymentTm, String> ColChequeDate;
    @FXML
    private TableColumn<ChequePaymentTm, Double> ColAmount;
    @FXML
    private TableColumn<ChequePaymentTm, String> ColStatus;

    @FXML
    private JFXTextField txtSearchDescription;

    @FXML
    private AnchorPane root;

    private final ObservableList<ChequePaymentTm> masterList = FXCollections.observableArrayList();

    private final ChequeBO chequeBO =
            (ChequeBO) BOFactory.getBoFactory().getBO(BOFactory.BOTypes.CHEQUE);

    // ================= INITIALIZE =================
    public void initialize() {

        ColId.setCellValueFactory(new PropertyValueFactory<>("chequeId"));
        ColPayId.setCellValueFactory(new PropertyValueFactory<>("paymentId"));
        colCustomerName.setCellValueFactory(new PropertyValueFactory<>("customerName"));
        colCustomerPhone.setCellValueFactory(new PropertyValueFactory<>("customerPhone"));
        colChequeNo.setCellValueFactory(new PropertyValueFactory<>("chequeNo"));
        colBankName.setCellValueFactory(new PropertyValueFactory<>("bankName"));
        colBranch.setCellValueFactory(new PropertyValueFactory<>("branch"));
        ColChequeDate.setCellValueFactory(new PropertyValueFactory<>("chequeDate"));
        ColAmount.setCellValueFactory(new PropertyValueFactory<>("amount"));
        ColStatus.setCellValueFactory(new PropertyValueFactory<>("status"));

        // ---------- STATUS TEXT COLOR ----------
        ColStatus.setCellFactory(col -> new TableCell<>() {
            @Override
            protected void updateItem(String status, boolean empty) {
                super.updateItem(status, empty);

                if (empty || status == null) {
                    setText(null);
                    return;
                }

                setText(status);
                setStyle("-fx-font-weight: bold");

                switch (status) {
                    case "PENDING" -> setTextFill(Color.ORANGE);
                    case "CLEARED" -> setTextFill(Color.GREEN);
                    case "BOUNCED" -> setTextFill(Color.RED);
                    default -> setTextFill(Color.BLACK);
                }
            }
        });

        // ---------- ROW FACTORY (Right Click + Double Click) ----------
        tblCheque.setRowFactory(tv -> {

            TableRow<ChequePaymentTm> row = new TableRow<>();

            // ----- Context Menu -----
            ContextMenu menu = new ContextMenu();
            MenuItem cleared = new MenuItem("Mark as CLEARED");
            MenuItem bounced = new MenuItem("Mark as BOUNCED");

            cleared.setOnAction(e -> updateStatus(row.getItem(), "CLEARED"));
            bounced.setOnAction(e -> updateStatus(row.getItem(), "BOUNCED"));

            menu.getItems().addAll(cleared, bounced);

            row.contextMenuProperty().bind(
                    javafx.beans.binding.Bindings.when(row.emptyProperty())
                            .then((ContextMenu) null)
                            .otherwise(menu)
            );

            // ----- Double Click -----
            row.setOnMouseClicked(e -> {
                if (e.getClickCount() == 2 && !row.isEmpty()) {

                    ChequePaymentTm tm = row.getItem();

                    if (!"PENDING".equals(tm.getStatus())) {
                        new Alert(Alert.AlertType.INFORMATION,
                                "Cheque already " + tm.getStatus()).show();
                        return;
                    }

                    Alert confirm = new Alert(
                            Alert.AlertType.CONFIRMATION,
                            "Mark cheque as CLEARED?"
                    );

                    confirm.showAndWait().ifPresent(btn -> {
                        if (btn == ButtonType.OK) {
                            updateStatus(tm, "CLEARED");
                        }
                    });
                }
            });

            return row;
        });

        loadAllCheques();
    }

    // ================= LOAD DATA =================
    private void loadAllCheques() {
        try {
            masterList.clear();
            List<ChequePayment> list = chequeBO.loadAll();

            for (ChequePayment c : list) {
                masterList.add(new ChequePaymentTm(
                        c.getChequeId(),
                        c.getPaymentId(),
                        c.getCustomerName(),
                        c.getCustomerPhone(),
                        c.getChequeNo(),
                        c.getBankName(),
                        c.getBranch(),
                        c.getChequeDate(),
                        c.getAmount(),
                        c.getStatus()
                ));
            }
            tblCheque.setItems(masterList);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ================= UPDATE STATUS =================
    private void updateStatus(ChequePaymentTm tm, String status) {
        if (tm == null) return;

        boolean updated = chequeBO.updateStatus(tm.getChequeId(), status);

        if (updated) {
            tm.setStatus(status);
            tblCheque.refresh();
        }
    }

    // ================= SEARCH =================
    @FXML
    void txtDescriptionOnKeyReleased(KeyEvent event) {

        String search = txtSearchDescription.getText().toLowerCase().trim();

        if (search.isEmpty()) {
            tblCheque.setItems(masterList);
            return;
        }

        ObservableList<ChequePaymentTm> filtered = FXCollections.observableArrayList();
        for (ChequePaymentTm tm : masterList) {
            if (tm.getCustomerName().toLowerCase().contains(search)) {
                filtered.add(tm);
            }
        }
        tblCheque.setItems(filtered);
    }

    @FXML
    void btnSearchOnAction(ActionEvent event) {
        txtDescriptionOnKeyReleased(null);

        if (tblCheque.getItems().isEmpty()) {
            new Alert(Alert.AlertType.INFORMATION, "No results found").show();
        }
    }

    @FXML
    void btnResetOnAction(ActionEvent event) {
        txtSearchDescription.clear();
        loadAllCheques();
    }

    @FXML
    void btnBackOnAction(ActionEvent event) {
        root.getScene().getWindow().hide();
    }

    public void btnDeleteOnAction(ActionEvent actionEvent) {
    }
}
