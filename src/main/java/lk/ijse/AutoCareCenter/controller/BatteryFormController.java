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
import lk.ijse.AutoCareCenter.model.SupplierDTO;
import lk.ijse.AutoCareCenter.model.tm.MaterialsTm;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class BatteryFormController {

    @FXML
    private TableView<MaterialsTm> tblMaterial;

    @FXML
    private TableColumn<?, ?> colCode, colDescription, colUnitPrice, colQtyOnHand, colSupId, colBrand, colDate, colStatus, colBarcode;

    @FXML
    private JFXComboBox<SupplierDTO> cmbSupId;

    @FXML
    private JFXTextField txtDescription, txtUnitPrice, txtQtyOnHand, txtBrand, txtSearchDescription;

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
        loadAllBatteryMaterials();
        loadSuppliers();
        setupKeyListeners();

        Platform.runLater(() ->
                lblId.getScene().getWindow().setOnHidden(event -> onClose())
        );
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
            String nextId = currentId != null ? "M" + (Integer.parseInt(currentId.split("M")[1]) + 1) : "M1";
            lblId.setText(nextId);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadAllBatteryMaterials() {
        tblMaterial.getItems().clear();
        try {
            ArrayList<MaterialDetailsDTO> all = materialDetailBO.loadAllByCategory("Battery");
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
        } catch (Exception e) {
            e.printStackTrace();
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
        txtBrand.setText(tm.getBrand());

        for (SupplierDTO s : cmbSupId.getItems()) {
            if (s.getId().equals(tm.getSupId())) {
                cmbSupId.setValue(s);
                break;
            }
        }
    }

    @FXML
    void btnSaveOnAction(ActionEvent event) {
        if (!isValid()) return;

        String code = lblId.getText();
        String supId = cmbSupId.getValue() != null ? cmbSupId.getValue().getId() : null;

        MaterialDetailsDTO dto = new MaterialDetailsDTO(
                code,
                supId,
                txtDescription.getText(),
                Double.parseDouble(txtUnitPrice.getText()),
                Integer.parseInt(txtQtyOnHand.getText()),
                "Battery",
                txtBrand.getText(),
                LocalDate.now().toString(),
                "Active"
        );

        try {
            if (materialDetailBO.save(dto)) {
                new Alert(Alert.AlertType.INFORMATION, "Battery item saved!").show();
                loadAllBatteryMaterials();
                clearFields();
                loadNextId();
            }
        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        }
    }

    @FXML
    void btnUpdateOnAction(ActionEvent event) {
        if (!isValid()) return;

        String code = lblId.getText();
        String supId = cmbSupId.getValue() != null ? cmbSupId.getValue().getId() : null;

        MaterialDetailsDTO dto = new MaterialDetailsDTO(
                code,
                supId,
                txtDescription.getText(),
                Double.parseDouble(txtUnitPrice.getText()),
                Integer.parseInt(txtQtyOnHand.getText()),
                "Battery",
                txtBrand.getText(),
                LocalDate.now().toString(),
                "Active"
        );

        try {
            if (materialDetailBO.update(dto)) {
                new Alert(Alert.AlertType.INFORMATION, "Battery item updated!").show();
                loadAllBatteryMaterials();
                clearFields();
                loadNextId();
            }
        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        }
    }

    @FXML
    void btnDeleteOnAction(ActionEvent event) {
        String code = lblId.getText();
        if (code.isEmpty()) {
            new Alert(Alert.AlertType.WARNING, "Select item to delete!").show();
            return;
        }

        Alert c = new Alert(Alert.AlertType.CONFIRMATION, "Delete this Battery item?", ButtonType.OK, ButtonType.CANCEL);
        if (c.showAndWait().orElse(ButtonType.CANCEL) == ButtonType.OK) {
            try {
                if (materialDetailBO.delete(code)) {
                    new Alert(Alert.AlertType.INFORMATION, "Deleted!").show();
                    loadAllBatteryMaterials();
                    clearFields();
                    loadNextId();
                }
            } catch (Exception e) {
                new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
            }
        }
    }

    @FXML
    void btnClearOnAction(ActionEvent event) {
        clearFields();
    }

    private void clearFields() {
        txtDescription.clear();
        txtQtyOnHand.clear();
        txtUnitPrice.clear();
        txtBrand.clear();
        cmbSupId.setValue(null);
        loadNextId();
    }

    private void loadSuppliers() {
        try {
            List<SupplierDTO> list = materialBO.getAllSuppliers();
            ObservableList<SupplierDTO> ob = FXCollections.observableArrayList(list);

            cmbSupId.setItems(ob);
            cmbSupId.setCellFactory(param -> new ListCell<>() {
                @Override
                protected void updateItem(SupplierDTO item, boolean empty) {
                    super.updateItem(item, empty);
                    setText(empty || item == null ? null : item.getName());
                }
            });

            cmbSupId.setButtonCell(new ListCell<>() {
                @Override
                protected void updateItem(SupplierDTO item, boolean empty) {
                    super.updateItem(item, empty);
                    setText(empty || item == null ? null : item.getName());
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setupKeyListeners() {
        Platform.runLater(() -> {
            keyHandler = event -> {
                if (event.getCode() == KeyCode.ENTER) {
                    btnSaveOnAction(new ActionEvent());
                } else if (event.getCode() == KeyCode.DELETE) {
                    btnDeleteOnAction(new ActionEvent());
                }
            };
            lblId.getScene().addEventFilter(KeyEvent.KEY_PRESSED, keyHandler);
        });
    }

    public boolean isValid() {
        return Regex.setTextColor(lk.ijse.AutoCareCenter.Util.TextField.UNITPRICE, txtUnitPrice)
                && Regex.setTextColor(lk.ijse.AutoCareCenter.Util.TextField.UNITPRICE, txtQtyOnHand);
    }

    @FXML
    void btnSearchOnAction(ActionEvent event) {
        String searchText = txtSearchDescription.getText().trim().toLowerCase();

        if (searchText.isEmpty()) {
            loadAllBatteryMaterials();
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
        loadAllBatteryMaterials();
    }

    @FXML
    void txtDescriptionOnKeyReleased(KeyEvent event) {
        String searchText = txtSearchDescription.getText().trim().toLowerCase();

        if (searchText.isEmpty()) {
            loadAllBatteryMaterials();
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

    @FXML
    void btnBackOnAction(ActionEvent actionEvent) throws IOException {
        if (keyHandler != null && lblId.getScene() != null) {
            lblId.getScene().removeEventFilter(KeyEvent.KEY_PRESSED, keyHandler);
        }
        setUi("tyres_form.fxml");
    }

    private void setUi(String fileName) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/" + fileName));
        Pane newRoot = fxmlLoader.load();
        root.getChildren().clear();
        root.getChildren().setAll(newRoot);
    }

    public void onClose() {
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
        System.out.println("barcode = " + barcode);
        try {
            BarcodeUtil.printBarcode(barcode);

        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR,
                    "Barcode print failed!").show();
        }
    }
}
