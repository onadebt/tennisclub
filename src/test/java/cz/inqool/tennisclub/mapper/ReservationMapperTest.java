package cz.inqool.tennisclub.mapper;

import cz.inqool.tennisclub.domain.model.Court;
import cz.inqool.tennisclub.domain.model.Customer;
import cz.inqool.tennisclub.domain.model.GameType;
import cz.inqool.tennisclub.domain.model.Reservation;
import cz.inqool.tennisclub.dto.reservation.ReservationDto;
import cz.inqool.tennisclub.dto.reservation.ReservationResponseDto;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ReservationMapperTest {

    private final ReservationMapper mapper = new ReservationMapper();

    @Test
    void toResponseDto_shouldMapCorrectly() {
        Reservation reservation = new Reservation();
        reservation.setTotalPrice(BigDecimal.valueOf(100.50));

        ReservationResponseDto dto = mapper.toResponseDto(reservation);

        assertEquals(BigDecimal.valueOf(100.50), dto.getTotalPrice());
    }

    @Test
    void toDto_shouldMapCorrectly() {
        Court court = new Court();
        court.setId(1L);

        Customer customer = new Customer();
        customer.setId(2L);

        Reservation reservation = new Reservation();
        reservation.setId(10L);
        reservation.setCourt(court);
        reservation.setCustomer(customer);
        reservation.setStartTime(LocalDateTime.of(2024, 1, 1, 10, 0));
        reservation.setEndTime(LocalDateTime.of(2024, 1, 1, 11, 0));
        reservation.setGameType(GameType.SINGLES);
        reservation.setTotalPrice(BigDecimal.valueOf(50.00));

        ReservationDto dto = mapper.toDto(reservation);

        assertEquals(10L, dto.getId());
        assertEquals(1L, dto.getCourtId());
        assertEquals(2L, dto.getCustomerId());
        assertEquals(reservation.getStartTime(), dto.getStartTime());
        assertEquals(reservation.getEndTime(), dto.getEndTime());
        assertEquals(GameType.SINGLES, dto.getGameType());
        assertEquals(BigDecimal.valueOf(50.00), dto.getTotalPrice());
    }
}

