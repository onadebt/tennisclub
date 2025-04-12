package cz.inqool.tennisclub.infrastructure.dao;

import cz.inqool.tennisclub.domain.model.Court;
import cz.inqool.tennisclub.infrastructure.dao.interfaces.CourtDao;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Transactional
@Repository
public class CourtDaoImpl implements CourtDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<Court> findById(Long id) {
        return Optional.ofNullable(entityManager.find(Court.class, id));
    }

    @Override
    public List<Court> findAll() {
        return entityManager.createQuery("SELECT c FROM Court c WHERE c.deleted = false", Court.class)
                .getResultList();
    }

    @Override
    public Court save(Court entity) {
        if (entity.getId() != null) {
            return entityManager.merge(entity);
        }
        entityManager.persist(entity);
        return entity;
    }

    @Override
    public void delete(Court entity) {
        if (entity != null) {
            entity.setDeleted(true);
            entityManager.merge(entity);
        }
    }

    @Override
    public Optional<Court> findByName(String courtNumber) {
        Court court = entityManager.find(Court.class, courtNumber);
        return (court != null && !court.isDeleted()) ? Optional.of(court) : Optional.empty();
    }

    @Override
    public List<Court> findAllBySurfaceTypeName(String surfaceTypeName) {
        return entityManager.createQuery("SELECT c FROM Court c WHERE c.surfaceType.name = :stn AND c.deleted = false", Court.class)
                .setParameter("stn", surfaceTypeName)
                .getResultList();
    }
}

