package cz.inqool.tennisclub.infrastructure.dao;

import cz.inqool.tennisclub.domain.model.SurfaceType;
import cz.inqool.tennisclub.infrastructure.dao.interfaces.SurfaceTypeDao;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@Transactional
public class SurfaceTypeDaoImpl implements SurfaceTypeDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<SurfaceType> findByName(String name) {
        return entityManager.createQuery(
                        "SELECT st FROM SurfaceType st WHERE st.name = :name AND st.deleted = false",
                        SurfaceType.class)
                .setParameter("name", name)
                .getResultStream()
                .findFirst();
    }

    @Override
    public List<SurfaceType> findAll() {
        return entityManager.createQuery(
                        "SELECT st FROM SurfaceType st WHERE st.deleted = false",
                        SurfaceType.class)
                .getResultList();
    }

    @Override
    public Optional<SurfaceType> findById(Long id) {
        SurfaceType entity = entityManager.find(SurfaceType.class, id);
        return Optional.of(entity)
                .filter(st -> !st.isDeleted());
    }

    @Override
    public SurfaceType save(SurfaceType entity) {
        if (entity.getId() != null) {
            return entityManager.merge(entity);
        }
        entityManager.persist(entity);
        return entity;
    }

    @Override
    public void delete(SurfaceType entity) {
        if (entity != null) {
            entity.setDeleted(true);
            entityManager.merge(entity);
        }
    }
}
