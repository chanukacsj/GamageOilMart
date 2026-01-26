package lk.ijse.AutoCareCenter.dao.custom.Impl;

import lk.ijse.AutoCareCenter.dao.SqlUtil;
import lk.ijse.AutoCareCenter.dao.custom.LoanItemDAO;
import lk.ijse.AutoCareCenter.model.LoanItemDTO;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class LoanItemDAOImpl implements LoanItemDAO {
    @Override
    public boolean save(LoanItemDTO dto) throws Exception {
        System.out.println("save loan item " + dto);
        return SqlUtil.execute(
                "INSERT INTO loan_item (loanId, itemName, qty) VALUES (?,?,?)",
                dto.getLoanId(),
                dto.getItemName(),
                dto.getQty()
        );
    }

    @Override
    public List<LoanItemDTO> getLoanItems(String loanId) throws SQLException, ClassNotFoundException {
        String sql = "SELECT * FROM loan_item WHERE loanId=?";
        ResultSet rs = SqlUtil.execute(sql, loanId);
        List<LoanItemDTO> items = new ArrayList<>();
        while (rs.next()) {
            items.add(new LoanItemDTO(rs.getString("loanId"), rs.getString("itemName"), rs.getInt("qty")));
        }
        return items;
    }
}
