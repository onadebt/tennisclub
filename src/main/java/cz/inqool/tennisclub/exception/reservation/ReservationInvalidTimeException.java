package cz.inqool.tennisclub.exception.reservation;

public class ReservationInvalidTimeException extends RuntimeException {
    public ReservationInvalidTimeException(String startTime, String endTime) {
        super("Invalid reservation time: " + startTime + " - " + endTime);
    }
}
