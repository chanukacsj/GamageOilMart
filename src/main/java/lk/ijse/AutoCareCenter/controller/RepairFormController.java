package lk.ijse.AutoCareCenter.controller;

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
import lk.ijse.AutoCareCenter.Util.Regex;
import lk.ijse.AutoCareCenter.Util.TextField;
import lk.ijse.AutoCareCenter.bo.BOFactory;
import lk.ijse.AutoCareCenter.bo.custom.EmployeeBO;
import lk.ijse.AutoCareCenter.bo.custom.RepairBO;
import lk.ijse.AutoCareCenter.bo.custom.VehicleBO;
import lk.ijse.AutoCareCenter.dao.custom.Impl.EmployeeDAOImpl;
import lk.ijse.AutoCareCenter.dao.custom.Impl.RepairDAOImpl;
import lk.ijse.AutoCareCenter.dao.custom.Impl.VehicleDAOImpl;
import lk.ijse.AutoCareCenter.entity.Repair;
import lk.ijse.AutoCareCenter.model.RepairDTO;
import lk.ijse.AutoCareCenter.model.tm.RepairTm;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class RepairFormController {

    @FXML
    private JFXComboBox<String> cmbEmpId;

    @FXML
    private JFXComboBox<String> cmbVehicleId;

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
    private AnchorPane root;

    @FXML
    private TableView<RepairTm> tblPay;

    @FXML
    private JFXTextField txtDescription;

    @FXML
    private JFXTextField txtEndTime;

    @FXML
    private JFXTextField txtStartTime;
    @FXML
    private Label lblId;
    RepairBO repairBO = (RepairBO) BOFactory.getBoFactory().getBO(BOFactory.BOTypes.REPAIR);

    Integer index;

    public void initialize() {
        setCellValueFactory();
        getEmployeeIds();
        getVehicleId();
        loadAllRepair();
        loadNextId();
    }

    private void setCellValueFactory() {
        colRepId.setCellValueFactory(new PropertyValueFactory<>("rId"));
        colStart.setCellValueFactory(new PropertyValueFactory<>("startTime"));
        colEndTime.setCellValueFactory(new PropertyValueFactory<>("endTime"));
        colDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
        colEmployeeId.setCellValueFactory(new PropertyValueFactory<>("empId"));
        colVehicleId.setCellValueFactory(new PropertyValueFactory<>("vId"));
    }

    @FXML
    void getRepairs(MouseEvent event) {
        index = tblPay.getSelectionModel().getSelectedIndex();
        if (index <= -1) {
            return;
        } else {
            lblId.setText(tblPay.getItems().get(index).getRId());
            txtStartTime.setText(tblPay.getItems().get(index).getStartTime());
            txtEndTime.setText(tblPay.getItems().get(index).getEndTime());
            txtDescription.setText(tblPay.getItems().get(index).getDescription());
            cmbEmpId.setValue(tblPay.getItems().get(index).getEmpId());
            cmbVehicleId.setValue(tblPay.getItems().get(index).getVId());


        }
    }
    private void loadNextId() {
        try {
            String currentId = repairBO.currentId();
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
            String[] split = currentId.split("R");
//            System.out.println("Arrays.toString(split) = " + Arrays.toString(split));
            int id = Integer.parseInt(split[1]);    //2
            return "R" + ++id;

        }
        return "R1";
    }

    public void loadAllRepair() {
        tblPay.getItems().clear();

        try {
            ArrayList<RepairDTO> repairDTOS = repairBO.loadAll();

            for (RepairDTO repairDTO : repairDTOS) {
                tblPay.getItems().add(new RepairTm(repairDTO.getRId(), repairDTO.getStartTime(), repairDTO.getEndTime(), repairDTO.getDescription(), repairDTO.getEmpId(), repairDTO.getVId()));
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        } catch (ClassNotFoundException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        }

    }

    private void clearFields() {

        txtStartTime.setText("");
        txtEndTime.setText("");
        txtDescription.setText("");
        cmbVehicleId.setValue("");
        cmbEmpId.setValue("");
        loadNextId();
    }

    @FXML
    void btnClearOnAction(ActionEvent event) {
        clearFields();
    }

    @FXML
    void btnDeleteOnAction(ActionEvent event) {
        String id = lblId.getText();

        try {
            boolean isDeleted = repairBO.delete(id);
            System.out.println("awa");
            if (isDeleted) {
                new Alert(Alert.AlertType.CONFIRMATION, "deleted!").show();
                System.out.println("delete");
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        loadAllRepair();

    }

    @FXML
    void btnSaveOnAction(ActionEvent event) {
        String id = lblId.getText();
        String StartTime = txtStartTime.getText();
        String EndTime = txtEndTime.getText();
        String description = txtDescription.getText();
        String empId = cmbEmpId.getValue();
        String vId = cmbVehicleId.getValue();


        RepairDTO repairDTO = new RepairDTO(id, StartTime, EndTime, description, empId, vId);
        if (isValid()) {

            try {
                boolean isSaved = repairBO.save(repairDTO);
                if (isSaved) {
                    new Alert(Alert.AlertType.CONFIRMATION, "Saved").show();
                }
            } catch (SQLException e) {
                new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
            loadAllRepair();
            clearFields();

        }
    }

    @FXML
    void btnUpdateOnAction(ActionEvent event) {

        String id = lblId.getText();
        String StartTime = txtStartTime.getText();
        String EndTime = txtEndTime.getText();
        String description = txtDescription.getText();
        String empId = cmbEmpId.getValue();
        String vId = cmbVehicleId.getValue();


        RepairDTO repairDTO = new RepairDTO(id, StartTime, EndTime, description, empId, vId);
        if (isValid()) {
            try {
                boolean isSaved = repairBO.update(repairDTO);
                if (isSaved) {
                    new Alert(Alert.AlertType.CONFIRMATION, "Update!").show();
                }
            } catch (SQLException e) {
                new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
            loadAllRepair();
            clearFields();

        }
    }

    @FXML
    void txtSearchOnAction(ActionEvent event) {
        String id = cmbVehicleId.getValue();

        try {
            Repair repairDTO = repairBO.searchById(id);

            if (repairDTO != null) {
                lblId.setText(repairDTO.getRId());
                txtStartTime.setText(repairDTO.getStartTime());
                txtEndTime.setText(repairDTO.getEndTime());
                txtDescription.setText(repairDTO.getDescription());
                cmbEmpId.setValue(repairDTO.getEmpId());
                cmbVehicleId.setValue(repairDTO.getVId());
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    private void getEmployeeIds() {

        ObservableList<String> obList = FXCollections.observableArrayList();

        try {
            List<String> idList = repairBO.getRepairIds();

            for (String id : idList) {
                obList.add(id);
            }
            cmbEmpId.setItems(obList);


        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    private void getVehicleId() {

        ObservableList<String> obList = FXCollections.observableArrayList();

        try {
            List<String> idList = repairBO.getVehicleIds();

            for (String id : idList) {
                obList.add(id);
            }
            cmbVehicleId.setItems(obList);


        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean isValid() {
       // if (!Regex.setTextColor(lk.ijse.AutoCareCenter.Util.TextField.ID, (JFXTextField) txtRepairId)) return false;
        if (!Regex.setTextColor(TextField.NAME, (JFXTextField) txtDescription)) return false;

        return true;
    }

    public void txtIDOnKeyReleased(KeyEvent keyEvent) {
       // Regex.setTextColor(lk.ijse.AutoCareCenter.Util.TextField.ID, (JFXTextField) txtRepairId);

    }

    public void txtDescriptionOnKeyReleased(KeyEvent keyEvent) {
        Regex.setTextColor(TextField.NAME, (JFXTextField) txtDescription);
    }
}
