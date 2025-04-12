package cz.inqool.tennisclub.infrastructure.dao;

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
class SurfaceTypeDaoImplTest {

    private final BigDecimal CLAY_PRICE = new BigDecimal("0.50");
    private final BigDecimal GRASS_PRICE = new BigDecimal("0.75");
    @Autowired
    private TestEntityManager testEntityManager;
    @Autowired
    private EntityManager entityManager;
    private SurfaceTypeDaoImpl surfaceTypeDao;

    @BeforeEach
    void setUp() {
        surfaceTypeDao = new SurfaceTypeDaoImpl();
        surfaceTypeDao.setEntityManager(entityManager);
    }

    @Test
    void findByName_ShouldReturnSurfaceType_WhenExists() {
        // Arrange
        SurfaceType clay = new SurfaceType("Clay", CLAY_PRICE);
        testEntityManager.persist(clay);

        // Act
        Optional<SurfaceType> result = surfaceTypeDao.findByName("Clay");

        // Assert
        assertTrue(result.isPresent());
        assertEquals("Clay", result.get().getName());
        assertEquals(0, CLAY_PRICE.compareTo(result.get().getPricePerMinute()));
    }

    @Test
    void findByName_ShouldReturnEmptyOptional_WhenNotExists() {
        // Act
        Optional<SurfaceType> result = surfaceTypeDao.findByName("Nonexistent");

        // Assert
        assertFalse(result.isPresent());
    }

    @Test
    void findByName_ShouldNotReturnDeletedSurfaceType() {
        // Arrange
        SurfaceType deletedSurface = new SurfaceType("Deleted", CLAY_PRICE);
        deletedSurface.setDeleted(true);
        testEntityManager.persist(deletedSurface);

        // Act
        Optional<SurfaceType> result = surfaceTypeDao.findByName("Deleted");

        // Assert
        assertFalse(result.isPresent());
    }

    @Test
    void findAll_ShouldReturnAllNonDeletedSurfaceTypes() {
        // Arrange
        SurfaceType clay = new SurfaceType("Clay", CLAY_PRICE);
        SurfaceType grass = new SurfaceType("Grass", GRASS_PRICE);
        SurfaceType deleted = new SurfaceType("Deleted", CLAY_PRICE);
        deleted.setDeleted(true);

        testEntityManager.persist(clay);
        testEntityManager.persist(grass);
        testEntityManager.persist(deleted);

        // Act
        List<SurfaceType> result = surfaceTypeDao.findAll();

        // Assert
        assertEquals(2, result.size());
        assertTrue(result.stream().anyMatch(st -> st.getName().equals("Clay")));
        assertTrue(result.stream().anyMatch(st -> st.getName().equals("Grass")));
        assertFalse(result.stream().anyMatch(st -> st.getName().equals("Deleted")));
    }

    @Test
    void findById_ShouldReturnSurfaceType_WhenExistsAndNotDeleted() {
        // Arrange
        SurfaceType clay = new SurfaceType("Clay", CLAY_PRICE);
        SurfaceType saved = testEntityManager.persist(clay);

        // Act
        Optional<SurfaceType> result = surfaceTypeDao.findById(saved.getId());

        // Assert
        assertTrue(result.isPresent());
        assertEquals("Clay", result.get().getName());
        assertEquals(0, CLAY_PRICE.compareTo(result.get().getPricePerMinute()));
    }

    @Test
    void findById_ShouldReturnEmptyOptional_WhenNotExists() {
        // Act
        Optional<SurfaceType> result = surfaceTypeDao.findById(999L);

        // Assert
        assertTrue(result.isEmpty());
    }

    @Test
    void findById_ShouldReturnEmptyOptional_WhenDeleted() {
        // Arrange
        SurfaceType deleted = new SurfaceType("Deleted", CLAY_PRICE);
        deleted.setDeleted(true);
        SurfaceType saved = testEntityManager.persist(deleted);

        // Act
        Optional<SurfaceType> result = surfaceTypeDao.findById(saved.getId());

        // Assert
        assertFalse(result.isPresent());
    }

    @Test
    void save_ShouldPersistNewSurfaceType() {
        // Arrange
        SurfaceType newSurface = new SurfaceType("New Surface", GRASS_PRICE);

        // Act
        SurfaceType saved = surfaceTypeDao.save(newSurface);

        // Assert
        assertNotNull(saved.getId());
        SurfaceType found = testEntityManager.find(SurfaceType.class, saved.getId());
        assertEquals("New Surface", found.getName());
        assertEquals(0, GRASS_PRICE.compareTo(found.getPricePerMinute()));
        assertFalse(found.isDeleted());
    }

    @Test
    void save_ShouldUpdateExistingSurfaceType() {
        // Arrange
        SurfaceType original = new SurfaceType("Original", CLAY_PRICE);
        SurfaceType saved = testEntityManager.persist(original);

        saved.setName("Updated");
        saved.setPricePerMinute(GRASS_PRICE);

        // Act
        SurfaceType updated = surfaceTypeDao.save(saved);

        // Assert
        assertEquals(saved.getId(), updated.getId());
        SurfaceType found = testEntityManager.find(SurfaceType.class, saved.getId());
        assertEquals("Updated", found.getName());
        assertEquals(0, GRASS_PRICE.compareTo(found.getPricePerMinute()));
    }

    @Test
    void delete_ShouldSoftDeleteSurfaceType() {
        // Arrange
        SurfaceType toDelete = new SurfaceType("To Delete", CLAY_PRICE);
        SurfaceType saved = testEntityManager.persist(toDelete);

        // Act
        surfaceTypeDao.delete(saved);

        // Assert
        SurfaceType found = testEntityManager.find(SurfaceType.class, saved.getId());
        assertTrue(found.isDeleted());
    }

    @Test
    void delete_ShouldDoNothing_WhenSurfaceTypeIsNull() {
        // Act & Assert (should not throw exception)
        surfaceTypeDao.delete(null);
    }
}
