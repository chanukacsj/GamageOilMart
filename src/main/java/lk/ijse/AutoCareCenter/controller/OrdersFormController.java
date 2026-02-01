package lk.ijse.AutoCareCenter.controller;

import com.jfoenix.controls.JFXButton;
import javafx.animation.PauseTransition;
import javafx.stage.Stage;
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
import javafx.util.Duration;
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
    private JFXComboBox<String> cmbPriceType;

    @FXML
    private JFXComboBox<String> cmbUnit;

    @FXML
    private JFXTextField txtServiceCharge;

    @FXML
    private JFXButton btnPlaceOrder;

    @FXML
    private JFXButton btnChequeClose;

    @FXML
    private JFXTextField txtPaidAmount;

    @FXML
    private JFXTextField txtDiscount;

    @FXML
    private Label lblDiscountAmount;

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
    private AnchorPane root;

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
    double discountAmount = 0;
    private double retailPrice = 0;
    private double wholesalePrice = 0;

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

        txtBarcode.setOnAction(e -> {
            System.out.println("txtBarcode");
            handleBarcodeScan();
        });

        Platform.runLater(() -> txtBarcode.requestFocus());
        txtServiceCharge.textProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal.matches("\\d*(\\.\\d*)?")) {
                calculateNetTotal();
            }
        });

        Platform.runLater(() -> {
            setupArrowNavigation(cmbMaterialDesc, txtQty, txtBarcode);
            setupArrowNavigation(txtQty, txtServiceCharge, cmbMaterialDesc);
            setupArrowNavigation(txtServiceCharge, txtPaidAmount, txtQty);
            setupArrowNavigation(txtPaidAmount, btnPlaceOrder, txtServiceCharge);
        });

//        txtQty.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
//            if (event.getCode() == KeyCode.ENTER) {
//                btnPlaceCartOnAction(new ActionEvent());
//                txtBarcode.requestFocus();
//                event.consume();
//            }
//        });


