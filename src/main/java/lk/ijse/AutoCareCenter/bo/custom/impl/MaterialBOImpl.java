package lk.ijse.AutoCareCenter.bo.custom.impl;

import lk.ijse.AutoCareCenter.bo.custom.MaterialBO;
import lk.ijse.AutoCareCenter.dao.DAOFactory;
import lk.ijse.AutoCareCenter.dao.custom.MaterialDAO;
import lk.ijse.AutoCareCenter.dao.custom.SupplierDAO;
import lk.ijse.AutoCareCenter.entity.Materials;
import lk.ijse.AutoCareCenter.entity.Supplier;
import lk.ijse.AutoCareCenter.model.MaterialsDTO;
import lk.ijse.AutoCareCenter.model.SupplierDTO;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MaterialBOImpl implements MaterialBO {
    MaterialDAO materialDAO = (MaterialDAO) DAOFactory.getDaoFactory().getDAO(DAOFactory.DAOTypes.MATERIAL);
    SupplierDAO supplierDAO = (SupplierDAO) DAOFactory.getDaoFactory().getDAO(DAOFactory.DAOTypes.SUPPLIER);
    @Override
    public boolean save(MaterialsDTO materialsDTO) throws Exception {
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

    @Override
    public List<SupplierDTO> getAllSuppliers() throws SQLException, ClassNotFoundException {
        List<Supplier> suppliers = supplierDAO.loadAll();
        List<SupplierDTO> dtoList = new ArrayList<>();

        for (Supplier s : suppliers) {
            dtoList.add(new SupplierDTO(s.getId(), s.getName()));
        }
        return dtoList;
    }

}
