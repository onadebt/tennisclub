package cz.inqool.tennisclub.domain.service;

import cz.inqool.tennisclub.domain.model.Customer;
import cz.inqool.tennisclub.exception.customer.CustomerAlreadyExistsException;
import cz.inqool.tennisclub.exception.customer.CustomerNotFoundException;
import cz.inqool.tennisclub.infrastructure.dao.interfaces.CustomerDao;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CustomerServiceTest {

    @Mock
    private CustomerDao customerDao;

    @InjectMocks
    private CustomerService customerService;

    private Customer existingCustomer;
    private Customer newCustomer;
    private final String PHONE_NUMBER = "+420123456789";
    private final String NAME = "Zdena Podkorenova";
    private final Long ID = 1L;

    @BeforeEach
    void setUp() {
        existingCustomer = new Customer(NAME, PHONE_NUMBER);
        existingCustomer.setId(ID);
        newCustomer = new Customer(NAME, PHONE_NUMBER);
    }

    @Test
    void create_ShouldSaveNewCustomer_WhenPhoneNumberNotExists() {
        when(customerDao.findByPhoneNumber(PHONE_NUMBER)).thenReturn(Optional.empty());
        when(customerDao.save(newCustomer)).thenReturn(existingCustomer);

        Customer result = customerService.create(newCustomer);

        assertEquals(existingCustomer, result);
        verify(customerDao).findByPhoneNumber(PHONE_NUMBER);
        verify(customerDao).save(newCustomer);
    }

    @Test
    void create_ShouldThrowException_WhenPhoneNumberExists() {
        when(customerDao.findByPhoneNumber(PHONE_NUMBER)).thenReturn(Optional.of(existingCustomer));

        assertThrows(CustomerAlreadyExistsException.class, () -> {
            customerService.create(newCustomer);
        });

        verify(customerDao).findByPhoneNumber(PHONE_NUMBER);
        verify(customerDao, never()).save(any());
    }

    @Test
    void update_ShouldSaveCustomer_WhenCustomerExists() {
        when(customerDao.findById(ID)).thenReturn(Optional.of(existingCustomer));
        when(customerDao.save(existingCustomer)).thenReturn(existingCustomer);

        Customer result = customerService.update(existingCustomer);

        assertEquals(existingCustomer, result);
        verify(customerDao).findById(ID);
        verify(customerDao).save(existingCustomer);
    }

    @Test
    void update_ShouldThrowException_WhenCustomerNotFound() {
        when(customerDao.findById(ID)).thenReturn(Optional.empty());

        assertThrows(CustomerNotFoundException.class, () -> {
            customerService.update(existingCustomer);
        });

        verify(customerDao).findById(ID);
        verify(customerDao, never()).save(any());
    }

    @Test
    void findOrCreate_ShouldReturnExistingCustomer_WhenFound() {
        when(customerDao.findByPhoneNumber(PHONE_NUMBER)).thenReturn(Optional.of(existingCustomer));

        Customer result = customerService.findOrCreate(PHONE_NUMBER, NAME);

        assertEquals(existingCustomer, result);
        verify(customerDao).findByPhoneNumber(PHONE_NUMBER);
        verify(customerDao, never()).save(any());
    }

    @Test
    void findOrCreate_ShouldCreateNewCustomer_WhenNotFound() {
        when(customerDao.findByPhoneNumber(PHONE_NUMBER)).thenReturn(Optional.empty());
        when(customerDao.save(any(Customer.class))).thenReturn(existingCustomer);

        Customer result = customerService.findOrCreate(PHONE_NUMBER, NAME);

        assertEquals(existingCustomer, result);
        verify(customerDao).findByPhoneNumber(PHONE_NUMBER);
        verify(customerDao).save(any(Customer.class));
    }

    @Test
    void getAll_ShouldReturnAllCustomers() {
        List<Customer> customers = List.of(existingCustomer);
        when(customerDao.findAll()).thenReturn(customers);

        List<Customer> result = customerService.getAll();

        assertEquals(1, result.size());
        assertEquals(existingCustomer, result.getFirst());
        verify(customerDao).findAll();
    }

    @Test
    void getById_ShouldReturnCustomer_WhenExists() {
        when(customerDao.findById(ID)).thenReturn(Optional.of(existingCustomer));

        Customer result = customerService.getById(ID);

        assertEquals(existingCustomer, result);
        verify(customerDao).findById(ID);
    }

    @Test
    void getById_ShouldThrowException_WhenNotFound() {
        when(customerDao.findById(ID)).thenReturn(Optional.empty());

        assertThrows(CustomerNotFoundException.class, () -> {
            customerService.getById(ID);
        });

        verify(customerDao).findById(ID);
    }

    @Test
    void getByPhoneNumber_ShouldReturnCustomer_WhenExists() {
        when(customerDao.findByPhoneNumber(PHONE_NUMBER)).thenReturn(Optional.of(existingCustomer));

        Customer result = customerService.getByPhoneNumber(PHONE_NUMBER);

        assertEquals(existingCustomer, result);
        verify(customerDao).findByPhoneNumber(PHONE_NUMBER);
    }

    @Test
    void getByPhoneNumber_ShouldThrowException_WhenNotFound() {
        when(customerDao.findByPhoneNumber(PHONE_NUMBER)).thenReturn(Optional.empty());

        assertThrows(CustomerNotFoundException.class, () -> {
            customerService.getByPhoneNumber(PHONE_NUMBER);
        });

        verify(customerDao).findByPhoneNumber(PHONE_NUMBER);
    }

    @Test
    void deleteById_ShouldDeleteCustomer_WhenExists() {
        when(customerDao.findById(ID)).thenReturn(Optional.of(existingCustomer));
        doNothing().when(customerDao).delete(existingCustomer);

        customerService.deleteById(ID);

        verify(customerDao).findById(ID);
        verify(customerDao).delete(existingCustomer);
    }

    @Test
    void deleteById_ShouldThrowException_WhenNotFound() {
        when(customerDao.findById(ID)).thenReturn(Optional.empty());

        assertThrows(CustomerNotFoundException.class, () -> {
            customerService.deleteById(ID);
        });

        verify(customerDao).findById(ID);
        verify(customerDao, never()).delete(any());
    }

    @Test
    void deleteByPhoneNumber_ShouldDeleteCustomer_WhenExists() {
        when(customerDao.findByPhoneNumber(PHONE_NUMBER)).thenReturn(Optional.of(existingCustomer));
        doNothing().when(customerDao).delete(existingCustomer);

        customerService.deleteByPhoneNumber(PHONE_NUMBER);

        verify(customerDao).findByPhoneNumber(PHONE_NUMBER);
        verify(customerDao).delete(existingCustomer);
    }

    @Test
    void deleteByPhoneNumber_ShouldThrowException_WhenNotFound() {
        when(customerDao.findByPhoneNumber(PHONE_NUMBER)).thenReturn(Optional.empty());

        assertThrows(CustomerNotFoundException.class, () -> {
            customerService.deleteByPhoneNumber(PHONE_NUMBER);
        });

        verify(customerDao).findByPhoneNumber(PHONE_NUMBER);
        verify(customerDao, never()).delete(any());
    }
}