package lk.ijse.AutoCareCenter.bo.custom;

import lk.ijse.AutoCareCenter.bo.SuperBO;
import lk.ijse.AutoCareCenter.entity.MaterialDetails;
import lk.ijse.AutoCareCenter.model.MaterialDetailsDTO;

import java.sql.SQLException;
import java.util.ArrayList;

public interface MaterialDetailBO extends SuperBO {
    public ArrayList<MaterialDetailsDTO> loadAll() throws SQLException, ClassNotFoundException;

    public boolean save(MaterialDetailsDTO materialDetailsDTO) throws SQLException, ClassNotFoundException;

    public boolean update(MaterialDetailsDTO materialDetailsDTO) throws SQLException, ClassNotFoundException;

    public MaterialDetails searchById(String code) throws SQLException, ClassNotFoundException;

}
