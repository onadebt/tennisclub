package cz.inqool.tennisclub.controller;

import cz.inqool.tennisclub.dto.customer.CustomerCreateDto;
import cz.inqool.tennisclub.dto.customer.CustomerDto;
import cz.inqool.tennisclub.facade.CustomerFacade;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CustomerController.class)
class CustomerControllerTest {

    private final String CUSTOMER_JSON = """
            {
                "name": "Zdena Podkorenova",
                "phoneNumber": "+420123456789"
            }
            """;
    @Autowired
    private MockMvc mockMvc;
    @MockitoBean
    private CustomerFacade customerFacade;

    @Test
    void createCustomer_ShouldReturnCreatedCustomer() throws Exception {
        CustomerDto dto = new CustomerDto(1L, "Zdena Podkorenova", "+420123456789");
        given(customerFacade.create(any(CustomerCreateDto.class))).willReturn(dto);

        mockMvc.perform(post("/api/customers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(CUSTOMER_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Zdena Podkorenova"))
                .andExpect(jsonPath("$.phoneNumber").value("+420123456789"));
    }

    @Test
    void getAllCustomers_ShouldReturnAllCustomers() throws Exception {
        List<CustomerDto> customers = List.of(
                new CustomerDto(1L, "Zdena Podkorenova", "+420123456789"),
                new CustomerDto(2L, "Jana Smidova", "+420987654321")
        );
        given(customerFacade.getAll()).willReturn(customers);

        mockMvc.perform(get("/api/customers"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].name").value("Zdena Podkorenova"))
                .andExpect(jsonPath("$[1].name").value("Jana Smidova"));
    }
}
