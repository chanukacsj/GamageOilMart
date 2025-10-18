package lk.ijse.AutoCareCenter.bo.custom.impl;

import lk.ijse.AutoCareCenter.bo.custom.CustomerBO;
import lk.ijse.AutoCareCenter.dao.DAOFactory;
import lk.ijse.AutoCareCenter.dao.custom.CustomerDAO;
import lk.ijse.AutoCareCenter.entity.Customer;
import lk.ijse.AutoCareCenter.model.CustomerDTO;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CustomerBOImpl implements CustomerBO {

    CustomerDAO customerDAO = (CustomerDAO) DAOFactory.getDaoFactory().getDAO(DAOFactory.DAOTypes.CUSTOMER);


    @Override
    public String currentId() throws SQLException, ClassNotFoundException {
        return customerDAO.currentId();
    }

    @Override
    public ArrayList<CustomerDTO> loadAll() throws SQLException, ClassNotFoundException {

        ArrayList<CustomerDTO> allCustomer = new ArrayList<>();
        List<Customer> all = customerDAO.loadAll();
        for (Customer c : all) {
            allCustomer.add(new CustomerDTO(c.getId(), c.getName(), c.getAddress(), c.getContact()));
        }
        return allCustomer;
    }

    @Override
    public boolean save(CustomerDTO dto) throws SQLException, ClassNotFoundException {
        return customerDAO.save(new Customer(dto.getId(), dto.getName(), dto.getAddress(), dto.getContact()));
    }

    @Override
    public boolean update(CustomerDTO dto) throws SQLException, ClassNotFoundException {
        return customerDAO.update(new Customer(dto.getId(), dto.getName(), dto.getAddress(), dto.getContact()));
    }

    @Override
    public boolean delete(String id) throws SQLException, ClassNotFoundException {
        return customerDAO.delete(id);
    }

    @Override
    public Customer searchById(String id) throws SQLException, ClassNotFoundException {
        return customerDAO.searchById(id);
    }

    @Override
    public List<String> getIds() throws SQLException, ClassNotFoundException {
        return customerDAO.getIds();
    }
}