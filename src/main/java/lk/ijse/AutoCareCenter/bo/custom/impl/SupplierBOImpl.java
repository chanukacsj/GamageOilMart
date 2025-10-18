package lk.ijse.AutoCareCenter.bo.custom.impl;

import lk.ijse.AutoCareCenter.bo.custom.SupplierBO;
import lk.ijse.AutoCareCenter.dao.DAOFactory;
import lk.ijse.AutoCareCenter.dao.custom.SupplierDAO;
import lk.ijse.AutoCareCenter.entity.Booking;
import lk.ijse.AutoCareCenter.entity.Supplier;
import lk.ijse.AutoCareCenter.model.BookingDTO;
import lk.ijse.AutoCareCenter.model.SupplierDTO;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SupplierBOImpl implements SupplierBO {
    SupplierDAO supplierDAO = (SupplierDAO) DAOFactory.getDaoFactory().getDAO(DAOFactory.DAOTypes.SUPPLIER);

    @Override
    public ArrayList<SupplierDTO> loadAll() throws SQLException, ClassNotFoundException {

        ArrayList<SupplierDTO> allSupplier = new ArrayList<>();
        ArrayList<Supplier> all = supplierDAO.loadAll();

        for (Supplier b : all) {
            allSupplier.add(new SupplierDTO(b.getId(), b.getName(), b.getContact(), b.getAddress()));
        }
        return allSupplier;
    }

    @Override
    public boolean save(SupplierDTO dto) throws SQLException, ClassNotFoundException {
        return supplierDAO.save(new Supplier(dto.getId(), dto.getName(), dto.getContact(), dto.getAddress()));
    }

    @Override
    public boolean update(SupplierDTO dto) throws SQLException, ClassNotFoundException {
        return supplierDAO.update(new Supplier(dto.getId(), dto.getName(), dto.getContact(), dto.getAddress()));
    }

    @Override
    public Supplier searchById(String id) throws SQLException, ClassNotFoundException {
        return supplierDAO.searchById(id);
    }

    @Override
    public boolean delete(String id) throws SQLException, ClassNotFoundException {
        return supplierDAO.delete(id);
    }

    @Override
    public List<String> getIds() throws SQLException, ClassNotFoundException {
        return supplierDAO.getIds();
    }

    @Override
    public String currentId() throws SQLException, ClassNotFoundException {
        return supplierDAO.currentId();
    }
}
