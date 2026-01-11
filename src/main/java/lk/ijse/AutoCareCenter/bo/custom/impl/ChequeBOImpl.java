package lk.ijse.AutoCareCenter.bo.custom.impl;

import lk.ijse.AutoCareCenter.bo.custom.ChequeBO;
import lk.ijse.AutoCareCenter.dao.DAOFactory;
import lk.ijse.AutoCareCenter.dao.custom.ChequeDAO;
import lk.ijse.AutoCareCenter.entity.ChequePayment;
import lk.ijse.AutoCareCenter.model.ChequePaymentDTO;

import java.sql.SQLException;
import java.util.List;

public class ChequeBOImpl implements ChequeBO {
    ChequeDAO chequeDAO = (ChequeDAO) DAOFactory.getDaoFactory().getDAO(DAOFactory.DAOTypes.CHEQUE);

    @Override
    public boolean saveCheque(ChequePayment cheque) throws Exception {
        return chequeDAO.save(cheque);
    }

    @Override
    public List<ChequePayment> loadAll() throws SQLException, ClassNotFoundException {
        return chequeDAO.loadAll();
    }

    @Override
    public boolean updateStatus(String chequeId, String status) {
        return chequeDAO.updateStatus(chequeId, status);
    }
}
