package cz.inqool.tennisclub.domain.service;

import cz.inqool.tennisclub.exception.reservation.ReservationInvalidTimeException;
import cz.inqool.tennisclub.exception.reservation.ReservationOverlapsException;
import cz.inqool.tennisclub.infrastructure.dao.interfaces.ReservationDao;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class ReservationValidator {
    private final ReservationDao reservationDao;

    public ReservationValidator(ReservationDao reservationDao) {
        this.reservationDao = reservationDao;
    }

    public void validateTime(LocalDateTime start, LocalDateTime end) {
        if (!start.isBefore(end)) {
            throw new ReservationInvalidTimeException(start.toString(), end.toString());
        }
    }

    public void ensureNoOverlap(String courtName, LocalDateTime start, LocalDateTime end) {
        if (reservationDao.existsOverlappingReservation(courtName, start, end)) {
            throw new ReservationOverlapsException(courtName, start.toString(), end.toString());
        }
    }

    public void ensureNoOverlapExcluding(Long id, String courtName, LocalDateTime start, LocalDateTime end) {
        if (reservationDao.existsOverlappingReservationExcludingId(id, courtName, start, end)) {
            throw new ReservationOverlapsException(courtName, start.toString(), end.toString());
        }
    }
}
