package cz.inqool.tennisclub.controller;

import cz.inqool.tennisclub.dto.reservation.ReservationCreateDto;
import cz.inqool.tennisclub.dto.reservation.ReservationDto;
import cz.inqool.tennisclub.dto.reservation.ReservationResponseDto;
import cz.inqool.tennisclub.dto.reservation.ReservationUpdateDto;
import cz.inqool.tennisclub.facade.ReservationFacade;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reservations")
public class ReservationController {

    private final ReservationFacade reservationFacade;

    @Autowired
    public ReservationController(ReservationFacade reservationFacade) {
        this.reservationFacade = reservationFacade;
    }

    @PostMapping
    public ResponseEntity<ReservationResponseDto> createReservation(
            @RequestBody @Valid ReservationCreateDto reservationCreateDto) {
        return new ResponseEntity<>(
                reservationFacade.createReservation(reservationCreateDto),
                HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ReservationResponseDto> updateReservation(
            @PathVariable Long id,
            @RequestBody @Valid ReservationUpdateDto reservationUpdateDto) {
        return new ResponseEntity<>(
                reservationFacade.updateReservation(id, reservationUpdateDto),
                HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<ReservationDto>> getAllReservations() {
        List<ReservationDto> reservations = reservationFacade.getAllReservations();
        return new ResponseEntity<>(reservations, HttpStatus.OK);
    }

    @GetMapping("/{phoneNumber}")
    public ResponseEntity<List<ReservationDto>> getReservationsByCustomerPhoneNumber(
            @PathVariable String phoneNumber,
            @RequestParam(required = false, defaultValue = "true") boolean futureOnly) {
        List<ReservationDto> reservations = reservationFacade.getByCustomerPhone(phoneNumber, futureOnly);
        return new ResponseEntity<>(reservations, HttpStatus.OK);
    }

    @GetMapping("/court/{courtName}")
    public ResponseEntity<List<ReservationDto>> getReservationsByCourtName(
            @PathVariable String courtName) {
        List<ReservationDto> reservations = reservationFacade.getByCourtName(courtName);
        return new ResponseEntity<>(reservations, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReservationById(@PathVariable Long id) {
        reservationFacade.deleteReservationById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}

