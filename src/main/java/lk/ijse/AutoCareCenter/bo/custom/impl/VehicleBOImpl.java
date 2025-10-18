package lk.ijse.AutoCareCenter.bo.custom.impl;

import lk.ijse.AutoCareCenter.bo.custom.VehicleBO;
import lk.ijse.AutoCareCenter.dao.DAOFactory;
import lk.ijse.AutoCareCenter.dao.custom.CustomerDAO;
import lk.ijse.AutoCareCenter.dao.custom.VehicleDAO;
import lk.ijse.AutoCareCenter.entity.Booking;
import lk.ijse.AutoCareCenter.entity.Customer;
import lk.ijse.AutoCareCenter.entity.Vehicle;
import lk.ijse.AutoCareCenter.model.BookingDTO;
import lk.ijse.AutoCareCenter.model.VehicleDTO;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class VehicleBOImpl implements VehicleBO {
    VehicleDAO vehicleDAO = (VehicleDAO) DAOFactory.getDaoFactory().getDAO(DAOFactory.DAOTypes.VEHICLE);
    CustomerDAO customerDAO = (CustomerDAO) DAOFactory.getDaoFactory().getDAO(DAOFactory.DAOTypes.CUSTOMER);
    @Override
    public ArrayList<VehicleDTO> loadAll() throws SQLException, ClassNotFoundException {
        ArrayList<VehicleDTO> allVehicle = new ArrayList<>();
        ArrayList<Vehicle> all = vehicleDAO.loadAll();

        for (Vehicle v : all) {
            allVehicle.add(new VehicleDTO(v.getId(), v.getType(), v.getNumber(), v.getCusId()));
        }
        return allVehicle;
    }

    @Override
    public boolean delete(String id) throws SQLException, ClassNotFoundException {
        return vehicleDAO.delete(id);
    }

    @Override
    public boolean save(VehicleDTO vehicleDTO) throws SQLException, ClassNotFoundException {
        return vehicleDAO.save(new Vehicle(vehicleDTO.getId(), vehicleDTO.getType(), vehicleDTO.getNumber(), vehicleDTO.getCusId()));
    }

    @Override
    public boolean update(VehicleDTO vehicleDTO) throws SQLException, ClassNotFoundException {
        return vehicleDAO.update(new Vehicle(vehicleDTO.getId(), vehicleDTO.getType(), vehicleDTO.getNumber(), vehicleDTO.getCusId()));
    }

    @Override
    public Vehicle searchById(String id) throws SQLException, ClassNotFoundException {
        return vehicleDAO.searchById(id);
    }

    @Override
    public List<String> getIds() throws SQLException, ClassNotFoundException {
        return vehicleDAO.getIds();
    }

    @Override
    public String currentId() throws SQLException, ClassNotFoundException {
        return vehicleDAO.currentId();
    }

    @Override
    public List<String> getCustomerIds() throws SQLException, ClassNotFoundException {
        return customerDAO.getIds();
    }

    @Override
    public Customer searchByCustomerId(String id) throws SQLException, ClassNotFoundException {
        return customerDAO.searchById(id);
    }
}
