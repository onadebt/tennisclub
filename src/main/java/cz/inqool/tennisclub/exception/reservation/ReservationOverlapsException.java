package cz.inqool.tennisclub.exception.reservation;

public class ReservationOverlapsException extends RuntimeException {
    public ReservationOverlapsException(String courtName, String startTime, String endTime) {
        super("Reservation overlaps for court name " + courtName + " from " + startTime + " to " + endTime);
    }
}
