package cz.inqool.tennisclub.infrastructure.dao;

import cz.inqool.tennisclub.domain.model.*;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
class ReservationDaoImplTest {

    @Autowired
    private TestEntityManager testEntityManager;

    @Autowired
    private EntityManager entityManager;

    private ReservationDaoImpl reservationDao;

    private Customer customer;
    private Court court;
    private Reservation existingReservation;

    @BeforeEach
    void setUp() {
        reservationDao = new ReservationDaoImpl();
        reservationDao.setEntityManager(entityManager);

        // Setup test data
        customer = new Customer("John Doe", "+420123456789");
        testEntityManager.persist(customer);

        SurfaceType surfaceType = new SurfaceType("Clay", new BigDecimal("0.50"));
        testEntityManager.persist(surfaceType);

        court = new Court("Court 1", surfaceType);
        testEntityManager.persist(court);

        existingReservation = new Reservation(
                court,
                customer,
                LocalDateTime.of(2023, 6, 1, 10, 0),
                LocalDateTime.of(2023, 6, 1, 11, 0),
                GameType.SINGLES,
                new BigDecimal("500.00")
        );
        testEntityManager.persist(existingReservation);
    }

    @Test
    void existsOverlappingReservation_ShouldReturnTrue_WhenOverlapExists() {
        // Arrange
        LocalDateTime start = LocalDateTime.of(2023, 6, 1, 10, 30);
        LocalDateTime end = LocalDateTime.of(2023, 6, 1, 11, 30);

        // Act
        boolean result = reservationDao.existsOverlappingReservation(court.getId().toString(), start, end);

        // Assert
        assertTrue(result);
    }

    @Test
    void existsOverlappingReservation_ShouldReturnFalse_WhenNoOverlap() {
        // Arrange
        LocalDateTime start = LocalDateTime.of(2023, 6, 1, 12, 0);
        LocalDateTime end = LocalDateTime.of(2023, 6, 1, 13, 0);

        // Act
        boolean result = reservationDao.existsOverlappingReservation(court.getId().toString(), start, end);

        // Assert
        assertFalse(result);
    }

    @Test
    void existsOverlappingReservationExcludingId_ShouldReturnTrue_WhenOtherReservationOverlaps() {
        // Arrange - Create another reservation that overlaps
        Reservation anotherReservation = new Reservation(
                court,
                customer,
                LocalDateTime.of(2023, 6, 1, 10, 30),
                LocalDateTime.of(2023, 6, 1, 11, 30),
                GameType.SINGLES,
                new BigDecimal("500.00")
        );
        testEntityManager.persist(anotherReservation);

        LocalDateTime start = LocalDateTime.of(2023, 6, 1, 10, 0);
        LocalDateTime end = LocalDateTime.of(2023, 6, 1, 11, 0);

        // Act
        boolean result = reservationDao.existsOverlappingReservationExcludingId(
                existingReservation.getId(),
                court.getCourtName(),
                start,
                end
        );

        // Assert
        assertTrue(result);
    }

    @Test
    void findAllByCustomerId_ShouldReturnCustomerReservations() {
        // Act
        List<Reservation> result = reservationDao.findAllByCustomerId(customer.getId());

        // Assert
        assertEquals(1, result.size());
        assertEquals(existingReservation.getId(), result.getFirst().getId());
    }

    @Test
    void findByCourtNameOrderedByCreated_ShouldReturnOrderedReservations() {
        // Arrange - Add another reservation for the same court
        Reservation anotherReservation = new Reservation(
                court,
                customer,
                LocalDateTime.of(2023, 6, 2, 10, 0),
                LocalDateTime.of(2023, 6, 2, 11, 0),
                GameType.SINGLES,
                new BigDecimal("500.00")
        );
        testEntityManager.persist(anotherReservation);

        // Act
        List<Reservation> result = reservationDao.findByCourtNameOrderedByCreated(court.getCourtName());

        // Assert
        assertEquals(2, result.size());
        assertTrue(result.get(0).getStartTime().isBefore(result.get(1).getStartTime()));
    }

    @Test
    void findFutureByPhone_ShouldReturnFutureReservations() {
        // Arrange
        LocalDateTime now = LocalDateTime.of(2023, 5, 1, 0, 0);

        // Act
        List<Reservation> result = reservationDao.findFutureByPhone(customer.getPhoneNumber(), now);

        // Assert
        assertEquals(1, result.size());
        assertEquals(existingReservation.getId(), result.getFirst().getId());
    }

    @Test
    void findByPhone_ShouldReturnAllCustomerReservations() {
        // Act
        List<Reservation> result = reservationDao.findByPhone(customer.getPhoneNumber());

        // Assert
        assertEquals(1, result.size());
        assertEquals(existingReservation.getId(), result.getFirst().getId());
    }

    @Test
    void findById_ShouldReturnReservation_WhenExists() {
        // Act
        Optional<Reservation> result = reservationDao.findById(existingReservation.getId());

        // Assert
        assertTrue(result.isPresent());
        assertEquals(existingReservation.getId(), result.get().getId());
    }

    @Test
    void findById_ShouldReturnEmpty_WhenNotExists() {
        // Act
        Optional<Reservation> result = reservationDao.findById(999L);

        // Assert
        assertFalse(result.isPresent());
    }

    @Test
    void findAll_ShouldReturnAllNonDeletedReservations() {
        // Arrange - Add a deleted reservation
        Reservation deletedReservation = new Reservation(
                court,
                customer,
                LocalDateTime.of(2023, 6, 2, 10, 0),
                LocalDateTime.of(2023, 6, 2, 11, 0),
                GameType.SINGLES,
                new BigDecimal("500.00")
        );
        deletedReservation.setDeleted(true);
        testEntityManager.persist(deletedReservation);

        // Act
        List<Reservation> result = reservationDao.findAll();

        // Assert
        assertEquals(1, result.size());
        assertEquals(existingReservation.getId(), result.getFirst().getId());
    }

    @Test
    void save_ShouldPersistNewReservation() {
        // Arrange
        Reservation newReservation = new Reservation(
                court,
                customer,
                LocalDateTime.of(2023, 6, 3, 10, 0),
                LocalDateTime.of(2023, 6, 3, 11, 0),
                GameType.SINGLES,
                new BigDecimal("500.00")
        );

        // Act
        Reservation result = reservationDao.save(newReservation);

        // Assert
        assertNotNull(result.getId());
        Reservation found = testEntityManager.find(Reservation.class, result.getId());
        assertEquals(newReservation.getStartTime(), found.getStartTime());
    }

    @Test
    void save_ShouldUpdateExistingReservation() {
        // Arrange
        existingReservation.setGameType(GameType.DOUBLES);

        // Act
        Reservation result = reservationDao.save(existingReservation);

        // Assert
        assertEquals(existingReservation.getId(), result.getId());
        Reservation found = testEntityManager.find(Reservation.class, result.getId());
        assertEquals(GameType.DOUBLES, found.getGameType());
    }

    @Test
    void delete_ShouldSoftDeleteReservation() {
        // Act
        reservationDao.delete(existingReservation);

        // Assert
        Reservation found = testEntityManager.find(Reservation.class, existingReservation.getId());
        assertTrue(found.isDeleted());
    }
}
