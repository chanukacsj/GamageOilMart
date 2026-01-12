package lk.ijse.AutoCareCenter.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Cursor;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import lk.ijse.AutoCareCenter.Util.Regex;
import lk.ijse.AutoCareCenter.bo.BOFactory;
import lk.ijse.AutoCareCenter.bo.custom.*;
import lk.ijse.AutoCareCenter.db.DbConnection;
import lk.ijse.AutoCareCenter.entity.ChequePayment;
import lk.ijse.AutoCareCenter.entity.MaterialDetails;
import lk.ijse.AutoCareCenter.model.*;
import lk.ijse.AutoCareCenter.model.tm.OrdersTm;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.view.JasperViewer;


import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

public class OrdersFormController {
    @FXML
    private TableColumn<?, ?> colCharge;

    @FXML
    private JFXComboBox<String> cmbMaterialDesc;

    @FXML
    private JFXTextField txtServiceCharge;

    @FXML
    private JFXButton btnPlaceOrder;

    @FXML
    private JFXButton btnChequeClose;

    @FXML
    private JFXTextField txtPaidAmount;

    @FXML
    private Label lblBalance;

    @FXML
    private JFXTextField txtCustomerName;

    @FXML
    private JFXTextField txtCustomerPhone;

    @FXML
    private JFXComboBox<String> cmbMaterialCode;

    @FXML
    private JFXTextField txtBranch;

    @FXML
    private JFXTextField txtChequeDate;

    @FXML
    private JFXTextField txtChequeNo;

    @FXML
    private TableColumn<?, ?> colAction;

    @FXML
    private TableColumn<?, ?> colDescription;

    @FXML
    private TableColumn<?, ?> colItemCode;

    @FXML
    private TableColumn<?, ?> colQty;

    @FXML
    private TableColumn<?, ?> colTotal;

    @FXML
    private TableColumn<?, ?> colUnitPrice;

    @FXML
    private Label LblCustomerName;
    @FXML
    private Label lblOrderId;

    @FXML
    private Label lblDescription;

    @FXML
    private Label lblNetTotal;

    @FXML
    private Label lblOrderDate;

    @FXML
    private AnchorPane chequePane;

    @FXML
    private JFXComboBox<String> cmbPaymentMethod;

    @FXML
    private Label lblQtyOnHand;

    @FXML
    private Label lblUnitPrice;

    @FXML
    private JFXTextField txtBank;

    @FXML
    private AnchorPane pane;

    @FXML
    private TableView<OrdersTm> tblOrderCart;

    @FXML
    private TextField txtQty;
    private ObservableList<OrdersTm> ordersList = FXCollections.observableArrayList();
    private double netTotal = 0;
    private String currentOrderId;
    PurchaseOrderBO purchaseOrderBO = (PurchaseOrderBO) BOFactory.getBoFactory().getBO(BOFactory.BOTypes.PO);
    ChequeBO chequeBO = (ChequeBO) BOFactory.getBoFactory().getBO(BOFactory.BOTypes.CHEQUE);
    double serviceCharge = 0.0;
    private Map<String, MaterialDetailsDTO> materialMap = new HashMap<>();


    @FXML
    private TextField txtBarcode;


    MaterialDetailBO materialDetailBO =
            (MaterialDetailBO) BOFactory.getBoFactory().getBO(BOFactory.BOTypes.MATERIALDETAILS);

