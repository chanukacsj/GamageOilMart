package lk.ijse.AutoCareCenter.bo.custom.impl;

import lk.ijse.AutoCareCenter.bo.custom.PaymentBO;
import lk.ijse.AutoCareCenter.dao.DAOFactory;
import lk.ijse.AutoCareCenter.dao.custom.MaterialDetailDAO;
import lk.ijse.AutoCareCenter.dao.custom.PaymentDAO;
import lk.ijse.AutoCareCenter.entity.MaterialDetails;
import lk.ijse.AutoCareCenter.entity.Payment;
import lk.ijse.AutoCareCenter.model.MaterialDetailsDTO;
import lk.ijse.AutoCareCenter.model.PaymentDTO;

import java.sql.SQLException;
import java.util.ArrayList;

public class PaymentBOImpl implements PaymentBO {
    PaymentDAO paymentDAO = (PaymentDAO) DAOFactory.getDaoFactory().getDAO(DAOFactory.DAOTypes.PAYMENT);
    @Override
    public ArrayList<PaymentDTO> loadAll() throws SQLException, ClassNotFoundException {
        ArrayList<Payment> all = paymentDAO.loadAll();
        ArrayList<PaymentDTO> allPayment = new ArrayList<>();
        for (Payment m : all) {
            allPayment.add(new PaymentDTO(m.getId(), m.getOrderId(), m.getCode(), m.getQty(), m.getUnitPrice(), m.getService_charge(), m.getTotal(), m.getDescription(), m.getDate(), m.getDiscount()));
        }
        return allPayment;
    }
}
