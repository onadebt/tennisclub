package cz.inqool.tennisclub.controller.exceptionhandler;

import cz.inqool.tennisclub.exception.surfacetype.SurfaceAlreadyExistsException;
import cz.inqool.tennisclub.exception.surfacetype.SurfaceTypeNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class SurfaceTypeExceptionHandler {

    @ExceptionHandler(SurfaceTypeNotFoundException.class)
    public ResponseEntity<?> handleUserNotFoundException(SurfaceTypeNotFoundException ex) {
        ProblemDetail pd = ProblemDetail.forStatus(HttpStatus.NOT_FOUND);
        pd.setTitle("SurfaceType Not Found Error");
        pd.setProperty("errorCode", "SURFACE_TYPE_NOT_FOUND");
        pd.setDetail(ex.getMessage());
        return new ResponseEntity<>(pd, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(SurfaceAlreadyExistsException.class)
    public ResponseEntity<?> handleSurfaceTypeNotFoundException(SurfaceAlreadyExistsException ex) {
        ProblemDetail pd = ProblemDetail.forStatus(HttpStatus.CONFLICT);
        pd.setTitle("SurfaceType Already Exists Error");
        pd.setProperty("errorCode", "SURFACE_TYPE_ALREADY_EXISTS");
        pd.setDetail(ex.getMessage());
        return new ResponseEntity<>(pd, HttpStatus.CONFLICT);
    }
}
