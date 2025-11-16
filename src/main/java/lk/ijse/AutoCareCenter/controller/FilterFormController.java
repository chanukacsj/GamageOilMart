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

public class FilterFormController {

    @FXML
    private TableView<MaterialsTm> tblMaterial;

    @FXML
    private TableColumn<?, ?> colCode, colDescription, colUnitPrice, colQtyOnHand, colSupId, colBrand, colDate, colStatus;

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
        loadAllFilterMaterials();
        getSupplierIds();
        setupKeyListeners();

        Platform.runLater(() -> {
            if (lblId.getScene() != null) {
                lblId.getScene().getWindow().setOnHidden(event -> onClose());
            }
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
    }

    private void loadNextId() {
        try {
            String currentId = materialDetailBO.currentId();
            lblId.setText(nextId(currentId));
        } catch (Exception e) {
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
        } catch (Exception e) {
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
        try {
            SupplierDTO sup = cmbSupId.getValue();
            MaterialDetailsDTO dto = new MaterialDetailsDTO(
                    lblId.getText(),
                    sup != null ? sup.getId() : null,
                    txtDescription.getText(),
                    Double.parseDouble(txtUnitPrice.getText()),
                    Integer.parseInt(txtQtyOnHand.getText()),
                    "Filter",
                    txtBrand.getText(),
                    LocalDate.now().toString(),
                    "Active"
            );

            boolean save = materialDetailBO.save(dto);
            if (save) {
                new Alert(Alert.AlertType.INFORMATION, "Filter item saved!").show();
                loadAllFilterMaterials();
                clearFields();
                loadNextId();
            }
        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        }
    }

    @FXML
    void btnUpdateOnAction(ActionEvent event) {
        try {
            SupplierDTO sup = cmbSupId.getValue();
            MaterialDetailsDTO dto = new MaterialDetailsDTO(
                    lblId.getText(),
                    sup != null ? sup.getId() : null,
                    txtDescription.getText(),
                    Double.parseDouble(txtUnitPrice.getText()),
                    Integer.parseInt(txtQtyOnHand.getText()),
                    "Filter",
                    txtBrand.getText(),
                    LocalDate.now().toString(),
                    "Active"
            );

            if (materialDetailBO.update(dto)) {
                new Alert(Alert.AlertType.INFORMATION, "Updated!").show();
                loadAllFilterMaterials();
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
            new Alert(Alert.AlertType.WARNING, "Select an item!").show();
            return;
        }

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION,
                "Delete this item?", ButtonType.OK, ButtonType.CANCEL);

        if (confirm.showAndWait().orElse(ButtonType.CANCEL) == ButtonType.OK) {
            try {
                if (materialDetailBO.delete(code)) {
                    new Alert(Alert.AlertType.INFORMATION, "Deleted!").show();
                    loadAllFilterMaterials();
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
        try {
            ObservableList<SupplierDTO> list = FXCollections.observableArrayList(materialBO.getAllSuppliers());
            cmbSupId.setItems(list);

            cmbSupId.setCellFactory(param -> new ListCell<>() {
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

        } catch (Exception e) {
            throw new RuntimeException(e);
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

    public void btnBackOnAction(ActionEvent event) throws IOException {
        if (keyHandler != null && lblId.getScene() != null) {
            lblId.getScene().removeEventFilter(KeyEvent.KEY_PRESSED, keyHandler);
        }
        setUi("oils_form.fxml");
    }

    private void setUi(String fileName) throws IOException {
        Pane load = FXMLLoader.load(getClass().getResource("/view/" + fileName));
        root.getChildren().setAll(load);
    }

    public void onClose() {
        if (keyHandler != null && lblId.getScene() != null) {
            lblId.getScene().removeEventFilter(KeyEvent.KEY_PRESSED, keyHandler);
        }
    }

    @FXML
    void btnSearchOnAction(ActionEvent event) {
        String searchText = txtSearchDescription.getText().trim().toLowerCase();

        if (searchText.isEmpty()) {
            loadAllFilterMaterials();
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
        loadAllFilterMaterials();
    }

    @FXML
    void txtDescriptionOnKeyReleased(KeyEvent event) {
        String searchText = txtSearchDescription.getText().trim().toLowerCase();

        if (searchText.isEmpty()) {
            loadAllFilterMaterials();
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

}
