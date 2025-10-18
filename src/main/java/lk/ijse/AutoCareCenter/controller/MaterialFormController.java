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
import lk.ijse.AutoCareCenter.bo.SuperBO;
import lk.ijse.AutoCareCenter.bo.custom.CustomerBO;
import lk.ijse.AutoCareCenter.bo.custom.MaterialBO;
import lk.ijse.AutoCareCenter.bo.custom.MaterialDetailBO;
import lk.ijse.AutoCareCenter.bo.custom.SupplierBO;
import lk.ijse.AutoCareCenter.dao.custom.Impl.MaterialDAOImpl;
import lk.ijse.AutoCareCenter.dao.custom.Impl.MaterialDetailsDAOImpl;
import lk.ijse.AutoCareCenter.dao.custom.Impl.SupplierDAOImpl;
import lk.ijse.AutoCareCenter.entity.MaterialDetails;
import lk.ijse.AutoCareCenter.model.*;

import lk.ijse.AutoCareCenter.model.tm.MaterialsTm;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class MaterialFormController {
    @FXML
    private TableView<MaterialsTm> tblMaterial;
    @FXML
    private JFXComboBox<String> cmbSupId;


    @FXML
    private TableColumn<?, ?> colCode;

    @FXML
    private TableColumn<?, ?> colDescription;

    @FXML
    private TableColumn<?, ?> colUnitPrice;

    @FXML
    private TableColumn<?, ?> colQtyOnHand;
    @FXML
    private TableColumn<?, ?> colSupId;

    @FXML
    private AnchorPane root;

    //  @FXML
    // private TextField txtCode;

    @FXML
    private TextField txtDescription;

    @FXML
    private TextField txtQtyOnHand;

    @FXML
    private TextField txtUnitPrice;
    @FXML
    private Label lblId;


    MaterialDetailBO materialDetailBO = (MaterialDetailBO) BOFactory.getBoFactory().getBO(BOFactory.BOTypes.MATERIALDETAILS);
    MaterialBO materialBO = (MaterialBO) BOFactory.getBoFactory().getBO(BOFactory.BOTypes.MATERIAL);

    Integer index;

    public void initialize() {
        setCellValueFactory();
        loadNextId();
        loadAllMaterials();
        getSuplierIds();
    }

    private void setCellValueFactory() {
        colCode.setCellValueFactory(new PropertyValueFactory<>("code"));
        colDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
        colUnitPrice.setCellValueFactory(new PropertyValueFactory<>("unitPrice"));
        colQtyOnHand.setCellValueFactory(new PropertyValueFactory<>("qtyOnHand"));
        colSupId.setCellValueFactory(new PropertyValueFactory<>("supId"));
    }

    @FXML
    void getMaterials(MouseEvent event) {
        index = tblMaterial.getSelectionModel().getSelectedIndex();
        if (index <= -1) {
            return;
        } else {
            lblId.setText(tblMaterial.getItems().get(index).getCode());
            txtDescription.setText(tblMaterial.getItems().get(index).getDescription());
            txtUnitPrice.setText(String.valueOf(tblMaterial.getItems().get(index).getUnitPrice()));
            txtQtyOnHand.setText(String.valueOf(tblMaterial.getItems().get(index).getQtyOnHand()));
            cmbSupId.setValue(tblMaterial.getItems().get(index).getSupId());
        }
    }

    private void loadNextId() {
        try {
            String currentId = materialBO.currentId();
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
            String[] split = currentId.split("M");
//            System.out.println("Arrays.toString(split) = " + Arrays.toString(split));
            int id = Integer.parseInt(split[1]);    //2
            return "M" + ++id;

        }
        return "M1";
    }


    public void loadAllMaterials() {
        tblMaterial.getItems().clear();

        try {
            ArrayList<MaterialDetailsDTO> materialDetailsDTOS = materialDetailBO.loadAll();

            for (MaterialDetailsDTO materialsDTO : materialDetailsDTOS) {
                tblMaterial.getItems().add(new MaterialsTm(materialsDTO.getCode(), materialsDTO.getSupId(), materialsDTO.getDescription(), materialsDTO.getUnitPrice(), materialsDTO.getQtyOnHand()));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }


    public void codeSearchOnAction(ActionEvent actionEvent) {
        String code = txtDescription.getText();

        try {
            MaterialDetails materialDetailsDTO = materialDetailBO.searchById(code);

            if (materialDetailsDTO != null) {
                lblId.setText(materialDetailsDTO.getCode());
                txtDescription.setText(materialDetailsDTO.getDescription());
                txtQtyOnHand.setText(String.valueOf(materialDetailsDTO.getQtyOnHand()));
                txtUnitPrice.setText(String.valueOf(materialDetailsDTO.getUnitPrice()));
                cmbSupId.setValue(materialDetailsDTO.getSupId());
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

    }

    public void btnSaveOnAction(ActionEvent actionEvent) {

        String code = lblId.getText();
        String supId = (String) cmbSupId.getValue();
        String description = txtDescription.getText();
        double unitprice = Double.parseDouble(txtUnitPrice.getText());
        int qtyonhand = Integer.parseInt(txtQtyOnHand.getText());


        MaterialsDTO materialsDTO = new MaterialsDTO(code);
        MaterialDetailsDTO materialDetailsDTO = new MaterialDetailsDTO(code, supId, description, unitprice, qtyonhand);


        try {

            boolean isSaved = materialBO.save(materialsDTO);
            if (isSaved) {
                new Alert(Alert.AlertType.CONFIRMATION, "material save").show();
                boolean isSavedS = materialDetailBO.save(materialDetailsDTO);
                if (isSavedS) {
                    new Alert(Alert.AlertType.CONFIRMATION, "material_details save").show();
                }
            }

        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        loadAllMaterials();
        clearFields();
        loadNextId();
    }


    public void btnUpdateOnAction(ActionEvent actionEvent) {
        String code = lblId.getText();
        String supId = (String) cmbSupId.getValue();
        String description = txtDescription.getText();
        double unitprice = Double.parseDouble(txtUnitPrice.getText());
        int qtyonhand = Integer.parseInt(txtQtyOnHand.getText());


        MaterialsDTO material = new MaterialsDTO(code);
        MaterialDetailsDTO materialDetailsDTO = new MaterialDetailsDTO(code, supId, description, unitprice, qtyonhand);
        if (isValid()) {
            try {
                // boolean isUpdated = MaterialsRepo.update(material);
                boolean isupdated = materialDetailBO.update(materialDetailsDTO);
         /*   if (isUpdated) {
                new Alert(Alert.AlertType.CONFIRMATION, "material updated!").show();
            }*/
                if (isupdated) {
                    new Alert(Alert.AlertType.CONFIRMATION, "material updated!").show();
                }
            } catch (SQLException e) {
                new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
            loadAllMaterials();
            clearFields();
            loadNextId();
        }
    }

    @FXML
    public void btnDeleteOnAction(ActionEvent actionEvent) {
        String Code = lblId.getText();

        try {
            boolean isDeleted = materialBO.delete(Code);
            if (isDeleted) {
                new Alert(Alert.AlertType.CONFIRMATION, "Material deleted!").show();
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        loadAllMaterials();
        clearFields();
        loadNextId();
    }

    private void clearFields() {

        txtDescription.setText("");
        cmbSupId.setValue("");
        loadNextId();
    }

    public void btnClearOnAction(ActionEvent actionEvent) {
        clearFields();
    }

    @FXML
    private void getSuplierIds() {

        ObservableList<String> obList = FXCollections.observableArrayList();

        try {
            List<String> idList = materialBO.getSupplierIds();

            for (String id : idList) {
                obList.add(id);
            }
            cmbSupId.setItems(obList);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean isValid() {
        //  if (!Regex.setTextColor(lk.ijse.AutoCareCenter.Util.TextField.ID, (JFXTextField) txtCode)) return false;
        if (!Regex.setTextColor(lk.ijse.AutoCareCenter.Util.TextField.UNITPRICE, (JFXTextField) txtUnitPrice))
            return false;
        if (!Regex.setTextColor(lk.ijse.AutoCareCenter.Util.TextField.UNITPRICE, (JFXTextField) txtQtyOnHand))
            return false;
        return true;
    }

    public void txtIDOnKeyReleased(KeyEvent keyEvent) {
        //  Regex.setTextColor(lk.ijse.AutoCareCenter.Util.TextField.ID, (JFXTextField) txtCode);
    }

    public void txtPriceOnKeyReleased(KeyEvent keyEvent) {
        Regex.setTextColor(lk.ijse.AutoCareCenter.Util.TextField.UNITPRICE, (JFXTextField) txtUnitPrice);

    }

    public void txtQtyOnKeyReleased(KeyEvent keyEvent) {
        Regex.setTextColor(lk.ijse.AutoCareCenter.Util.TextField.UNITPRICE, (JFXTextField) txtQtyOnHand);
    }

}