package cz.inqool.tennisclub.controller.exceptionhandler;

import cz.inqool.tennisclub.exception.court.CourtAlreadyExistsException;
import cz.inqool.tennisclub.exception.court.CourtNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class CourtExceptionHandler {

    @ExceptionHandler(CourtNotFoundException.class)
    public ProblemDetail handleCourtNotFoundException(CourtNotFoundException ex) {
        ProblemDetail pd = ProblemDetail.forStatus(HttpStatus.NOT_FOUND);
        pd.setTitle("Court Not Found Error");
        pd.setProperty("errorCode", "COURT_NOT_FOUND");
        pd.setDetail(ex.getMessage());
        return pd;
    }

    @ExceptionHandler(CourtAlreadyExistsException.class)
    public ProblemDetail handleCourtAlreadyExistsException(CourtAlreadyExistsException ex) {
        ProblemDetail pd = ProblemDetail.forStatus(HttpStatus.CONFLICT);
        pd.setTitle("Court Already Exists Error");
        pd.setProperty("errorCode", "COURT_ALREADY_EXISTS");
        pd.setDetail(ex.getMessage());
        return pd;
    }
}
