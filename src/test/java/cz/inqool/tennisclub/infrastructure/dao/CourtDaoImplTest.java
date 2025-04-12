package cz.inqool.tennisclub.infrastructure.dao;

import cz.inqool.tennisclub.domain.model.Court;
import cz.inqool.tennisclub.domain.model.SurfaceType;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
class CourtDaoImplTest {

    @Autowired
    private TestEntityManager testEntityManager;

    @Autowired
    private EntityManager entityManager;

    private CourtDaoImpl courtDao;

    @BeforeEach
    void setUp() {
        courtDao = new CourtDaoImpl();
        courtDao.setEntityManager(entityManager);
    }

    @Test
    void findById_ShouldReturnCourt_WhenCourtExists() {
        // Arrange
        SurfaceType claySurface = new SurfaceType("Clay", BigDecimal.valueOf(1.0));
        testEntityManager.persist(claySurface);

        Court court = new Court();
        court.setCourtName("Court 1");
        court.setSurfaceType(claySurface);
        Court savedCourt = testEntityManager.persist(court);

        // Act
        Optional<Court> result = courtDao.findById(savedCourt.getId());

        // Assert
        assertTrue(result.isPresent());
        assertEquals("Court 1", result.get().getCourtName());
        assertEquals(claySurface, result.get().getSurfaceType());
    }

    @Test
    void findById_ShouldReturnEmptyOptional_WhenCourtDoesNotExist() {
        // Act
        Optional<Court> result = courtDao.findById(999L);

        // Assert
        assertFalse(result.isPresent());
    }

    @Test
    void findAll_ShouldReturnAllNonDeletedCourts() {
        // Arrange
        SurfaceType surfaceType = new SurfaceType("Grass", BigDecimal.valueOf(1.0));
        testEntityManager.persist(surfaceType);

        Court court1 = new Court("Court 1", surfaceType);
        Court court2 = new Court("Court 2", surfaceType);
        testEntityManager.persist(court1);
        testEntityManager.persist(court2);

        // Act
        List<Court> result = courtDao.findAll();

        // Assert
        assertEquals(2, result.size());
        assertTrue(result.stream().anyMatch(c -> c.getCourtName().equals("Court 1")));
        assertTrue(result.stream().anyMatch(c -> c.getCourtName().equals("Court 2")));
    }

    @Test
    void findAll_ShouldNotReturnDeletedCourts() {
        // Arrange
        SurfaceType surfaceType = new SurfaceType("Hard", BigDecimal.valueOf(0.5));
        testEntityManager.persist(surfaceType);

        Court activeCourt = new Court("Active Court", surfaceType);
        Court deletedCourt = new Court("Deleted Court", surfaceType);
        deletedCourt.setDeleted(true);

        testEntityManager.persist(activeCourt);
        testEntityManager.persist(deletedCourt);

        // Act
        List<Court> result = courtDao.findAll();

        // Assert
        assertEquals(1, result.size());
        assertEquals("Active Court", result.getFirst().getCourtName());
    }

    @Test
    void save_ShouldPersistNewCourt() {
        // Arrange
        SurfaceType surfaceType = new SurfaceType("Clay", BigDecimal.valueOf(1.0));
        testEntityManager.persist(surfaceType);

        Court newCourt = new Court();
        newCourt.setCourtName("New Court");
        newCourt.setSurfaceType(surfaceType);

        // Act
        Court savedCourt = courtDao.save(newCourt);

        // Assert
        assertNotNull(savedCourt.getId());
        Court foundCourt = testEntityManager.find(Court.class, savedCourt.getId());
        assertEquals("New Court", foundCourt.getCourtName());
        assertEquals(surfaceType, foundCourt.getSurfaceType());
    }

    @Test
    void save_ShouldUpdateExistingCourt() {
        // Arrange
        SurfaceType surfaceType = new SurfaceType("Grass", BigDecimal.valueOf(1.0));
        testEntityManager.persist(surfaceType);

        Court originalCourt = new Court("Original Name", surfaceType);
        Court savedCourt = testEntityManager.persist(originalCourt);

        savedCourt.setCourtName("Updated Name");

        // Act
        Court updatedCourt = courtDao.save(savedCourt);

        // Assert
        assertEquals(savedCourt.getId(), updatedCourt.getId());
        Court foundCourt = testEntityManager.find(Court.class, savedCourt.getId());
        assertEquals("Updated Name", foundCourt.getCourtName());
    }

    @Test
    void delete_ShouldSoftDeleteCourt() {
        // Arrange
        SurfaceType surfaceType = new SurfaceType("Hard", BigDecimal.valueOf(0.5));
        testEntityManager.persist(surfaceType);

        Court court = new Court("Court to Delete", surfaceType);
        Court savedCourt = testEntityManager.persist(court);

        // Act
        courtDao.delete(savedCourt);

        // Assert
        Court foundCourt = testEntityManager.find(Court.class, savedCourt.getId());
        assertTrue(foundCourt.isDeleted());
    }

    @Test
    void findAllBySurfaceTypeName_ShouldReturnCourtsWithMatchingSurfaceType() {
        // Arrange
        SurfaceType clay = new SurfaceType("Clay", BigDecimal.valueOf(1.0));
        SurfaceType grass = new SurfaceType("Grass", BigDecimal.valueOf(2.0));
        testEntityManager.persist(clay);
        testEntityManager.persist(grass);

        Court court1 = new Court("Clay Court 1", clay);
        Court court2 = new Court("Clay Court 2", clay);
        Court court3 = new Court("Grass Court", grass);
        testEntityManager.persist(court1);
        testEntityManager.persist(court2);
        testEntityManager.persist(court3);

        // Act
        List<Court> result = courtDao.findAllBySurfaceTypeName("Clay");

        // Assert
        assertEquals(2, result.size());
        assertTrue(result.stream().allMatch(c -> c.getSurfaceType().getName().equals("Clay")));
    }

    @Test
    void findAllBySurfaceTypeName_ShouldNotReturnDeletedCourts() {
        // Arrange
        SurfaceType surfaceType = new SurfaceType("Clay", BigDecimal.valueOf(1.0));
        testEntityManager.persist(surfaceType);

        Court activeCourt = new Court("Active Clay Court", surfaceType);
        Court deletedCourt = new Court("Deleted Clay Court", surfaceType);
        deletedCourt.setDeleted(true);
        testEntityManager.persist(activeCourt);
        testEntityManager.persist(deletedCourt);

        // Act
        List<Court> result = courtDao.findAllBySurfaceTypeName("Clay");

        // Assert
        assertEquals(1, result.size());
        assertEquals("Active Clay Court", result.getFirst().getCourtName());
    }
}