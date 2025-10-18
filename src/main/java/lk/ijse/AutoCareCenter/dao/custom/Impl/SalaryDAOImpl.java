package lk.ijse.AutoCareCenter.dao.custom.Impl;

import lk.ijse.AutoCareCenter.dao.SqlUtil;
import lk.ijse.AutoCareCenter.dao.custom.SalaryDAO;
import lk.ijse.AutoCareCenter.entity.Salary;
import lk.ijse.AutoCareCenter.model.OrdersDTO;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SalaryDAOImpl implements SalaryDAO {
    public ArrayList<Salary> loadAll() throws SQLException, ClassNotFoundException {


        ArrayList<Salary> allSalary = new ArrayList<>();
        ResultSet rst = SqlUtil.execute("SELECT * FROM salary");

        while (rst.next()) {
            Salary salary = new Salary(rst.getString("sId"), rst.getInt("amount"), rst.getString("month"), rst.getString("empId"));
            allSalary.add(salary);
        }

        return allSalary;
    }

    public boolean save(Salary entity) throws SQLException, ClassNotFoundException {
        return SqlUtil.execute("INSERT INTO salary VALUES (?,?,?,?)", entity.getId(), entity.getAmount(), entity.getMonth(), entity.getEmpId());
    }

    public boolean update(Salary entity) throws SQLException, ClassNotFoundException {
        return SqlUtil.execute("UPDATE salary SET amount=?, month=?, empId=? WHERE sId=?", entity.getAmount(), entity.getMonth(), entity.getEmpId(), entity.getId());

    }

    public Salary searchById(String id) throws SQLException, ClassNotFoundException {
        ResultSet rst = SqlUtil.execute("SELECT * FROM salary WHERE sId = ?", id);
        rst.next();
        return new Salary(id + "", rst.getInt("amount"), rst.getString("month"), rst.getString("empId")) ;
    }



    public boolean delete(String id) throws SQLException, ClassNotFoundException {
        return SqlUtil.execute("DELETE FROM salary WHERE sId = ?", id);
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
        ResultSet rst = SqlUtil.execute("SELECT sId FROM salary ORDER BY sId desc LIMIT 1");

        if(rst.next()) {
            return rst.getString(1);
        }
        return null;
    }
}
