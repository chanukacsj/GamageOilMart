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
import lk.ijse.AutoCareCenter.bo.custom.SupplierBO;
import lk.ijse.AutoCareCenter.dao.custom.Impl.SupplierDAOImpl;
import lk.ijse.AutoCareCenter.entity.Supplier;
import lk.ijse.AutoCareCenter.model.SupplierDTO;
import lk.ijse.AutoCareCenter.model.tm.SupplierTm;

import java.sql.SQLException;
import java.util.ArrayList;

public class SupplierFormController {

    @FXML
    private TableColumn<?, ?> colAddress;

    @FXML
    private TableColumn<?, ?> colContact;

    @FXML
    private TableColumn<?, ?> colId;


    @FXML
    private TableColumn<?, ?> colName;

    @FXML
    private TableView<SupplierTm> tblSupplier;

    @FXML
    private TextField txtAddress;

    @FXML
    private TextField txtContact;

    @FXML
    private TextField txtName;
    @FXML
    private AnchorPane root;
    @FXML
    private Label lblId;
    Integer index;

    SupplierBO supplierBO = (SupplierBO) BOFactory.getBoFactory().getBO(BOFactory.BOTypes.SUPPLIER);

    public void initialize() {
        setCellValueFactory();
        loadAllSupplier();
        loadNextId();

    }

    private void setCellValueFactory() {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colAddress.setCellValueFactory(new PropertyValueFactory<>("address"));
        colContact.setCellValueFactory(new PropertyValueFactory<>("contact"));

    }

    @FXML
    void getSupplier(MouseEvent event) {
        index = tblSupplier.getSelectionModel().getSelectedIndex();
        if (index <= -1) {
            return;
        } else {
            lblId.setText(tblSupplier.getItems().get(index).getId());
            txtName.setText(tblSupplier.getItems().get(index).getName());
            txtAddress.setText(tblSupplier.getItems().get(index).getAddress());
            txtContact.setText(tblSupplier.getItems().get(index).getContact());
        }
    }

    private void loadNextId() {
        try {
            String currentId = supplierBO.currentId();
            String nextId = nextId(currentId);

            lblId.setText(nextId);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private String nextId(String currentId) {
        if (currentId != null) {
            String[] split = currentId.split("S");
//            System.out.println("Arrays.toString(split) = " + Arrays.toString(split));
            int id = Integer.parseInt(split[1]);    //2
            return "S" + ++id;

        }
        return "S";
    }

    public void loadAllSupplier() {
        tblSupplier.getItems().clear();

        try {
            ArrayList<SupplierDTO> supplierDTOS = supplierBO.loadAll();

            for (SupplierDTO supplierDTO : supplierDTOS) {
                tblSupplier.getItems().add(new SupplierTm(supplierDTO.getId(), supplierDTO.getName(), supplierDTO.getContact(), supplierDTO.getAddress()));
            }

        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        } catch (ClassNotFoundException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        }

    }

    private void clearFields() {

        txtName.setText("");
        txtAddress.setText("");
        txtContact.setText("");
        loadNextId();
    }

    @FXML
    void btnClearOnAction(ActionEvent event) {
        clearFields();

    }

    @FXML
    void btnSaveOnAction(ActionEvent event) {
        String id = lblId.getText();
        String name = txtName.getText();
        String address = txtAddress.getText();
        String contact = txtContact.getText();

        SupplierDTO supplierDTO = new SupplierDTO(id, name, address, contact);
        if (isValid()) {
            try {
                boolean isSaved = supplierBO.save(supplierDTO);
                if (isSaved) {
                    new Alert(Alert.AlertType.CONFIRMATION, "Supplier saved!").show();
                }
            } catch (SQLException e) {
                new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
            loadAllSupplier();
            clearFields();
        }
    }

    @FXML
    void btnUpdateOnAction(ActionEvent event) {

        String id = lblId.getText();
        String name = txtName.getText();
        String address = txtAddress.getText();
        String contact = txtContact.getText();

        SupplierDTO supplierDTO = new SupplierDTO(id, name, address, contact);
        if (isValid()) {
            try {
                boolean isUpdated = supplierBO.update(supplierDTO);
                if (isUpdated) {
                    new Alert(Alert.AlertType.CONFIRMATION, "supplier updated!").show();
                }
            } catch (SQLException e) {
                new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
        loadAllSupplier();
        clearFields();
    }

    @FXML
    void txtSearchOnAction(ActionEvent event) {
        String id = txtContact.getText();

        try {
            Supplier supplierDTO = supplierBO.searchById(id);

            if (supplierDTO != null) {
                lblId.setText(supplierDTO.getId());
                txtName.setText(supplierDTO.getName());
                txtAddress.setText(supplierDTO.getAddress());
                txtContact.setText(supplierDTO.getContact());
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

    }

    @FXML
    public void btnDeleteOnAction(ActionEvent actionEvent) {
        String id = lblId.getText();

        try {
            boolean isDeleted = supplierBO.delete(id);
            if (isDeleted) {
                new Alert(Alert.AlertType.CONFIRMATION, "supplier deleted!").show();
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        loadAllSupplier();
        clearFields();
    }

    public boolean isValid() {
        // if (!Regex.setTextColor(lk.ijse.AutoCareCenter.Util.TextField.ID, (JFXTextField) txtId)) return false;
        if (!Regex.setTextColor(lk.ijse.AutoCareCenter.Util.TextField.NAME, (JFXTextField) txtName)) return false;
        if (!Regex.setTextColor(lk.ijse.AutoCareCenter.Util.TextField.CONTACT, (JFXTextField) txtContact)) return false;
        if (!Regex.setTextColor(lk.ijse.AutoCareCenter.Util.TextField.ADDRESS, (JFXTextField) txtAddress)) return false;
        return true;
    }

    public void txtIdOnKeyReleased(KeyEvent keyEvent) {
        // Regex.setTextColor(lk.ijse.AutoCareCenter.Util.TextField.ID, (JFXTextField) txtId);
    }

    public void txtNameOnKeyReleased(KeyEvent keyEvent) {
        Regex.setTextColor(lk.ijse.AutoCareCenter.Util.TextField.NAME, (JFXTextField) txtName);

    }

    public void txtAddressOnKeyReleased(KeyEvent keyEvent) {
        Regex.setTextColor(lk.ijse.AutoCareCenter.Util.TextField.ADDRESS, (JFXTextField) txtAddress);
    }

    public void txtContactOnKeyReleased(KeyEvent keyEvent) {
        Regex.setTextColor(lk.ijse.AutoCareCenter.Util.TextField.CONTACT, (JFXTextField) txtContact);
    }
}
