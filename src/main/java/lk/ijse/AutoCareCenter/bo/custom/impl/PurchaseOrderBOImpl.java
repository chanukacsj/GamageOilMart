package lk.ijse.AutoCareCenter.bo.custom.impl;

import lk.ijse.AutoCareCenter.bo.custom.PurchaseOrderBO;
import lk.ijse.AutoCareCenter.dao.DAOFactory;
import lk.ijse.AutoCareCenter.dao.custom.*;
import lk.ijse.AutoCareCenter.db.DbConnection;
import lk.ijse.AutoCareCenter.entity.Customer;
import lk.ijse.AutoCareCenter.entity.MaterialDetails;
import lk.ijse.AutoCareCenter.entity.OrderDetails;
import lk.ijse.AutoCareCenter.entity.Orders;
import lk.ijse.AutoCareCenter.model.*;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class PurchaseOrderBOImpl implements PurchaseOrderBO {

    OrderDAO orderDAO = (OrderDAO) DAOFactory.getDaoFactory().getDAO(DAOFactory.DAOTypes.ORDER);
    OrderDetailDAO orderDetailDAO = (OrderDetailDAO) DAOFactory.getDaoFactory().getDAO(DAOFactory.DAOTypes.ORDERDETAILS);

    MaterialDetailDAO materialDetailDAO = (MaterialDetailDAO) DAOFactory.getDaoFactory().getDAO(DAOFactory.DAOTypes.MATERIALDETAIL);
    CustomerDAO customerDAO = (CustomerDAO) DAOFactory.getDaoFactory().getDAO(DAOFactory.DAOTypes.CUSTOMER);

    MaterialDAO materialDAO = (MaterialDAO) DAOFactory.getDaoFactory().getDAO(DAOFactory.DAOTypes.MATERIAL);

    BookingDAO bookingDAO = (BookingDAO) DAOFactory.getDaoFactory().getDAO(DAOFactory.DAOTypes.BOOKING);


    @Override
    public boolean exist(String orderId) throws SQLException, ClassNotFoundException {

        return orderDAO.exist(orderId);
    }

    @Override
    public String currentId() throws SQLException, ClassNotFoundException {
        return orderDAO.currentId();
    }

    @Override
    public boolean saveOrder(OrdersDTO dto) throws SQLException, ClassNotFoundException {
        Connection connection = null;
        try {
            connection = DbConnection.getInstance().getConnection();
            boolean b1 = orderDAO.exist(dto.getOrderId());
            System.out.println("awa1");

            /*if order id already exist*/
            if (b1) {
                return false;

            }

            connection.setAutoCommit(false);
            //Save the Order to the order table
            boolean b2 = orderDAO.save(new Orders(dto.getOrderId(), dto.getCusId(), dto.getDate(), dto.getBId()));


            if (!b2) {

                connection.rollback();
                connection.setAutoCommit(true);
                return false;
            }

            for (OrderDetailsDTO d : dto.getOrderDetails()) {

                OrderDetails orderDetails = new OrderDetails(d.getOrderId(), d.getCode(), d.getQty(), d.getUnitPrice(), d.getService_charge(), d.getTotal());
                boolean b3 = orderDetailDAO.save(orderDetails);
                System.out.println("save");
                if (!b3) {
                    connection.rollback();
                    connection.setAutoCommit(true);
                    return false;
                }
                //Search & Update Item
                MaterialDetailsDTO materialDetails = findItem(d.getCode());
                materialDetails.setQtyOnHand(materialDetails.getQtyOnHand() - d.getQty());

                //update item
                boolean b = materialDetailDAO.update(new MaterialDetails(materialDetails.getCode(), materialDetails.getSupId(), materialDetails.getDescription(), materialDetails.getUnitPrice(), materialDetails.getQtyOnHand()));

                if (!b) {
                    connection.rollback();
                    connection.setAutoCommit(true);
                    return false;
                }
            }
            connection.commit();
            connection.setAutoCommit(true);
            return true;

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return false;
    }


    @Override
    public MaterialDetailsDTO findItem(String code) throws SQLException, ClassNotFoundException {
        try {
            MaterialDetails i = materialDetailDAO.searchById(code);
            return new MaterialDetailsDTO(i.getCode(), i.getSupId(), i.getDescription(), i.getUnitPrice(), i.getQtyOnHand());
        } catch (SQLException e) {
            throw new RuntimeException("Failed to find the Material " + code, e);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<String> getCustomerIds() throws SQLException, ClassNotFoundException {
        return null;
    }

    @Override
    public List<String> getMaterialsIds() throws SQLException, ClassNotFoundException {
        return null;
    }

    @Override
    public List<String> getBooking() throws SQLException, ClassNotFoundException {
        return null;
    }

    @Override
    public Customer searchById(String id) throws SQLException, ClassNotFoundException {
        return customerDAO.searchById(id);
    }

    @Override
    public List<String> getIds() throws SQLException, ClassNotFoundException {
        return customerDAO.getIds();
    }

    @Override
    public MaterialDetails searchByMaterialId(String code) throws SQLException, ClassNotFoundException {
        return materialDetailDAO.searchById(code);
    }

    @Override
    public List<String> getCodes() throws SQLException, ClassNotFoundException {
        return materialDAO.getCodes();
    }

    @Override
    public List<String> getBookingIds() throws SQLException, ClassNotFoundException {
        return bookingDAO.getIds();
    }

}
