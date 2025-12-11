package lk.ijse.AutoCareCenter.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import lk.ijse.AutoCareCenter.bo.BOFactory;
import lk.ijse.AutoCareCenter.bo.custom.LoanBO;
import lk.ijse.AutoCareCenter.model.LoanDTO;
import lk.ijse.AutoCareCenter.model.LoanPaymentDTO;
import lk.ijse.AutoCareCenter.model.tm.LoanTm;
import lk.ijse.AutoCareCenter.model.tm.OrdersTm;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public class LoanPaymentController {

    @FXML
    private JFXButton btnClear, btnPay, btnSaveLoan;
    @FXML
    private Label lblBalance, lblDescription, lblTotal, lblOrderId, lblUnitPrice;
    @FXML
    private JFXComboBox<?> cmbMaterialCode;
    @FXML
    private Label lblLoanId;
    @FXML private TableView<OrdersTm> tblOrderCart;
    @FXML private TableColumn<OrdersTm, String> colItemCode;
    @FXML private TableColumn<OrdersTm, String> colDescription;
    @FXML private TableColumn<OrdersTm, Integer> colQty;
    @FXML private TableColumn<OrdersTm, Double> colUnitPrice;
    @FXML private TableColumn<OrdersTm, Double> colCharge;
    @FXML private TableColumn<OrdersTm, Double> colTotal;
    @FXML private TableColumn<OrdersTm, Void> colAction;

    @FXML private JFXTextField txtCustomerName;
    @FXML private JFXTextField txtDownPayment;
    @FXML private JFXTextField txtPayAmount;
    @FXML private JFXTextField txtPhone;
    @FXML private JFXTextField txtQty;
    @FXML private JFXTextField txtServiceCharge;

    // Loan Table
    @FXML private TableView<LoanTm> tblLoans;
    @FXML private TableColumn<LoanTm, String> colLoanId;
    @FXML private TableColumn<LoanTm, String> colOrderId;
    @FXML private TableColumn<LoanTm, String> colCustName;
    @FXML private TableColumn<LoanTm, String> colPhone;
    @FXML private TableColumn<LoanTm, Double> colTotalAmount;
    @FXML private TableColumn<LoanTm, Double> colPaidAmount;
    @FXML private TableColumn<LoanTm, Double> colRemainingAmount;
    @FXML private TableColumn<LoanTm, String> colNextDue;
    @FXML private TableColumn<LoanTm, String> colStatus;
    @FXML private TableColumn<LoanTm, Void> colActions;

    private final LoanBO loanBO = (LoanBO) BOFactory.getBoFactory().getBO(BOFactory.BOTypes.LOAN);
    private List<OrdersTm> ordersList;

    public void initialize() {
        setupOrderTable();
        setupLoanTable();

        txtDownPayment.textProperty().addListener((obs, oldVal, newVal) -> calculateTotal());

        loadNextId();
        loadAllLoans();
    }

    // ------------------ Table Setup ------------------

    private void setupOrderTable() {
        colItemCode.setCellValueFactory(new PropertyValueFactory<>("code"));
        colDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
        colQty.setCellValueFactory(new PropertyValueFactory<>("qty"));
        colUnitPrice.setCellValueFactory(new PropertyValueFactory<>("unitPrice"));
        colCharge.setCellValueFactory(new PropertyValueFactory<>("service_charge"));
        colTotal.setCellValueFactory(new PropertyValueFactory<>("total"));

        colAction.setCellFactory(param -> new TableCell<OrdersTm, Void>() {
            private final Button btn = new Button("Remove");

            {
                btn.setStyle("-fx-background-color: #e53935; -fx-text-fill:white;");
                btn.setCursor(Cursor.HAND);
                btn.setOnAction(e -> {
                    OrdersTm selected = getTableView().getItems().get(getIndex());
                    getTableView().getItems().remove(selected);
                    calculateTotal();
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : btn);
            }
        });
    }

    private void setupLoanTable() {
        colLoanId.setCellValueFactory(new PropertyValueFactory<>("loanId"));
        colOrderId.setCellValueFactory(new PropertyValueFactory<>("orderId"));
        colCustName.setCellValueFactory(new PropertyValueFactory<>("customerName"));
        colPhone.setCellValueFactory(new PropertyValueFactory<>("phone"));
        colTotalAmount.setCellValueFactory(new PropertyValueFactory<>("total"));
        colPaidAmount.setCellValueFactory(new PropertyValueFactory<>("paid"));
        colRemainingAmount.setCellValueFactory(new PropertyValueFactory<>("remaining"));
        colNextDue.setCellValueFactory(new PropertyValueFactory<>("nextDue"));
        colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));

        colActions.setCellFactory(param -> new TableCell<LoanTm, Void>() {
            private final Button btn = new Button("Pay");

            {
                btn.setStyle("-fx-background-color:#43a047; -fx-text-fill:white;");
                btn.setCursor(Cursor.HAND);

                btn.setOnAction(e -> {
                    LoanTm tm = getTableView().getItems().get(getIndex());
                    openPaymentWindow(tm.getLoanId());
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : btn);
            }
        });
    }

    // ------------------ Load Loans ------------------

    private void loadAllLoans() {
        try {
            List<LoanDTO> list = loanBO.getAllLoans();
            ObservableList<LoanTm> obList = FXCollections.observableArrayList();
            for (LoanDTO d : list) {
                obList.add(new LoanTm(
                        d.getLoanId(),
                        d.getOrderId(),
                        d.getCustomerName(),
                        d.getPhone(),
                        d.getTotal(),
                        d.getPaid(),
                        d.getRemaining(),
                        d.getNextDue(),
                        d.getStatus(),
                        d.getDate()
                ));
            }

            tblLoans.setItems(obList);

        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Failed to load loans!").show();
        }
    }

    // ------------------ Payment Window ------------------
    @FXML
    private void openPaymentWindow(String loanId) {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Loan Payment");
        dialog.setHeaderText("Enter Payment Amount");
        dialog.setContentText("Amount:");

        dialog.showAndWait().ifPresent(amountStr -> {
            try {
                double amount = Double.parseDouble(amountStr);

                LoanPaymentDTO dto = new LoanPaymentDTO(
                        UUID.randomUUID().toString(),
                        loanId,
                        amount,
                        LocalDate.now()
                );

                boolean ok = loanBO.payLoan(dto);

                if (ok) {
                    new Alert(Alert.AlertType.INFORMATION, "Payment Successful!").show();
                    loadAllLoans();
                } else {
                    new Alert(Alert.AlertType.ERROR, "Payment Failed!").show();
                }

            } catch (Exception e) {
                new Alert(Alert.AlertType.ERROR, "Invalid Amount!").show();
            }
        });
    }
    // ------------------ Loan Save ------------------

    @FXML
    private void btnSaveLoanOnAction() {

        if (txtCustomerName.getText().isEmpty() || txtPhone.getText().isEmpty()) {
            new Alert(Alert.AlertType.ERROR, "Customer details are required!").show();
            return;
        }

        double total = lblTotal.getText().isEmpty() ? 0.0 : Double.parseDouble(lblTotal.getText());
        double downPayment = txtDownPayment.getText().isEmpty() ? 0.0 : Double.parseDouble(txtDownPayment.getText());
        double balance = lblBalance.getText().isEmpty() ? 0.0 : Double.parseDouble(lblBalance.getText());
        System.out.println("total_"+total);
        LoanDTO dto = new LoanDTO(
                lblLoanId.getText(),
                lblOrderId.getText(),
                txtCustomerName.getText(),
                txtPhone.getText(),
                total,
                downPayment,
                balance,
                "0.0",
                "ONGOING",
                LocalDate.now().toString()
        );

        System.out.println("dto" + dto);
        try {
            boolean saved = loanBO.save(dto);
            new Alert(saved ? Alert.AlertType.INFORMATION : Alert.AlertType.ERROR,
                    saved ? "Loan Saved Successfully!" : "Loan Save Failed!").show();

            if (saved) loadAllLoans();
        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Error while saving loan!").show();
        }
    }

    // ------------------ Utility Methods ------------------

    private void loadNextId() {
        try {
            String currentId = loanBO.currentId();
            lblLoanId.setText(nextId(currentId));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private String nextId(String currentId) {
        if (currentId != null) {
            String[] split = currentId.split("L");
            return "L" + (Integer.parseInt(split[1]) + 1);
        }
        return "L1";
    }

    private void calculateTotal() {
        double totalAmount = tblOrderCart.getItems().stream()
                .mapToDouble(OrdersTm::getTotal)
                .sum();

        lblTotal.setText(String.valueOf(totalAmount));

        double downPayment = txtDownPayment.getText().isEmpty()
                ? 0
                : Double.parseDouble(txtDownPayment.getText());

        lblBalance.setText(String.valueOf(totalAmount - downPayment));
    }

    public void setOrdersList(List<OrdersTm> ordersList) {
        this.ordersList = ordersList;
        tblOrderCart.setItems(FXCollections.observableArrayList(ordersList));
        calculateTotal();
    }

    @FXML
    private void btnBackOnAction() {
        Stage stage = (Stage) tblLoans.getScene().getWindow();
        stage.close();
    }
    public void setOrderId(String orderId) {
        lblOrderId.setText(orderId);
    }
    public void setNetTotal(double netTotal) {
        lblTotal.setText(String.valueOf(netTotal));
        calculateTotal();
    }
    public void setServiceCharge(double charge) {
        txtServiceCharge.setText(String.valueOf(charge));
    }
    public void setUnitPrice(double price) {
        lblUnitPrice.setText(String.valueOf(price));
    }
    public void setDescription(String description) {
        lblDescription.setText(description);
    }


    public void openPaymentWindow(ActionEvent actionEvent) {
    }
}
