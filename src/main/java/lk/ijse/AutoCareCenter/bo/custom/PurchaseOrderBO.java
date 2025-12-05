package lk.ijse.AutoCareCenter.bo.custom;

import lk.ijse.AutoCareCenter.bo.SuperBO;
import lk.ijse.AutoCareCenter.entity.MaterialDetails;
import lk.ijse.AutoCareCenter.entity.OrderDetails;
import lk.ijse.AutoCareCenter.entity.Payment;
import lk.ijse.AutoCareCenter.model.MaterialDetailsDTO;
import lk.ijse.AutoCareCenter.model.OrdersDTO;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public interface PurchaseOrderBO extends SuperBO {

    String getDescriptionByCode(String code) throws Exception;

    boolean exist(String orderId) throws SQLException, ClassNotFoundException;

    public String currentId() throws SQLException, ClassNotFoundException;

    public boolean saveOrder(OrdersDTO dto) throws SQLException, ClassNotFoundException;

    public MaterialDetailsDTO findItem(String code) throws SQLException, ClassNotFoundException;

    public List<String> getMaterialsIds() throws SQLException, ClassNotFoundException;

    public List<String> getBooking() throws SQLException, ClassNotFoundException;

    public List<String> getIds() throws SQLException, ClassNotFoundException;

    public MaterialDetails searchByMaterialId(String code) throws SQLException, ClassNotFoundException;

    public List<String> getCodes() throws SQLException, ClassNotFoundException;

    public List<String> getBookingIds() throws SQLException, ClassNotFoundException;

    public ArrayList<Payment> loadAll() throws SQLException, ClassNotFoundException;

}
