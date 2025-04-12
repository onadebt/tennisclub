package cz.inqool.tennisclub.domain.service;

import cz.inqool.tennisclub.domain.model.Court;
import cz.inqool.tennisclub.domain.model.Customer;
import cz.inqool.tennisclub.domain.model.GameType;
import cz.inqool.tennisclub.domain.model.Reservation;
import cz.inqool.tennisclub.exception.reservation.ReservationInvalidTimeException;
import cz.inqool.tennisclub.exception.reservation.ReservationNotFoundException;
import cz.inqool.tennisclub.exception.reservation.ReservationOverlapsException;
import cz.inqool.tennisclub.infrastructure.dao.interfaces.ReservationDao;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReservationServiceTest {

    private final Long RESERVATION_ID = 1L;
    private final String COURT_NAME = "1";
    private final String PHONE_NUMBER = "+420123456789";
    private final String CUSTOMER_NAME = "Zdena Podkorenova";
    private final GameType GAME_TYPE = GameType.SINGLES;
    private final LocalDateTime START_TIME = LocalDateTime.of(2025, 6, 1, 10, 0);
    private final LocalDateTime END_TIME = LocalDateTime.of(2025, 6, 1, 11, 0);
    private final BigDecimal TOTAL_PRICE = BigDecimal.valueOf(500);
    @Mock
    private ReservationDao reservationDao;
    @Mock
    private CustomerService customerService;
    @Mock
    private CourtService courtService;
    @Mock
    private ReservationValidator reservationValidator;
    @Mock
    private PriceCalculatorService priceCalculator;
    @InjectMocks
    private ReservationService reservationService;
    private Court court;
    private Customer customer;
    private Reservation reservation;

    @BeforeEach
    void setUp() {
        court = new Court();
        court.setCourtName(COURT_NAME);

        customer = new Customer();
        customer.setPhoneNumber(PHONE_NUMBER);
        customer.setName(CUSTOMER_NAME);

        reservation = new Reservation();
        reservation.setId(RESERVATION_ID);
        reservation.setCourt(court);
        reservation.setCustomer(customer);
        reservation.setStartTime(START_TIME);
        reservation.setEndTime(END_TIME);
        reservation.setGameType(GAME_TYPE);
        reservation.setTotalPrice(TOTAL_PRICE);
        reservation.setDeleted(false);
    }

    @Test
    void createReservation_ShouldCreateNewReservation_WhenValid() {
        when(courtService.getByName(COURT_NAME)).thenReturn(court);
        when(customerService.findOrCreate(PHONE_NUMBER, CUSTOMER_NAME)).thenReturn(customer);
        when(priceCalculator.calculate(court, GAME_TYPE, START_TIME, END_TIME)).thenReturn(TOTAL_PRICE);
        when(reservationDao.save(any(Reservation.class))).thenReturn(reservation);

        Reservation result = reservationService.createReservation(
                COURT_NAME, PHONE_NUMBER, CUSTOMER_NAME, GAME_TYPE, START_TIME, END_TIME
        );

        assertNotNull(result);
        assertEquals(RESERVATION_ID, result.getId());
        verify(reservationValidator).validateTime(START_TIME, END_TIME);
        verify(reservationValidator).ensureNoOverlap(COURT_NAME, START_TIME, END_TIME);
        verify(courtService).getByName(COURT_NAME);
        verify(customerService).findOrCreate(PHONE_NUMBER, CUSTOMER_NAME);
        verify(priceCalculator).calculate(court, GAME_TYPE, START_TIME, END_TIME);
        verify(reservationDao).save(any(Reservation.class));
    }

    @Test
    void createReservation_ShouldThrow_WhenTimeInvalid() {
        doThrow(new ReservationInvalidTimeException(START_TIME.toString(), END_TIME.toString()))
                .when(reservationValidator).validateTime(END_TIME, START_TIME);

        assertThrows(ReservationInvalidTimeException.class,
                () -> reservationService.createReservation(
                COURT_NAME, PHONE_NUMBER, CUSTOMER_NAME, GAME_TYPE, END_TIME, START_TIME
        ));

        verify(reservationValidator).validateTime(END_TIME, START_TIME);
        verifyNoInteractions(courtService, customerService, priceCalculator, reservationDao);
    }

    @Test
    void createReservation_ShouldThrow_WhenOverlapExists() {
        doThrow(new ReservationOverlapsException(COURT_NAME, START_TIME.toString(), END_TIME.toString()))
                .when(reservationValidator).ensureNoOverlap(COURT_NAME, START_TIME, END_TIME);

        assertThrows(ReservationOverlapsException.class,
                () -> reservationService.createReservation(
                        COURT_NAME, PHONE_NUMBER, CUSTOMER_NAME, GAME_TYPE, START_TIME, END_TIME
        ));

        verify(reservationValidator).validateTime(START_TIME, END_TIME);
        verify(reservationValidator).ensureNoOverlap(COURT_NAME, START_TIME, END_TIME);
        verifyNoInteractions(courtService, customerService, priceCalculator, reservationDao);
    }

    @Test
    void updateReservation_ShouldUpdate_WhenValidChanges() {
        LocalDateTime newStart = START_TIME.plusHours(1);
        LocalDateTime newEnd = END_TIME.plusHours(1);
        GameType newGameType = GameType.DOUBLES;
        BigDecimal newPrice = TOTAL_PRICE.add(BigDecimal.valueOf(100));

        when(reservationDao.findById(RESERVATION_ID)).thenReturn(Optional.of(reservation));
        when(priceCalculator.calculate(court, newGameType, newStart, newEnd)).thenReturn(newPrice);
        when(reservationDao.save(reservation)).thenReturn(reservation);

        Reservation result = reservationService.updateReservation(
                RESERVATION_ID, newStart, newEnd, newGameType
        );

        assertEquals(newStart, result.getStartTime());
        assertEquals(newEnd, result.getEndTime());
        assertEquals(newGameType, result.getGameType());
        assertEquals(newPrice, result.getTotalPrice());
        verify(reservationValidator).validateTime(newStart, newEnd);
        verify(reservationValidator).ensureNoOverlapExcluding(RESERVATION_ID, COURT_NAME, newStart, newEnd);
        verify(priceCalculator).calculate(court, newGameType, newStart, newEnd);
    }

    @Test
    void updateReservation_ShouldNotUpdate_WhenNoChangesNeeded() {
        when(reservationDao.findById(RESERVATION_ID)).thenReturn(Optional.of(reservation));
        when(reservationDao.save(reservation)).thenReturn(reservation);

        Reservation result = reservationService.updateReservation(
                RESERVATION_ID, START_TIME, END_TIME, GAME_TYPE
        );

        assertEquals(START_TIME, result.getStartTime());
        assertEquals(END_TIME, result.getEndTime());
        assertEquals(GAME_TYPE, result.getGameType());
        assertEquals(TOTAL_PRICE, result.getTotalPrice());
        verify(reservationValidator).validateTime(START_TIME, END_TIME);
        verify(reservationValidator).ensureNoOverlapExcluding(RESERVATION_ID, COURT_NAME, START_TIME, END_TIME);
        verify(priceCalculator, never()).calculate(any(), any(), any(), any());
        verify(reservationDao).save(reservation);
    }

    @Test
    void updateReservation_ShouldThrow_WhenReservationNotFound() {
        when(reservationDao.findById(RESERVATION_ID)).thenReturn(Optional.empty());

        assertThrows(ReservationNotFoundException.class,
                () -> reservationService.updateReservation(RESERVATION_ID, START_TIME, END_TIME, GAME_TYPE));

        verify(reservationDao).findById(RESERVATION_ID);
        verifyNoInteractions(reservationValidator, priceCalculator);
    }

    @Test
    void getReservationsByCourtName_ShouldReturnReservations() {
        List<Reservation> expected = List.of(reservation);
        when(reservationDao.findByCourtNameOrderedByCreated(COURT_NAME)).thenReturn(expected);

        List<Reservation> result = reservationService.getReservationsByCourtName(COURT_NAME);

        assertEquals(1, result.size());
        assertEquals(reservation, result.getFirst());
        verify(reservationDao).findByCourtNameOrderedByCreated(COURT_NAME);
    }

    @Test
    void getReservationsByPhone_ShouldReturnFutureReservations_WhenFutureOnlyTrue() {
        LocalDateTime fixedNow = LocalDateTime.now();
        List<Reservation> expected = List.of(reservation);

        when(reservationDao.findFutureByPhone(PHONE_NUMBER, fixedNow)).thenReturn(expected);

        try (MockedStatic<LocalDateTime> mocked = mockStatic(LocalDateTime.class)) {
            mocked.when(LocalDateTime::now).thenReturn(fixedNow);

            List<Reservation> result = reservationService.getReservationsByPhone(PHONE_NUMBER, true);

            assertEquals(1, result.size());
            assertEquals(reservation, result.getFirst());
        }
    }

    @Test
    void getReservationsByPhone_ShouldReturnAllReservations_WhenFutureOnlyFalse() {
        List<Reservation> expected = List.of(reservation);
        when(reservationDao.findByPhone(PHONE_NUMBER)).thenReturn(expected);

        List<Reservation> result = reservationService.getReservationsByPhone(PHONE_NUMBER, false);

        assertEquals(1, result.size());
        assertEquals(reservation, result.getFirst());
        verify(reservationDao).findByPhone(PHONE_NUMBER);
    }

    @Test
    void getAllReservations_ShouldReturnAllReservations() {
        List<Reservation> expected = List.of(reservation);
        when(reservationDao.findAll()).thenReturn(expected);

        List<Reservation> result = reservationService.getAllReservations();

        assertEquals(1, result.size());
        assertEquals(reservation, result.getFirst());
        verify(reservationDao).findAll();
    }

    @Test
    void deleteReservationById_ShouldSoftDelete_WhenExists() {
        when(reservationDao.findById(RESERVATION_ID)).thenReturn(Optional.of(reservation));
        when(reservationDao.save(reservation)).thenReturn(reservation);

        reservationService.deleteReservationById(RESERVATION_ID);

        assertTrue(reservation.isDeleted());
        verify(reservationDao).findById(RESERVATION_ID);
        verify(reservationDao).save(reservation);
    }

    @Test
    void deleteReservationById_ShouldThrow_WhenNotFound() {
        when(reservationDao.findById(RESERVATION_ID)).thenReturn(Optional.empty());

        assertThrows(ReservationNotFoundException.class,
                () -> reservationService.deleteReservationById(RESERVATION_ID));

        verify(reservationDao).findById(RESERVATION_ID);
        verify(reservationDao, never()).save(any());
    }

    @Test
    void deleteReservationById_ShouldThrow_WhenAlreadyDeleted() {
        reservation.setDeleted(true);
        when(reservationDao.findById(RESERVATION_ID)).thenReturn(Optional.of(reservation));

        assertThrows(ReservationNotFoundException.class,
                () -> reservationService.deleteReservationById(RESERVATION_ID));

        verify(reservationDao).findById(RESERVATION_ID);
        verify(reservationDao, never()).save(any());
    }
}
