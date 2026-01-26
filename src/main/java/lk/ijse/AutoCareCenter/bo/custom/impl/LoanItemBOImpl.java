package lk.ijse.AutoCareCenter.bo.custom.impl;

import lk.ijse.AutoCareCenter.bo.custom.LoanItemBO;
import lk.ijse.AutoCareCenter.dao.DAOFactory;
import lk.ijse.AutoCareCenter.dao.custom.LoanDAO;
import lk.ijse.AutoCareCenter.dao.custom.LoanItemDAO;
import lk.ijse.AutoCareCenter.model.LoanItemDTO;

public class LoanItemBOImpl implements LoanItemBO {
    LoanItemDAO loanItemDAO = (LoanItemDAO) DAOFactory.getDaoFactory().getDAO(DAOFactory.DAOTypes.LOANITEM);

    @Override
    public boolean save(LoanItemDTO loan) throws Exception {
        return loanItemDAO.save(loan);
    }
}
