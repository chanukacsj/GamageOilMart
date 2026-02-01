package lk.ijse.AutoCareCenter.bo.custom.impl;

import lk.ijse.AutoCareCenter.bo.custom.PurchaseOrderBO;
import lk.ijse.AutoCareCenter.dao.DAOFactory;
import lk.ijse.AutoCareCenter.dao.custom.*;
import lk.ijse.AutoCareCenter.db.DbConnection;
import lk.ijse.AutoCareCenter.entity.MaterialDetails;
import lk.ijse.AutoCareCenter.entity.OrderDetails;
import lk.ijse.AutoCareCenter.entity.Orders;
import lk.ijse.AutoCareCenter.entity.Payment;
import lk.ijse.AutoCareCenter.model.*;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class PurchaseOrderBOImpl implements PurchaseOrderBO {

    OrderDAO orderDAO = (OrderDAO) DAOFactory.getDaoFactory().getDAO(DAOFactory.DAOTypes.ORDER);
    OrderDetailDAO orderDetailDAO = (OrderDetailDAO) DAOFactory.getDaoFactory().getDAO(DAOFactory.DAOTypes.ORDERDETAILS);

    MaterialDetailDAO materialDetailDAO = (MaterialDetailDAO) DAOFactory.getDaoFactory().getDAO(DAOFactory.DAOTypes.MATERIALDETAIL);

    MaterialDAO materialDAO = (MaterialDAO) DAOFactory.getDaoFactory().getDAO(DAOFactory.DAOTypes.MATERIAL);

    PaymentDAO paymentDAO = (PaymentDAO) DAOFactory.getDaoFactory().getDAO(DAOFactory.DAOTypes.PAYMENT);
    String generatedPaymentId = null;

    @Override
    public boolean exist(String orderId) throws SQLException, ClassNotFoundException {

        return orderDAO.exist(orderId);
    }

    @Override
    public String currentId() throws SQLException, ClassNotFoundException {
        return orderDAO.currentId();
    }

    @Override
    public String saveOrder(OrdersDTO dto) throws SQLException, ClassNotFoundException {
        Connection connection = null;
        try {
            connection = DbConnection.getInstance().getConnection();
            boolean b1 = orderDAO.exist(dto.getOrderId());
            /*if order id already exist*/
            if (b1) {
                return null;

            }

            connection.setAutoCommit(false);
            //Save the Order to the order table
            boolean b2 = orderDAO.save(new Orders(dto.getOrderId(), dto.getDate()));


            if (!b2) {

                connection.rollback();
                connection.setAutoCommit(true);
                return null;
            }

            for (OrderDetailsDTO d : dto.getOrderDetails()) {

                OrderDetails orderDetails = new OrderDetails(d.getOrderId(), d.getCode(), d.getQty(), d.getUnitPrice(), d.getService_charge(), d.getTotal(),d.getDiscount());
                boolean b3 = orderDetailDAO.save(orderDetails);
                System.out.println("save");
                if (!b3) {
                    connection.rollback();
                    connection.setAutoCommit(true);
                    return null;
                }
                //Search & Update Item
                MaterialDetailsDTO materialDetails = findItem(d.getCode());
                materialDetails.setQtyOnHand(materialDetails.getQtyOnHand() - d.getQty());

                //update item
                System.out.println("update");
                System.out.println(materialDetails.getWholesalePrice());
                boolean b = materialDetailDAO.update(new MaterialDetails(materialDetails.getCode(), materialDetails.getSupId(), materialDetails.getDescription(), materialDetails.getUnitPrice(), materialDetails.getQtyOnHand(), materialDetails.getCategory(), materialDetails.getBrand(), materialDetails.getAddedDate(), materialDetails.getStatus(),materialDetails.getUnitCost(),materialDetails.getWholesalePrice()));

                if (!b) {
                    connection.rollback();
                    connection.setAutoCommit(true);
                    return null;
                }
                String date = LocalDateTime.now()
                        .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                String paymentId = "P" + System.nanoTime();
                generatedPaymentId = paymentId;
                Payment payment = new Payment(
                        paymentId,
                        dto.getOrderId(),
                        d.getCode(),
                        d.getQty(),
                        d.getUnitPrice(),
                        d.getService_charge(),
                        d.getTotal(),
                        "ORDER PAYMENT",
                        date,
                        d.getDiscount()
                        );

                boolean b4 = paymentDAO.save(payment);
                if (!b4) {
                    connection.rollback();
                    connection.setAutoCommit(true);
                    return null;
                }
            }
            connection.commit();
            connection.setAutoCommit(true);
            return generatedPaymentId;

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return null;
    }


    @Override
    public MaterialDetailsDTO findItem(String code) throws SQLException, ClassNotFoundException {
        try {
            MaterialDetails i = materialDetailDAO.searchById(code);
            return new MaterialDetailsDTO(i.getCode(), i.getSupId(), i.getDescription(), i.getUnitPrice(), i.getQtyOnHand(), i.getCategory(), i.getBrand(), i.getAddedDate(), i.getStatus(),i.getUnitCost(),i.getWholesalePrice());
        } catch (SQLException e) {
            throw new RuntimeException("Failed to find the Material " + code, e);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
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
    public List<String> getIds() throws SQLException, ClassNotFoundException {
        return List.of();
    }

    @Override
    public MaterialDetails searchByMaterialId(String code) throws SQLException, ClassNotFoundException {
        return materialDetailDAO.searchById(code);
    }

    @Override
    public List<String> getCodes() throws SQLException, ClassNotFoundException {
        return materialDetailDAO.getCodes();
    }

    @Override
    public List<String> getBookingIds() throws SQLException, ClassNotFoundException {
        return null;
    }

    @Override
    public ArrayList<Payment> loadAll() throws SQLException, ClassNotFoundException {
        return paymentDAO.loadAll();
    }

    @Override
    public String getDescriptionByCode(String code) throws Exception {
        return materialDetailDAO.getDescriptionByCode(code);
    }


}
