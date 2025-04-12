package cz.inqool.tennisclub.facade;

import cz.inqool.tennisclub.domain.model.GameType;
import cz.inqool.tennisclub.domain.model.Reservation;
import cz.inqool.tennisclub.domain.service.ReservationService;
import cz.inqool.tennisclub.dto.reservation.ReservationCreateDto;
import cz.inqool.tennisclub.dto.reservation.ReservationDto;
import cz.inqool.tennisclub.dto.reservation.ReservationResponseDto;
import cz.inqool.tennisclub.dto.reservation.ReservationUpdateDto;
import cz.inqool.tennisclub.mapper.ReservationMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReservationFacadeTest {

    @Mock
    private ReservationService reservationService;

    @Mock
    private ReservationMapper reservationMapper;

    @InjectMocks
    private ReservationFacade reservationFacade;

    @Test
    void createReservation_shouldCreateAndReturnResponseDto() {
        ReservationCreateDto createDto = new ReservationCreateDto();
        createDto.setCourtName("Court A");
        createDto.setCustomerPhone("123456789");
        createDto.setCustomerName("John Doe");
        createDto.setGameType(GameType.SINGLES);
        createDto.setStartTime(LocalDateTime.now());
        createDto.setEndTime(LocalDateTime.now().plusHours(1));

        Reservation reservation = new Reservation();
        ReservationResponseDto responseDto = new ReservationResponseDto();

        when(reservationService.createReservation(
                createDto.getCourtName(),
                createDto.getCustomerPhone(),
                createDto.getCustomerName(),
                createDto.getGameType(),
                createDto.getStartTime(),
                createDto.getEndTime()))
                .thenReturn(reservation);

        when(reservationMapper.toResponseDto(reservation)).thenReturn(responseDto);

        ReservationResponseDto result = reservationFacade.createReservation(createDto);

        assertEquals(responseDto, result);
        verify(reservationService).createReservation(
                createDto.getCourtName(),
                createDto.getCustomerPhone(),
                createDto.getCustomerName(),
                createDto.getGameType(),
                createDto.getStartTime(),
                createDto.getEndTime());

        verify(reservationMapper).toResponseDto(reservation);
    }

    @Test
    void updateReservation_shouldUpdateAndReturnResponseDto() {
        Long id = 1L;
        ReservationUpdateDto updateDto = new ReservationUpdateDto();
        updateDto.setStartTime(LocalDateTime.now());
        updateDto.setEndTime(LocalDateTime.now().plusHours(2));
        updateDto.setGameType(GameType.DOUBLES);

        Reservation reservation = new Reservation();
        ReservationResponseDto responseDto = new ReservationResponseDto();

        when(reservationService.updateReservation(
                id,
                updateDto.getStartTime(),
                updateDto.getEndTime(),
                updateDto.getGameType()))
                .thenReturn(reservation);

        when(reservationMapper.toResponseDto(reservation)).thenReturn(responseDto);

        ReservationResponseDto result = reservationFacade.updateReservation(id, updateDto);

        assertEquals(responseDto, result);
        verify(reservationService).updateReservation(id, updateDto.getStartTime(), updateDto.getEndTime(), updateDto.getGameType());
        verify(reservationMapper).toResponseDto(reservation);
    }

    @Test
    void getAllReservations_shouldReturnMappedList() {
        List<Reservation> reservations = List.of(new Reservation(), new Reservation());
        when(reservationService.getAllReservations()).thenReturn(reservations);
        when(reservationMapper.toDto(any())).thenReturn(new ReservationDto());

        List<ReservationDto> result = reservationFacade.getAllReservations();

        assertEquals(2, result.size());
        verify(reservationService).getAllReservations();
        verify(reservationMapper, times(2)).toDto(any());
    }

    @Test
    void getByCourtName_shouldReturnMappedList() {
        String courtName = "Center Court";
        List<Reservation> reservations = List.of(new Reservation(), new Reservation());
        when(reservationService.getReservationsByCourtName(courtName)).thenReturn(reservations);
        when(reservationMapper.toDto(any())).thenReturn(new ReservationDto());

        List<ReservationDto> result = reservationFacade.getByCourtName(courtName);

        assertEquals(2, result.size());
        verify(reservationService).getReservationsByCourtName(courtName);
        verify(reservationMapper, times(2)).toDto(any());
    }

    @Test
    void getByCustomerPhone_shouldReturnMappedList() {
        String phone = "123456789";
        boolean futureOnly = true;
        List<Reservation> reservations = List.of(new Reservation(), new Reservation());
        when(reservationService.getReservationsByPhone(phone, futureOnly)).thenReturn(reservations);
        when(reservationMapper.toDto(any())).thenReturn(new ReservationDto());

        List<ReservationDto> result = reservationFacade.getByCustomerPhone(phone, futureOnly);

        assertEquals(2, result.size());
        verify(reservationService).getReservationsByPhone(phone, futureOnly);
        verify(reservationMapper, times(2)).toDto(any());
    }

    @Test
    void deleteReservationById_shouldCallService() {
        Long id = 42L;

        reservationFacade.deleteReservationById(id);

        verify(reservationService).deleteReservationById(id);
    }
}

