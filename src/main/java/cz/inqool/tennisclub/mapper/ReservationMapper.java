package cz.inqool.tennisclub.mapper;

import cz.inqool.tennisclub.domain.model.Reservation;
import cz.inqool.tennisclub.dto.reservation.ReservationDto;
import cz.inqool.tennisclub.dto.reservation.ReservationResponseDto;
import org.springframework.stereotype.Component;

@Component
public class ReservationMapper {
    public ReservationResponseDto toResponseDto(Reservation reservation) {
        return new ReservationResponseDto(reservation.getTotalPrice());
    }

    public ReservationDto toDto(Reservation reservation) {
        return new ReservationDto(
                reservation.getId(),
                reservation.getCourt().getId(),
                reservation.getCustomer().getId(),
                reservation.getStartTime(),
                reservation.getEndTime(),
                reservation.getGameType(),
                reservation.getTotalPrice()
        );
    }
}
