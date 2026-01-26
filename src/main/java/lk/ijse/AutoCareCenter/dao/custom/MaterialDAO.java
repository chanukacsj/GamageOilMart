package lk.ijse.AutoCareCenter.dao.custom;

import lk.ijse.AutoCareCenter.dao.CurdDAO;
import lk.ijse.AutoCareCenter.dao.SqlUtil;
import lk.ijse.AutoCareCenter.entity.Materials;
import lk.ijse.AutoCareCenter.model.MaterialsDTO;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public interface MaterialDAO extends CurdDAO<Materials> {
    public List<String> getCodes() throws SQLException, ClassNotFoundException ;

    boolean updateQty(String code, int qty) throws SQLException, ClassNotFoundException;
}
