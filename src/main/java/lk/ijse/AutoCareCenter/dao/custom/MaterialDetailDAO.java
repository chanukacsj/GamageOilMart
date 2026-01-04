package lk.ijse.AutoCareCenter.dao.custom;

import lk.ijse.AutoCareCenter.dao.CurdDAO;
import lk.ijse.AutoCareCenter.entity.MaterialDetails;
import lk.ijse.AutoCareCenter.model.MaterialDetailsDTO;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public interface MaterialDetailDAO extends CurdDAO<MaterialDetails> {
    ArrayList<MaterialDetailsDTO> loadAllByCategory(String category) ;

    List<String> getCodes() throws SQLException, ClassNotFoundException;

    public String getDescriptionByCode(String code) throws Exception ;

    MaterialDetailsDTO findByBarcode(String barcode);
}
