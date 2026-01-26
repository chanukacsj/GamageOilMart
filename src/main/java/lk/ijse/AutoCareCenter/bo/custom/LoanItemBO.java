package lk.ijse.AutoCareCenter.bo.custom;

import lk.ijse.AutoCareCenter.bo.SuperBO;
import lk.ijse.AutoCareCenter.model.LoanItemDTO;

public interface LoanItemBO extends SuperBO {
    boolean save(LoanItemDTO loan) throws Exception;
}
