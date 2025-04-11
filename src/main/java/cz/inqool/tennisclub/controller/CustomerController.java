package cz.inqool.tennisclub.controller;

import cz.inqool.tennisclub.dto.customer.CustomerCreateDto;
import cz.inqool.tennisclub.dto.customer.CustomerDto;
import cz.inqool.tennisclub.facade.CustomerFacade;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/customers")
public class CustomerController {

    private final CustomerFacade customerFacade;

    @Autowired
    public CustomerController(CustomerFacade customerFacade) {
        this.customerFacade = customerFacade;
    }

    @PostMapping
    public ResponseEntity<CustomerDto> createCustomer(@RequestBody @Valid CustomerCreateDto customerDto) {
        CustomerDto createdCustomer = customerFacade.create(customerDto);
        return ResponseEntity.ok(createdCustomer);
    }

    @GetMapping
    public ResponseEntity<List<CustomerDto>> getAllCustomers() {
        List<CustomerDto> customers = customerFacade.getAll();
        return ResponseEntity.ok(customers);
    }
}
