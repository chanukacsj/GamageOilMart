package lk.ijse.AutoCareCenter.dao.custom.Impl;

import lk.ijse.AutoCareCenter.dao.SqlUtil;
import lk.ijse.AutoCareCenter.dao.custom.BookingDAO;
import lk.ijse.AutoCareCenter.entity.Booking;
import lk.ijse.AutoCareCenter.model.OrdersDTO;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class BookingDAOImpl implements BookingDAO {

    public ArrayList<Booking> loadAll() throws SQLException, ClassNotFoundException {

        ArrayList<Booking> allBooking = new ArrayList<>();
        ResultSet rst = SqlUtil.execute("SELECT * FROM Booking");

        while (rst.next()) {
            Booking booking = new Booking(rst.getString("bId"), rst.getString("date"), rst.getString("description"), rst.getString("contact"), rst.getString("id"), rst.getString("vId"), rst.getString("time"));
            allBooking.add(booking);
        }
        return allBooking;
    }


    public boolean save(Booking entity) throws SQLException, ClassNotFoundException {
        return SqlUtil.execute("INSERT INTO Booking VALUES(?, ?, ?,?,?,?,?)", entity.getId(), entity.getDate(), entity.getDescription(), entity.getContact(), entity.getCusId(), entity.getVId(), entity.getTime());
    }

    public boolean delete(String id) throws SQLException, ClassNotFoundException {
        return SqlUtil.execute("DELETE FROM Booking WHERE bId=?", id);
    }

    public boolean update(Booking entity) throws SQLException, ClassNotFoundException {
        return SqlUtil.execute("UPDATE Booking SET date = ?, description = ?,contact = ? ,id = ?,vId = ?,time = ? WHERE bId = ?", entity.getDate(), entity.getDescription(), entity.getContact(), entity.getCusId(), entity.getVId(), entity.getTime(), entity.getId());
    }

    public Booking searchById(String id) throws SQLException, ClassNotFoundException {
        ResultSet rst = SqlUtil.execute("SELECT * FROM Booking WHERE bId = ?", id);
        rst.next();
        return new Booking(id + "", rst.getString("date"), rst.getString("description"), rst.getString("contact"), rst.getString("id"), rst.getString("vId"), rst.getString("time"));
    }

    public List<String> getIds() throws SQLException, ClassNotFoundException {

        ResultSet rst = SqlUtil.execute("SELECT bId FROM Booking");
        List<String> ids = new ArrayList<>();
        while (rst.next()) {
            ids.add(rst.getString(1));
        }
        return ids;
    }

    @Override
    public int getMaterialCount() throws SQLException, ClassNotFoundException {
        return 0;
    }

    @Override
    public String currentId() throws SQLException, ClassNotFoundException {
        ResultSet rst = SqlUtil.execute("SELECT bId FROM booking ORDER BY bId desc LIMIT 1");

        if(rst.next()) {
            return rst.getString(1);
        }
        return null;
    }

    public int getBookingCount() throws SQLException, ClassNotFoundException, SQLException {
        ResultSet resultSet = SqlUtil.execute("SELECT COUNT(*) AS booking_count FROM booking");

        if (resultSet.next()) {
            return resultSet.getInt("booking_count");
        }
        return 0;
    }
}
