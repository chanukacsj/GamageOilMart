package lk.ijse.AutoCareCenter.dao.custom.Impl;

import lk.ijse.AutoCareCenter.dao.SqlUtil;
import lk.ijse.AutoCareCenter.dao.custom.MaterialDetailDAO;
import lk.ijse.AutoCareCenter.entity.MaterialDetails;
import lk.ijse.AutoCareCenter.model.MaterialDetailsDTO;

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
                    rst.getDouble("unitPrice"),
                    rst.getInt("qtyOnHand"),
                    rst.getString("category"),
                    rst.getString("brand"),
                    rst.getString("addedDate"),
                    rst.getString("status")
            ));
        }
        return allMaterialDetails;
    }

    @Override
    public boolean save(MaterialDetails entity) throws SQLException, ClassNotFoundException {
        // ✅ Fixed: 9 columns and 9 placeholders
        return SqlUtil.execute(
                "INSERT INTO material_details (code, supId, description, unitPrice, qtyOnHand, category, brand, addedDate, status) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)",
                entity.getCode(),
                entity.getSupId(),
                entity.getDescription(),
                entity.getUnitPrice(),
                entity.getQtyOnHand(),
                entity.getCategory(),
                entity.getBrand(),
                entity.getAddedDate(),
                entity.getStatus()
        );
    }

    @Override
    public boolean update(MaterialDetails entity) throws SQLException, ClassNotFoundException {
        return SqlUtil.execute(
                "UPDATE material_details SET supId = ?, description = ?, unitPrice = ?, qtyOnHand = ?, category = ?, brand = ?, addedDate = ?, status = ? WHERE code = ?",
                entity.getSupId(),
                entity.getDescription(),
                entity.getUnitPrice(),
                entity.getQtyOnHand(),
                entity.getCategory(),
                entity.getBrand(),
                entity.getAddedDate(),
                entity.getStatus(),
                entity.getCode()
        );
    }

    @Override
    public MaterialDetails searchById(String code) throws SQLException, ClassNotFoundException {
        ResultSet rst = SqlUtil.execute(
                "SELECT * FROM material_details WHERE code = ?", code
        );

        if (rst.next()) {
            return new MaterialDetails(
                    rst.getString("code"),
                    rst.getString("supId"),
                    rst.getString("description"),
                    rst.getDouble("unitPrice"),
                    rst.getInt("qtyOnHand"),
                    rst.getString("category"),
                    rst.getString("brand"),
                    rst.getString("addedDate"),
                    rst.getString("status")
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
        System.out.println("in currentId method");
        ResultSet rst = SqlUtil.execute(
                "SELECT code FROM material_details ORDER BY CAST(SUBSTR(code, 2) AS INTEGER) DESC LIMIT 1"
        );


        if (rst.next()) {
            String code = rst.getString("code");
            return code;
        } else {
            System.out.println("No code found in material_details table!");
        }

        return null;
    }


    @Override
    public ArrayList<MaterialDetailsDTO> loadAllByCategory(String category) {
        ArrayList<MaterialDetailsDTO> list = new ArrayList<>();
        try {
            ResultSet rs = SqlUtil.execute("SELECT * FROM material_details WHERE category = ?", category);
            while (rs.next()) {
                list.add(new MaterialDetailsDTO(
                        rs.getString("code"),
                        rs.getString("supId"),
                        rs.getString("description"),
                        rs.getDouble("unitPrice"),
                        rs.getInt("qtyOnHand"),
                        rs.getString("category"),
                        rs.getString("brand"),
                        rs.getString("addedDate"),
                        rs.getString("status")
                ));
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return list;
    }


    @Override
    public List<String> getCodes() throws SQLException, ClassNotFoundException {
        List<String> codes = new ArrayList<>();
        ResultSet rst = SqlUtil.execute("SELECT code FROM material_details");
        while (rst.next()) {
            codes.add(rst.getString("code"));
        }
        return codes;
    }
}
