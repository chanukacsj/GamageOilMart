package lk.ijse.AutoCareCenter.dao.custom.Impl;

import lk.ijse.AutoCareCenter.dao.SqlUtil;
import lk.ijse.AutoCareCenter.dao.custom.OrderDetailDAO;
import lk.ijse.AutoCareCenter.entity.OrderDetails;
import lk.ijse.AutoCareCenter.model.OrderDetailsDTO;
import lk.ijse.AutoCareCenter.model.OrdersDTO;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class OrderDetailsDAOImpl implements OrderDetailDAO {

    public boolean save(OrderDetails details) throws SQLException, ClassNotFoundException {
        return SqlUtil.execute(
                "INSERT INTO order_details (orderId, code, qty, unitPrice, service_charge, total) VALUES (?, ?, ?, ?, ?, ?)",
                details.getOrderId(),
                details.getCode(),
                details.getQty(),
                details.getUnitPrice(),
                details.getService_charge(),
                details.getTotal()
        );
    }



    @Override
    public ArrayList<OrderDetails> loadAll() throws SQLException, ClassNotFoundException {
        return null;
    }

    @Override
    public boolean delete(String id) throws SQLException, ClassNotFoundException {
        return false;
    }

    @Override
    public boolean update(OrderDetails DTO) throws SQLException, ClassNotFoundException {
        return false;
    }

    @Override
    public OrderDetails searchById(String id) throws SQLException, ClassNotFoundException {
        return null;
    }

    @Override
    public List<String> getIds() throws SQLException, ClassNotFoundException {
        return null;
    }

    @Override
    public int getMaterialCount() throws SQLException, ClassNotFoundException {
        return 0;
    }

    @Override
    public String currentId() throws SQLException, ClassNotFoundException {
        return null;
    }
}
