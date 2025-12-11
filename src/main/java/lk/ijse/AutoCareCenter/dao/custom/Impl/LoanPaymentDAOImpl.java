package lk.ijse.AutoCareCenter.dao.custom.Impl;

import lk.ijse.AutoCareCenter.dao.SqlUtil;
import lk.ijse.AutoCareCenter.dao.custom.LoanPaymentDAO;
import lk.ijse.AutoCareCenter.model.LoanPaymentDTO;

public class LoanPaymentDAOImpl implements LoanPaymentDAO {
    @Override
    public boolean save(LoanPaymentDTO dto) throws Exception {
        return SqlUtil.execute(
                "INSERT INTO loan_payment VALUES (?,?,?,?)",
                dto.getPaymentId(),
                dto.getLoanId(),
                dto.getAmount(),
                dto.getDate()
        );
    }
}
