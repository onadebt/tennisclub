package cz.inqool.tennisclub.infrastructure.dao.interfaces;

import cz.inqool.tennisclub.domain.model.Reservation;

import java.time.LocalDateTime;
import java.util.List;

public interface ReservationDao extends GenericDao<Reservation> {
    boolean existsOverlappingReservation(String courtNumber, LocalDateTime start, LocalDateTime end);
    List<Reservation> findAllByCustomerId(Long customerId);
    List<Reservation> findByCourtNameOrderedByCreated(String courtName);
    List<Reservation> findFutureByPhone(String phone, LocalDateTime now);
    List<Reservation> findByPhone(String phone);
    Boolean existsOverlappingReservationExcludingId(Long id, String courtName, LocalDateTime start, LocalDateTime end);
}

