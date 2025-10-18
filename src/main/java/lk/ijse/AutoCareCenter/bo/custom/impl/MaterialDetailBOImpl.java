package lk.ijse.AutoCareCenter.bo.custom.impl;

import lk.ijse.AutoCareCenter.bo.custom.MaterialDetailBO;
import lk.ijse.AutoCareCenter.dao.DAOFactory;
import lk.ijse.AutoCareCenter.dao.custom.MaterialDetailDAO;
import lk.ijse.AutoCareCenter.entity.MaterialDetails;
import lk.ijse.AutoCareCenter.model.MaterialDetailsDTO;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MaterialDetailBOImpl implements MaterialDetailBO {
    MaterialDetailDAO materialDetailDAO = (MaterialDetailDAO) DAOFactory.getDaoFactory().getDAO(DAOFactory.DAOTypes.MATERIALDETAIL);

    @Override
    public ArrayList<MaterialDetailsDTO> loadAll() throws SQLException, ClassNotFoundException {

        ArrayList<MaterialDetails> all = materialDetailDAO.loadAll();
        ArrayList<MaterialDetailsDTO> allMaterialDetails = new ArrayList<>();
        for (MaterialDetails m : all) {
            allMaterialDetails.add(new MaterialDetailsDTO(m.getCode(), m.getSupId(), m.getDescription(), m.getUnitPrice(), m.getQtyOnHand()));
        }
        return allMaterialDetails;
    }

    @Override
    public boolean save(MaterialDetailsDTO materialDetailsDTO) throws SQLException, ClassNotFoundException {
        return materialDetailDAO.save(new MaterialDetails(materialDetailsDTO.getCode(), materialDetailsDTO.getSupId(), materialDetailsDTO.getDescription(), materialDetailsDTO.getUnitPrice(), materialDetailsDTO.getQtyOnHand()));
    }

    @Override
    public boolean update(MaterialDetailsDTO materialDetailsDTO) throws SQLException, ClassNotFoundException {
        return materialDetailDAO.update(new MaterialDetails(materialDetailsDTO.getCode(), materialDetailsDTO.getSupId(), materialDetailsDTO.getDescription(), materialDetailsDTO.getUnitPrice(), materialDetailsDTO.getQtyOnHand()));
    }

    @Override
    public MaterialDetails searchById(String code) throws SQLException, ClassNotFoundException {
        return materialDetailDAO.searchById(code);
    }

}
