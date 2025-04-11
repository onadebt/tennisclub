package cz.inqool.tennisclub.domain.service;

import cz.inqool.tennisclub.domain.model.Customer;
import cz.inqool.tennisclub.exception.customer.CustomerAlreadyExistsException;
import cz.inqool.tennisclub.exception.customer.CustomerNotFoundException;
import cz.inqool.tennisclub.infrastructure.dao.interfaces.CustomerDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerService {

    private final CustomerDao customerDao;

    @Autowired
    public CustomerService(CustomerDao customerDao) {
        this.customerDao = customerDao;
    }

    public Customer create(Customer customer) {
        if (customerDao.findByPhoneNumber(customer.getPhoneNumber()).isPresent()) {
            throw new CustomerAlreadyExistsException(customer.getPhoneNumber());
        }
        return customerDao.save(customer);
    }

    public Customer update(Customer customer) {
        if (customerDao.findById(customer.getId()).isEmpty()) {
            throw new CustomerNotFoundException(customer.getId());
        }
        return customerDao.save(customer);
    }

    public Customer findOrCreate(String phoneNumber, String name) {
        return customerDao.findByPhoneNumber(phoneNumber)
                .orElseGet(() -> customerDao.save(new Customer(name, phoneNumber)));
    }

    public List<Customer> getAll() {
        return customerDao.findAll();
    }

    public Customer getById(Long id) {
        return customerDao.findById(id).orElseThrow(() -> new CustomerNotFoundException(id));
    }

    public Customer getByPhoneNumber(String phoneNumber) {
        return customerDao.findByPhoneNumber(phoneNumber).orElseThrow(() -> new CustomerNotFoundException(phoneNumber));
    }

    public void deleteById(Long id) {
        Customer customer = customerDao.findById(id).orElseThrow(() -> new CustomerNotFoundException(id));
        customerDao.delete(customer);
    }

    public void deleteByPhoneNumber(String phoneNumber) {
        Customer customer = customerDao.findByPhoneNumber(phoneNumber).orElseThrow(() -> new CustomerNotFoundException(phoneNumber));
        customerDao.delete(customer);
    }
}
