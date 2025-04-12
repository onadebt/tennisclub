package cz.inqool.tennisclub.domain.service;

import cz.inqool.tennisclub.domain.model.SurfaceType;
import cz.inqool.tennisclub.exception.surfacetype.SurfaceAlreadyExistsException;
import cz.inqool.tennisclub.exception.surfacetype.SurfaceTypeNotFoundException;
import cz.inqool.tennisclub.infrastructure.dao.interfaces.SurfaceTypeDao;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SurfaceTypeServiceTest {

    @Mock
    private SurfaceTypeDao surfaceTypeDao;

    @InjectMocks
    private SurfaceTypeService surfaceTypeService;

    private SurfaceType clayCourt;
    private SurfaceType grassCourt;

    @BeforeEach
    void setUp() {
        clayCourt = new SurfaceType("Clay", BigDecimal.valueOf(1.0));
        grassCourt = new SurfaceType("Grass",  BigDecimal.valueOf(1.5));
    }

    @Test
    void create_ShouldThrow_WhenSurfaceTypeAlreadyExists() {
        // Given
        when(surfaceTypeDao.findByName(clayCourt.getName())).thenReturn(Optional.of(clayCourt));

        // When & Then
        assertThrows(SurfaceAlreadyExistsException.class, () -> surfaceTypeService.create(clayCourt));
    }

    @Test
    void create_ShouldThrow_WhenSurfaceTypeNameIsNull() {
        // Given
        SurfaceType surfaceType = new SurfaceType();
        surfaceType.setName(null);

        // When & Then
        assertThrows(IllegalArgumentException.class, () -> surfaceTypeService.create(surfaceType));
    }

    @Test
    void create_ShouldThrow_WhenSurfaceTypeNameIsEmpty() {
        // Given
        SurfaceType surfaceType = new SurfaceType();
        surfaceType.setName("");

        // When & Then
        assertThrows(IllegalArgumentException.class, () -> surfaceTypeService.create(surfaceType));
    }

    @Test
    void create_ShouldThrow_WhenSurfaceTypeNameIsBlank() {
        // Given
        SurfaceType surfaceType = new SurfaceType();
        surfaceType.setName("   ");

        // When & Then
        assertThrows(IllegalArgumentException.class, () -> surfaceTypeService.create(surfaceType));
    }
    @Test
    void create_ShouldSaveNewSurfaceType_WhenNameDoesNotExist() {
        // Arrange
        when(surfaceTypeDao.findByName("Clay")).thenReturn(Optional.empty());
        when(surfaceTypeDao.save(clayCourt)).thenReturn(clayCourt);

        // Act
        SurfaceType result = surfaceTypeService.create(clayCourt);

        // Assert
        assertEquals(clayCourt, result);
        verify(surfaceTypeDao).findByName("Clay");
        verify(surfaceTypeDao).save(clayCourt);
    }

    @Test
    void create_ShouldThrowException_WhenNameAlreadyExists() {
        // Arrange
        when(surfaceTypeDao.findByName("Clay")).thenReturn(Optional.of(clayCourt));

        // Act & Assert
        assertThrows(SurfaceAlreadyExistsException.class, () -> {
            surfaceTypeService.create(clayCourt);
        });

        verify(surfaceTypeDao).findByName("Clay");
        verify(surfaceTypeDao, never()).save(any());
    }

    // GetAll Tests
    @Test
    void getAll_ShouldReturnAllSurfaceTypes() {
        // Arrange
        List<SurfaceType> expected = List.of(clayCourt, grassCourt);
        when(surfaceTypeDao.findAll()).thenReturn(expected);

        // Act
        List<SurfaceType> result = surfaceTypeService.getAll();

        // Assert
        assertEquals(2, result.size());
        assertEquals(expected, result);
        verify(surfaceTypeDao).findAll();
    }

    // GetById Tests
    @Test
    void getById_ShouldReturnSurfaceType_WhenExists() {
        // Arrange
        when(surfaceTypeDao.findById(1L)).thenReturn(Optional.of(clayCourt));

        // Act
        SurfaceType result = surfaceTypeService.getById(1L);

        // Assert
        assertEquals(clayCourt, result);
        verify(surfaceTypeDao).findById(1L);
    }

    @Test
    void getById_ShouldThrowException_WhenNotFound() {
        // Arrange
        when(surfaceTypeDao.findById(99L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(SurfaceTypeNotFoundException.class, () -> {
            surfaceTypeService.getById(99L);
        });

        verify(surfaceTypeDao).findById(99L);
    }

    // Delete Tests
    @Test
    void deleteById_ShouldDeleteSurfaceType_WhenExists() {
        // Arrange
        when(surfaceTypeDao.findById(1L)).thenReturn(Optional.of(clayCourt));
        doNothing().when(surfaceTypeDao).delete(clayCourt);

        // Act
        surfaceTypeService.deleteById(1L);

        // Assert
        verify(surfaceTypeDao).findById(1L);
        verify(surfaceTypeDao).delete(clayCourt);
    }

    @Test
    void deleteById_ShouldThrowException_WhenNotFound() {
        // Arrange
        when(surfaceTypeDao.findById(99L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(SurfaceTypeNotFoundException.class, () -> {
            surfaceTypeService.deleteById(99L);
        });

        verify(surfaceTypeDao).findById(99L);
        verify(surfaceTypeDao, Mockito.never()).delete(any());
    }

    // GetByName Tests
    @Test
    void getByName_ShouldReturnSurfaceType_WhenExists() {
        // Arrange
        when(surfaceTypeDao.findByName("Clay")).thenReturn(Optional.of(clayCourt));

        // Act
        SurfaceType result = surfaceTypeService.getByName("Clay");

        // Assert
        assertEquals(clayCourt, result);
        verify(surfaceTypeDao).findByName("Clay");
    }

    @Test
    void getByName_ShouldThrowException_WhenNotFound() {
        // Arrange
        when(surfaceTypeDao.findByName("Nonexistent")).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(SurfaceTypeNotFoundException.class, () -> {
            surfaceTypeService.getByName("Nonexistent");
        });

        verify(surfaceTypeDao).findByName("Nonexistent");
    }
}