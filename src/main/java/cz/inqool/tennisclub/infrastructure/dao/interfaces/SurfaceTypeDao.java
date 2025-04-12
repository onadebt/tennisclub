package cz.inqool.tennisclub.infrastructure.dao.interfaces;

import cz.inqool.tennisclub.domain.model.SurfaceType;

import java.util.Optional;

public interface SurfaceTypeDao extends GenericDao<SurfaceType> {
    Optional<SurfaceType> findByName(String name);
}
