package cz.inqool.tennisclub.exception.customer;

public class CustomerAlreadyExistsException extends RuntimeException {

    public CustomerAlreadyExistsException(String phoneNumber) {
        super("Customer with phone number " + phoneNumber + " already exists.");
    }

    public CustomerAlreadyExistsException(Long id) {
        super("Customer with id " + id + " already exists.");
    }
}
