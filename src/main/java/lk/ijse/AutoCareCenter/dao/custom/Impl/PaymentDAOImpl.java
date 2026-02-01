package lk.ijse.AutoCareCenter.dao.custom.Impl;

import lk.ijse.AutoCareCenter.dao.SqlUtil;
import lk.ijse.AutoCareCenter.dao.custom.PaymentDAO;
import lk.ijse.AutoCareCenter.entity.Payment;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PaymentDAOImpl implements PaymentDAO {
    @Override
    public ArrayList<Payment> loadAll() throws SQLException, ClassNotFoundException {
        ArrayList<Payment> all = new ArrayList<>();
        ResultSet resultSet = SqlUtil.execute("SELECT * FROM payment");
        while (resultSet.next()) {
            all.add(new Payment(
                    resultSet.getString("id"),
                    resultSet.getString("orderId"),
                    resultSet.getString("code"),
                    resultSet.getInt("qty"),
                    resultSet.getDouble("unitPrice"),
                    resultSet.getDouble("service_charge"),
                    resultSet.getDouble("total"),
                    resultSet.getString("description"),
                    resultSet.getString("date"),
                    resultSet.getDouble("discount")
            ));

        }
        return all;
    }

    @Override
    public boolean save(Payment DTO) throws SQLException, ClassNotFoundException {
        System.out.println("save payment "+DTO);
        return SqlUtil.execute(
                "INSERT INTO payment (id,orderId,code,qty,unitPrice,service_charge,total,description,date,discount) VALUES (?,?,?,?,?,?,?,?,?,?)",
                DTO.getId(),
                DTO.getOrderId(),
                DTO.getCode(),
                DTO.getQty(),
                DTO.getUnitPrice(),
                DTO.getService_charge(),
                DTO.getTotal(),
                DTO.getDescription(),
                DTO.getDate(),
                DTO.getDiscount()
        );
    }

    @Override
    public boolean delete(String id) throws SQLException, ClassNotFoundException {
        return false;
    }

    @Override
    public boolean update(Payment DTO) throws SQLException, ClassNotFoundException {
        return false;
    }

    @Override
    public Payment searchById(String id) throws SQLException, ClassNotFoundException {
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
    public double getDailyProfit() throws Exception {
        ResultSet rs = SqlUtil.execute(
                "SELECT SUM((p.unitPrice - md.unitCost) * p.qty) AS profit " +
                        "FROM payment p " +
                        "JOIN material_details md ON p.code = md.code " +
                        "WHERE DATE(p.date) = DATE('now')"
        );
        return rs.next() ? rs.getDouble("profit") : 0.0;
    }


    @Override
    public double getMonthlyProfit() throws Exception {
        ResultSet rs = SqlUtil.execute(
                "SELECT SUM((p.unitPrice - md.unitCost) * p.qty) AS profit " +
                        "FROM payment p " +
                        "JOIN material_details md ON p.code = md.code " +
                        "WHERE strftime('%Y-%m', p.date) = strftime('%Y-%m','now')"
        );
        return rs.next() ? rs.getDouble("profit") : 0.0;
    }


    @Override
    public double getYearlyProfit() throws Exception {
        ResultSet rs = SqlUtil.execute(
                "SELECT SUM((p.unitPrice - md.unitCost) * p.qty) AS profit " +
                        "FROM payment p " +
                        "JOIN material_details md ON p.code = md.code " +
                        "WHERE strftime('%Y', p.date) = strftime('%Y','now')"
        );
        return rs.next() ? rs.getDouble("profit") : 0.0;
    }

}
