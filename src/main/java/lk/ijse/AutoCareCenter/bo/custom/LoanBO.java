package lk.ijse.AutoCareCenter.bo.custom;

import lk.ijse.AutoCareCenter.bo.SuperBO;
import lk.ijse.AutoCareCenter.entity.Loan;
import lk.ijse.AutoCareCenter.model.LoanDTO;
import lk.ijse.AutoCareCenter.model.LoanItemDTO;
import lk.ijse.AutoCareCenter.model.LoanPaymentDTO;

import java.sql.SQLException;
import java.util.List;

public interface LoanBO extends SuperBO {
    List<LoanDTO> getAllLoans() throws Exception;
    boolean payLoan(LoanPaymentDTO dto) throws Exception;
    boolean save(LoanDTO loan) throws Exception;
    String currentId() throws SQLException, ClassNotFoundException;

    List<LoanItemDTO> getLoanItems(String loanId) throws SQLException, ClassNotFoundException;
}
