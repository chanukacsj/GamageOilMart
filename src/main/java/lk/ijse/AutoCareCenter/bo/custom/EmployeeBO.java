package lk.ijse.AutoCareCenter.bo.custom;

import lk.ijse.AutoCareCenter.bo.SuperBO;
import lk.ijse.AutoCareCenter.entity.Employee;
import lk.ijse.AutoCareCenter.model.EmployeeDTO;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public interface EmployeeBO extends SuperBO {
    public ArrayList<EmployeeDTO> loadAll() throws SQLException, ClassNotFoundException;

    public boolean delete(String id) throws SQLException, ClassNotFoundException;

    public boolean save(EmployeeDTO employeeDTO) throws SQLException, ClassNotFoundException;

    public boolean update(EmployeeDTO employeeDTO) throws SQLException, ClassNotFoundException;

    public Employee searchById(String id) throws SQLException, ClassNotFoundException;

    public List<String> getIds() throws SQLException, ClassNotFoundException;

    public  String currentId() throws SQLException, ClassNotFoundException;

}
