package lk.ijse.AutoCareCenter.controller;

import com.jfoenix.controls.JFXTextField;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import lk.ijse.AutoCareCenter.Util.Regex;
import lk.ijse.AutoCareCenter.bo.BOFactory;
import lk.ijse.AutoCareCenter.bo.custom.SupplierBO;
import lk.ijse.AutoCareCenter.dao.custom.Impl.SupplierDAOImpl;
import lk.ijse.AutoCareCenter.entity.Supplier;
import lk.ijse.AutoCareCenter.model.SupplierDTO;
import lk.ijse.AutoCareCenter.model.tm.MaterialsTm;
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

    private EventHandler<KeyEvent> keyHandler;

    SupplierBO supplierBO = (SupplierBO) BOFactory.getBoFactory().getBO(BOFactory.BOTypes.SUPPLIER);

    public void initialize() {
        setCellValueFactory();
        loadAllSupplier();
        loadNextId();
        setupKeyListeners();

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
        if (currentId != null && currentId.startsWith("S")) {
            String[] split = currentId.split("S");
            int id = 0;

            if (split.length > 1 && !split[1].isEmpty()) {
                id = Integer.parseInt(split[1]);
            }

            id++;
            return String.format("S%03d", id);
        }

        return "S001";
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

//    @FXML
//    void get(MouseEvent event) {
//        index = tblSupplier.getSelectionModel().getSelectedIndex();
//        if (index <= -1) return;
//
//        SupplierTm tm = tblSupplier.getItems().get(index);
//        lblId.setText(tm.getId());
//        txtName.setText(tm.getName());
//        txtAddress.setText(tm.getAddress());
//        txtContact.setText(tm.getContact());
//
//    }

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
                System.out.println("e = " + e);
                new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
            loadAllSupplier();
            clearFields();
        }
    }
    private void setupKeyListeners() {
        Platform.runLater(() -> {
            keyHandler = event -> {
                if (event.getCode() == KeyCode.ENTER) {
                    btnSaveOnAction(new ActionEvent());
                    event.consume();
                } else if (event.getCode() == KeyCode.DELETE) {
                    btnDeleteOnAction(new ActionEvent());
                    event.consume();
                }
            };
            lblId.getScene().addEventFilter(KeyEvent.KEY_PRESSED, keyHandler);
        });
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
    public void btnDeleteOnAction(ActionEvent event) {
        String id = lblId.getText();

        // Check if a supplier is selected
        if (id == null || id.isEmpty()) {
            new Alert(Alert.AlertType.WARNING, "Please select a supplier to delete!").show();
            return;
        }

        // Confirmation alert
        Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmAlert.setTitle("Confirm Delete");
        confirmAlert.setHeaderText(null);
        confirmAlert.setContentText("Are you sure you want to delete this supplier?");

        // Wait for user choice
        ButtonType result = confirmAlert.showAndWait().orElse(ButtonType.CANCEL);

        if (result == ButtonType.OK) {
            try {
                boolean isDeleted = supplierBO.delete(id);

                if (isDeleted) {
                    new Alert(Alert.AlertType.INFORMATION, "Supplier deleted successfully!").show();
                    loadAllSupplier();
                    clearFields();
                    loadNextId();
                } else {
                    new Alert(Alert.AlertType.WARNING, "Delete failed. Supplier may not exist!").show();
                }

            } catch (SQLException | ClassNotFoundException e) {
                new Alert(Alert.AlertType.ERROR, "Error while deleting supplier: " + e.getMessage()).show();
            }

        } else {
            new Alert(Alert.AlertType.INFORMATION, "Delete cancelled.").show();
        }
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

    public void onClose() {
        System.out.println("On close");
        if (keyHandler != null && lblId.getScene() != null) {
            lblId.getScene().removeEventFilter(KeyEvent.KEY_PRESSED, keyHandler);
        }
    }

}
