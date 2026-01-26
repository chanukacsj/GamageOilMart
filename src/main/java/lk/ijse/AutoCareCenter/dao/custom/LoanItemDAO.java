package lk.ijse.AutoCareCenter.dao.custom;

import lk.ijse.AutoCareCenter.dao.SuperDAO;
import lk.ijse.AutoCareCenter.model.LoanItemDTO;

import java.sql.SQLException;
import java.util.List;

public interface LoanItemDAO extends SuperDAO {

    boolean save(LoanItemDTO dto) throws Exception;

    List<LoanItemDTO> getLoanItems(String loanId) throws SQLException, ClassNotFoundException;
}
