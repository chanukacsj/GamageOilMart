package lk.ijse.AutoCareCenter.bo.custom.impl;

import lk.ijse.AutoCareCenter.bo.custom.LoanBO;
import lk.ijse.AutoCareCenter.dao.DAOFactory;
import lk.ijse.AutoCareCenter.dao.custom.*;
import lk.ijse.AutoCareCenter.db.DbConnection;
import lk.ijse.AutoCareCenter.entity.MaterialDetails;
import lk.ijse.AutoCareCenter.model.LoanDTO;
import lk.ijse.AutoCareCenter.model.LoanItemDTO;
import lk.ijse.AutoCareCenter.model.LoanPaymentDTO;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class LoanBOImpl implements LoanBO {

    private final LoanDAO loanDAO =
            (LoanDAO) DAOFactory.getDaoFactory().getDAO(DAOFactory.DAOTypes.LOAN);

    private final LoanItemDAO loanItemDAO =
            (LoanItemDAO) DAOFactory.getDaoFactory().getDAO(DAOFactory.DAOTypes.LOANITEM);

    private final LoanPaymentDAO loanPaymentDAO =
            (LoanPaymentDAO) DAOFactory.getDaoFactory().getDAO(DAOFactory.DAOTypes.LOANPAYMENT);

    private final MaterialDAO materialDAO =
            (MaterialDAO) DAOFactory.getDaoFactory().getDAO(DAOFactory.DAOTypes.MATERIAL);

    // ------------------ Load ------------------

    @Override
    public List<LoanDTO> getAllLoans() throws Exception {
        return loanDAO.getAll();
    }

    // ------------------ Save Loan + Items + Stock ------------------


    @Override
    public boolean save(LoanDTO dto) throws Exception {

        Connection con = DbConnection.getInstance().getConnection();
        try {
            con.setAutoCommit(false);

            // 1️⃣ Save Loan Header
            boolean loanSaved = loanDAO.save(dto);
            if (!loanSaved) {
                con.rollback();
                return false;
            }

            // 2️⃣ Save Loan Items + Update Material Stock
            List<LoanItemDTO> items = dto.getItems();
            for (LoanItemDTO item : items) {

                // Save loan item
                boolean itemSaved = loanItemDAO.save(item);
                if (!itemSaved) {
                    System.out.println("Error saving loan item");
                    con.rollback();
                    return false;
                }

                // Reduce stock
                boolean stockUpdated = materialDAO.updateQty(item.getCode(), item.getQty());
                if (!stockUpdated) {
                    System.out.println("Error updating stock");
                    con.rollback();
                    return false;
                }
            }

            // 3️⃣ Commit if everything is successful
            con.commit();
            return true;

        } catch (Exception e) {
            con.rollback();
            throw e;
        } finally {
            con.setAutoCommit(true);
        }
    }


    // ------------------ Pay Loan ------------------

    @Override
    public boolean payLoan(LoanPaymentDTO dto) throws Exception {

        LoanDTO loan = loanDAO.get(dto.getLoanId());
        double newBalance = loan.getRemaining() - dto.getAmount();

        if (newBalance < 0) {
            throw new RuntimeException("Payment exceeds remaining balance!");
        }

        boolean updated = loanDAO.updateBalance(dto.getLoanId(), newBalance);
        boolean saved = loanPaymentDAO.save(dto);

        return updated && saved;
    }

    // ------------------ ID ------------------

    @Override
    public String currentId() throws SQLException, ClassNotFoundException {
        return loanDAO.currentId();
    }

    @Override
    public List<LoanItemDTO> getLoanItems(String loanId) throws SQLException, ClassNotFoundException {
        return loanItemDAO.getLoanItems(loanId);
    }
}
