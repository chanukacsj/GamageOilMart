package lk.ijse.AutoCareCenter.dao.custom;

import lk.ijse.AutoCareCenter.dao.SuperDAO;
import lk.ijse.AutoCareCenter.model.LoanPaymentDTO;

public interface LoanPaymentDAO extends SuperDAO {
    boolean save(LoanPaymentDTO dto) throws Exception;
}