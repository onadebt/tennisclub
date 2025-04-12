package cz.inqool.tennisclub.exception.customer;

public class CustomerNotFoundException extends RuntimeException {

    public CustomerNotFoundException(Long id) {
        super("Customer with ID " + id + " not found");
    }

    public CustomerNotFoundException(String phoneNumber) {
        super("Customer with phone number " + phoneNumber + " not found");
    }
}
