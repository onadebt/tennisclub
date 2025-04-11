package cz.inqool.tennisclub.controller.exceptionhandler;

import cz.inqool.tennisclub.exception.reservation.ReservationInvalidTimeException;
import cz.inqool.tennisclub.exception.reservation.ReservationNotFoundException;
import cz.inqool.tennisclub.exception.reservation.ReservationOverlapsException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ReservationExceptionHandler {

    @ExceptionHandler(ReservationNotFoundException.class)
    public ProblemDetail handleReservationNotFoundException(ReservationNotFoundException ex) {
        ProblemDetail pd = ProblemDetail.forStatus(HttpStatus.NOT_FOUND);
        pd.setTitle("Reservation Not Found Error");
        pd.setProperty("errorCode", "RESERVATION_NOT_FOUND");
        pd.setDetail(ex.getMessage());
        return pd;
    }

    @ExceptionHandler(ReservationOverlapsException.class)
    public ProblemDetail handleReservationOverlapsException(ReservationOverlapsException ex) {
        ProblemDetail pd = ProblemDetail.forStatus(HttpStatus.CONFLICT);
        pd.setTitle("Reservation Overlaps Error");
        pd.setProperty("errorCode", "RESERVATION_OVERLAPS");
        pd.setDetail(ex.getMessage());
        return pd;
    }

    @ExceptionHandler(ReservationInvalidTimeException.class)
    public ProblemDetail handleReservationInvalidTimeException(ReservationInvalidTimeException ex) {
        ProblemDetail pd = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
        pd.setTitle("Reservation Invalid Time Error");
        pd.setProperty("errorCode", "RESERVATION_INVALID_TIME");
        pd.setDetail(ex.getMessage());
        return pd;
    }
}
