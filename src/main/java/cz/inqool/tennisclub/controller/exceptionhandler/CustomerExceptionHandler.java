package cz.inqool.tennisclub.controller.exceptionhandler;

import cz.inqool.tennisclub.exception.customer.CustomerAlreadyExistsException;
import cz.inqool.tennisclub.exception.customer.CustomerNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class CustomerExceptionHandler {

    @ExceptionHandler(CustomerNotFoundException.class)
    public ProblemDetail handleCustomerNotFoundException(CustomerNotFoundException ex) {
        ProblemDetail pd = ProblemDetail.forStatus(HttpStatus.NOT_FOUND);
        pd.setTitle("Customer Not Found Error");
        pd.setProperty("errorCode", "CUSTOMER_NOT_FOUND");
        pd.setDetail(ex.getMessage());
        return pd;
    }

    @ExceptionHandler(CustomerAlreadyExistsException.class)
    public ProblemDetail handleCustomerAlreadyExistsException(CustomerAlreadyExistsException ex) {
        ProblemDetail pd = ProblemDetail.forStatus(HttpStatus.CONFLICT);
        pd.setTitle("Customer Already Exists Error");
        pd.setProperty("errorCode", "CUSTOMER_ALREADY_EXISTS");
        pd.setDetail(ex.getMessage());
        return pd;
    }
}
