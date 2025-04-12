package cz.inqool.tennisclub.facade;

import cz.inqool.tennisclub.domain.model.Reservation;
import cz.inqool.tennisclub.domain.service.ReservationService;
import cz.inqool.tennisclub.dto.reservation.ReservationCreateDto;
import cz.inqool.tennisclub.dto.reservation.ReservationDto;
import cz.inqool.tennisclub.dto.reservation.ReservationResponseDto;
import cz.inqool.tennisclub.dto.reservation.ReservationUpdateDto;
import cz.inqool.tennisclub.mapper.ReservationMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReservationFacade {

    private final ReservationService reservationService;
    private final ReservationMapper reservationMapper;

    @Autowired
    public ReservationFacade(ReservationService reservationService, ReservationMapper reservationMapper) {
        this.reservationService = reservationService;
        this.reservationMapper = reservationMapper;
    }

    public ReservationResponseDto createReservation(ReservationCreateDto reservationCreateDto) {

        Reservation newReservation = reservationService.createReservation(reservationCreateDto.getCourtName(),
                reservationCreateDto.getCustomerPhone(),
                reservationCreateDto.getCustomerName(),
                reservationCreateDto.getGameType(),
                reservationCreateDto.getStartTime(),
                reservationCreateDto.getEndTime());

        return reservationMapper.toResponseDto(newReservation);
    }

    public ReservationResponseDto updateReservation(Long id, ReservationUpdateDto reservationUpdateDto) {
        Reservation updatedReservation = reservationService.updateReservation(id,
                reservationUpdateDto.getStartTime(),
                reservationUpdateDto.getEndTime(),
                reservationUpdateDto.getGameType());

        return reservationMapper.toResponseDto(updatedReservation);
    }

    public List<ReservationDto> getAllReservations() {
        List<Reservation> reservations = reservationService.getAllReservations();
        return reservations.stream()
                .map(reservationMapper::toDto)
                .toList();
    }

    public List<ReservationDto> getByCourtName(String courtName) {
        List<Reservation> reservations = reservationService.getReservationsByCourtName(courtName);
        return reservations.stream()
                .map(reservationMapper::toDto)
                .toList();
    }

    public List<ReservationDto> getByCustomerPhone(String phone, boolean futureOnly) {
        List<Reservation> reservations = reservationService.getReservationsByPhone(phone, futureOnly);
        return reservations.stream()
                .map(reservationMapper::toDto)
                .toList();
    }

    public void deleteReservationById(Long reservationId) {
        reservationService.deleteReservationById(reservationId);
    }
}
