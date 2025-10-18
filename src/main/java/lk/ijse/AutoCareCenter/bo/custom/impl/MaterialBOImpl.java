package lk.ijse.AutoCareCenter.bo.custom.impl;

import lk.ijse.AutoCareCenter.bo.custom.MaterialBO;
import lk.ijse.AutoCareCenter.dao.DAOFactory;
import lk.ijse.AutoCareCenter.dao.custom.MaterialDAO;
import lk.ijse.AutoCareCenter.dao.custom.SupplierDAO;
import lk.ijse.AutoCareCenter.entity.Materials;
import lk.ijse.AutoCareCenter.model.MaterialsDTO;

import java.sql.SQLException;
import java.util.List;

public class MaterialBOImpl implements MaterialBO {
    MaterialDAO materialDAO = (MaterialDAO) DAOFactory.getDaoFactory().getDAO(DAOFactory.DAOTypes.MATERIAL);
    SupplierDAO supplierDAO = (SupplierDAO) DAOFactory.getDaoFactory().getDAO(DAOFactory.DAOTypes.SUPPLIER);
    @Override
    public boolean save(MaterialsDTO materialsDTO) throws SQLException, ClassNotFoundException {
        return materialDAO.save(new Materials(materialsDTO.getCode()));
    }

    @Override
    public boolean delete(String code) throws SQLException, ClassNotFoundException {
        return materialDAO.delete(code);
    }

    @Override
    public List<String> getCodes() throws SQLException, ClassNotFoundException {
        return materialDAO.getCodes();
    }

    @Override
    public int getMaterialCount() throws SQLException, ClassNotFoundException {
        return materialDAO.getMaterialCount();
    }
    @Override
    public String currentId() throws SQLException, ClassNotFoundException {
        return materialDAO.currentId();
    }

    @Override
    public List<String> getSupplierIds() throws SQLException, ClassNotFoundException {
        return supplierDAO.getIds();
    }
}
