package lk.ijse.AutoCareCenter.dao;
import lk.ijse.AutoCareCenter.dao.custom.Impl.*;
public class DAOFactory {

    private static DAOFactory daoFactory;

    private DAOFactory() {
    }

    public static DAOFactory getDaoFactory() {
        return (daoFactory == null) ? daoFactory = new DAOFactory() : daoFactory;
    }

    public enum DAOTypes {
        EMPLOYEE, CUSTOMER, VEHICLE, SUPPLIER, SALARY, REPAIR, MATERIAL, ORDER, ORDERDETAILS, MATERIALDETAIL, BOOKING, LOAN, LOANPAYMENT, PAYMENT
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
            case LOAN:
                return new LoanDAOImpl();
                case LOANPAYMENT:
                return new LoanPaymentDAOImpl();
            default:
                return null;
        }
    }
}
