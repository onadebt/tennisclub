package cz.inqool.tennisclub.mapper;

import cz.inqool.tennisclub.domain.model.Customer;
import cz.inqool.tennisclub.dto.customer.CustomerCreateDto;
import cz.inqool.tennisclub.dto.customer.CustomerDto;
import cz.inqool.tennisclub.dto.customer.CustomerUpdateDto;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CustomerMapperTest {

    private final CustomerMapper mapper = new CustomerMapper();

    @Test
    void toEntity_fromUpdateDto_shouldMapCorrectly() {
        CustomerUpdateDto dto = new CustomerUpdateDto();
        dto.setName("Karel");
        dto.setPhoneNumber("123456789");

        Customer customer = mapper.toEntity(dto);

        assertEquals("Karel", customer.getName());
        assertEquals("123456789", customer.getPhoneNumber());
    }

    @Test
    void toEntity_fromCreateDto_shouldMapCorrectly() {
        CustomerCreateDto dto = new CustomerCreateDto();
        dto.setName("Baru");
        dto.setPhoneNumber("987654321");

        Customer customer = mapper.toEntity(dto);

        assertEquals("Baru", customer.getName());
        assertEquals("987654321", customer.getPhoneNumber());
    }

    @Test
    void toDto_shouldMapCorrectly() {
        Customer customer = new Customer();
        customer.setId(5L);
        customer.setName("Martin");
        customer.setPhoneNumber("555000111");

        CustomerDto dto = mapper.toDto(customer);

        assertEquals(5L, dto.getId());
        assertEquals("Martin", dto.getName());
        assertEquals("555000111", dto.getPhoneNumber());
    }
}

