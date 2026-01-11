package lk.ijse.AutoCareCenter.dao.custom;

import lk.ijse.AutoCareCenter.dao.CurdDAO;
import lk.ijse.AutoCareCenter.entity.ChequePayment;
import lk.ijse.AutoCareCenter.model.ChequePaymentDTO;

public interface ChequeDAO extends CurdDAO<ChequePayment> {
    boolean updateStatus(String chequeId, String status);
}
