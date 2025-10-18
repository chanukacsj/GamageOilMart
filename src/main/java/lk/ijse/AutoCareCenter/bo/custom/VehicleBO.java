package lk.ijse.AutoCareCenter.bo.custom;

import lk.ijse.AutoCareCenter.bo.SuperBO;
import lk.ijse.AutoCareCenter.dao.SqlUtil;
import lk.ijse.AutoCareCenter.entity.Customer;
import lk.ijse.AutoCareCenter.entity.Vehicle;
import lk.ijse.AutoCareCenter.model.VehicleDTO;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public interface VehicleBO extends SuperBO {
    public ArrayList<VehicleDTO> loadAll() throws SQLException, ClassNotFoundException;

    public boolean delete(String id) throws SQLException, ClassNotFoundException;

    public boolean save(VehicleDTO vehicleDTO) throws SQLException, ClassNotFoundException;

    public boolean update(VehicleDTO vehicleDTO) throws SQLException, ClassNotFoundException;

    public Vehicle searchById(String id) throws SQLException, ClassNotFoundException;

    public List<String> getIds() throws SQLException, ClassNotFoundException;

    public String currentId() throws SQLException, ClassNotFoundException;

    public List<String> getCustomerIds() throws SQLException, ClassNotFoundException;

    public Customer searchByCustomerId(String id) throws SQLException, ClassNotFoundException;

}
