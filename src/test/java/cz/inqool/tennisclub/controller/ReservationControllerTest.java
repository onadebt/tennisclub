package cz.inqool.tennisclub.controller;

import cz.inqool.tennisclub.domain.model.GameType;
import cz.inqool.tennisclub.dto.reservation.ReservationCreateDto;
import cz.inqool.tennisclub.dto.reservation.ReservationDto;
import cz.inqool.tennisclub.dto.reservation.ReservationResponseDto;
import cz.inqool.tennisclub.dto.reservation.ReservationUpdateDto;
import cz.inqool.tennisclub.facade.ReservationFacade;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ReservationController.class)
class ReservationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ReservationFacade reservationFacade;

    private final String RESERVATION_CREATE_JSON = """
        {
            "courtId": 1,
            "customerPhone": "+420123456789",
            "customerName": "John Doe",
            "courtName": "Court 1",
            "gameType": "SINGLES",
            "startTime": "01-06-2023 10:00",
            "endTime": "01-06-2023 11:00"
        }
        """;

    private final String RESERVATION_UPDATE_JSON = """
        {
            "startTime": "01-06-2023 11:00",
            "endTime": "01-06-2023 12:00",
            "gameType": "DOUBLES"
        }
        """;

    private ReservationDto createTestReservationDto() {
        return new ReservationDto(
                1L,
                1L,
                1L,
                LocalDateTime.of(2023, 6, 1, 10, 0),
                LocalDateTime.of(2023, 6, 1, 11, 0),
                GameType.SINGLES,
                new BigDecimal("500.00")
        );
    }

    @Test
    void createReservation_ShouldReturnCreatedReservation() throws Exception {
        ReservationResponseDto responseDto = new ReservationResponseDto(createTestReservationDto().getTotalPrice());
        given(reservationFacade.createReservation(any(ReservationCreateDto.class))).willReturn(responseDto);

        mockMvc.perform(post("/api/reservations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(RESERVATION_CREATE_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.totalPrice").value(500.00));
    }

    @Test
    void updateReservation_ShouldReturnUpdatedReservation() throws Exception {
        ReservationDto updatedDto = new ReservationDto(
                1L,
                1L,
                1L,
                LocalDateTime.of(2023, 6, 1, 11, 0),
                LocalDateTime.of(2023, 6, 1, 12, 0),
                GameType.DOUBLES,
                new BigDecimal("600.00")
        );
        ReservationResponseDto responseDto = new ReservationResponseDto(updatedDto.getTotalPrice());
        given(reservationFacade.updateReservation(anyLong(), any(ReservationUpdateDto.class))).willReturn(responseDto);

        mockMvc.perform(put("/api/reservations/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(RESERVATION_UPDATE_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalPrice").value(600.00));
    }

    @Test
    void getAllReservations_ShouldReturnAllReservations() throws Exception {
        List<ReservationDto> reservations = List.of(createTestReservationDto());
        given(reservationFacade.getAllReservations()).willReturn(reservations);

        mockMvc.perform(get("/api/reservations"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].courtId").value(1))
                .andExpect(jsonPath("$[0].customerId").value(1));
    }

    @Test
    void getReservationsByCustomerPhoneNumber_ShouldReturnReservations() throws Exception {
        List<ReservationDto> reservations = List.of(createTestReservationDto());
        given(reservationFacade.getByCustomerPhone(anyString(), anyBoolean())).willReturn(reservations);

        mockMvc.perform(get("/api/reservations/+420123456789"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].customerId").value(1));
    }

    @Test
    void getReservationsByCourtName_ShouldReturnReservations() throws Exception {
        List<ReservationDto> reservations = List.of(createTestReservationDto());
        given(reservationFacade.getByCourtName(anyString())).willReturn(reservations);

        mockMvc.perform(get("/api/reservations/court/Court1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].courtId").value(1));
    }

    @Test
    void deleteReservationById_ShouldReturnNoContent() throws Exception {
        doNothing().when(reservationFacade).deleteReservationById(1L);

        mockMvc.perform(delete("/api/reservations/1"))
                .andExpect(status().isNoContent());
    }
}