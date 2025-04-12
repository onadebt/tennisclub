package cz.inqool.tennisclub.domain.service;

import cz.inqool.tennisclub.exception.reservation.ReservationInvalidTimeException;
import cz.inqool.tennisclub.exception.reservation.ReservationOverlapsException;
import cz.inqool.tennisclub.infrastructure.dao.interfaces.ReservationDao;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ReservationValidatorTest {

    private final LocalDateTime validStart = LocalDateTime.of(2025, 1, 1, 10, 0);
    private final LocalDateTime validEnd = LocalDateTime.of(2025, 1, 1, 11, 0);
    @Mock
    private ReservationDao reservationDao;
    private ReservationValidator validator;

    @BeforeEach
    void setUp() {
        validator = new ReservationValidator(reservationDao);
    }

    @Test
    void validateTime_ShouldNotThrow_WhenStartIsBeforeEnd() {
        assertDoesNotThrow(() -> validator.validateTime(validStart, validEnd));
    }

    @Test
    void validateTime_ShouldThrow_WhenStartEqualsEnd() {
        LocalDateTime start = LocalDateTime.of(2025, 1, 1, 10, 0);
        LocalDateTime end = start;

        assertThrows(
                ReservationInvalidTimeException.class,
                () -> validator.validateTime(start, end)
        );
    }

    @Test
    void validateTime_ShouldThrow_WhenStartIsAfterEnd() {
        LocalDateTime start = LocalDateTime.of(2025, 1, 1, 11, 0);
        LocalDateTime end = LocalDateTime.of(2025, 1, 1, 10, 0);

        assertThrows(
                ReservationInvalidTimeException.class,
                () -> validator.validateTime(start, end)
        );
    }

    @Test
    void ensureNoOverlap_ShouldNotThrow_WhenNoOverlappingReservationExists() {
        when(reservationDao.existsOverlappingReservation("Court1", validStart, validEnd))
                .thenReturn(false);

        assertDoesNotThrow(() -> validator.ensureNoOverlap("Court1", validStart, validEnd));
        verify(reservationDao).existsOverlappingReservation("Court1", validStart, validEnd);
    }

    @Test
    void ensureNoOverlap_ShouldThrow_WhenOverlappingReservationExists() {
        when(reservationDao.existsOverlappingReservation("Court1", validStart, validEnd))
                .thenReturn(true);

        assertThrows(
                ReservationOverlapsException.class,
                () -> validator.ensureNoOverlap("Court1", validStart, validEnd)
        );

        verify(reservationDao).existsOverlappingReservation("Court1", validStart, validEnd);
    }

    @Test
    void ensureNoOverlapExcluding_ShouldNotThrow_WhenNoOverlappingReservationExists() {
        Long excludedId = 1L;
        when(reservationDao.existsOverlappingReservationExcludingId(excludedId, "Court1", validStart, validEnd))
                .thenReturn(false);

        assertDoesNotThrow(() ->
                validator.ensureNoOverlapExcluding(excludedId, "Court1", validStart, validEnd)
        );

        verify(reservationDao).existsOverlappingReservationExcludingId(excludedId, "Court1", validStart, validEnd);
    }

    @Test
    void ensureNoOverlapExcluding_ShouldThrow_WhenOverlappingReservationExists() {
        Long excludedId = 1L;
        when(reservationDao.existsOverlappingReservationExcludingId(excludedId, "Court1", validStart, validEnd))
                .thenReturn(true);

        assertThrows(
                ReservationOverlapsException.class,
                () -> validator.ensureNoOverlapExcluding(excludedId, "Court1", validStart, validEnd)
        );

        verify(reservationDao).existsOverlappingReservationExcludingId(excludedId, "Court1", validStart, validEnd);
    }
}