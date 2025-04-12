package cz.inqool.tennisclub.infrastructure.dao.interfaces;

import java.util.List;
import java.util.Optional;

public interface GenericDao<T> {
    Optional<T> findById(Long id);
    List<T> findAll();
    T save(T entity);
    void delete(T entity);
}
