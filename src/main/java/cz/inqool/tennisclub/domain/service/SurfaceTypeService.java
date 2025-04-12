package cz.inqool.tennisclub.domain.service;

import cz.inqool.tennisclub.domain.model.SurfaceType;
import cz.inqool.tennisclub.exception.surfacetype.SurfaceAlreadyExistsException;
import cz.inqool.tennisclub.exception.surfacetype.SurfaceTypeNotFoundException;
import cz.inqool.tennisclub.infrastructure.dao.interfaces.SurfaceTypeDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SurfaceTypeService {

    private final SurfaceTypeDao surfaceTypeDao;

    @Autowired
    public SurfaceTypeService(SurfaceTypeDao surfaceTypeDao) {
        this.surfaceTypeDao = surfaceTypeDao;
    }

    public SurfaceType create(SurfaceType surfaceType) {
        if (surfaceTypeDao.findByName(surfaceType.getName()).isPresent()) {
            throw new SurfaceAlreadyExistsException(surfaceType.getName());
        }

        if (surfaceType.getName() == null || surfaceType.getName().isEmpty() || surfaceType.getName().isBlank()) {
            throw new IllegalArgumentException("Surface type name cannot be null, empty or blank");
        }

        return surfaceTypeDao.save(surfaceType);
    }

    public List<SurfaceType> getAll() {
        return surfaceTypeDao.findAll();
    }

    public SurfaceType getById(Long id) {
        return surfaceTypeDao.findById(id)
                .orElseThrow(() -> new SurfaceTypeNotFoundException(id));
    }

    public void deleteById(Long id) {
        Optional<SurfaceType> surfaceType = surfaceTypeDao.findById(id);
        if (surfaceType.isEmpty()) {
            throw new SurfaceTypeNotFoundException(id);
        }
        surfaceTypeDao.delete(surfaceType.get());
    }

    public SurfaceType getByName(String name) {
        return surfaceTypeDao.findByName(name)
                .orElseThrow(() -> new SurfaceTypeNotFoundException(name));
    }
}
