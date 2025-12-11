package lk.ijse.AutoCareCenter.bo.custom.impl;

import lk.ijse.AutoCareCenter.bo.custom.LoanBO;
import lk.ijse.AutoCareCenter.dao.DAOFactory;
import lk.ijse.AutoCareCenter.dao.SqlUtil;
import lk.ijse.AutoCareCenter.dao.custom.LoanDAO;
import lk.ijse.AutoCareCenter.dao.custom.LoanPaymentDAO;
import lk.ijse.AutoCareCenter.dao.custom.MaterialDAO;
import lk.ijse.AutoCareCenter.model.LoanDTO;
import lk.ijse.AutoCareCenter.model.LoanPaymentDTO;

import java.sql.SQLException;
import java.util.List;

public class LoanBOImpl implements LoanBO {

    LoanDAO loanDAO = (LoanDAO) DAOFactory.getDaoFactory().getDAO(DAOFactory.DAOTypes.LOAN);
    LoanPaymentDAO loanPaymentDAO = (LoanPaymentDAO) DAOFactory.getDaoFactory().getDAO(DAOFactory.DAOTypes.LOANPAYMENT);

    @Override
    public List<LoanDTO> getAllLoans() throws Exception {
        return loanDAO.getAll();
    }
    @Override
    public boolean payLoan(LoanPaymentDTO dto) throws Exception {

        LoanDTO loan = loanDAO.get(dto.getLoanId());
        System.out.println("loan = " + loan);
        System.out.println("amount"+dto.getAmount());
        double newBalance = loan.getRemaining() - dto.getAmount();

        if (newBalance < 0) {
            throw new RuntimeException("Payment exceeds remaining balance!");
        }

        boolean updated = loanDAO.updateBalance(dto.getLoanId(), newBalance);
        boolean saved = loanPaymentDAO.save(dto);

        return updated && saved;
    }

    @Override
    public boolean save(LoanDTO loan) throws Exception {
        return loanDAO.save(loan);
    }

    @Override
    public String currentId() throws SQLException, ClassNotFoundException {
        return loanDAO.currentId();
    }
}
