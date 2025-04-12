package cz.inqool.tennisclub.facade;

import cz.inqool.tennisclub.domain.model.Customer;
import cz.inqool.tennisclub.domain.service.CustomerService;
import cz.inqool.tennisclub.dto.customer.CustomerCreateDto;
import cz.inqool.tennisclub.dto.customer.CustomerDto;
import cz.inqool.tennisclub.dto.customer.CustomerUpdateDto;
import cz.inqool.tennisclub.mapper.CustomerMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CustomerFacadeTest {

    @Mock
    private CustomerService customerService;

    @Mock
    private CustomerMapper customerMapper;

    @InjectMocks
    private CustomerFacade customerFacade;

    @Test
    void create_shouldMapAndReturnDto() {
        CustomerCreateDto createDto = new CustomerCreateDto();
        Customer customer = new Customer();
        Customer savedCustomer = new Customer();
        CustomerDto customerDto = new CustomerDto();

        when(customerMapper.toEntity(createDto)).thenReturn(customer);
        when(customerService.create(customer)).thenReturn(savedCustomer);
        when(customerMapper.toDto(savedCustomer)).thenReturn(customerDto);

        CustomerDto result = customerFacade.create(createDto);

        assertEquals(customerDto, result);
        verify(customerMapper).toEntity(createDto);
        verify(customerService).create(customer);
        verify(customerMapper).toDto(savedCustomer);
    }

    @Test
    void update_shouldMapWithIdAndReturnDto() {
        Long id = 1L;
        CustomerUpdateDto updateDto = new CustomerUpdateDto();
        Customer mappedCustomer = new Customer();
        mappedCustomer.setId(id);
        Customer updatedCustomer = new Customer();
        CustomerDto updatedDto = new CustomerDto();

        when(customerMapper.toEntity(updateDto)).thenReturn(mappedCustomer);
        when(customerService.update(mappedCustomer)).thenReturn(updatedCustomer);
        when(customerMapper.toDto(updatedCustomer)).thenReturn(updatedDto);

        CustomerDto result = customerFacade.update(id, updateDto);

        assertEquals(updatedDto, result);
        verify(customerMapper).toEntity(updateDto);
        verify(customerService).update(mappedCustomer);
        verify(customerMapper).toDto(updatedCustomer);
    }

    @Test
    void getByPhoneNumber_shouldReturnDto() {
        String phone = "123456789";
        Customer customer = new Customer();
        CustomerDto dto = new CustomerDto();

        when(customerService.getByPhoneNumber(phone)).thenReturn(customer);
        when(customerMapper.toDto(customer)).thenReturn(dto);

        CustomerDto result = customerFacade.getByPhoneNumber(phone);

        assertEquals(dto, result);
        verify(customerService).getByPhoneNumber(phone);
        verify(customerMapper).toDto(customer);
    }

    @Test
    void getById_shouldReturnDto() {
        Long id = 1L;
        Customer customer = new Customer();
        CustomerDto dto = new CustomerDto();

        when(customerService.getById(id)).thenReturn(customer);
        when(customerMapper.toDto(customer)).thenReturn(dto);

        CustomerDto result = customerFacade.getById(id);

        assertEquals(dto, result);
        verify(customerService).getById(id);
        verify(customerMapper).toDto(customer);
    }

    @Test
    void getAll_shouldReturnListOfDtos() {
        List<Customer> customerList = List.of(new Customer(), new Customer());
        when(customerService.getAll()).thenReturn(customerList);
        when(customerMapper.toDto(any())).thenReturn(new CustomerDto());

        List<CustomerDto> result = customerFacade.getAll();

        assertEquals(2, result.size());
        verify(customerService).getAll();
        verify(customerMapper, times(2)).toDto(any());
    }
}

