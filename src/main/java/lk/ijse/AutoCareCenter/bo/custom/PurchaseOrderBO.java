package lk.ijse.AutoCareCenter.bo.custom;

import lk.ijse.AutoCareCenter.bo.SuperBO;
import lk.ijse.AutoCareCenter.dao.SqlUtil;
import lk.ijse.AutoCareCenter.entity.Customer;
import lk.ijse.AutoCareCenter.entity.MaterialDetails;
import lk.ijse.AutoCareCenter.entity.Materials;
import lk.ijse.AutoCareCenter.model.MaterialDetailsDTO;
import lk.ijse.AutoCareCenter.model.MaterialsDTO;
import lk.ijse.AutoCareCenter.model.OrderDetailsDTO;
import lk.ijse.AutoCareCenter.model.OrdersDTO;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public interface PurchaseOrderBO extends SuperBO {


    boolean exist(String orderId) throws SQLException, ClassNotFoundException;

    public String currentId() throws SQLException, ClassNotFoundException;

    public boolean saveOrder(OrdersDTO dto) throws SQLException, ClassNotFoundException;

    public MaterialDetailsDTO findItem(String code) throws SQLException, ClassNotFoundException;

    public List<String> getCustomerIds() throws SQLException, ClassNotFoundException;

    public List<String> getMaterialsIds() throws SQLException, ClassNotFoundException;

    public List<String> getBooking() throws SQLException, ClassNotFoundException;

    public Customer searchById(String id) throws SQLException, ClassNotFoundException;

    public List<String> getIds() throws SQLException, ClassNotFoundException;

    public MaterialDetails searchByMaterialId(String code) throws SQLException, ClassNotFoundException;

    public List<String> getCodes() throws SQLException, ClassNotFoundException;

    public List<String> getBookingIds() throws SQLException, ClassNotFoundException;
}
