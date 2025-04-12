package cz.inqool.tennisclub.domain.service;

import cz.inqool.tennisclub.domain.model.Court;
import cz.inqool.tennisclub.domain.model.SurfaceType;
import cz.inqool.tennisclub.exception.court.CourtAlreadyExistsException;
import cz.inqool.tennisclub.exception.court.CourtNotFoundException;
import cz.inqool.tennisclub.infrastructure.dao.interfaces.CourtDao;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CourtServiceTest {

    private final Long ID = 1L;
    private final String COURT_NAME = "1";
    private final String SURFACE_TYPE_NAME = "Clay";
    @Mock
    private CourtDao courtDao;
    @InjectMocks
    private CourtService courtService;
    private Court existingCourt;
    private Court newCourt;

    @BeforeEach
    void setUp() {
        SurfaceType surfaceType = new SurfaceType();
        surfaceType.setName(SURFACE_TYPE_NAME);

        existingCourt = new Court();
        existingCourt.setId(ID);
        existingCourt.setCourtName(COURT_NAME);
        existingCourt.setSurfaceType(surfaceType);
        existingCourt.setDeleted(false);

        newCourt = new Court();
        newCourt.setCourtName(COURT_NAME);
        newCourt.setSurfaceType(surfaceType);
    }

    @Test
    void create_ShouldSaveNewCourt_WhenNameDoesNotExist() {
        when(courtDao.findByName(COURT_NAME)).thenReturn(Optional.empty());
        when(courtDao.save(newCourt)).thenReturn(existingCourt);

        Court result = courtService.create(newCourt);

        assertEquals(existingCourt, result);
        verify(courtDao).findByName(COURT_NAME);
        verify(courtDao).save(newCourt);
    }

    @Test
    void create_ShouldThrowException_WhenNameAlreadyExists() {
        when(courtDao.findByName(COURT_NAME)).thenReturn(Optional.of(existingCourt));

        assertThrows(CourtAlreadyExistsException.class, () -> courtService.create(newCourt));

        verify(courtDao).findByName(COURT_NAME);
        verify(courtDao, never()).save(any());
    }

    @Test
    void update_ShouldSaveCourt_WhenCourtExists() {
        when(courtDao.findById(ID)).thenReturn(Optional.of(existingCourt));
        when(courtDao.save(existingCourt)).thenReturn(existingCourt);

        Court result = courtService.update(existingCourt);

        assertEquals(existingCourt, result);
        verify(courtDao).findById(ID);
        verify(courtDao).save(existingCourt);
    }

    @Test
    void update_ShouldThrowException_WhenCourtNotFound() {
        when(courtDao.findById(ID)).thenReturn(Optional.empty());

        assertThrows(CourtNotFoundException.class, () -> courtService.update(existingCourt));

        verify(courtDao).findById(ID);
        verify(courtDao, never()).save(any());
    }

    @Test
    void getById_ShouldReturnCourt_WhenExists() {
        when(courtDao.findById(ID)).thenReturn(Optional.of(existingCourt));

        Court result = courtService.getById(ID);

        assertEquals(existingCourt, result);
        verify(courtDao).findById(ID);
    }

    @Test
    void getById_ShouldThrowException_WhenNotFound() {
        when(courtDao.findById(ID)).thenReturn(Optional.empty());

        assertThrows(CourtNotFoundException.class, () -> courtService.getById(ID));

        verify(courtDao).findById(ID);
    }

    @Test
    void getByName_ShouldReturnCourt_WhenExists() {
        when(courtDao.findByName(COURT_NAME)).thenReturn(Optional.of(existingCourt));

        Court result = courtService.getByName(COURT_NAME);

        assertEquals(existingCourt, result);
        verify(courtDao).findByName(COURT_NAME);
    }

    @Test
    void getByName_ShouldThrowException_WhenNotFound() {
        when(courtDao.findByName(COURT_NAME)).thenReturn(Optional.empty());

        assertThrows(CourtNotFoundException.class, () -> courtService.getByName(COURT_NAME));

        verify(courtDao).findByName(COURT_NAME);
    }

    @Test
    void deleteById_ShouldSoftDeleteCourt_WhenExists() {
        when(courtDao.findById(ID)).thenReturn(Optional.of(existingCourt));
        when(courtDao.save(existingCourt)).thenReturn(existingCourt);

        courtService.deleteById(ID);

        assertTrue(existingCourt.isDeleted());
        verify(courtDao).findById(ID);
        verify(courtDao).save(existingCourt);
    }

    @Test
    void deleteById_ShouldThrowException_WhenNotFound() {
        when(courtDao.findById(ID)).thenReturn(Optional.empty());

        assertThrows(CourtNotFoundException.class, () -> courtService.deleteById(ID));

        verify(courtDao).findById(ID);
        verify(courtDao, never()).save(any());
    }

    @Test
    void deleteByName_ShouldSoftDeleteCourt_WhenExists() {
        when(courtDao.findByName(COURT_NAME)).thenReturn(Optional.of(existingCourt));
        when(courtDao.save(existingCourt)).thenReturn(existingCourt);

        courtService.deleteByName(COURT_NAME);

        assertTrue(existingCourt.isDeleted());
        verify(courtDao).findByName(COURT_NAME);
        verify(courtDao).save(existingCourt);
    }

    @Test
    void deleteByName_ShouldThrowException_WhenNotFound() {
        when(courtDao.findByName(COURT_NAME)).thenReturn(Optional.empty());

        assertThrows(CourtNotFoundException.class, () -> courtService.deleteByName(COURT_NAME));

        verify(courtDao).findByName(COURT_NAME);
        verify(courtDao, never()).save(any());
    }

    @Test
    void getAll_ShouldReturnAllCourts() {
        List<Court> courts = List.of(existingCourt);
        when(courtDao.findAll()).thenReturn(courts);

        List<Court> result = courtService.getAll();

        assertEquals(1, result.size());
        assertEquals(existingCourt, result.getFirst());
        verify(courtDao).findAll();
    }

    @Test
    void getAllBySurfaceTypeName_ShouldReturnCourts_WhenFound() {
        List<Court> courts = List.of(existingCourt);
        when(courtDao.findAllBySurfaceTypeName(SURFACE_TYPE_NAME)).thenReturn(courts);

        List<Court> result = courtService.getAllBySurfaceTypeName(SURFACE_TYPE_NAME);

        assertEquals(1, result.size());
        assertEquals(existingCourt, result.getFirst());
        verify(courtDao).findAllBySurfaceTypeName(SURFACE_TYPE_NAME);
    }

    @Test
    void getAllBySurfaceTypeName_ShouldReturnEmptyList_WhenNoneFound() {
        when(courtDao.findAllBySurfaceTypeName(SURFACE_TYPE_NAME)).thenReturn(List.of());

        List<Court> result = courtService.getAllBySurfaceTypeName(SURFACE_TYPE_NAME);

        assertTrue(result.isEmpty());
        verify(courtDao).findAllBySurfaceTypeName(SURFACE_TYPE_NAME);
    }
}
