package lk.ijse.AutoCareCenter.dao.custom.Impl;

import lk.ijse.AutoCareCenter.dao.SqlUtil;
import lk.ijse.AutoCareCenter.dao.custom.MaterialDAO;
import lk.ijse.AutoCareCenter.entity.Materials;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MaterialDAOImpl implements MaterialDAO {

    @Override
    public boolean save(Materials entity) throws SQLException, ClassNotFoundException {
        // In SQLite, specify column names for clarity
        return SqlUtil.execute("INSERT INTO materials (code) VALUES(?)", entity.getCode());
    }

    @Override
    public boolean delete(String code) throws SQLException, ClassNotFoundException {
        return SqlUtil.execute("DELETE FROM materials WHERE code = ?", code);
    }

    @Override
    public List<String> getCodes() throws SQLException, ClassNotFoundException {
        List<String> codes = new ArrayList<>();
        ResultSet rst = SqlUtil.execute("SELECT code FROM materials");
        while (rst.next()) {
            codes.add(rst.getString("code"));
        }
        return codes;
    }

    @Override
    public boolean updateQty(String code, int qty) throws SQLException, ClassNotFoundException {
        return SqlUtil.execute(
                "UPDATE material_details SET qtyOnHand = qtyOnHand - ? WHERE code = ? AND qtyOnHand >= ?",
                qty,
                code,
                qty
        );
    }

    @Override
    public int getMaterialCount() throws SQLException, ClassNotFoundException {
        ResultSet rst = SqlUtil.execute("SELECT COUNT(*) AS materials_count FROM materials");
        if (rst.next()) {
            return rst.getInt("materials_count");
        }
        return 0;
    }

    @Override
    public String currentId() throws SQLException, ClassNotFoundException {
        // SQLite supports ORDER BY and LIMIT normally
        ResultSet rst = SqlUtil.execute("SELECT code FROM materials ORDER BY code DESC LIMIT 1");
        if (rst.next()) {
            return rst.getString("code");
        }
        return null;
    }

    @Override
    public ArrayList<Materials> loadAll() throws SQLException, ClassNotFoundException {
        ArrayList<Materials> all = new ArrayList<>();
        ResultSet rst = SqlUtil.execute("SELECT * FROM materials");
        while (rst.next()) {
            all.add(new Materials(rst.getString("code")));
        }
        return all;
    }

    @Override
    public boolean update(Materials entity) throws SQLException, ClassNotFoundException {
        // Optional if you plan to add more columns later (like name, price, etc.)
        return SqlUtil.execute("UPDATE materials SET code=? WHERE code=?", entity.getCode(), entity.getCode());
    }

    @Override
    public Materials searchById(String code) throws SQLException, ClassNotFoundException {
        ResultSet rst = SqlUtil.execute("SELECT * FROM materials WHERE code=?", code);
        if (rst.next()) {
            return new Materials(rst.getString("code"));
        }
        return null;
    }

    @Override
    public List<String> getIds() throws SQLException, ClassNotFoundException {
        List<String> ids = new ArrayList<>();
        ResultSet rst = SqlUtil.execute("SELECT code FROM materials");
        while (rst.next()) {
            ids.add(rst.getString("code"));
        }
        return ids;
    }
}
