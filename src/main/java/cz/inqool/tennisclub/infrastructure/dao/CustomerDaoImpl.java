package cz.inqool.tennisclub.infrastructure.dao;

import cz.inqool.tennisclub.domain.model.Customer;
import cz.inqool.tennisclub.infrastructure.dao.interfaces.CustomerDao;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import lombok.Setter;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Setter
@Transactional
@Repository
public class CustomerDaoImpl implements CustomerDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<Customer> findByPhoneNumber(String phoneNumber) {
        return entityManager.createQuery("SELECT c FROM Customer c WHERE c.phoneNumber = :phoneNumber AND c.deleted = false", Customer.class)
                .setParameter("phoneNumber", phoneNumber)
                .getResultStream()
                .findFirst();
    }

    @Override
    public Optional<Customer> findById(Long id) {
        return Optional.ofNullable(entityManager.find(Customer.class, id));
    }

    @Override
    public List<Customer> findAll() {
        return entityManager.createQuery("SELECT c FROM Customer c WHERE c.deleted = false ", Customer.class)
                .getResultList();
    }

    @Override
    public Customer save(Customer entity) {
        if (entity.getId() != null) {
            return entityManager.merge(entity);
        }
        entityManager.persist(entity);
        return entity;
    }

    @Override
    public void delete(Customer entity) {
        if (entity != null) {
            entity.setDeleted(true);
            entityManager.merge(entity);
        }
    }
}
