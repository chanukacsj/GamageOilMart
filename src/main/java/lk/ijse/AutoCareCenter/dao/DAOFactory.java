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
         SUPPLIER, MATERIAL, ORDER, ORDERDETAILS, MATERIALDETAIL, LOAN, LOANPAYMENT, PAYMENT, CHEQUE, LOANITEM
    }

    public SuperDAO getDAO(DAOTypes daoTypes) {
        switch (daoTypes) {
            case SUPPLIER:
                return new SupplierDAOImpl();
            case MATERIAL:
                return new MaterialDAOImpl();
            case MATERIALDETAIL:
                return new MaterialDetailsDAOImpl();
            case ORDER:
                return new OrderDAOImpl();
            case ORDERDETAILS:
                return new OrderDetailsDAOImpl();
            case PAYMENT:
                return new PaymentDAOImpl();
            case LOAN:
                return new LoanDAOImpl();
            case LOANPAYMENT:
                return new LoanPaymentDAOImpl();
            case CHEQUE:
                return new ChequeDAOImpl();
            case LOANITEM:
                return new LoanItemDAOImpl();
            default:
                return null;
        }
    }
}
