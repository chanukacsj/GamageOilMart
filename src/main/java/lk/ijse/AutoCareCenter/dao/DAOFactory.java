package lk.ijse.AutoCareCenter.dao;

import lk.ijse.AutoCareCenter.dao.custom.Impl.*;

import static com.lowagie.text.pdf.PdfName.ORDER;
import static com.lowagie.text.pdf.PdfName.ca;

public class DAOFactory {

    private static DAOFactory daoFactory;

    private DAOFactory() {
    }

    public static DAOFactory getDaoFactory() {
        return (daoFactory == null) ? daoFactory = new DAOFactory() : daoFactory;
    }

    public enum DAOTypes {
        EMPLOYEE, CUSTOMER, VEHICLE, SUPPLIER, SALARY, REPAIR, MATERIAL, ORDER, ORDERDETAILS, MATERIALDETAIL, BOOKING, PAYMENT
    }

    public SuperDAO getDAO(DAOTypes daoTypes) {
        switch (daoTypes) {
            case VEHICLE:
                return new VehicleDAOImpl();
            case SUPPLIER:
                return new SupplierDAOImpl();
            case SALARY:
                return new SalaryDAOImpl();
            case MATERIAL:
                return new MaterialDAOImpl();
            case MATERIALDETAIL:
                return new MaterialDetailsDAOImpl();
            case ORDER:
                return new OrderDAOImpl();
            case ORDERDETAILS:
                return new OrderDetailsDAOImpl();
            case BOOKING:
                return new BookingDAOImpl();
            case EMPLOYEE:
                return new EmployeeDAOImpl();
            case PAYMENT:
                return new PaymentDAOImpl();
            default:
                return null;
        }
    }
}