//        tblOrderCart.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
//
//            if (event.getCode() == KeyCode.DELETE) {
//                OrdersTm selected = tblOrderCart.getSelectionModel().getSelectedItem();
//                if (selected != null) {
//                    ordersList.remove(selected);
//                    calculateNetTotal();
//                }
//            }
//
//            if (event.getCode() == KeyCode.ESCAPE) {
//                txtBarcode.requestFocus();
//            }
//        });
//        txtQty.setOnKeyPressed(event -> {
//            if (event.getCode() == KeyCode.ENTER) {
//                btnPlaceCartOnAction(new ActionEvent());
//                txtBarcode.requestFocus();
//            }
//        });


        cmbUnit.setItems(FXCollections.observableArrayList("ML", "L"));
        cmbUnit.setValue("ML"); // default

        cmbUnit.setDisable(true); // default disable
        cmbMaterialDesc.setEditable(true);

        txtQty.setOnAction(event -> {
            btnPlaceCartOnAction(new ActionEvent());
            txtBarcode.requestFocus(); // next scan ready
        });
        root.addEventFilter(KeyEvent.KEY_PRESSED, e -> {

            switch (e.getCode()) {

//                case ENTER:
//                    try {
//                        btnPlaceCartOnAction(new ActionEvent());
//                    } catch (Exception ex) {
//                        throw new RuntimeException(ex);
//                    }
//                    break;

                case ESCAPE:
                    clearForm();
                    break;

                case F2:
                    cmbPaymentMethod.setValue("CASH");
                    break;

                case F3:
                    cmbPaymentMethod.setValue("CHEQUE");
                    break;
                case SHIFT:
                    try {
                        btnPlaceOrderOnAction(new ActionEvent());
                    } catch (Exception ex) {
                        throw new RuntimeException(ex);
                    }
                    break;
                case DELETE:
                    OrdersTm selected = tblOrderCart.getSelectionModel().getSelectedItem();
                    if (selected != null) {
                        ordersList.remove(selected);
                        calculateNetTotal();
                    }
                    break;
            }
        });

        txtDiscount.textProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal.matches("\\d*(\\.\\d*)?")) { // allow numbers with decimal
                calculateNetTotal();
            } else {
                txtDiscount.setText(oldVal); // prevent invalid input
            }
        });
        cmbPriceType.setItems(FXCollections.observableArrayList(
                "RETAIL",
                "WHOLESALE"
        ));

        cmbPriceType.setValue("RETAIL"); // default


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
        String qtyText = txtQty.getText();
        String unitPriceText = lblUnitPrice.getText();
        //   int qtyOnHand = Integer.parseInt(lblQtyOnHand.getText());
        double inputQty = Double.parseDouble(qtyText);
        double qtyOnHand = Double.parseDouble(lblQtyOnHand.getText());

        String unit = cmbUnit.isDisabled() ? "PCS" : cmbUnit.getValue();

        double finalQty = convertToBaseUnit(inputQty, unit);

        if (finalQty > qtyOnHand) {
            new Alert(Alert.AlertType.WARNING,
                    "Quantity exceeds available stock!").show();
            txtQty.clear();
            return;
        }

        if (code == null || code.isEmpty()) {
            new Alert(Alert.AlertType.WARNING, "Select an item first!").show();
            cmbMaterialCode.requestFocus();
            return;
        }
        if (description == null || description.isEmpty()) {
            new Alert(Alert.AlertType.WARNING, "Description is missing!").show();
            cmbMaterialCode.requestFocus();
            return;
        }
        if (qtyText == null || qtyText.isEmpty()) {
            new Alert(Alert.AlertType.WARNING, "Enter quantity!").show();
            txtQty.requestFocus();
            return;
        }
        if (!qtyText.matches("\\d+")) {
            new Alert(Alert.AlertType.WARNING, "Quantity must be a number!").show();
            txtQty.requestFocus();
            return;
        }
        if (unitPriceText == null || unitPriceText.isEmpty()) {
            new Alert(Alert.AlertType.WARNING, "Unit price is missing!").show();
            cmbMaterialCode.requestFocus();
            return;
        }

        int qty = Integer.parseInt(qtyText);
        double unitPrice = Double.parseDouble(unitPriceText);

        if (qtyOnHand <= 0) {
            new Alert(Alert.AlertType.WARNING, "Item is out of stock!").show();
            txtQty.clear();
            return;
        }

        if (qty > qtyOnHand) {
            new Alert(Alert.AlertType.WARNING, "Quantity exceeds available stock!").show();
            txtQty.clear();
            return;
        }
        double total = finalQty * unitPrice;

        //double total = qty * unitPrice;

        JFXButton btnRemove = new JFXButton("remove");
        btnRemove.setCursor(Cursor.HAND);

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

                double existingQty = ordersList.get(i).getQty(); // base unit (ml)
                double newQty = existingQty + finalQty;          // finalQty = converted qty

                if (newQty > qtyOnHand) {
                    new Alert(Alert.AlertType.WARNING,
                            "Cannot add more than available stock!").show();
                    return;
                }

                ordersList.get(i).setQty((int) newQty);
                ordersList.get(i).setTotal(newQty * unitPrice);

                tblOrderCart.refresh();
                calculateNetTotal();
                txtQty.clear();
                return;
            }
            java.awt.Toolkit.getDefaultToolkit().beep();
        }


        OrdersTm ordersTm = new OrdersTm(
                code,
                description + " (" + inputQty + " " + unit + ")",
                (int) finalQty,
                unitPrice,
                total,
                btnRemove
        );

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

        // Service charge
        if (txtServiceCharge.getText() == null || txtServiceCharge.getText().isEmpty()) {
            serviceCharge = 0;
        } else {
            try {
                serviceCharge = Double.parseDouble(txtServiceCharge.getText());
            } catch (NumberFormatException e) {
                serviceCharge = 0;
            }
        }

        double totalBeforeDiscount = netTotal + serviceCharge;

        // Direct discount amount
        double discountAmount = 0;
        if (txtDiscount.getText() != null && !txtDiscount.getText().isEmpty()) {
            try {
                discountAmount = Double.parseDouble(txtDiscount.getText());
            } catch (NumberFormatException e) {
                discountAmount = 0;
            }
        }

        if (discountAmount > totalBeforeDiscount) {
            discountAmount = totalBeforeDiscount;
        }

        lblDiscountAmount.setText(String.format("%.2f", discountAmount));

        double finalTotal = totalBeforeDiscount - discountAmount;
        lblNetTotal.setText(String.format("%.2f", finalTotal));
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

        if (txtDiscount.getText() != null && !txtDiscount.getText().isEmpty()) {
            try {
                discountAmount = Double.parseDouble(txtDiscount.getText());
                if (discountAmount < 0) {
                    new Alert(Alert.AlertType.WARNING, "Discount cannot be negative!").show();
                    txtDiscount.requestFocus();
                    return;
                }
            } catch (NumberFormatException e) {
                new Alert(Alert.AlertType.WARNING, "Invalid discount value!").show();
                txtDiscount.requestFocus();
                return;
            }
        }


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
                                    tm.getTotal(),
                                    discountAmount
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
                saveChequePayment(paymentId);
            }

            Optional<ButtonType> result = new Alert(
                    Alert.AlertType.CONFIRMATION,
                    "Print bill now?",
                    ButtonType.YES,
                    ButtonType.NO
            ).showAndWait();

            if (result.orElse(ButtonType.NO) == ButtonType.YES) {
                printBill();
            }

            new Alert(Alert.AlertType.INFORMATION, "Order placed successfully!").show();
            loadNextOrderId();

            PauseTransition pause = new PauseTransition(Duration.seconds(1.5));
            pause.setOnFinished(e -> refreshPage());
            pause.play();

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

        try {
            MaterialDetails materials = purchaseOrderBO.searchByMaterialId(code);

            if (materials != null) {


                retailPrice = materials.getUnitPrice();
                wholesalePrice = materials.getWholesalePrice();

                MaterialDetailsDTO dto = new MaterialDetailsDTO(
                        materials.getCode(),
                        materials.getSupId(),
                        materials.getDescription(),
                        materials.getCategory(),
                        materials.getBrand(),
                        materials.getUnitPrice(),
                        materials.getUnitCost(),
                        materials.getWholesalePrice(),
                        materials.getQtyOnHand(),
                        materials.getBarcode()
                );


                applyMaterialToUI(dto);


                if ("OIL".equalsIgnoreCase(materials.getCategory())) {
                    cmbUnit.setDisable(false);
                } else {
                    cmbUnit.setDisable(true);
                    cmbUnit.setValue("PCS");
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
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
        data.put("ServiceCharge", txtServiceCharge.getText());

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
            MaterialDetailsDTO dto = materialDetailBO.findByBarcode(barcode);
            if (dto == null) {
                new Alert(Alert.AlertType.WARNING, "Item not found for barcode!").show();
                txtBarcode.clear();
                return;
            }

            double qtyOnHand = dto.getQtyOnHand();

            // 🔹 Qty (default = 1)
            double inputQty = 1;
            if (txtQty.getText() != null && txtQty.getText().matches("\\d+")) {
                inputQty = Double.parseDouble(txtQty.getText());
            }

            double finalQty = convertToBaseUnit(inputQty, "PCS");

            if (finalQty > qtyOnHand) {
                new Alert(Alert.AlertType.WARNING, "Quantity exceeds available stock!").show();
                txtQty.clear();
                return;
            }


            double selectedPrice =
                    "WHOLESALE".equals(cmbPriceType.getValue())
                            ? dto.getWholesalePrice()
                            : dto.getUnitPrice();


            // 🔹 Check cart
            Optional<OrdersTm> existing = ordersList.stream()
                    .filter(item -> item.getCode().equals(dto.getCode()))
                    .findFirst();

            if (existing.isPresent()) {
                OrdersTm tm = existing.get();
                double newQty = tm.getQty() + finalQty;

                if (newQty > qtyOnHand) {
                    new Alert(Alert.AlertType.WARNING, "Cannot add more than available stock!").show();
                    return;
                }

                tm.setQty((int) newQty);
                tm.setTotal(newQty * tm.getUnitPrice());
                tblOrderCart.refresh();

            } else {
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
                        (int) finalQty,
                        selectedPrice,
                        finalQty * selectedPrice,
                        btnRemove
                );

                ordersList.add(ordersTm);
                tblOrderCart.setItems(ordersList);
            }

            // 🔹 UI updates
            applyMaterialToUI(dto);
          //  lblQtyOnHand.setText(String.valueOf(dto.getQtyOnHand()));

            calculateNetTotal();
            txtQty.clear();
            txtBarcode.clear();
            txtBarcode.requestFocus();

        } catch (Exception e) {
            e.printStackTrace();
            java.awt.Toolkit.getDefaultToolkit().beep();
        }
    }



    public void txtBarcodeOnAction(ActionEvent actionEvent) {
        //handleBarcodeScan();
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
    private void applyMaterialToUI(MaterialDetailsDTO dto) {

        if (dto == null) return;

        // Sync combos
        cmbMaterialCode.setValue(dto.getCode());

        // Labels
        lblDescription.setText(dto.getDescription());
        lblQtyOnHand.setText(String.valueOf(dto.getQtyOnHand()));

        // Prices (🔥 update controller level variables)
        retailPrice = dto.getUnitPrice();
        wholesalePrice = dto.getWholesalePrice();

        // Apply selected price type
        if ("WHOLESALE".equals(cmbPriceType.getValue())) {
            lblUnitPrice.setText(String.valueOf(wholesalePrice));
        } else {
            lblUnitPrice.setText(String.valueOf(retailPrice));
        }

        // Unit handling
        if ("OIL".equalsIgnoreCase(dto.getCategory())) {
            cmbUnit.setDisable(false);
            cmbUnit.setValue("ML");
        } else {
            cmbUnit.setDisable(true);
            cmbUnit.setValue("PCS");
        }
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

    //    private void enableDescriptionFilter() {
//        cmbMaterialDesc.getEditor().textProperty().addListener((obs, oldText, newText) -> {
//
//            if (newText == null) return;
//
//            // 🔥 If empty → reload all descriptions
//            if (newText.trim().isEmpty()) {
//                cmbMaterialDesc.setItems(
//                        FXCollections.observableArrayList(materialMap.keySet())
//                );
//                return;
//            }
//
//            List<String> filtered = materialMap.keySet().stream()
//                    .filter(desc -> desc.toLowerCase().contains(newText.toLowerCase()))
//                    .collect(Collectors.toList());
//
//            cmbMaterialDesc.setItems(FXCollections.observableArrayList(filtered));
//            cmbMaterialDesc.show();
//        });
//    }
    private void enableDescriptionFilter() {

        TextField editor = cmbMaterialDesc.getEditor();

        editor.textProperty().addListener((obs, oldText, newText) -> {

            if (newText == null || newText.isEmpty()) {
                cmbMaterialDesc.hide();
                cmbMaterialDesc.setItems(
                        FXCollections.observableArrayList(materialMap.keySet())
                );
                return;
            }

            List<String> filtered = materialMap.keySet().stream()
                    .filter(desc -> desc.toLowerCase().contains(newText.toLowerCase()))
                    .collect(Collectors.toList());

            cmbMaterialDesc.setItems(FXCollections.observableArrayList(filtered));

            if (!filtered.isEmpty()) {
                cmbMaterialDesc.show();
            }
        });
    }


    @FXML
    void cmbMaterialDescOnAction(ActionEvent event) {

        String desc = cmbMaterialDesc.getValue();
        if (desc == null) return;

        MaterialDetailsDTO dto = materialMap.get(desc);
        if (dto == null) return;

        applyMaterialToUI(dto);
        cmbMaterialDesc.getEditor().clear();
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

    private double convertToBaseUnit(double qty, String unit) {

        if (unit == null) return qty;

        switch (unit) {
            case "L":
                return qty * 1000;     // 1L = 1000ml
            default: // ML
                return qty;
        }
    }
    @FXML
    void cmbPriceTypeOnAction(ActionEvent event) {

        if (cmbPriceType.getValue() == null) return;

        if ("WHOLESALE".equals(cmbPriceType.getValue())) {
            lblUnitPrice.setText(String.valueOf(wholesalePrice));
        } else {
            lblUnitPrice.setText(String.valueOf(retailPrice));
        }

        txtQty.requestFocus();
    }


    private void refreshPage() {
        try {
            AnchorPane pane = FXMLLoader.load(
                    getClass().getResource("/view/orders_form.fxml")
            );

            root.getChildren().setAll(pane);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
