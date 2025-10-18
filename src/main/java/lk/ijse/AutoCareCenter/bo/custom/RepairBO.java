package lk.ijse.AutoCareCenter.bo.custom;

import lk.ijse.AutoCareCenter.bo.SuperBO;
import lk.ijse.AutoCareCenter.dao.SqlUtil;
import lk.ijse.AutoCareCenter.entity.Repair;
import lk.ijse.AutoCareCenter.model.RepairDTO;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public interface RepairBO extends SuperBO {
    public ArrayList<RepairDTO> loadAll() throws SQLException, ClassNotFoundException;

    public boolean delete(String id) throws SQLException, ClassNotFoundException;

    public boolean save(RepairDTO repairDTO) throws SQLException, ClassNotFoundException;

    public boolean update(RepairDTO repairDTO) throws SQLException, ClassNotFoundException;

    public Repair searchById(String id) throws SQLException, ClassNotFoundException;

    public  String currentId() throws SQLException, ClassNotFoundException;

    public List<String> getRepairIds() throws SQLException, ClassNotFoundException ;

    public List<String> getVehicleIds() throws SQLException, ClassNotFoundException ;
}
