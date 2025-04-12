package cz.inqool.tennisclub.infrastructure.dao.interfaces;

import cz.inqool.tennisclub.domain.model.Court;

import java.util.List;
import java.util.Optional;

public interface CourtDao extends GenericDao<Court> {
    Optional<Court> findByName(String courtName);
    List<Court> findAllBySurfaceTypeName(String surfaceTypeName);
    List<Court> findAllBySurfaceTypeId(Long surfaceTypeId);

}

