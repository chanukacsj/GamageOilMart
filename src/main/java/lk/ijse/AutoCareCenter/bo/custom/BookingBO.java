package lk.ijse.AutoCareCenter.bo.custom;

import lk.ijse.AutoCareCenter.bo.SuperBO;
import lk.ijse.AutoCareCenter.dao.SqlUtil;
import lk.ijse.AutoCareCenter.entity.Booking;
import lk.ijse.AutoCareCenter.model.BookingDTO;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public interface BookingBO extends SuperBO {
    public ArrayList<BookingDTO> loadAllBooking() throws SQLException, ClassNotFoundException;

    public boolean save(BookingDTO bookingDTO) throws SQLException, ClassNotFoundException;

    public boolean delete(String id) throws SQLException, ClassNotFoundException;

    public boolean update(BookingDTO bookingDTO) throws SQLException, ClassNotFoundException;

    public Booking searchById(String id) throws SQLException, ClassNotFoundException;

    public List<String> getIds() throws SQLException, ClassNotFoundException;

    public  String currentId() throws SQLException, ClassNotFoundException;

    public List<String> getBookingIds() throws SQLException, ClassNotFoundException ;
}
