package lk.ijse.AutoCareCenter.dao.custom.Impl;

import lk.ijse.AutoCareCenter.dao.SqlUtil;
import lk.ijse.AutoCareCenter.dao.custom.EmployeeDAO;
import lk.ijse.AutoCareCenter.entity.Employee;
import lk.ijse.AutoCareCenter.model.OrdersDTO;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class EmployeeDAOImpl implements EmployeeDAO {

    public ArrayList<Employee> loadAll() throws SQLException, ClassNotFoundException {

        ArrayList<Employee> allEmployee = new ArrayList<>();
        ResultSet rst =  SqlUtil.execute("SELECT * FROM employee");
        while (rst.next()) {
            Employee employee = new Employee(rst.getString("empId"), rst.getString("empName"), rst.getString("contact"), rst.getString("address"));
            allEmployee.add(employee);
        }

        return allEmployee;
    }

    public boolean delete(String id) throws SQLException, ClassNotFoundException {
        return SqlUtil.execute("DELETE FROM Employee WHERE empId = ?",id);
    }

    public boolean save(Employee entity) throws SQLException, ClassNotFoundException {
        return SqlUtil.execute("INSERT INTO Employee VALUES(?, ?, ?, ?)",entity.getId(),entity.getName(),entity.getContact(),entity.getAddress());
    }

    public boolean update(Employee entity) throws SQLException, ClassNotFoundException {
        return  SqlUtil.execute("UPDATE Employee SET empName = ?,contact = ?, address = ?  WHERE empId = ?",entity.getName(),entity.getContact(),entity.getAddress(),entity.getId());
    }

    public Employee searchById(String id) throws SQLException, ClassNotFoundException {
        ResultSet rst =  SqlUtil.execute("SELECT * FROM Employee WHERE EmpId = ?",id);
        rst.next();
        return new Employee(id + "", rst.getString("EmpName"), rst.getString("address"),rst.getString("contact"));

    }

    public List<String> getIds() throws SQLException, ClassNotFoundException {
        List<String> ids = new ArrayList<>();

        ResultSet rst = SqlUtil.execute("SELECT empId FROM employee");
        while (rst.next()) {
            ids.add(rst.getString(1));
        }


        return ids;
    }

    @Override
    public int getMaterialCount() throws SQLException, ClassNotFoundException {
        return 0;
    }

    @Override
    public String currentId() throws SQLException, ClassNotFoundException {
        ResultSet rst = SqlUtil.execute("SELECT empId FROM employee ORDER BY empId desc LIMIT 1");

        if(rst.next()) {
            return rst.getString(1);
        }
        return null;
    }

}
