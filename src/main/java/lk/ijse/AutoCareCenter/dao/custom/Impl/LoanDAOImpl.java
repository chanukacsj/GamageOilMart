package lk.ijse.AutoCareCenter.dao.custom.Impl;

import lk.ijse.AutoCareCenter.dao.SqlUtil;
import lk.ijse.AutoCareCenter.dao.custom.LoanDAO;
import lk.ijse.AutoCareCenter.model.LoanDTO;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class LoanDAOImpl implements LoanDAO {

    @Override
    public List<LoanDTO> getAll() throws Exception {
        ResultSet rs = SqlUtil.execute("SELECT * FROM loan");

        List<LoanDTO> list = new ArrayList<>();

        while (rs.next()) {
            list.add(new LoanDTO(
                    rs.getString("loanId"),
                    rs.getString("orderId"),
                    rs.getString("customerName"),
                    rs.getString("phone"),
                    rs.getDouble("total"),
                    rs.getDouble("paid"),
                    rs.getDouble("remaining"),
                    rs.getString("nextDue"),
                    rs.getString("status"),
                    rs.getString("date")

            ));
        }
        System.out.println("list" + list);
        return list;
    }

    @Override
    public LoanDTO get(String id) throws Exception {
        ResultSet rs = SqlUtil.execute("SELECT * FROM loan WHERE loanId=?", id);

        if (rs.next()) {
            return new LoanDTO(
                    rs.getString("loanId"),
                    rs.getString("orderId"),
                    rs.getString("customerName"),
                    rs.getString("phone"),
                    rs.getDouble("total"),
                    rs.getDouble("paid"),
                    rs.getDouble("remaining"),
                    rs.getString("nextDue"),
                    rs.getString("status"),
                    rs.getString("date")

            );
        }
        return null;
    }

    @Override
    public boolean updateBalance(String loanId, double newBalance) throws Exception {
        return SqlUtil.execute(
                "UPDATE loan SET remaining=?, paid=(total-?), status=? WHERE loanId=?",
                newBalance,
                newBalance,
                (newBalance == 0 ? "PAID" : "ONGOING"),
                loanId
        );
    }
    @Override
    public boolean save(LoanDTO dto) throws Exception {
        System.out.println("save loan "+dto);
        return SqlUtil.execute(
                "INSERT INTO loan (loanId, orderId, customerName, phone, total, paid, remaining, nextDue, status, date) " +
                        "VALUES (?,?,?,?,?,?,?,?,?,?)",
                dto.getLoanId(),
                dto.getOrderId(),
                dto.getCustomerName(),
                dto.getPhone(),
                dto.getTotal(),
                dto.getPaid(),
                dto.getRemaining(),
                dto.getNextDue(),
                dto.getStatus(),
                dto.getDate()
        );
    }

    @Override
    public String currentId() throws SQLException, ClassNotFoundException {
        ResultSet rs = SqlUtil.execute(
                "SELECT loanId FROM loan ORDER BY CAST(SUBSTR(loanId, 2) AS INTEGER) DESC LIMIT 1"
        );

        if (rs.next()) {
            return rs.getString("loanId");
        }
        return null;
    }


}
