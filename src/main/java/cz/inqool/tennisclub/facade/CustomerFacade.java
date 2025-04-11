package cz.inqool.tennisclub.facade;

import cz.inqool.tennisclub.domain.model.Customer;
import cz.inqool.tennisclub.domain.service.CustomerService;
import cz.inqool.tennisclub.dto.customer.CustomerCreateDto;
import cz.inqool.tennisclub.dto.customer.CustomerDto;
import cz.inqool.tennisclub.dto.customer.CustomerUpdateDto;
import cz.inqool.tennisclub.mapper.CustomerMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CustomerFacade {

    private final CustomerService customerService;
    private final CustomerMapper customerMapper;

    @Autowired
    public CustomerFacade(CustomerService customerService, CustomerMapper customerMapper) {
        this.customerService = customerService;
        this.customerMapper = customerMapper;
    }

    public CustomerDto create(CustomerCreateDto createDto) {
        Customer customer = customerMapper.toEntity(createDto);
        Customer savedCustomer = customerService.create(customer);
        return customerMapper.toDto(savedCustomer);
    }

    public CustomerDto update(Long id, CustomerUpdateDto updateDto) {
        Customer customer = customerMapper.toEntity(updateDto);
        customer.setId(id);
        Customer updatedCustomer = customerService.update(customer);
        return customerMapper.toDto(updatedCustomer);
    }

    public CustomerDto getByPhoneNumber(String phoneNumber) {
        Customer customer = customerService.getByPhoneNumber(phoneNumber);
        return customerMapper.toDto(customer);
    }

    public CustomerDto getById(Long id) {
        Customer customer = customerService.getById(id);
        return customerMapper.toDto(customer);
    }

    public List<CustomerDto> getAll() {
        List<Customer> customers = customerService.getAll();
        return customers.stream()
                .map(customerMapper::toDto)
                .collect(Collectors.toList());
    }
}
