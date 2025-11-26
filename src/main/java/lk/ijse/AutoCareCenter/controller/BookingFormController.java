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
import lk.ijse.AutoCareCenter.bo.custom.BookingBO;
import lk.ijse.AutoCareCenter.bo.custom.VehicleBO;
import lk.ijse.AutoCareCenter.dao.custom.Impl.BookingDAOImpl;
import lk.ijse.AutoCareCenter.dao.custom.Impl.VehicleDAOImpl;
import lk.ijse.AutoCareCenter.entity.Booking;
import lk.ijse.AutoCareCenter.model.BookingDTO;
import lk.ijse.AutoCareCenter.model.tm.BookingTm;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class BookingFormController {

    @FXML
    private JFXComboBox<String> CmbCusId;

    @FXML
    private JFXComboBox<String> CmbVehicleId;

    @FXML
    private TableColumn<?, ?> ColTime;

    @FXML
    private TableColumn<?, ?> colCusId;
    @FXML
    private TableColumn<?, ?> colContact;
    @FXML
    private TableColumn<?, ?> colDate;

    @FXML
    private TableColumn<?, ?> colDescription;

    @FXML
    private TableColumn<?, ?> colId;

    @FXML
    private TableColumn<?, ?> colVid;

    @FXML
    private AnchorPane root;

    @FXML
    private TableView<BookingTm> tblBooking;

  //  @FXML
    //private TextField txtBookingId;
    @FXML
    private JFXTextField txtTime;
    @FXML
    private TextField txtDescription;
    @FXML
    private TextField txtContact;
    @FXML
    private DatePicker txtDate;

    @FXML
    private Label lblCustomerName;
    @FXML
    private Label lblId;

    BookingBO bookingBO = (BookingBO) BOFactory.getBoFactory().getBO(BOFactory.BOTypes.BOOKING);
    Integer index;
    public void initialize() {
        setCellValueFactory();
        loadNextId();
        getVehicleIds();
        loadAllBooking();

    }

    private void setCellValueFactory() {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colCusId.setCellValueFactory(new PropertyValueFactory<>("cusId"));
        colVid.setCellValueFactory(new PropertyValueFactory<>("vId"));
        colDate.setCellValueFactory(new PropertyValueFactory<>("date"));
        colDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
        colContact.setCellValueFactory(new PropertyValueFactory<>("contact"));
        ColTime.setCellValueFactory(new PropertyValueFactory<>("time"));
    }
    public void getBooking(MouseEvent mouseEvent) {
        index = tblBooking.getSelectionModel().getSelectedIndex();
        if(index <= -1) {
            return;
        } else {
            lblId.setText(tblBooking.getItems().get(index).getId());
            CmbCusId.setValue(tblBooking.getItems().get(index).getCusId());
            CmbVehicleId.setValue(tblBooking.getItems().get(index).getVId());
            txtDate.setValue(LocalDate.parse(tblBooking.getItems().get(index).getDate()));
            txtDescription.setText(tblBooking.getItems().get(index).getDescription());
            txtContact.setText(tblBooking.getItems().get(index).getContact());
            txtTime.setText(tblBooking.getItems().get(index).getTime());
        }
    }
    private void loadNextId() {
        try {
            String currentId = bookingBO.currentId();
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
            String[] split = currentId.split("B");
//            System.out.println("Arrays.toString(split) = " + Arrays.toString(split));
            int id = Integer.parseInt(split[1]);    //2
            return "B" + ++id;

        }
        return "B1";
    }
    @FXML
    void btnSaveOnAction(ActionEvent event) {
        String id = lblId.getText();
        String date = String.valueOf(txtDate.getValue());
        String description = txtDescription.getText();
        String contact = txtContact.getText();
        String cusId = (String) CmbCusId.getValue();
        String vId = (String) CmbVehicleId.getValue();
        String time = txtTime.getText();

        if (isValid()) {
            BookingDTO bookingDTO = new BookingDTO(id, date, description, contact, cusId, vId, time);
            try {
                boolean isSaved = bookingBO.save(bookingDTO);

                if (isSaved) {
                    new Alert(Alert.AlertType.CONFIRMATION, "Booking saved!").show();
                }
            } catch (SQLException e) {
                new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
            loadAllBooking();
            clearFields();
            loadNextId();
        }
    }

    @FXML
    void btnDeleteOnAction(ActionEvent event) {
        String id = lblId.getText();

        try {
            boolean isDeleted = bookingBO.delete(id);
            if (isDeleted) {
                new Alert(Alert.AlertType.CONFIRMATION, "Booking deleted!").show();
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        loadAllBooking();
        clearFields();
    }

    @FXML
    void btnUpdateOnAction(ActionEvent event) {
        String id = lblId.getText();
        String date = String.valueOf(txtDate.getValue());
        String description = txtDescription.getText();
        String contact = txtContact.getText();
        String cusId = (String) CmbCusId.getValue();
        String vId = (String) CmbVehicleId.getValue();
        String time = txtTime.getText();


        BookingDTO bookingDTO = new BookingDTO(id, date, description, contact, cusId, vId, time);
        try {
            boolean isUpdated = bookingBO.update(bookingDTO);
            if (isUpdated) {
                new Alert(Alert.AlertType.CONFIRMATION, "Booking updated!").show();
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        loadAllBooking();
        clearFields();
        loadNextId();
    }

    @FXML
    void txtSearchOnAction(ActionEvent event) {
        String id = txtContact.getText();


        try {
            Booking bookingDTO = bookingBO.searchById(id);

            if (bookingDTO != null) {
                lblId.setText(bookingDTO.getId());
                txtDate.setValue(LocalDate.parse(bookingDTO.getDate()));
                txtDescription.setText(bookingDTO.getDescription());
                txtContact.setText(bookingDTO.getContact());
                CmbCusId.setValue(bookingDTO.getCusId());
                CmbVehicleId.setValue(bookingDTO.getVId());
                txtTime.setText(bookingDTO.getTime());
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    private void getVehicleIds() {

        ObservableList<String> obList = FXCollections.observableArrayList();

        try {
            List<String> idList = bookingBO.getBookingIds();
            for (String id : idList) {
                obList.add(id);
            }
            CmbVehicleId.setItems(obList);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }




    public void loadAllBooking() {
        tblBooking.getItems().clear();


        try {
            ArrayList<BookingDTO> bookingDTOS = bookingBO.loadAllBooking();

            for (BookingDTO bookingDTO : bookingDTOS) {
                tblBooking.getItems().add(new BookingTm(bookingDTO.getId(), bookingDTO.getDate(), bookingDTO.getDescription(), bookingDTO.getContact(), bookingDTO.getCusId(), bookingDTO.getVId(), bookingDTO.getTime()));
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        } catch (ClassNotFoundException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        }
    }

    private void clearFields() {
       // lblId.setText("");
        txtContact.setText("");
        txtDescription.setText("");
        txtTime.setText("");
        CmbCusId.setValue("");
        CmbVehicleId.setValue("");
        loadNextId();
    }

    @FXML
    void btnClearOnAction(ActionEvent event) {
        clearFields();
    }

    public void txtBookingIDOnKeyReleased(KeyEvent keyEvent) {
      //  Regex.setTextColor(lk.ijse.AutoCareCenter.Util.TextField.ID, (JFXTextField) txtBookingId);
    }

    public boolean isValid() {
      //  if (!Regex.setTextColor(lk.ijse.AutoCareCenter.Util.TextField.ID, (JFXTextField) txtBookingId)) return false;
        if (!Regex.setTextColor(lk.ijse.AutoCareCenter.Util.TextField.CONTACT, (JFXTextField) txtContact)) return false;
        if (!Regex.setTextColor(lk.ijse.AutoCareCenter.Util.TextField.NAME, (JFXTextField) txtDescription))
            return false;


        return true;
    }

    public void txtBookingContactOnKeyReleased(KeyEvent keyEvent) {
        Regex.setTextColor(lk.ijse.AutoCareCenter.Util.TextField.CONTACT, (JFXTextField) txtContact);

    }

    public void txtBookingDescriptionOnKeyReleased(KeyEvent keyEvent) {
        Regex.setTextColor(lk.ijse.AutoCareCenter.Util.TextField.NAME, (JFXTextField) txtDescription);
    }


}

