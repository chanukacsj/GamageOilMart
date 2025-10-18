package lk.ijse.AutoCareCenter.dao.custom.Impl;

import lk.ijse.AutoCareCenter.dao.SqlUtil;
import lk.ijse.AutoCareCenter.dao.custom.MaterialDetailDAO;
import lk.ijse.AutoCareCenter.entity.MaterialDetails;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MaterialDetailsDAOImpl implements MaterialDetailDAO {

    @Override
    public ArrayList<MaterialDetails> loadAll() throws SQLException, ClassNotFoundException {
        ArrayList<MaterialDetails> allMaterialDetails = new ArrayList<>();
        ResultSet rst = SqlUtil.execute("SELECT * FROM material_details");
        while (rst.next()) {
            allMaterialDetails.add(new MaterialDetails(
                    rst.getString("code"),
                    rst.getString("supId"),
                    rst.getString("description"),
                    rst.getDouble("unit_price"),
                    rst.getInt("qty_on_hand")
            ));
        }
        return allMaterialDetails;
    }

    @Override
    public boolean save(MaterialDetails entity) throws SQLException, ClassNotFoundException {
        // Explicit column list — safer for SQLite
        return SqlUtil.execute(
                "INSERT INTO material_details (code, supId, description, unit_price, qty_on_hand) VALUES (?, ?, ?, ?, ?)",
                entity.getCode(),
                entity.getSupId(),
                entity.getDescription(),
                entity.getUnitPrice(),
                entity.getQtyOnHand()
        );
    }

    @Override
    public boolean update(MaterialDetails entity) throws SQLException, ClassNotFoundException {
        return SqlUtil.execute(
                "UPDATE material_details SET supId = ?, description = ?, unit_price = ?, qty_on_hand = ? WHERE code = ?",
                entity.getSupId(),
                entity.getDescription(),
                entity.getUnitPrice(),
                entity.getQtyOnHand(),
                entity.getCode()
        );
    }

    @Override
    public MaterialDetails searchById(String code) throws SQLException, ClassNotFoundException {
        ResultSet rst = SqlUtil.execute("SELECT * FROM material_details WHERE code = ?", code);
        if (rst.next()) {
            return new MaterialDetails(
                    rst.getString("code"),
                    rst.getString("supId"),
                    rst.getString("description"),
                    rst.getDouble("unit_price"),
                    rst.getInt("qty_on_hand")
            );
        }
        return null;
    }

    @Override
    public boolean delete(String code) throws SQLException, ClassNotFoundException {
        return SqlUtil.execute("DELETE FROM material_details WHERE code = ?", code);
    }

    @Override
    public List<String> getIds() throws SQLException, ClassNotFoundException {
        List<String> ids = new ArrayList<>();
        ResultSet rst = SqlUtil.execute("SELECT code FROM material_details");
        while (rst.next()) {
            ids.add(rst.getString("code"));
        }
        return ids;
    }

    @Override
    public int getMaterialCount() throws SQLException, ClassNotFoundException {
        ResultSet rst = SqlUtil.execute("SELECT COUNT(*) AS material_count FROM material_details");
        if (rst.next()) {
            return rst.getInt("material_count");
        }
        return 0;
    }

    @Override
    public String currentId() throws SQLException, ClassNotFoundException {
        ResultSet rst = SqlUtil.execute("SELECT code FROM material_details ORDER BY code DESC LIMIT 1");
        if (rst.next()) {
            return rst.getString("code");
        }
        return null;
    }
}
