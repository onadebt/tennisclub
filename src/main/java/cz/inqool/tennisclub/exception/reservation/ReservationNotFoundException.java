package cz.inqool.tennisclub.exception.reservation;

public class ReservationNotFoundException extends RuntimeException {

    public ReservationNotFoundException(Long id) {
        super("Reservation with ID " + id + " not found.");
    }

    public ReservationNotFoundException(String message) {
        super(message);
    }
}
