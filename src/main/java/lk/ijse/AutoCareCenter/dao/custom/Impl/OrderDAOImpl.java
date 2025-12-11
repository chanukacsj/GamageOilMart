package lk.ijse.AutoCareCenter.dao.custom.Impl;

import lk.ijse.AutoCareCenter.dao.SqlUtil;
import lk.ijse.AutoCareCenter.dao.custom.OrderDAO;
import lk.ijse.AutoCareCenter.entity.Orders;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class OrderDAOImpl implements OrderDAO {

    @Override
    public String currentId() throws SQLException, ClassNotFoundException {
        ResultSet rst = SqlUtil.execute(
                "SELECT orderId FROM orders ORDER BY CAST(SUBSTR(orderId, 2) AS INTEGER) DESC LIMIT 1"
        );
        if (rst.next()) {
            return rst.getString("orderId");
        }
        return null;
    }



    @Override
    public boolean exist(String orderId) throws SQLException, ClassNotFoundException {
        ResultSet rst = SqlUtil.execute("SELECT orderId FROM orders WHERE orderId = ?", orderId);
        return rst.next();
    }

    public int getOrderCount() throws SQLException, ClassNotFoundException {
        ResultSet rst = SqlUtil.execute("SELECT COUNT(*) AS order_count FROM orders");
        if (rst.next()) {
            return rst.getInt("order_count");
        }
        return 0;
    }

    @Override
    public ArrayList<Orders> loadAll() throws SQLException, ClassNotFoundException {
        return null;
    }

    @Override
    public boolean save(Orders DTO) throws SQLException, ClassNotFoundException {
        System.out.println("save order");
        return SqlUtil.execute(
                "INSERT INTO orders (orderId,date) VALUES (?,?)",
                DTO.getOrderId(),
                DTO.getDate()
        );
    }

    @Override
    public boolean delete(String id) throws SQLException, ClassNotFoundException {
        return SqlUtil.execute("DELETE FROM orders WHERE orderId = ?", id);
    }

    @Override
    public boolean update(Orders DTO) throws SQLException, ClassNotFoundException {
        return SqlUtil.execute(
                "UPDATE orders SET date = ? WHERE orderId = ?",
                DTO.getDate(),
                DTO.getOrderId()
        );
    }

    @Override
    public Orders searchById(String id) throws SQLException, ClassNotFoundException {

        return null;
    }

    @Override
    public List<String> getIds() throws SQLException, ClassNotFoundException {
        List<String> ids = new ArrayList<>();
        ResultSet rst = SqlUtil.execute("SELECT orderId FROM orders");
        while (rst.next()) {
            ids.add(rst.getString("orderId"));
        }
        return ids;
    }

    @Override
    public int getMaterialCount() throws SQLException, ClassNotFoundException {
        // Not relevant for orders, but implemented for interface compatibility
        return 0;
    }
}
