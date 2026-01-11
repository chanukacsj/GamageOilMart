package lk.ijse.AutoCareCenter.dao.custom.Impl;

import lk.ijse.AutoCareCenter.dao.SqlUtil;
import lk.ijse.AutoCareCenter.dao.custom.ChequeDAO;
import lk.ijse.AutoCareCenter.entity.ChequePayment;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ChequeDAOImpl implements ChequeDAO {
    @Override
    public ArrayList<ChequePayment> loadAll() throws SQLException, ClassNotFoundException {
        ResultSet resultSet = SqlUtil.execute("SELECT * FROM cheque_payment");
        ArrayList<ChequePayment> all = new ArrayList<>();
        while (resultSet.next()) {
            all.add(new ChequePayment(
                    resultSet.getString("chequeId"),
                    resultSet.getString("paymentId"),
                    resultSet.getString("customerName"),
                    resultSet.getString("customerPhone"),
                    resultSet.getString("chequeNo"),
                    resultSet.getString("bankName"),
                    resultSet.getString("branch"),
                    resultSet.getString("chequeDate"),
                    resultSet.getDouble("amount"),
                    resultSet.getString("status")
            ));
        }
        return all;
    }

    @Override
    public boolean save(ChequePayment chequePayment) throws Exception {
        return SqlUtil.execute(
                "INSERT INTO cheque_payment VALUES (?,?,?,?,?,?,?,?,?,?)",
                chequePayment.getChequeId(),
                chequePayment.getPaymentId(),
                chequePayment.getCustomerName(),
                chequePayment.getCustomerPhone(),
                chequePayment.getChequeNo(),
                chequePayment.getBankName(),
                chequePayment.getBranch(),
                chequePayment.getChequeDate(),
                chequePayment.getAmount(),
                chequePayment.getStatus()
        );
    }

    @Override
    public boolean delete(String id) throws SQLException, ClassNotFoundException {
        return false;
    }

    @Override
    public boolean update(ChequePayment DTO) throws SQLException, ClassNotFoundException {
        return false;
    }

    @Override
    public ChequePayment searchById(String id) throws SQLException, ClassNotFoundException {
        return null;
    }

    @Override
    public List<String> getIds() throws SQLException, ClassNotFoundException {
        return List.of();
    }

    @Override
    public int getMaterialCount() throws SQLException, ClassNotFoundException {
        return 0;
    }

    @Override
    public String currentId() throws SQLException, ClassNotFoundException {
        return "";
    }

    @Override
    public boolean updateStatus(String chequeId, String status) {
        try {
            return SqlUtil.execute(
                    "UPDATE cheque_payment SET status=? WHERE chequeId=?",
                    status,
                    chequeId
            );
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
