package cz.inqool.tennisclub.domain.service;

import cz.inqool.tennisclub.domain.model.Court;
import cz.inqool.tennisclub.domain.model.Customer;
import cz.inqool.tennisclub.domain.model.GameType;
import cz.inqool.tennisclub.domain.model.Reservation;
import cz.inqool.tennisclub.exception.reservation.ReservationNotFoundException;
import cz.inqool.tennisclub.infrastructure.dao.interfaces.ReservationDao;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class ReservationService {

    private final ReservationDao reservationDao;
    private final CustomerService customerService;
    private final CourtService courtService;
    private final ReservationValidator reservationValidator;
    private final PriceCalculatorService priceCalculator;

    @Autowired
    public ReservationService(ReservationDao reservationDao,
                              CustomerService customerService,
                              CourtService courtService,
                              ReservationValidator reservationValidator,
                              PriceCalculatorService priceCalculator) {
        this.reservationDao = reservationDao;
        this.customerService = customerService;
        this.courtService = courtService;
        this.reservationValidator = reservationValidator;
        this.priceCalculator = priceCalculator;
    }

    @Transactional
    public Reservation createReservation(String courtName, String customerPhone, String customerName,
                                         GameType gameType, LocalDateTime startTime, LocalDateTime endTime) {
        reservationValidator.validateTime(startTime, endTime);
        reservationValidator.ensureNoOverlap(courtName, startTime, endTime);
        Court court = courtService.getByName(courtName);
        Customer customer = customerService.findOrCreate(customerPhone, customerName);

        BigDecimal price = priceCalculator.calculate(court, gameType, startTime, endTime);

        Reservation reservation = new Reservation(court, customer, startTime, endTime, gameType, price);
        return reservationDao.save(reservation);
    }

    @Transactional
    public Reservation updateReservation(Long reservationId, LocalDateTime newStart, LocalDateTime newEnd,
                                         GameType newGameType) {
        Reservation reservation = getReservationById(reservationId);

        reservationValidator.validateTime(newStart, newEnd);
        reservationValidator.ensureNoOverlapExcluding(reservationId, reservation.getCourt().getCourtName(), newStart, newEnd);

        if (reservation.needsUpdate(newStart, newEnd, newGameType)) {
            BigDecimal newPrice = priceCalculator.calculate(reservation.getCourt(), newGameType, newStart, newEnd);
            reservation.update(newStart, newEnd, newGameType, newPrice);
        }

        return reservationDao.save(reservation);
    }

    public List<Reservation> getReservationsByCourtName(String courtName) {
        return reservationDao.findByCourtNameOrderedByCreated(courtName);
    }

    public List<Reservation> getReservationsByPhone(String phone, boolean futureOnly) {
        return futureOnly
                ? reservationDao.findFutureByPhone(phone, LocalDateTime.now())
                : reservationDao.findByPhone(phone);
    }

    public List<Reservation> getAllReservations() {
        return reservationDao.findAll();
    }

    public void deleteReservationById(Long reservationId) {
        Reservation reservation = getReservationById(reservationId);
        reservation.setDeleted(true);
        reservationDao.save(reservation);
    }

    private Reservation getReservationById(Long id) {
        return reservationDao.findById(id)
                .filter(r -> !r.isDeleted())
                .orElseThrow(() -> new ReservationNotFoundException(id));
    }
}
