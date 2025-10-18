package lk.ijse.AutoCareCenter.bo.custom;

import lk.ijse.AutoCareCenter.bo.SuperBO;
import lk.ijse.AutoCareCenter.dao.SqlUtil;
import lk.ijse.AutoCareCenter.entity.Supplier;
import lk.ijse.AutoCareCenter.model.SalaryDTO;
import lk.ijse.AutoCareCenter.model.SupplierDTO;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public interface SupplierBO extends SuperBO {
    public ArrayList<SupplierDTO> loadAll() throws SQLException, ClassNotFoundException ;

    public boolean save(SupplierDTO dto) throws SQLException, ClassNotFoundException ;

    public boolean update(SupplierDTO dto) throws SQLException, ClassNotFoundException;

    public Supplier searchById(String id) throws SQLException, ClassNotFoundException ;

    public boolean delete(String id) throws SQLException, ClassNotFoundException;

    public List<String> getIds() throws SQLException, ClassNotFoundException ;
    public  String currentId() throws SQLException, ClassNotFoundException;

}
