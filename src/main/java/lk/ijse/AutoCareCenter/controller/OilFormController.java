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
import lk.ijse.AutoCareCenter.model.SupplierDTO;
import lk.ijse.AutoCareCenter.model.tm.MaterialsTm;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class OilFormController {

    @FXML
    private TableView<MaterialsTm> tblMaterial;

    @FXML
    private TableColumn<?, ?> colCode, colDescription, colUnitPrice, colQtyOnHand, colSupId, colBrand, colDate, colStatus, colBarcode;

    @FXML
    private JFXComboBox<SupplierDTO> cmbSupId;

    @FXML
    private JFXTextField txtDescription, txtUnitPrice, txtQtyOnHand, txtBrand;

    @FXML
    private Label lblId;

    @FXML
    private JFXTextField txtSearchDescription;

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
        loadAllOilMaterials();
        getSupplierIds();
        setupKeyListeners();
        Platform.runLater(() -> {
            lblId.getScene().getWindow().setOnHidden(event -> onClose());
        });

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
        colBarcode.setCellValueFactory(new PropertyValueFactory<>("barcode"));
    }

    private void loadNextId() {
        try {
            String currentId = materialDetailBO.currentId();
            System.out.println("currentId = " + currentId);
            String nextId = nextId(currentId);
            System.out.println("nextId = " + nextId);
            lblId.setText(nextId);
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private String nextId(String currentId) {
        if (currentId != null) {
            String[] split = currentId.split("M");
            int id = Integer.parseInt(split[1]);
            return "M" + ++id;
        }
        return "M1";
    }

    private void loadAllOilMaterials() {
        tblMaterial.getItems().clear();

        try {
            ArrayList<MaterialDetailsDTO> all = materialDetailBO.loadAllByCategory("Oil");
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
                        dto.getStatus(),
                        dto.getBarcode()
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
        for (SupplierDTO supplier : cmbSupId.getItems()) {
            if (supplier.getId().equals(tm.getSupId())) {
                cmbSupId.setValue(supplier);
                break;
            }
        }
        txtBrand.setText(tm.getBrand());
    }

    @FXML
    void btnSaveOnAction(ActionEvent event) {
        String code = lblId.getText();
        SupplierDTO selectedSupplier = cmbSupId.getValue();
        String supId = selectedSupplier != null ? selectedSupplier.getId() : null;
        String description = txtDescription.getText();
        String category = "Oil";
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
                new Alert(Alert.AlertType.INFORMATION, "Oil item saved successfully!").show();
                loadAllOilMaterials();
                clearFields();
                loadNextId();
            }
        } catch (SQLException | ClassNotFoundException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    @FXML
    void btnUpdateOnAction(ActionEvent event) {
        String code = lblId.getText();
        SupplierDTO selectedSupplier = cmbSupId.getValue();
        String supId = selectedSupplier != null ? selectedSupplier.getId() : null;
        String description = txtDescription.getText();
        String brand = txtBrand.getText();
        double unitPrice = Double.parseDouble(txtUnitPrice.getText());
        int qtyOnHand = Integer.parseInt(txtQtyOnHand.getText());

        MaterialDetailsDTO detailsDTO = new MaterialDetailsDTO(code, supId, description, unitPrice, qtyOnHand, "Oil", brand, LocalDate.now().toString(), "Active");

        if (isValid()) {
            try {
                boolean isUpdated = materialDetailBO.update(detailsDTO);
                if (isUpdated) {
                    new Alert(Alert.AlertType.INFORMATION, "Oil item updated!").show();
                    loadAllOilMaterials();
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

        // Check if a code is selected
        if (code == null || code.isEmpty()) {
            new Alert(Alert.AlertType.WARNING, "Please select an item to delete!").show();
            return;
        }

        // Confirmation alert
        Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmAlert.setTitle("Confirm Delete");
        confirmAlert.setHeaderText(null);
        confirmAlert.setContentText("Are you sure you want to delete this Oil item?");

        // Wait for user choice
        ButtonType result = confirmAlert.showAndWait().orElse(ButtonType.CANCEL);

        if (result == ButtonType.OK) {
            try {
                boolean isDeleted = materialDetailBO.delete(code);
                if (isDeleted) {
                    new Alert(Alert.AlertType.INFORMATION, "Oil item deleted successfully!").show();
                    loadAllOilMaterials();
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
    }

    private void clearFields() {
        loadNextId();
        txtDescription.clear();
        txtQtyOnHand.clear();
        txtUnitPrice.clear();
        txtBrand.clear();
        cmbSupId.setValue(null);
    }
    @FXML
    void codeSearchOnAction(ActionEvent event) {
        System.out.println("Code search button clicked!");
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

    private void getSupplierIds() {
        ObservableList<SupplierDTO> obList = FXCollections.observableArrayList();

        try {
            List<SupplierDTO> supplierList = materialBO.getAllSuppliers();

            obList.addAll(supplierList);
            cmbSupId.setItems(obList);

            cmbSupId.setCellFactory(param -> new ListCell<>() {
                @Override
                protected void updateItem(SupplierDTO item, boolean empty) {
                    super.updateItem(item, empty);
                    setText(empty || item == null ? null : item.getName());
                }
            });

            cmbSupId.setButtonCell(new ListCell<>() {
                protected void updateItem(SupplierDTO item, boolean empty) {
                    super.updateItem(item, empty);
                    setText(empty || item == null ? null : item.getName());
                }
            });

        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }


    public boolean isValid() {
        return Regex.setTextColor(lk.ijse.AutoCareCenter.Util.TextField.UNITPRICE, txtUnitPrice)
                && Regex.setTextColor(lk.ijse.AutoCareCenter.Util.TextField.UNITPRICE, txtQtyOnHand);
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

    @FXML
    void txtPriceOnKeyReleased(KeyEvent event) {
        Regex.setTextColor(lk.ijse.AutoCareCenter.Util.TextField.UNITPRICE, txtUnitPrice);
    }

    @FXML
    void txtQtyOnKeyReleased(KeyEvent event) {
        Regex.setTextColor(lk.ijse.AutoCareCenter.Util.TextField.UNITPRICE, txtQtyOnHand);
    }

    public void txtIDOnKeyReleased(KeyEvent keyEvent) {
    }

    public void btnBackOnAction(ActionEvent actionEvent) throws IOException {
        if (keyHandler != null && lblId.getScene() != null) {
            lblId.getScene().removeEventFilter(KeyEvent.KEY_PRESSED, keyHandler);
        }
        setUi("oils_form.fxml");
    }



    @FXML
    void btnSearchOnAction(ActionEvent event) {
        String searchText = txtSearchDescription.getText().trim().toLowerCase();

        if (searchText.isEmpty()) {
            loadAllOilMaterials();
            return;
        }

        ObservableList<MaterialsTm> filteredList = FXCollections.observableArrayList();

        for (MaterialsTm tm : tblMaterial.getItems()) {
            if (tm.getDescription().toLowerCase().contains(searchText)) {
                filteredList.add(tm);
            }
        }

        tblMaterial.setItems(filteredList);

        if (filteredList.isEmpty()) {
            new Alert(Alert.AlertType.INFORMATION, "No results found for '" + searchText + "'").show();
        }
    }

    @FXML
    void btnResetOnAction(ActionEvent event) {
        txtSearchDescription.clear();
        loadAllOilMaterials();
    }

    @FXML
    void txtDescriptionOnKeyReleased(KeyEvent event) {
        String searchText = txtSearchDescription.getText().trim().toLowerCase();

        if (searchText.isEmpty()) {
            loadAllOilMaterials();
            return;
        }

        ObservableList<MaterialsTm> filteredList = FXCollections.observableArrayList();
        for (MaterialsTm tm : tblMaterial.getItems()) {
            if (tm.getDescription().toLowerCase().contains(searchText)) {
                filteredList.add(tm);
            }
        }
        tblMaterial.setItems(filteredList);
    }
    public void onClose() {
        System.out.println("On close Oil Form");
        if (keyHandler != null && lblId.getScene() != null) {
            lblId.getScene().removeEventFilter(KeyEvent.KEY_PRESSED, keyHandler);
        }
    }


    @FXML
    void btnPrintBarcodeOnAction(ActionEvent event) {

        MaterialsTm selected =
                tblMaterial.getSelectionModel().getSelectedItem();

        if (selected == null) {
            new Alert(Alert.AlertType.WARNING,
                    "Please select an item first!").show();
            return;
        }

        String barcode = selected.getBarcode();
        try {
            BarcodeUtil.printBarcode(barcode);

        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR,
                    "Barcode print failed!").show();
        }
    }

}
