package cz.inqool.tennisclub.domain.service;

import cz.inqool.tennisclub.domain.model.Court;
import cz.inqool.tennisclub.exception.court.CourtAlreadyExistsException;
import cz.inqool.tennisclub.exception.court.CourtNotFoundException;
import cz.inqool.tennisclub.infrastructure.dao.interfaces.CourtDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CourtService {

    private final CourtDao courtDao;

    @Autowired
    public CourtService(CourtDao courtDao) {
        this.courtDao = courtDao;
    }

    public Court create(Court court) {
        if (courtDao.findByName(court.getCourtName()).isPresent()) {
            throw new CourtAlreadyExistsException(court.getCourtName());
        }
        return courtDao.save(court);
    }

    public Court update(Court court) {
        if (courtDao.findById(court.getId()).isEmpty()) {
            throw new CourtNotFoundException(court.getCourtName());
        }
        return courtDao.save(court);
    }

    public Court getById(Long id) {
        return courtDao.findById(id).
                orElseThrow(() -> new CourtNotFoundException(id));
    }

    public Court getByName(String courtName) {
        return courtDao.findByName(courtName).
                orElseThrow(() -> new CourtNotFoundException(courtName));
    }

    public void deleteById(Long id) {
        Court court = courtDao.findById(id).
                orElseThrow(() -> new CourtNotFoundException(id));

        court.setDeleted(true);
        courtDao.save(court);
    }

    public void deleteByName(String courtName) {
        Court court = courtDao.findByName(courtName).
                orElseThrow(() -> new CourtNotFoundException(courtName));

        court.setDeleted(true);
        courtDao.save(court);
    }

    public List<Court> getAll() {
        return courtDao.findAll();
    }

    public List<Court> getAllBySurfaceTypeName(String surfaceTypeName) {
        return courtDao.findAllBySurfaceTypeName(surfaceTypeName);
    }
}
