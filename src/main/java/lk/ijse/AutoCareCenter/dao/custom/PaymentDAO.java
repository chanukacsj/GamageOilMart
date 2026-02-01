package lk.ijse.AutoCareCenter.dao.custom;

import lk.ijse.AutoCareCenter.dao.CurdDAO;
import lk.ijse.AutoCareCenter.entity.Payment;

public interface PaymentDAO extends CurdDAO<Payment> {
    double getDailyProfit() throws Exception;
    double getMonthlyProfit() throws Exception;
    double getYearlyProfit() throws Exception;
}
