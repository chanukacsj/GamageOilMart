package lk.ijse.AutoCareCenter.dao.custom.Impl;

import lk.ijse.AutoCareCenter.dao.SqlUtil;
import lk.ijse.AutoCareCenter.dao.custom.SupplierDAO;
import lk.ijse.AutoCareCenter.entity.Supplier;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SupplierDAOImpl implements SupplierDAO {

    @Override
    public ArrayList<Supplier> loadAll() throws SQLException, ClassNotFoundException {
        ArrayList<Supplier> allSuppliers = new ArrayList<>();
        ResultSet rst = SqlUtil.execute("SELECT * FROM suppliers");

        while (rst.next()) {
            allSuppliers.add(
                    new Supplier(
                            rst.getString("supId"),
                            rst.getString("supName"),
                            rst.getString("contact"),
                            rst.getString("address")
                    )
            );
        }

        return allSuppliers;
    }

    @Override
    public boolean save(Supplier entity) throws SQLException, ClassNotFoundException {
        // ✅ Always include column names — safer for SQLite
        return SqlUtil.execute(
                "INSERT INTO suppliers (supId, supName, contact, address) VALUES (?, ?, ?, ?)",
                entity.getId(),
                entity.getName(),
                entity.getContact(),
                entity.getAddress()
        );
    }

    @Override
    public boolean update(Supplier entity) throws SQLException, ClassNotFoundException {
        return SqlUtil.execute(
                "UPDATE suppliers SET supName = ?, contact = ?, address = ? WHERE supId = ?",
                entity.getName(),
                entity.getContact(),
                entity.getAddress(),
                entity.getId()
        );
    }

    @Override
    public Supplier searchById(String id) throws SQLException, ClassNotFoundException {
        ResultSet rst = SqlUtil.execute("SELECT * FROM suppliers WHERE supId = ?", id);
        if (rst.next()) {
            return new Supplier(
                    rst.getString("supId"),
                    rst.getString("supName"),
                    rst.getString("contact"),
                    rst.getString("address")
            );
        }
        return null;
    }

    @Override
    public boolean delete(String id) throws SQLException, ClassNotFoundException {
        return SqlUtil.execute("DELETE FROM suppliers WHERE supId = ?", id);
    }

    @Override
    public List<String> getIds() throws SQLException, ClassNotFoundException {
        List<String> ids = new ArrayList<>();
        ResultSet rst = SqlUtil.execute("SELECT supId FROM suppliers");
        while (rst.next()) {
            ids.add(rst.getString(1));
        }
        return ids;
    }

    @Override
    public int getMaterialCount() throws SQLException, ClassNotFoundException {
        // Optional: Return count of suppliers
        ResultSet rst = SqlUtil.execute("SELECT COUNT(*) AS count FROM suppliers");
        if (rst.next()) {
            return rst.getInt("count");
        }
        return 0;
    }

    @Override
    public String currentId() throws SQLException, ClassNotFoundException {
        // ✅ SQLite supports ORDER BY and LIMIT
        ResultSet rst = SqlUtil.execute("SELECT supId FROM suppliers ORDER BY supId DESC LIMIT 1");
        if (rst.next()) {
            return rst.getString("supId");
        }
        return null;
    }
}
