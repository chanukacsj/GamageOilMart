package lk.ijse.AutoCareCenter.dao.custom.Impl;

import lk.ijse.AutoCareCenter.dao.SqlUtil;
import lk.ijse.AutoCareCenter.dao.custom.RepairDAO;
import lk.ijse.AutoCareCenter.entity.Repair;
import lk.ijse.AutoCareCenter.model.OrdersDTO;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class RepairDAOImpl implements RepairDAO {
    public ArrayList<Repair> loadAll() throws SQLException, ClassNotFoundException {


        ArrayList<Repair> allRepairs = new ArrayList<>();
        ResultSet rst = SqlUtil.execute("SELECT * FROM repairAndService");

        while (rst.next()) {
            Repair repair = new Repair(rst.getString("rid"), rst.getString("startTime"), rst.getString("endTime"), rst.getString("description"), rst.getString("empId"), rst.getString("vId"));
            allRepairs.add(repair);
        }

        return allRepairs;

    }

    public boolean delete(String id) throws SQLException, ClassNotFoundException {
        return SqlUtil.execute("DELETE FROM repairAndService WHERE rid=?", id);
    }

    public boolean save(Repair entity) throws SQLException, ClassNotFoundException {
        return SqlUtil.execute("INSERT INTO repairAndService VALUES(?,?,?,?,?,?)", entity.getRId(), entity.getStartTime(), entity.getEndTime(), entity.getDescription(), entity.getEmpId(), entity.getVId());
    }

    public boolean update(Repair entity) throws SQLException, ClassNotFoundException {
        return SqlUtil.execute("UPDATE repairAndService SET startTime = ?, endTime = ?,description = ? ,empId = ?,vId = ? WHERE rid = ?", entity.getStartTime(), entity.getEndTime(), entity.getDescription(), entity.getEmpId(), entity.getVId(), entity.getRId());
    }

    public Repair searchById(String id) throws SQLException, ClassNotFoundException {
        ResultSet rst = SqlUtil.execute("SELECT * FROM repairAndService WHERE rid=?", id);
        rst.next();
        return new Repair(id + "", rst.getString("startTime"), rst.getString("endTime"), rst.getString("description"), rst.getString("empId"), rst.getString("vId"));

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
        ResultSet rst = SqlUtil.execute("SELECT rid FROM repairAndService ORDER BY rid desc LIMIT 1");

        if(rst.next()) {
            return rst.getString(1);
        }
        return null;
    }
}
