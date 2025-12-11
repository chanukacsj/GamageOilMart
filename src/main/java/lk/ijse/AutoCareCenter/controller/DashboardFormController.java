package lk.ijse.AutoCareCenter.controller;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import lk.ijse.AutoCareCenter.dao.custom.Impl.*;
import lk.ijse.AutoCareCenter.db.DbConnection;
import lk.ijse.AutoCareCenter.model.RepairDTO;
import lk.ijse.AutoCareCenter.model.tm.RepairTm;
import javafx.util.Duration;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class DashboardFormController {
    @FXML
    private TableColumn<?, ?> colDescription;

    @FXML
    private TableColumn<?, ?> colEmployeeId;

    @FXML
    private TableColumn<?, ?> colEndTime;

    @FXML
    private TableColumn<?, ?> colRepId;

    @FXML
    private TableColumn<?, ?> colStart;

    @FXML
    private TableColumn<?, ?> colVehicleId;
    @FXML
    private TableView<RepairTm> tblPay;
    @FXML
    public AnchorPane dashPane;
    @FXML
    public AnchorPane rootNode;
    @FXML
    public Label lblCustomerCount;
    @FXML
    private Label lblBookingCount;


    @FXML
    private Label lblMaterialCount;

    @FXML
    private Label lblOrdersCount;


    @FXML
    private Label lblTime;

    private int customerCount;
    private int bookingCount;
    private int materialCount;
    private int ordersCount;

    BookingDAOImpl bookingDAO = new BookingDAOImpl();



    public void initialize() {
       // setCellValueFactory();
      //  loadAllRepair();
        initClock();
//        try {
//            customerCount = getCustomerCount();
//            ordersCount = getOrdersCount();
//            bookingCount = getBookingCount();
//            materialCount = getMaterialCount();
//
//        } catch (SQLException e) {
//            throw new RuntimeException(e);
//        }
//        setCustomerCount(customerCount);
//        setOrdersCount(ordersCount);
//        setBookingCount(bookingCount);
//        setMaterialCount(materialCount);


    }

//    private void setCellValueFactory() {
//        colRepId.setCellValueFactory(new PropertyValueFactory<>("rId"));
//        colStart.setCellValueFactory(new PropertyValueFactory<>("startTime"));
//        colEndTime.setCellValueFactory(new PropertyValueFactory<>("endTime"));
//        colDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
//        colEmployeeId.setCellValueFactory(new PropertyValueFactory<>("empId"));
//        colVehicleId.setCellValueFactory(new PropertyValueFactory<>("vId"));
//    }

    private void initClock() {
        Timeline clock = new Timeline(new KeyFrame(Duration.ZERO, e -> {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd        HH:mm:ss");
            lblTime.setText(LocalDateTime.now().format(formatter));
        }), new KeyFrame(Duration.seconds(1)));
        clock.setCycleCount(Animation.INDEFINITE);
        clock.play();
    }

    private void setCustomerCount(int customerCount) {

        lblCustomerCount.setText(String.valueOf(customerCount));
    }

    private void setOrdersCount(int ordersCount) {

        lblOrdersCount.setText(String.valueOf(ordersCount));
    }

    private void setBookingCount(int bookingCount) {

        lblBookingCount.setText(String.valueOf(bookingCount));
    }

    private void setMaterialCount(int materialCount) {

        lblMaterialCount.setText(String.valueOf(materialCount));
    }


    private int getOrdersCount() throws SQLException {
        OrderDAOImpl orderDAO = new OrderDAOImpl();
        int orderCount = 0;
        try {
            orderCount = orderDAO.getOrderCount();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        return orderCount;
    }



    private int getBookingCount() throws SQLException {

        int bookingCount = 0;
        try {
            bookingCount = bookingDAO.getBookingCount();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        return bookingCount;
    }

    private int getMaterialCount() throws SQLException {
        int materialsCount = 0;

        MaterialDAOImpl materialDAO = new MaterialDAOImpl();
        try {
            materialsCount =  materialDAO.getMaterialCount();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        return materialsCount;
    }

    private void setUi(String fileName) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/" + fileName));
        Pane root = fxmlLoader.load();
        try {
            dashPane.getChildren().clear();
            dashPane.getChildren().setAll(root);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void btnCustomerOnAction() throws IOException {
        setUi("customer_form.fxml");
    }

    public void btnOrdersOnAction() throws IOException {
        setUi("orders_form.fxml");
    }

    public void btnBookingOnAction() throws IOException {
        setUi("booking_form.fxml");
    }

    public void btnVehicleOnAction() throws IOException {
        setUi("vehicle_form.fxml");
    }

    public void btnEmployeeOnAction() throws IOException {
        setUi("Employee_form.fxml");
    }

    public void btnSalaryOnAction() throws IOException {
        setUi("salary_form.fxml");
    }

    public void btnSupplierOnAction() throws IOException {
        setUi("supplier_form.fxml");
    }

    public void btnMaterialsOnAction() throws IOException {
        setUi("material_form.fxml");
    }

    public void btnRepairOnAction() throws IOException {
        setUi("repair_form.fxml");
    }

    public void exit(ActionEvent actionEvent) throws IOException {

        AnchorPane rootNode = FXMLLoader.load(this.getClass().getResource("/view/login_form.fxml"));

        Scene scene = new Scene(rootNode);
        Stage stage = (Stage) this.rootNode.getScene().getWindow();
        stage.setScene(scene);
        stage.setTitle("Login Form");
    }

//    public void loadAllRepair() {
//        ObservableList<RepairTm> repairList = FXCollections.observableArrayList();
//
//        try {
//            ArrayList<Repair> repairsList = repairDAO.loadAll();
//            for (Repair repairDTO : repairsList) {
//                repairList.add(new RepairTm(
//                        repairDTO.getRId(),
//                        repairDTO.getStartTime(),
//                        repairDTO.getEndTime(),
//                        repairDTO.getDescription(),
//                        repairDTO.getEmpId(),
//                        repairDTO.getVId()
//                ));
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//        } catch (ClassNotFoundException e) {
//            throw new RuntimeException(e);
//        }
//        tblPay.setItems(repairList);
//    }

    public void btnHomeOnAction(ActionEvent actionEvent) throws IOException {
        AnchorPane rootNode = FXMLLoader.load(this.getClass().getResource("/view/dashboard_form.fxml"));

        Scene scene = new Scene(rootNode);
        Stage stage = (Stage) this.rootNode.getScene().getWindow();
        stage.setScene(scene);
        stage.setTitle("Login Form");
    }

    public void btnAllPayOnAction(ActionEvent actionEvent) throws IOException {
        setUi("AllPay.fxml");
    }

    public void btnLoanOnAction(ActionEvent actionEvent) throws IOException {
        setUi("LoanPaymentForm.fxml");
    }
}
