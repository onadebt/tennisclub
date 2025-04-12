package cz.inqool.tennisclub.mapper;

import cz.inqool.tennisclub.domain.model.Customer;
import cz.inqool.tennisclub.dto.customer.CustomerCreateDto;
import cz.inqool.tennisclub.dto.customer.CustomerDto;
import cz.inqool.tennisclub.dto.customer.CustomerUpdateDto;
import org.springframework.stereotype.Component;

@Component
public class CustomerMapper {

    public Customer toEntity(CustomerUpdateDto updateDto) {
        Customer customer = new Customer();
        customer.setName(updateDto.getName());
        customer.setPhoneNumber(updateDto.getPhoneNumber());
        return customer;
    }

    public Customer toEntity(CustomerCreateDto createDto) {
        Customer customer = new Customer();
        customer.setName(createDto.getName());
        customer.setPhoneNumber(createDto.getPhoneNumber());
        return customer;
    }

    public CustomerDto toDto(Customer customer) {
        CustomerDto customerDto = new CustomerDto();
        customerDto.setId(customer.getId());
        customerDto.setName(customer.getName());
        customerDto.setPhoneNumber(customer.getPhoneNumber());
        return customerDto;
    }
}
