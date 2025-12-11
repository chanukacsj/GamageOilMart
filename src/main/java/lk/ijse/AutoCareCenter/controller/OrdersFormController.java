package lk.ijse.AutoCareCenter.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
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
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import lk.ijse.AutoCareCenter.Util.Regex;
import lk.ijse.AutoCareCenter.bo.BOFactory;
import lk.ijse.AutoCareCenter.bo.custom.*;
import lk.ijse.AutoCareCenter.db.DbConnection;
import lk.ijse.AutoCareCenter.entity.MaterialDetails;
import lk.ijse.AutoCareCenter.model.*;
import lk.ijse.AutoCareCenter.model.tm.OrdersTm;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.view.JasperViewer;


import java.io.IOException;
import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

public class OrdersFormController {
    @FXML
    private TableColumn<?, ?> colCharge;
    @FXML
    private JFXTextField txtServiceCharge;

    @FXML
    private JFXComboBox<String> cmbMaterialCode;

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
    private Label lblQtyOnHand;

    @FXML
    private Label lblUnitPrice;

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

    public void initialize() {
        setCellValueFactory();
        loadNextOrderId();
        setDate();
        getMaterialsIds();
    }

    private void setCellValueFactory() {
        colItemCode.setCellValueFactory(new PropertyValueFactory<>("code"));
        colDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
        colQty.setCellValueFactory(new PropertyValueFactory<>("qty"));
        colCharge.setCellValueFactory(new PropertyValueFactory<>("service_charge"));
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
        double service_charge = 0;
        if (!txtServiceCharge.getText().isEmpty()) {
            try {
                service_charge = Double.parseDouble(txtServiceCharge.getText());
            } catch (NumberFormatException e) {
                service_charge = 0; // fallback if invalid input
            }
        }
        double totals = (qty * unitPrice);
        double total = totals + service_charge;
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

            if (result.orElse(no).equals(yes)) {

                int selectedIndex = tblOrderCart.getSelectionModel().getSelectedIndex();
                System.out.println("Selected index = " + selectedIndex);

                if (selectedIndex >= 0) {
                    OrdersTm selectedItem = ordersList.get(selectedIndex);
                    System.out.println("Removing: " + selectedItem);

                    ordersList.remove(selectedIndex);

                    tblOrderCart.getItems().setAll(ordersList);
                    tblOrderCart.refresh();

                    calculateNetTotal();
                } else {
                    System.out.println("No item selected!");
                }
            }
        });

        for (int i = 0; i < tblOrderCart.getItems().size(); i++) {
            if (code.equals(colItemCode.getCellData(i))) {
                qty += ordersList.get(i).getQty();
                total = unitPrice * qty + service_charge;

                ordersList.get(i).setQty(qty);
                ordersList.get(i).setTotal(total);
                ordersList.get(i).setService_charge(service_charge);

                tblOrderCart.refresh();
                calculateNetTotal();
                txtQty.setText("");
                return;
            }
        }
        OrdersTm ordersTm = new OrdersTm(code, description, qty, unitPrice, service_charge, total, btnRemove);
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
//        double ServiceCharge = Double.parseDouble(txtServiceCharge.getText());
        lblNetTotal.setText(String.valueOf(netTotal));
    }


    @FXML
    void btnPlaceOrderOnAction(ActionEvent event) {

        if (tblOrderCart.getItems().isEmpty()) {
            new Alert(Alert.AlertType.WARNING,
                    "Cart is empty! Please add items before placing an order.").show();
            return;
        }
        String orderId = lblOrderId.getText();
        Date date = Date.valueOf(LocalDate.now());
        System.out.println(netTotal + "net total");
        boolean b = false;
        try {
            b = saveOrder(orderId, date,
                    tblOrderCart.getItems().stream().map(tm -> new OrderDetailsDTO(orderId, tm.getCode(), tm.getQty(), tm.getUnitPrice(), tm.getService_charge(), tm.getTotal())).collect(Collectors.toList()));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        if (b) {
            new Alert(Alert.AlertType.INFORMATION, "Order placed!").show();
        } else {
            new Alert(Alert.AlertType.WARNING, "order not placed!").show();
        }
    }

    public boolean saveOrder(String orderId, Date date, List<OrderDetailsDTO> orderDetails) throws SQLException, ClassNotFoundException {
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
        double ServiceCharge = Double.parseDouble(txtServiceCharge.getText());
        String lastTot = String.valueOf(netTotal);

        JasperDesign jasperDesign =
                JRXmlLoader.load("src/main/resources/Reports/newOne.jrxml");
        JasperReport jasperReport =
                JasperCompileManager.compileReport(jasperDesign);

        Map<String, Object> data = new HashMap<>();
        data.put("orderId", lblOrderId.getText());
        data.put("nettotal", lastTot);
        data.put("ServiceCharge", txtServiceCharge.getText());

        JasperPrint jasperPrint =
                JasperFillManager.fillReport(
                        jasperReport,
                        data,
                        DbConnection.getInstance().getConnection());

        JasperViewer.viewReport(jasperPrint, false);
    }

    @FXML
    void btnViewPaymentsOnAction(ActionEvent event) {

        try {
            AnchorPane pane = FXMLLoader.load(
                    getClass().getResource("/view/PaymentViewForm.fxml")
            );

            Scene scene = new Scene(pane);

            Stage stage = new Stage();
            stage.setScene(scene);
            stage.setTitle("Payment History");
            stage.centerOnScreen();
            stage.show();

        } catch (IOException e) {
            throw new RuntimeException("Failed to load PaymentViewForm.fxml", e);
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

}
