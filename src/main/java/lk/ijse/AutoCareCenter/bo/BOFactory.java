package lk.ijse.AutoCareCenter.bo;

import lk.ijse.AutoCareCenter.bo.custom.impl.*;

public class BOFactory {
    private static BOFactory boFactory;

    private BOFactory() {

    }

    public static BOFactory getBoFactory() {
        return (boFactory == null) ? boFactory = new BOFactory() : boFactory;
    }

    public enum BOTypes {
        VEHICLE, BOOKING, EMPLOYEE, SUPPLIER, MATERIAL, MATERIALDETAILS, PO, SALARY, REPAIR, PAYMENT, LOAN, CHEQUE
    }

    public SuperBO getBO(BOTypes types) {
        switch (types) {
            case VEHICLE:
                return new VehicleBOImpl();
            case BOOKING:
                return new BookingBOImpl();
            case EMPLOYEE:
                return new EmployeeBOImpl();
            case SUPPLIER:
                return new SupplierBOImpl();
            case MATERIAL:
                return new MaterialBOImpl();
            case PO:
                return new PurchaseOrderBOImpl();
            case SALARY:
                return new SalaryBOImpl();
            case MATERIALDETAILS:
                return new MaterialDetailBOImpl();
            case PAYMENT:
                return new PaymentBOImpl();
            case LOAN:
                return new LoanBOImpl();
            case CHEQUE:
                return new ChequeBOImpl();
            default:
                return null;
        }
    }

}
