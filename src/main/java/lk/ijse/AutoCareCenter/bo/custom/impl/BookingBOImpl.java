package lk.ijse.AutoCareCenter.bo.custom.impl;

import lk.ijse.AutoCareCenter.bo.custom.BookingBO;
import lk.ijse.AutoCareCenter.dao.DAOFactory;
import lk.ijse.AutoCareCenter.dao.SqlUtil;
import lk.ijse.AutoCareCenter.dao.custom.BookingDAO;
import lk.ijse.AutoCareCenter.dao.custom.CustomerDAO;
import lk.ijse.AutoCareCenter.dao.custom.VehicleDAO;
import lk.ijse.AutoCareCenter.entity.Booking;
import lk.ijse.AutoCareCenter.entity.Customer;
import lk.ijse.AutoCareCenter.model.BookingDTO;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class BookingBOImpl implements BookingBO {

    BookingDAO bookingDAO = (BookingDAO) DAOFactory.getDaoFactory().getDAO(DAOFactory.DAOTypes.BOOKING);
    CustomerDAO customerDAO = (CustomerDAO) DAOFactory.getDaoFactory().getDAO(DAOFactory.DAOTypes.CUSTOMER);
    VehicleDAO vehicleDAO = (VehicleDAO) DAOFactory.getDaoFactory().getDAO(DAOFactory.DAOTypes.VEHICLE);
    public ArrayList<BookingDTO> loadAllBooking() throws SQLException, ClassNotFoundException {

        ArrayList<BookingDTO> allBooking = new ArrayList<>();
        ArrayList<Booking> all = bookingDAO.loadAll();

        for (Booking b : all) {
            allBooking.add(new BookingDTO(b.getId(), b.getDate(), b.getDescription(), b.getContact(), b.getCusId(), b.getVId(), b.getTime()));
        }
        return allBooking;
    }


    public boolean save(BookingDTO dto) throws SQLException, ClassNotFoundException {
        return bookingDAO.save(new Booking(dto.getId(), dto.getDate(), dto.getDescription(), dto.getContact(), dto.getCusId(), dto.getVId(), dto.getTime()));
    }

    public boolean delete(String id) throws SQLException, ClassNotFoundException {
        return bookingDAO.delete(id);
    }

    public boolean update(BookingDTO dto) throws SQLException, ClassNotFoundException {
        return bookingDAO.update(new Booking(dto.getId(), dto.getDate(), dto.getDescription(), dto.getContact(), dto.getCusId(), dto.getVId(), dto.getTime()));
    }

    public Booking searchById(String id) throws SQLException, ClassNotFoundException {
        return bookingDAO.searchById(id);
    }

    public List<String> getIds() throws SQLException, ClassNotFoundException {

        return bookingDAO.getIds();
    }

    @Override
    public String currentId() throws SQLException, ClassNotFoundException {
        return bookingDAO.currentId();
    }

    @Override
    public Customer searchByCusId(String id) throws SQLException, ClassNotFoundException {
        return customerDAO.searchById(id);
    }

    @Override
    public List<String> getCusIds() throws SQLException, ClassNotFoundException {
        return customerDAO.getIds();
    }

    @Override
    public List<String> getBookingIds() throws SQLException, ClassNotFoundException {
        return vehicleDAO.getIds();
    }

}
