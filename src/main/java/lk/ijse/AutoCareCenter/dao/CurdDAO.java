package lk.ijse.AutoCareCenter.dao;

import lk.ijse.AutoCareCenter.model.OrdersDTO;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public interface CurdDAO <T> extends SuperDAO {
    public ArrayList<T> loadAll() throws SQLException, ClassNotFoundException;

    public boolean save(T DTO) throws SQLException, ClassNotFoundException;

    public boolean delete(String id) throws SQLException, ClassNotFoundException;

    public boolean update(T DTO) throws SQLException, ClassNotFoundException;

    public T searchById(String id) throws SQLException, ClassNotFoundException;

    public List<String> getIds() throws SQLException, ClassNotFoundException;

    public int getMaterialCount() throws SQLException, ClassNotFoundException;

    public String currentId() throws SQLException, ClassNotFoundException ;

}
