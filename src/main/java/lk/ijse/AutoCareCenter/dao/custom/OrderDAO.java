package lk.ijse.AutoCareCenter.dao.custom;

import lk.ijse.AutoCareCenter.dao.CurdDAO;
import lk.ijse.AutoCareCenter.dao.SqlUtil;
import lk.ijse.AutoCareCenter.entity.Orders;
import lk.ijse.AutoCareCenter.model.OrdersDTO;

import java.sql.ResultSet;
import java.sql.SQLException;

public interface OrderDAO extends CurdDAO<Orders> {
    public String currentId() throws SQLException, ClassNotFoundException;
    public boolean exist(String orderId) throws SQLException, ClassNotFoundException ;

}