    public void initialize() {
        setCellValueFactory();
        loadNextOrderId();
        setDate();
        getMaterialsIds();
        loadMaterialDescriptions();
        cmbPaymentMethod.getItems().addAll(
                "CASH",
                "CHEQUE"
        );

        chequePane.setVisible(false);

//        txtBarcode.setOnAction(e -> {
//            handleBarcodeScan();
//        });
        Platform.runLater(() -> txtBarcode.requestFocus());
        txtServiceCharge.textProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal.matches("\\d*(\\.\\d*)?")) {
                calculateNetTotal();
            }
        });

        Platform.runLater(() -> {
            setupArrowNavigation(txtBarcode, cmbMaterialDesc, null);
            setupArrowNavigation(cmbMaterialDesc, txtQty, txtBarcode);
            setupArrowNavigation(txtQty, txtServiceCharge, cmbMaterialDesc);
            setupArrowNavigation(txtServiceCharge, txtPaidAmount, txtQty);
            setupArrowNavigation(txtPaidAmount, btnPlaceOrder, txtServiceCharge);
        });

        txtQty.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            if (event.getCode() == KeyCode.ENTER) {
                btnPlaceCartOnAction(new ActionEvent());
                txtBarcode.requestFocus();
                event.consume();
            }
        });


        tblOrderCart.addEventFilter(KeyEvent.KEY_PRESSED, event -> {

            if (event.getCode() == KeyCode.DELETE) {
                OrdersTm selected = tblOrderCart.getSelectionModel().getSelectedItem();
                if (selected != null) {
                    ordersList.remove(selected);
                    calculateNetTotal();
                }
            }

            if (event.getCode() == KeyCode.ESCAPE) {
                txtBarcode.requestFocus();
            }
        });
        txtQty.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                btnPlaceCartOnAction(new ActionEvent());
                txtBarcode.requestFocus();
            }
        });


    }


    private void setCellValueFactory() {
        colItemCode.setCellValueFactory(new PropertyValueFactory<>("code"));
        colDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
        colQty.setCellValueFactory(new PropertyValueFactory<>("qty"));
        colUnitPrice.setCellValueFactory(new PropertyValueFactory<>("unitPrice"));
        colTotal.setCellValueFactory(new PropertyValueFactory<>("total"));
        colAction.setCellValueFactory(new PropertyValueFactory<>("btnRemove"));


    }

    private void loadNextOrderId() {
        try {
            String currentId = purchaseOrderBO.currentId();
            System.out.println("currentId = " + currentId);
            String nextId = nextId(currentId);
            System.out.println("nextId = " + nextId);

            lblOrderId.setText(nextId);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }


    private String nextId(String currentId) {
        if (currentId != null) {
            int id = Integer.parseInt(currentId.replace("O", ""));
            id++;
            return "O" + id;
        }
        return "O1";
    }


    @FXML
    void btnPlaceCartOnAction(ActionEvent event) {
        String code = cmbMaterialCode.getValue();
        String description = lblDescription.getText();
        int qty = Integer.parseInt(txtQty.getText());
        double unitPrice = Double.parseDouble(lblUnitPrice.getText());
        double totals = (qty * unitPrice);
        double total = totals;
        JFXButton btnRemove = new JFXButton("remove");
        btnRemove.setCursor(Cursor.HAND);

        if (cmbMaterialCode.getValue() == null) {
            new Alert(Alert.AlertType.WARNING, "Select an item first!").show();
            cmbMaterialCode.requestFocus();
            return;
        }

        if (txtQty.getText().isEmpty()) {
            new Alert(Alert.AlertType.WARNING, "Enter quantity!").show();
            txtQty.requestFocus();
            return;
        }

        btnRemove.setOnAction(e -> {

            ButtonType yes = new ButtonType("Yes", ButtonBar.ButtonData.OK_DONE);
            ButtonType no = new ButtonType("No", ButtonBar.ButtonData.CANCEL_CLOSE);

            Optional<ButtonType> result = new Alert(
                    Alert.AlertType.CONFIRMATION,
                    "Are you sure to remove?",
                    yes, no
            ).showAndWait();

            if (result.orElse(no) == yes) {

                OrdersTm tm = tblOrderCart.getItems()
                        .stream()
                        .filter(item -> item.getBtnRemove() == btnRemove)
                        .findFirst()
                        .orElse(null);

                if (tm != null) {
                    ordersList.remove(tm);
                    tblOrderCart.refresh();
                    calculateNetTotal();
                }
            }
        });


        for (int i = 0; i < tblOrderCart.getItems().size(); i++) {
            if (code.equals(colItemCode.getCellData(i))) {
                qty += ordersList.get(i).getQty();
                total = unitPrice * qty ;

                ordersList.get(i).setQty(qty);
                ordersList.get(i).setTotal(total);

                tblOrderCart.refresh();
                calculateNetTotal();
                txtQty.setText("");
                return;
            }
        }
        OrdersTm ordersTm = new OrdersTm(code, description, qty, unitPrice, total, btnRemove);
        System.out.println("total = " + total);
        ordersList.add(ordersTm);

        tblOrderCart.setItems(ordersList);
        txtQty.setText("");
        calculateNetTotal();
    }


    private void calculateNetTotal() {
        netTotal = 0;

        for (int i = 0; i < tblOrderCart.getItems().size(); i++) {
            netTotal += (double) colTotal.getCellData(i);
        }

        if (txtServiceCharge.getText() == null || txtServiceCharge.getText().isEmpty()) {
            serviceCharge = 0;
        } else {
            try {
                serviceCharge = Double.parseDouble(txtServiceCharge.getText());
            } catch (NumberFormatException e) {
                serviceCharge = 0;
            }
        }

        double total = netTotal + serviceCharge;
        lblNetTotal.setText(String.format("%.2f", total));
    }



    @FXML
    void btnPlaceOrderOnAction(ActionEvent event) throws Exception {

        if (tblOrderCart.getItems().isEmpty()) {
            new Alert(Alert.AlertType.WARNING,
                    "Cart is empty! Please add items before placing an order.").show();
            return;
        }
        String orderId = lblOrderId.getText();
        Date date = Date.valueOf(LocalDate.now());
        System.out.println(netTotal + "net total");
        String paymentId;
        try {
            paymentId = saveOrder(
                    orderId,
                    date,
                    tblOrderCart.getItems().stream()
                            .map(tm -> new OrderDetailsDTO(
                                    orderId,
                                    tm.getCode(),
                                    tm.getQty(),
                                    tm.getUnitPrice(),
                                    serviceCharge,
                                    tm.getTotal()
                            ))
                            .collect(Collectors.toList())
            );
        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Order failed!").show();
            return;
        }
        if (paymentId != null) {
            if ("CHEQUE".equals(cmbPaymentMethod.getValue())) {
                System.out.println("cheque" + paymentId);
                saveChequePayment(paymentId);
            }
            // 🧾 ASK TO PRINT BILL
            Optional<ButtonType> result = new Alert(
                    Alert.AlertType.CONFIRMATION,
                    "Print bill now?",
                    ButtonType.YES,
                    ButtonType.NO
            ).showAndWait();

            if (result.orElse(ButtonType.NO) == ButtonType.YES) {
                printBill(); // 🔥 AUTO PRINT
            }
            new Alert(Alert.AlertType.INFORMATION, "Order placed successfully!").show();
            loadNextOrderId();
           // tblOrderCart.getItems().clear();
            clearForm();
        } else {
            new Alert(Alert.AlertType.WARNING, "Order not placed!").show();
        }
    }

    public String saveOrder(String orderId, Date date, List<OrderDetailsDTO> orderDetails) throws SQLException, ClassNotFoundException {
        OrdersDTO orderDTO = new OrdersDTO(orderId, date, orderDetails);
        return purchaseOrderBO.saveOrder(orderDTO);
    }

    @FXML
    void cmbmaterialOnAction(ActionEvent event) {
        String code = cmbMaterialCode.getValue();
        System.out.println("code = " + code);
        try {
            MaterialDetails materials = purchaseOrderBO.searchByMaterialId(code);
            if (materials != null) {
                lblDescription.setText(materials.getDescription());
                lblUnitPrice.setText(String.valueOf(materials.getUnitPrice()));
                lblQtyOnHand.setText(String.valueOf(materials.getQtyOnHand()));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        txtQty.requestFocus();
    }

    @FXML
    void txtQtyOnAction(ActionEvent event) {
        btnPlaceCartOnAction(event);

    }

    private void setDate() {
        LocalDate now = LocalDate.now();
        lblOrderDate.setText(String.valueOf(now));
    }

    public void getMaterialsIds() {

        ObservableList<String> obList = FXCollections.observableArrayList();

        try {
            List<String> idList = purchaseOrderBO.getCodes();

            for (String id : idList) {
                obList.add(id);
            }
            cmbMaterialCode.setItems(obList);


        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }


    public boolean isValid() {
        if (!Regex.setTextColor(lk.ijse.AutoCareCenter.Util.TextField.SERVICECHARGE, txtServiceCharge)) return false;
        if (!Regex.setTextColor(lk.ijse.AutoCareCenter.Util.TextField.QTY, (JFXTextField) txtQty)) return false;

        return true;
    }


    public void txtSchargeOnKeyReleased(KeyEvent keyEvent) {

        Regex.setTextColor(lk.ijse.AutoCareCenter.Util.TextField.SERVICECHARGE, txtServiceCharge);
    }

    public void txtQtyOnKeyReleased(KeyEvent keyEvent) {
        Regex.setTextColor(lk.ijse.AutoCareCenter.Util.TextField.SERVICECHARGE, (JFXTextField) txtQty);

    }

    public void btnPrintBillOnAction(ActionEvent actionEvent) throws JRException, SQLException {
        String lastTot = String.valueOf(netTotal);

        JasperDesign jasperDesign =
                JRXmlLoader.load("src/main/resources/Reports/newOne.jrxml");
        JasperReport jasperReport =
                JasperCompileManager.compileReport(jasperDesign);

        Map<String, Object> data = new HashMap<>();
        data.put("orderId", lblOrderId.getText());
        data.put("nettotal", lblNetTotal.getText());
        data.put("ServiceCharge",txtServiceCharge.getText());

        JasperPrint jasperPrint =
                JasperFillManager.fillReport(
                        jasperReport,
                        data,
                        DbConnection.getInstance().getConnection());

        JasperViewer.viewReport(jasperPrint, false);
    }
    private void printBill() {
        try {
            JasperDesign jasperDesign =
                    JRXmlLoader.load("src/main/resources/Reports/newOne.jrxml");

            JasperReport jasperReport =
                    JasperCompileManager.compileReport(jasperDesign);

            Map<String, Object> data = new HashMap<>();
            data.put("orderId", lblOrderId.getText());
            data.put("nettotal", lblNetTotal.getText());
            data.put("ServiceCharge",
                    txtServiceCharge.getText().isEmpty() ? "0.00" : txtServiceCharge.getText());

            JasperPrint jasperPrint =
                    JasperFillManager.fillReport(
                            jasperReport,
                            data,
                            DbConnection.getInstance().getConnection()
                    );

            // 🔥 AUTO VIEW
            JasperViewer.viewReport(jasperPrint, false);

            // 🔥 AUTO PRINT (optional – uncomment if needed)
            // JasperPrintManager.printReport(jasperPrint, false);

        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Bill print failed!").show();
        }
    }


    @FXML
    private void openLoanPayment() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/LoanPaymentForm.fxml"));
            Parent root = loader.load();

            LoanPaymentController controller = loader.getController();

            // General info
            controller.setOrderId(lblOrderId.getText());
            controller.setNetTotal(Double.parseDouble(lblNetTotal.getText()));
            double serviceCharge = txtServiceCharge.getText().isEmpty() ? 0.0 :
                    Double.parseDouble(txtServiceCharge.getText());
            controller.setServiceCharge(serviceCharge);

            controller.setUnitPrice(Double.parseDouble(lblUnitPrice.getText()));
            controller.setDescription(lblDescription.getText());

            // 1️⃣ Full cart
            controller.setOrdersList(new ArrayList<>(ordersList));

            // 2️⃣ Optionally: Only selected items
            // ObservableList<OrdersTm> selectedItems = tblOrderCart.getSelectionModel().getSelectedItems();
            // controller.setOrdersList(new ArrayList<>(selectedItems));

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void ServiceChargeOnAction(ActionEvent actionEvent) {
    }
    private void handleBarcodeScan() {
        String barcode = txtBarcode.getText().trim();

        if (barcode.isEmpty()) return;

        try {
            // Find item by barcode
            MaterialDetailsDTO dto = materialDetailBO.findByBarcode(barcode);

            if (dto == null) {
                java.awt.Toolkit.getDefaultToolkit().beep(); // beep on error
                new Alert(Alert.AlertType.WARNING, "Item not found for barcode!").show();
                txtBarcode.clear();
                return;
            }

            String code = dto.getCode();

            // Check if item already exists in cart
            Optional<OrdersTm> existing = ordersList.stream()
                    .filter(item -> item.getCode().equals(code))
                    .findFirst();

            if (existing.isPresent()) {
                OrdersTm tm = existing.get();
                if (tm.getQty() + 1 > dto.getQtyOnHand()) {
                    java.awt.Toolkit.getDefaultToolkit().beep(); // beep on stock limit
                    new Alert(Alert.AlertType.WARNING, "Cannot add more than available stock!").show();
                } else {
                    tm.setQty(tm.getQty() + 1); // increment qty
                    tm.setTotal(tm.getQty() * tm.getUnitPrice()); // update total
                }
                tblOrderCart.refresh();
            } else {
                if (dto.getQtyOnHand() < 1) {
                    java.awt.Toolkit.getDefaultToolkit().beep(); // beep on stock empty
                    new Alert(Alert.AlertType.WARNING, "Item out of stock!").show();
                    txtBarcode.clear();
                    return;
                }

                // Add new item
                JFXButton btnRemove = new JFXButton("remove");
                btnRemove.setCursor(Cursor.HAND);
                btnRemove.setOnAction(e -> {
                    ordersList.removeIf(item -> item.getBtnRemove() == btnRemove);
                    tblOrderCart.refresh();
                    calculateNetTotal();
                });

                OrdersTm ordersTm = new OrdersTm(
                        dto.getCode(),
                        dto.getDescription(),
                        1, // default qty 1
                        dto.getUnitPrice(),
                        dto.getUnitPrice(), // total = unitPrice * qty
                        btnRemove
                );
                ordersList.add(ordersTm);
            }

            // Update UI labels
            cmbMaterialCode.setValue(dto.getCode());
            lblDescription.setText(dto.getDescription());
            lblUnitPrice.setText(String.valueOf(dto.getUnitPrice()));
            lblQtyOnHand.setText(String.valueOf(dto.getQtyOnHand()));

            calculateNetTotal();

            // Ready for next scan
            txtBarcode.clear();
            txtBarcode.requestFocus();

            java.awt.Toolkit.getDefaultToolkit().beep(); // beep on successful scan

        } catch (Exception e) {
            e.printStackTrace();
            java.awt.Toolkit.getDefaultToolkit().beep(); // beep on exception
        }
    }

    public void txtBarcodeOnAction(ActionEvent actionEvent) {
        handleBarcodeScan();
    }

    private void saveChequePayment(String paymentId) throws Exception {

        String chequeId = "C" + System.nanoTime();

        ChequePayment chequePayment = new ChequePayment(
                chequeId,
                paymentId,
                txtCustomerName.getText(),
                txtCustomerPhone.getText(),
                txtChequeNo.getText(),
                txtBank.getText(),
                txtBranch.getText(),
                txtChequeDate.getText(),
                Double.parseDouble(lblNetTotal.getText()),
                "PENDING"
        );

        boolean saved = chequeBO.saveCheque(chequePayment);

        if (saved) {
            new Alert(Alert.AlertType.INFORMATION, "Cheque saved").show();
        } else {
            new Alert(Alert.AlertType.ERROR, "Cheque failed").show();
        }
    }
    @FXML
    private void btnChequeCloseOnAction(ActionEvent event) {
        // Hide the cheque pane
        chequePane.setVisible(false);

        // Clear all fields
        txtChequeNo.clear();
        txtBank.clear();
        txtBranch.clear();
        txtChequeDate.clear();
        txtCustomerName.clear();
        txtCustomerPhone.clear();
    }
    @FXML
    void txtPaidAmountOnKeyReleased(KeyEvent event) {

        try {
            double netTotal = Double.parseDouble(lblNetTotal.getText());
            double paidAmount = Double.parseDouble(txtPaidAmount.getText());

            double balance = paidAmount - netTotal;

            if (balance < 0) {
                lblBalance.setText("0.00");
                lblBalance.setTextFill(javafx.scene.paint.Color.RED);
            } else {
                lblBalance.setText(String.format("%.2f", balance));
                lblBalance.setTextFill(javafx.scene.paint.Color.web("#27ae60"));
            }

        } catch (NumberFormatException e) {
            lblBalance.setText("0.00");
        }
    }
    @FXML
    void cmbPaymentMethodOnAction(ActionEvent event) {

        String method = cmbPaymentMethod.getValue();

        if ("CHEQUE".equals(method)) {
            chequePane.setVisible(true);
            txtPaidAmount.setDisable(true); // cheque → exact amount
            lblBalance.setText("0.00");
        } else {
            chequePane.setVisible(false);
            txtPaidAmount.setDisable(false); // cash → balance needed
        }
    }
    private void clearForm() {

        // 🧹 Clear table
        ordersList.clear();
        tblOrderCart.refresh();

        // 🧹 Clear totals
        netTotal = 0;
        serviceCharge = 0.0;
        lblNetTotal.setText("0.00");
        lblBalance.setText("0.00");

        // 🧹 Clear inputs
        txtQty.clear();
        txtServiceCharge.clear();
        txtPaidAmount.clear();
        txtBarcode.clear();

        // 🧹 Reset material selection
        cmbMaterialCode.getSelectionModel().clearSelection();
        lblDescription.setText("");
        lblUnitPrice.setText("");
        lblQtyOnHand.setText("");

        // 🧹 Reset payment
        cmbPaymentMethod.getSelectionModel().clearSelection();
        chequePane.setVisible(false);

        // 🧹 Clear cheque fields
        txtChequeNo.clear();
        txtBank.clear();
        txtBranch.clear();
        txtChequeDate.clear();
        txtCustomerName.clear();
        txtCustomerPhone.clear();

        // 🧹 Focus back to barcode (POS style)
        Platform.runLater(() -> txtBarcode.requestFocus());
    }

    private void loadMaterialDescriptions() {
        ObservableList<String> descList = FXCollections.observableArrayList();

        try {
            List<MaterialDetailsDTO> list = materialDetailBO.loadAll();

            for (MaterialDetailsDTO dto : list) {
                descList.add(dto.getDescription());
                materialMap.put(dto.getDescription(), dto);
            }

            cmbMaterialDesc.setItems(descList);
            enableDescriptionFilter();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void enableDescriptionFilter() {
        cmbMaterialDesc.getEditor().textProperty().addListener((obs, oldText, newText) -> {

            if (newText == null) return;

            List<String> filtered = materialMap.keySet().stream()
                    .filter(desc -> desc.toLowerCase().contains(newText.toLowerCase()))
                    .collect(Collectors.toList());

            cmbMaterialDesc.setItems(FXCollections.observableArrayList(filtered));
            cmbMaterialDesc.show();
        });
    }
    @FXML
    void cmbMaterialDescOnAction(ActionEvent event) {

        String desc = cmbMaterialDesc.getValue();
        if (desc == null) return;

        MaterialDetailsDTO dto = materialMap.get(desc);
        if (dto == null) return;

        // 🔥 Auto load everything
        cmbMaterialCode.setValue(dto.getCode());
        lblDescription.setText(dto.getDescription());
        lblUnitPrice.setText(String.valueOf(dto.getUnitPrice()));
        lblQtyOnHand.setText(String.valueOf(dto.getQtyOnHand()));

        txtQty.requestFocus();
    }
    private void setupArrowNavigation(Control current, Control next, Control previous) {

        current.addEventFilter(KeyEvent.KEY_PRESSED, event -> {

            switch (event.getCode()) {

                case ENTER:
                case DOWN:
                    if (next != null) {
                        next.requestFocus();
                        event.consume();
                    }
                    break;

                case UP:
                    if (previous != null) {
                        previous.requestFocus();
                        event.consume();
                    }
                    break;
            }
        });
    }


}
