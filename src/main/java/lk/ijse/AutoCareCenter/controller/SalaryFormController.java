package lk.ijse.AutoCareCenter.controller;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import lk.ijse.AutoCareCenter.Util.Regex;
import lk.ijse.AutoCareCenter.bo.BOFactory;
import lk.ijse.AutoCareCenter.bo.custom.EmployeeBO;
import lk.ijse.AutoCareCenter.bo.custom.SalaryBO;
import lk.ijse.AutoCareCenter.dao.custom.Impl.EmployeeDAOImpl;
import lk.ijse.AutoCareCenter.dao.custom.Impl.SalaryDAOImpl;
import lk.ijse.AutoCareCenter.entity.Employee;
import lk.ijse.AutoCareCenter.entity.Salary;
import lk.ijse.AutoCareCenter.model.*;
import lk.ijse.AutoCareCenter.model.tm.SalaryTm;


import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SalaryFormController {


    @FXML
    private JFXComboBox<String> CmbEmpId;



    @FXML
    private TextField amount;

    @FXML
    private TableColumn<?, ?> colAmount;


    @FXML
    private TableColumn<?, ?> colId;

    @FXML
    private TableColumn<?, ?> colSid;


    @FXML
    private AnchorPane rootNode;



    @FXML
    private TableView<SalaryTm> tblSalary;
    @FXML
    private TableColumn<?, ?> colMonth;
    @FXML
    private JFXComboBox<String> CmbMonth;
    @FXML
    private Label LblEmpName;
    @FXML
    private Label lblId;
    SalaryBO salaryBO = (SalaryBO) BOFactory.getBoFactory().getBO(BOFactory.BOTypes.SALARY);
    Integer index;

    public void initialize() {
        setCellValueFactory();
        getEmployeeIds();
        setMonth();
        loadAllSalary();
        loadNextId();
    }

    private void setCellValueFactory() {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colAmount.setCellValueFactory(new PropertyValueFactory<>("amount"));
        colMonth.setCellValueFactory(new PropertyValueFactory<>("month"));
        colSid.setCellValueFactory(new PropertyValueFactory<>("empId"));
    }
    @FXML
    void getSalary(MouseEvent event) {
        index = tblSalary.getSelectionModel().getSelectedIndex();
        if(index <= -1) {
            return;
        } else {
            lblId.setText(tblSalary.getItems().get(index).getId());
            amount.setText(String.valueOf(tblSalary.getItems().get(index).getAmount()));
            CmbMonth.setValue(tblSalary.getItems().get(index).getMonth());
            CmbEmpId.setValue(tblSalary.getItems().get(index).getEmpId());
        }
    }
    private void loadNextId() {
        try {
            String currentId = salaryBO.currentId();
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
        return "S1";
    }

    private void setMonth() {
        ObservableList<String> obList = FXCollections.observableArrayList();
        obList.add("January");
        obList.add("February");
        obList.add("March");
        obList.add("April");
        obList.add("May");
        obList.add("June");
        obList.add("July");
        obList.add("August");
        obList.add("September");
        obList.add("October");
        obList.add("November");
        obList.add("December");
        CmbMonth.setItems(obList);
    }
    public void loadAllSalary() {
        tblSalary.getItems().clear();

        try{
           ArrayList<SalaryDTO> salaryDTOS = salaryBO.loadAll();

           for (SalaryDTO salaryDTO : salaryDTOS) {
               tblSalary.getItems().add(new SalaryTm(salaryDTO.getId(), salaryDTO.getAmount(), salaryDTO.getMonth(), salaryDTO.getEmpId()));
           }
       } catch (SQLException e) {
           new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
       } catch (ClassNotFoundException e) {
           new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
       }
    }

    @FXML
    void btnSaveOnAction(ActionEvent event) {
        String id = lblId.getText();
        int Amount = Integer.parseInt(amount.getText());
        String Month = CmbMonth.getValue();
        String EmpId = (String) CmbEmpId.getValue();


        SalaryDTO salaryDTO = new SalaryDTO(id, Amount, Month, EmpId);
        if (isValid()) {
            try {
                boolean isSaved = salaryBO.save(salaryDTO);
                if (isSaved) {
                    new Alert(Alert.AlertType.CONFIRMATION, "salary saved!").show();
                }
            } catch (SQLException e) {
                new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            loadAllSalary();
            clearFields();
        }
    }

    @FXML
    void btnUpdateOnAction(ActionEvent event) {
        String id = lblId.getText();
        int Amount = Integer.parseInt(amount.getText());
        String Month = CmbMonth.getValue();
        String EmpId = (String) CmbEmpId.getValue();


        SalaryDTO salaryDTO = new SalaryDTO(id, Amount, Month, EmpId);
        if (isValid()) {
            try {
                boolean isUpdated = salaryBO.update(salaryDTO);
                if (isUpdated) {
                    new Alert(Alert.AlertType.CONFIRMATION, "vehicle updated!").show();
                }
            } catch (SQLException e) {
                new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
            loadAllSalary();
            clearFields();
        }
    }

    @FXML
    void txtSearchOnAction(ActionEvent event) {
        String id = lblId.getText();


        try {
            Salary salaryDTO = salaryBO.searchById(id);

            if (salaryDTO != null) {
                lblId.setText(salaryDTO.getId());
                amount.setText(String.valueOf(salaryDTO.getAmount()));
                CmbMonth.setValue(salaryDTO.getMonth());
                CmbEmpId.setValue(salaryDTO.getEmpId());
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public void btnDeleteOnAction(ActionEvent actionEvent) {
        String id = lblId.getText();

        try {
            boolean isDeleted = salaryBO.delete(id);
            if (isDeleted) {
                new Alert(Alert.AlertType.CONFIRMATION, "vehicle deleted!").show();
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        loadAllSalary();
        clearFields();
    }

    @FXML
    private void getEmployeeIds() {

        ObservableList<String> obList = FXCollections.observableArrayList();

        try {
            List<String> idList = salaryBO.getIds();

            for (String id : idList) {
                obList.add(id);
            }
            CmbEmpId.setItems(obList);


        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public void cmbEmployeeOnAction(ActionEvent actionEvent) {

        String id = CmbEmpId.getValue();
        try {
            Employee employeeDTO = salaryBO.searchByEmpId(id);
            if (employeeDTO != null) {
                LblEmpName.setText(employeeDTO.getName());

            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

    }



    private void clearFields() {

        amount.setText("");
        CmbMonth.setValue("");
        CmbEmpId.setValue("");
        loadNextId();
    }

    public void btnClearOnAction(ActionEvent actionEvent) {
        clearFields();
    }

    public boolean isValid() {
      //  if (!Regex.setTextColor(lk.ijse.AutoCareCenter.Util.TextField.ID, (JFXTextField) sId)) return false;
        if (!Regex.setTextColor(lk.ijse.AutoCareCenter.Util.TextField.UNITPRICE, (JFXTextField) amount)) return false;

        return true;
    }

    public void txtIDOnKeyReleased(KeyEvent keyEvent) {
       // Regex.setTextColor(lk.ijse.AutoCareCenter.Util.TextField.ID, (JFXTextField) sId);

    }

    public void txtAmountOnKeyReleased(KeyEvent keyEvent) {
        Regex.setTextColor(lk.ijse.AutoCareCenter.Util.TextField.UNITPRICE, (JFXTextField) amount);
    }
}

