package lk.ijse.AutoCareCenter.bo.custom;

import lk.ijse.AutoCareCenter.bo.SuperBO;
import lk.ijse.AutoCareCenter.model.PaymentDTO;

import java.sql.SQLException;
import java.util.ArrayList;

public interface PaymentBO extends SuperBO {
    public ArrayList<PaymentDTO> loadAll() throws SQLException, ClassNotFoundException;

}
