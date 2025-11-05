package lk.ijse.AutoCareCenter.dao.custom;

import lk.ijse.AutoCareCenter.dao.CurdDAO;
import lk.ijse.AutoCareCenter.entity.MaterialDetails;
import lk.ijse.AutoCareCenter.model.MaterialDetailsDTO;

import java.util.ArrayList;

public interface MaterialDetailDAO extends CurdDAO<MaterialDetails> {
    ArrayList<MaterialDetailsDTO> loadAllByCategory(String category) ;
}
