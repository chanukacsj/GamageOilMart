package lk.ijse.AutoCareCenter.bo.custom;

import lk.ijse.AutoCareCenter.bo.SuperBO;
import lk.ijse.AutoCareCenter.entity.ChequePayment;
import lk.ijse.AutoCareCenter.model.ChequePaymentDTO;

import java.sql.SQLException;
import java.util.List;

public interface ChequeBO extends SuperBO {
    boolean saveCheque(ChequePayment cheque) throws Exception;

    List<ChequePayment> loadAll() throws SQLException, ClassNotFoundException;

    boolean updateStatus(String chequeId, String status);
}
