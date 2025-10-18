package lk.ijse.AutoCareCenter.controller;


import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import lk.ijse.AutoCareCenter.Util.Regex;
import lk.ijse.AutoCareCenter.bo.BOFactory;
import lk.ijse.AutoCareCenter.bo.custom.CustomerBO;
import lk.ijse.AutoCareCenter.dao.custom.Impl.CustomerDAOImpl;
import lk.ijse.AutoCareCenter.entity.Customer;
import lk.ijse.AutoCareCenter.model.CustomerDTO;
import lk.ijse.AutoCareCenter.model.tm.CustomerTm;

import java.sql.SQLException;
import java.util.ArrayList;

public class CustomerFormController {

    @FXML
    private TableColumn<?, ?> colAddress;
    @FXML
    private Label lblCusId;

    @FXML
    private TableColumn<?, ?> colId;

    @FXML
    private TableColumn<?, ?> colName;

    @FXML
    private TableColumn<?, ?> colContact;

    @FXML
    private TableView<CustomerTm> tblCustomer;
    @FXML
    private AnchorPane root;
    @FXML
    private TextField txtAddress;

   // @FXML
   // private TextField txtId;

    @FXML
    private TextField txtName;

    @FXML
    private TextField txtContact;
    CustomerBO customerBO = (CustomerBO) BOFactory.getBoFactory().getBO(BOFactory.BOTypes.CUSTOMER);
    Integer index;

    public void initialize() {
        // this.customerList = getAllCustomers();
        setCellValueFactory();
        loadNextId();
        //loadCustomerTable();
        loadAllCustomers();
    }

    private void setCellValueFactory() {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colContact.setCellValueFactory(new PropertyValueFactory<>("contact"));
        colAddress.setCellValueFactory(new PropertyValueFactory<>("address"));


    }

    @FXML
    void getCustomers(MouseEvent event) {
        index = tblCustomer.getSelectionModel().getSelectedIndex();
        if (index <= -1) {
            return;
        } else {
            lblCusId.setText(tblCustomer.getItems().get(index).getId());
            txtName.setText(tblCustomer.getItems().get(index).getName());
            txtAddress.setText(tblCustomer.getItems().get(index).getAddress());
            txtContact.setText(tblCustomer.getItems().get(index).getContact());
        }
    }

    private void loadNextId() {
        try {
            String currentId = customerBO.currentId();
            String nextId = nextId(currentId);

            lblCusId.setText(nextId);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private String nextId(String currentId) {
        if (currentId != null) {
            String[] split = currentId.split("C");
//            System.out.println("Arrays.toString(split) = " + Arrays.toString(split));
            int id = Integer.parseInt(split[1]);    //2
            return "C" + ++id;

        }
        return "C1";
    }

    public void loadAllCustomers() {
        tblCustomer.getItems().clear();

        try {

            ArrayList<CustomerDTO> customerDTOS = customerBO.loadAll();

            for (CustomerDTO customerDTO : customerDTOS) {
                tblCustomer.getItems().add(new CustomerTm(customerDTO.getId(), customerDTO.getName(), customerDTO.getContact(),customerDTO.getAddress()));
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        } catch (ClassNotFoundException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        }
    }

    public void btnSaveOnAction(ActionEvent actionEvent) {
        String id = lblCusId.getText();
        String name = txtName.getText();
        String address = txtAddress.getText();
        String contact = txtContact.getText();

        System.out.println("" + id + " " + name + " " + address + " " + contact);
        CustomerDTO customerDTO = new CustomerDTO(id, name, address, contact);
        if (isValid()) {
            try {

                boolean isSaved = customerBO.save(customerDTO);
                System.out.println(customerDTO);
                if (isSaved) {
                    new Alert(Alert.AlertType.CONFIRMATION, "customer saved!").show();
                }
            } catch (SQLException e) {
                new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
            loadAllCustomers();
            clearFields();
            loadNextId();
        }
    }

    private void clearFields() {
      //  txtId.setText("");
        txtName.setText("");
        txtAddress.setText("");
        txtContact.setText("");
        loadNextId();
    }

    @FXML
    public void btnUpdateOnAction(ActionEvent actionEvent) {
        String id = lblCusId.getText();
        String name = txtName.getText();
        String address = txtAddress.getText();
        String contact = txtContact.getText();

        CustomerDTO customerDTO = new CustomerDTO(id, name, address, contact);
        if (isValid()) {
            try {


                boolean isUpdated = customerBO.update(customerDTO);
                if (isUpdated) {
                    new Alert(Alert.AlertType.CONFIRMATION, "customer updated!").show();
                }
            } catch (SQLException e) {
                new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
            loadAllCustomers();
            clearFields();
            loadNextId();
        }
    }

    @FXML
    public void btnDeleteOnAction(ActionEvent actionEvent) {
        String id = lblCusId.getText();

        try {

            boolean isDeleted = customerBO.delete(id);
            if (isDeleted) {
                new Alert(Alert.AlertType.CONFIRMATION, "customer deleted!").show();
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        loadAllCustomers();
        clearFields();
        loadNextId();
    }

    public void btnClearOnAction(ActionEvent actionEvent) {
        clearFields();

    }

    @FXML
    void txtSearchOnAction(ActionEvent event) {
        String id = txtName.getText();

        try {

            Customer customerDTO = customerBO.searchById(id);


            if (customerDTO != null) {
                lblCusId.setText(customerDTO.getId());
                txtName.setText(customerDTO.getName());
                txtAddress.setText(customerDTO.getAddress());
                txtContact.setText(customerDTO.getContact());
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean isValid() {
       // if (!Regex.setTextColor(lk.ijse.AutoCareCenter.Util.TextField.ID, (JFXTextField) txtId)) return false;
        if (!Regex.setTextColor(lk.ijse.AutoCareCenter.Util.TextField.NAME, (JFXTextField) txtName)) return false;
        if (!Regex.setTextColor(lk.ijse.AutoCareCenter.Util.TextField.CONTACT, (JFXTextField) txtContact)) return false;
        if (!Regex.setTextColor(lk.ijse.AutoCareCenter.Util.TextField.ADDRESS, (JFXTextField) txtAddress)) return false;
        return true;
    }

    public void txtCustomerIDOnKeyReleased(KeyEvent keyEvent) {
      //  Regex.setTextColor(lk.ijse.AutoCareCenter.Util.TextField.ID, (JFXTextField) txtId);
    }

    public void txtCustomerNameOnKeyReleased(KeyEvent keyEvent) {
        Regex.setTextColor(lk.ijse.AutoCareCenter.Util.TextField.NAME, (JFXTextField) txtName);

    }

    public void txtCustomerAddressOnKeyReleased(KeyEvent keyEvent) {
        Regex.setTextColor(lk.ijse.AutoCareCenter.Util.TextField.ADDRESS, (JFXTextField) txtAddress);
    }

    public void txtCustomerContactOnKeyReleased(KeyEvent keyEvent) {
        Regex.setTextColor(lk.ijse.AutoCareCenter.Util.TextField.CONTACT, (JFXTextField) txtContact);
    }
}

