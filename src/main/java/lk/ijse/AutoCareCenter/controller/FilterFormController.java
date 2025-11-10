package lk.ijse.AutoCareCenter.controller;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import lk.ijse.AutoCareCenter.Util.Regex;
import lk.ijse.AutoCareCenter.bo.BOFactory;
import lk.ijse.AutoCareCenter.bo.custom.MaterialBO;
import lk.ijse.AutoCareCenter.bo.custom.MaterialDetailBO;
import lk.ijse.AutoCareCenter.model.MaterialDetailsDTO;
import lk.ijse.AutoCareCenter.model.MaterialsDTO;
import lk.ijse.AutoCareCenter.model.tm.MaterialsTm;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class FilterFormController {

    @FXML
    private TableView<MaterialsTm> tblMaterial;

    @FXML
    private TableColumn<?, ?> colCode, colDescription, colUnitPrice, colQtyOnHand, colSupId, colBrand, colDate, colStatus;

    @FXML
    private JFXComboBox<String> cmbSupId;

    @FXML
    private JFXTextField txtDescription, txtUnitPrice, txtQtyOnHand, txtBrand;

    @FXML
    private Label lblId;

    @FXML
    private AnchorPane root;
    private EventHandler<KeyEvent> keyHandler;


    private final MaterialDetailBO materialDetailBO =
            (MaterialDetailBO) BOFactory.getBoFactory().getBO(BOFactory.BOTypes.MATERIALDETAILS);
    private final MaterialBO materialBO =
            (MaterialBO) BOFactory.getBoFactory().getBO(BOFactory.BOTypes.MATERIAL);

    private Integer index;

    public void initialize() {
        setCellValueFactory();
        loadNextId();
        loadAllFilterMaterials();
        getSupplierIds();
        setupKeyListeners();
    }

    private void setCellValueFactory() {
        colCode.setCellValueFactory(new PropertyValueFactory<>("code"));
        colDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
        colUnitPrice.setCellValueFactory(new PropertyValueFactory<>("unitPrice"));
        colQtyOnHand.setCellValueFactory(new PropertyValueFactory<>("qtyOnHand"));
        colSupId.setCellValueFactory(new PropertyValueFactory<>("supId"));
        colBrand.setCellValueFactory(new PropertyValueFactory<>("brand"));
        colDate.setCellValueFactory(new PropertyValueFactory<>("addedDate"));
        colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
    }

    private void loadNextId() {
        try {
            String currentId = materialDetailBO.currentId();
            String nextId = nextId(currentId);
            lblId.setText(nextId);
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private String nextId(String currentId) {
        if (currentId != null) {
            String[] split = currentId.split("M");
            int id = Integer.parseInt(split[1]);
            id++;
            return String.format("M%d", id);
        }
        return "M1";
    }

    private void setUi(String fileName) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/" + fileName));
        Pane Newroot = fxmlLoader.load();
        try {
            root.getChildren().clear();
            root.getChildren().setAll(Newroot);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadAllFilterMaterials() {
        tblMaterial.getItems().clear();

        try {
            ArrayList<MaterialDetailsDTO> all = materialDetailBO.loadAllByCategory("Filter");
            for (MaterialDetailsDTO dto : all) {
                tblMaterial.getItems().add(new MaterialsTm(
                        dto.getCode(),
                        dto.getSupId(),
                        dto.getDescription(),
                        dto.getUnitPrice(),
                        dto.getQtyOnHand(),
                        dto.getCategory(),
                        dto.getBrand(),
                        dto.getAddedDate(),
                        dto.getStatus()
                ));
            }
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    void getMaterials(MouseEvent event) {
        index = tblMaterial.getSelectionModel().getSelectedIndex();
        if (index <= -1) return;

        MaterialsTm tm = tblMaterial.getItems().get(index);
        lblId.setText(tm.getCode());
        txtDescription.setText(tm.getDescription());
        txtUnitPrice.setText(String.valueOf(tm.getUnitPrice()));
        txtQtyOnHand.setText(String.valueOf(tm.getQtyOnHand()));
        cmbSupId.setValue(tm.getSupId());
        txtBrand.setText(tm.getBrand());
    }

    @FXML
    void btnSaveOnAction(ActionEvent event) {
        String code = lblId.getText();
        String supId = cmbSupId.getValue();
        String description = txtDescription.getText();
        String category = "Filter";
        String brand = txtBrand.getText();
        double unitPrice = Double.parseDouble(txtUnitPrice.getText());
        int qtyOnHand = Integer.parseInt(txtQtyOnHand.getText());
        String addedDate = LocalDate.now().toString();
        String status = "Active";

        MaterialsDTO materialsDTO = new MaterialsDTO(code);
        MaterialDetailsDTO detailsDTO = new MaterialDetailsDTO(code, supId, description, unitPrice, qtyOnHand, category, brand, addedDate, status);

        try {
            boolean isDetailSaved = materialDetailBO.save(detailsDTO);
            if (isDetailSaved) {
                new Alert(Alert.AlertType.INFORMATION, "Filter item saved successfully!").show();
                loadAllFilterMaterials();
                clearFields();
                loadNextId();
            }
        } catch (SQLException | ClassNotFoundException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        }
    }

    @FXML
    void btnUpdateOnAction(ActionEvent event) {
        String code = lblId.getText();
        String supId = cmbSupId.getValue();
        String description = txtDescription.getText();
        String brand = txtBrand.getText();
        double unitPrice = Double.parseDouble(txtUnitPrice.getText());
        int qtyOnHand = Integer.parseInt(txtQtyOnHand.getText());

        MaterialDetailsDTO detailsDTO = new MaterialDetailsDTO(code, supId, description, unitPrice, qtyOnHand, "Filter", brand, LocalDate.now().toString(), "Active");

        if (isValid()) {
            try {
                boolean isUpdated = materialDetailBO.update(detailsDTO);
                if (isUpdated) {
                    new Alert(Alert.AlertType.INFORMATION, "Filter item updated!").show();
                    loadAllFilterMaterials();
                    clearFields();
                    loadNextId();
                }
            } catch (SQLException | ClassNotFoundException e) {
                new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
            }
        }
    }

    @FXML
    void btnDeleteOnAction(ActionEvent event) {
        String code = lblId.getText();

        if (code == null || code.isEmpty()) {
            new Alert(Alert.AlertType.WARNING, "Please select an item to delete!").show();
            return;
        }

        Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmAlert.setTitle("Confirm Delete");
        confirmAlert.setHeaderText(null);
        confirmAlert.setContentText("Are you sure you want to delete this Filter item?");
        ButtonType result = confirmAlert.showAndWait().orElse(ButtonType.CANCEL);

        if (result == ButtonType.OK) {
            try {
                boolean isDeleted = materialDetailBO.delete(code);
                if (isDeleted) {
                    new Alert(Alert.AlertType.INFORMATION, "Filter item deleted successfully!").show();
                    loadAllFilterMaterials();
                    clearFields();
                    loadNextId();
                } else {
                    new Alert(Alert.AlertType.WARNING, "Delete failed. Item may not exist!").show();
                }
            } catch (SQLException | ClassNotFoundException e) {
                new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
            }
        } else {
            new Alert(Alert.AlertType.INFORMATION, "Delete cancelled.").show();
        }
    }

    @FXML
    void btnClearOnAction(ActionEvent event) {
        clearFields();
        loadNextId();
    }

    private void clearFields() {
        txtDescription.clear();
        txtQtyOnHand.clear();
        txtUnitPrice.clear();
        txtBrand.clear();
        cmbSupId.setValue(null);
    }

    private void getSupplierIds() {
        ObservableList<String> obList = FXCollections.observableArrayList();
        try {
            List<String> idList = materialBO.getSupplierIds();
            obList.addAll(idList);
            cmbSupId.setItems(obList);
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean isValid() {
        return Regex.setTextColor(lk.ijse.AutoCareCenter.Util.TextField.UNITPRICE, txtUnitPrice)
                && Regex.setTextColor(lk.ijse.AutoCareCenter.Util.TextField.UNITPRICE, txtQtyOnHand);
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
    void txtPriceOnKeyReleased(KeyEvent event) {
        Regex.setTextColor(lk.ijse.AutoCareCenter.Util.TextField.UNITPRICE, txtUnitPrice);
    }

    @FXML
    void txtQtyOnKeyReleased(KeyEvent event) {
        Regex.setTextColor(lk.ijse.AutoCareCenter.Util.TextField.UNITPRICE, txtQtyOnHand);
    }

    @FXML
    void codeSearchOnAction(ActionEvent event) {
        System.out.println("Code search button clicked!");
    }

    public void btnBackOnAction(ActionEvent actionEvent) throws IOException {
        if (keyHandler != null && lblId.getScene() != null) {
            lblId.getScene().removeEventFilter(KeyEvent.KEY_PRESSED, keyHandler);
        }
        setUi("oils_form.fxml");
    }

}
