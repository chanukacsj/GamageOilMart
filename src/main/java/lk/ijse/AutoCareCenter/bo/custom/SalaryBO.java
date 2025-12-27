package lk.ijse.AutoCareCenter.bo.custom;

import lk.ijse.AutoCareCenter.bo.SuperBO;
import lk.ijse.AutoCareCenter.entity.Employee;
import lk.ijse.AutoCareCenter.entity.Salary;
import lk.ijse.AutoCareCenter.model.SalaryDTO;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public interface SalaryBO extends SuperBO {

    public ArrayList<SalaryDTO> loadAll() throws SQLException, ClassNotFoundException;

    public boolean save(SalaryDTO salaryDTO) throws Exception;

    public boolean update(SalaryDTO salaryDTO) throws SQLException, ClassNotFoundException;

    public Salary searchById(String id) throws SQLException, ClassNotFoundException;

    public boolean delete(String id) throws SQLException, ClassNotFoundException;

    public String currentId() throws SQLException, ClassNotFoundException;

    public List<String> getIds() throws SQLException, ClassNotFoundException;
    public Employee searchByEmpId(String id) throws SQLException, ClassNotFoundException ;
}
