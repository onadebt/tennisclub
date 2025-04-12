package cz.inqool.tennisclub.infrastructure.dao;

import cz.inqool.tennisclub.domain.model.Reservation;
import cz.inqool.tennisclub.infrastructure.dao.interfaces.ReservationDao;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Transactional
@Repository
public class ReservationDaoImpl implements ReservationDao {

    @PersistenceContext
    private EntityManager em;

    @Override
    public boolean existsOverlappingReservation(String courtName, LocalDateTime start, LocalDateTime end) {
        String jpql = """
                    SELECT COUNT(r) FROM Reservation r
                    WHERE r.court.id = :courtId AND r.deleted = false
                    AND (
                        (r.startTime < :end AND r.endTime > :start)
                    )
                """;

        Long count = em.createQuery(jpql, Long.class)
                .setParameter("courtId", courtName)
                .setParameter("start", start)
                .setParameter("end", end)
                .getSingleResult();

        return count > 0;
    }

    @Override
    public Boolean existsOverlappingReservationExcludingId(Long id, String courtName, LocalDateTime start, LocalDateTime end) {
        String jpql = """
                    SELECT COUNT(r) FROM Reservation r
                    WHERE r.id != :id AND r.court.courtName = :courtName AND r.deleted = false
                    AND (
                        (r.startTime < :end AND r.endTime > :start)
                    )
                """;

        Long count = em.createQuery(jpql, Long.class)
                .setParameter("id", id)
                .setParameter("courtName", courtName)
                .setParameter("start", start)
                .setParameter("end", end)
                .getSingleResult();

        return count > 0;
    }

    @Override
    public List<Reservation> findAllByCustomerId(Long customerId) {
        String jpql = "SELECT r FROM Reservation r WHERE r.customer.id = :customerId AND r.deleted = false";
        return em.createQuery(jpql, Reservation.class)
                .setParameter("customerId", customerId)
                .getResultList();
    }

    @Override
    public List<Reservation> findByCourtNameOrderedByCreated(String courtName) {
        return em.createQuery("SELECT r FROM Reservation r WHERE r.court.courtName = :courtName AND r.deleted = false ORDER BY r.startTime", Reservation.class)
                .setParameter("courtName", courtName)
                .getResultList();
    }

    @Override
    public List<Reservation> findFutureByPhone(String phone, LocalDateTime now) {
        String jpql = "SELECT r FROM Reservation r WHERE r.customer.phoneNumber = :phone AND r.deleted = false AND r.startTime > :now";
        return em.createQuery(jpql, Reservation.class)
                .setParameter("phone", phone)
                .setParameter("now", now)
                .getResultList();
    }

    @Override
    public List<Reservation> findByPhone(String phone) {
        String jpql = "SELECT r FROM Reservation r WHERE r.customer.phoneNumber = :phone AND r.deleted = false";
        return em.createQuery(jpql, Reservation.class)
                .setParameter("phone", phone)
                .getResultList();
    }

    @Override
    public Optional<Reservation> findById(Long id) {
        return Optional.ofNullable(em.find(Reservation.class, id));
    }

    @Override
    public List<Reservation> findAll() {
        return em.createQuery("SELECT r FROM Reservation r WHERE r.deleted = false", Reservation.class)
                .getResultList();
    }

    @Override
    public Reservation save(Reservation entity) {
        if (entity.getId() == null) {
            em.persist(entity);
            return entity;
        } else {
            return em.merge(entity);
        }
    }

    @Override
    public void delete(Reservation entity) {
        entity.setDeleted(true);
        em.merge(entity);
    }
}

