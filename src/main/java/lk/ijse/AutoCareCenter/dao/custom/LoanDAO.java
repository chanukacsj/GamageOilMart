package lk.ijse.AutoCareCenter.dao.custom;

import lk.ijse.AutoCareCenter.dao.CurdDAO;
import lk.ijse.AutoCareCenter.dao.SuperDAO;
import lk.ijse.AutoCareCenter.model.LoanDTO;

import java.sql.SQLException;
import java.util.List;

public interface LoanDAO extends SuperDAO {
    List<LoanDTO> getAll() throws Exception;
    LoanDTO get(String id) throws Exception;
    boolean updateBalance(String loanId, double newBalance) throws Exception;
    boolean save(LoanDTO dto) throws Exception;
    String currentId() throws SQLException, ClassNotFoundException;
}
