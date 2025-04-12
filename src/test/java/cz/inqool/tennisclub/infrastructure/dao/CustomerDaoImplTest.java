package cz.inqool.tennisclub.infrastructure.dao;

import cz.inqool.tennisclub.domain.model.Customer;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;


import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
class CustomerDaoImplTest {

    @Autowired
    private TestEntityManager testEntityManager;

    @Autowired
    private EntityManager entityManager;

    private CustomerDaoImpl customerDao;

    @BeforeEach
    void setUp() {
        customerDao = new CustomerDaoImpl();
        customerDao.setEntityManager(entityManager);
    }

    @Test
    void findByPhoneNumber_ShouldReturnCustomer_WhenExists() {
        // Arrange
        Customer customer = new Customer("Zdena Podkorenova", "+420123456789");
        testEntityManager.persist(customer);

        // Act
        Optional<Customer> result = customerDao.findByPhoneNumber("+420123456789");

        // Assert
        assertTrue(result.isPresent());
        assertEquals("Zdena Podkorenova", result.get().getName());
        assertEquals("+420123456789", result.get().getPhoneNumber());
    }

    @Test
    void findByPhoneNumber_ShouldReturnEmptyOptional_WhenNotExists() {
        // Act
        Optional<Customer> result = customerDao.findByPhoneNumber("+420987654321");

        // Assert
        assertFalse(result.isPresent());
    }

    @Test
    void findByPhoneNumber_ShouldNotReturnDeletedCustomer() {
        // Arrange
        Customer customer = new Customer("Deleted User", "+420987654321");
        customer.setDeleted(true);
        testEntityManager.persist(customer);

        // Act
        Optional<Customer> result = customerDao.findByPhoneNumber("+420987654321");

        // Assert
        assertFalse(result.isPresent());
    }

    @Test
    void findById_ShouldReturnCustomer_WhenExists() {
        // Arrange
        Customer customer = new Customer("Java Macova", "+420111222333");
        Customer savedCustomer = testEntityManager.persist(customer);

        // Act
        Optional<Customer> result = customerDao.findById(savedCustomer.getId());

        // Assert
        assertTrue(result.isPresent());
        assertEquals("Java Macova", result.get().getName());
        assertEquals("+420111222333", result.get().getPhoneNumber());
    }

    @Test
    void findById_ShouldReturnEmptyOptional_WhenNotExists() {
        // Act
        Optional<Customer> result = customerDao.findById(999L);

        // Assert
        assertFalse(result.isPresent());
    }

    @Test
    void findAll_ShouldReturnAllNonDeletedCustomers() {
        // Arrange
        Customer customer1 = new Customer("Customer 1", "+420111111111");
        Customer customer2 = new Customer("Customer 2", "+420222222222");
        Customer deletedCustomer = new Customer("Deleted Customer", "+420333333333");
        deletedCustomer.setDeleted(true);

        testEntityManager.persist(customer1);
        testEntityManager.persist(customer2);
        testEntityManager.persist(deletedCustomer);

        // Act
        List<Customer> result = customerDao.findAll();

        // Assert
        assertEquals(2, result.size());
        assertTrue(result.stream().anyMatch(c -> c.getPhoneNumber().equals("+420111111111")));
        assertTrue(result.stream().anyMatch(c -> c.getPhoneNumber().equals("+420222222222")));
        assertFalse(result.stream().anyMatch(c -> c.getPhoneNumber().equals("+420333333333")));
    }

    @Test
    void save_ShouldPersistNewCustomer() {
        // Arrange
        Customer newCustomer = new Customer("New Customer", "+420444444444");

        // Act
        Customer savedCustomer = customerDao.save(newCustomer);

        // Assert
        assertNotNull(savedCustomer.getId());
        Customer foundCustomer = testEntityManager.find(Customer.class, savedCustomer.getId());
        assertEquals("New Customer", foundCustomer.getName());
        assertEquals("+420444444444", foundCustomer.getPhoneNumber());
    }

    @Test
    void save_ShouldUpdateExistingCustomer() {
        // Arrange
        Customer originalCustomer = new Customer("Original Name", "+420555555555");
        Customer savedCustomer = testEntityManager.persist(originalCustomer);

        savedCustomer.setName("Updated Name");
        savedCustomer.setPhoneNumber("+420666666666");

        // Act
        Customer updatedCustomer = customerDao.save(savedCustomer);

        // Assert
        assertEquals(savedCustomer.getId(), updatedCustomer.getId());
        Customer foundCustomer = testEntityManager.find(Customer.class, savedCustomer.getId());
        assertEquals("Updated Name", foundCustomer.getName());
        assertEquals("+420666666666", foundCustomer.getPhoneNumber());
    }

    @Test
    void delete_ShouldSoftDeleteCustomer() {
        // Arrange
        Customer customer = new Customer("To Delete", "+420777777777");
        Customer savedCustomer = testEntityManager.persist(customer);

        // Act
        customerDao.delete(savedCustomer);

        // Assert
        Customer foundCustomer = testEntityManager.find(Customer.class, savedCustomer.getId());
        assertTrue(foundCustomer.isDeleted());
    }

    @Test
    void delete_ShouldDoNothing_WhenCustomerIsNull() {
        // Act & Assert (should not throw exception)
        customerDao.delete(null);
    }
}
