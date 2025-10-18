package lk.ijse.AutoCareCenter.dao.custom.Impl;

import lk.ijse.AutoCareCenter.dao.SqlUtil;
import lk.ijse.AutoCareCenter.dao.custom.VehicleDAO;
import lk.ijse.AutoCareCenter.entity.Vehicle;
import lk.ijse.AutoCareCenter.model.OrdersDTO;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class VehicleDAOImpl implements VehicleDAO {


    public ArrayList<Vehicle> loadAll() throws SQLException, ClassNotFoundException {
        ResultSet rst = SqlUtil.execute("SELECT * FROM Vehicle");

        ArrayList<Vehicle> allVehicle = new ArrayList<>();

        while (rst.next()) {
            Vehicle vehicle = new Vehicle(rst.getString("vId"), rst.getString("type"), rst.getString("number"), rst.getString("id"));
            allVehicle.add(vehicle);
        }

        return allVehicle;
    }

    public boolean delete(String id) throws SQLException, ClassNotFoundException {
        return SqlUtil.execute("DELETE FROM vehicle WHERE vId = ?", id);
    }

    public boolean save(Vehicle entity) throws SQLException, ClassNotFoundException {
        return SqlUtil.execute("INSERT INTO vehicle VALUES(?, ?, ?, ?)", entity.getId(), entity.getType(), entity.getNumber(), entity.getCusId());
    }

    public boolean update(Vehicle entity) throws SQLException, ClassNotFoundException {
        return SqlUtil.execute("UPDATE vehicle SET type = ?, number = ?,id = ? WHERE vId = ?", entity.getType(), entity.getNumber(), entity.getCusId(), entity.getId());
    }

    public Vehicle searchById(String id) throws SQLException, ClassNotFoundException {
        ResultSet rst = SqlUtil.execute("SELECT * FROM vehicle WHERE vId = ?", id);

        rst.next();
        return new Vehicle(id + "", rst.getString("type"), rst.getString("number"), rst.getString("id"));
    }

    public List<String> getIds() throws SQLException, ClassNotFoundException {

        ArrayList<String> ids = new ArrayList<>();

        ResultSet rst = SqlUtil.execute("SELECT vId FROM vehicle");
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
        ResultSet rst = SqlUtil.execute("SELECT vId FROM vehicle ORDER BY vId desc LIMIT 1");

        if(rst.next()) {
            return rst.getString(1);
        }
        return null;
    }
}
