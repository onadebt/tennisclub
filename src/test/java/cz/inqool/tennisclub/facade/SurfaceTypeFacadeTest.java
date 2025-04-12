package cz.inqool.tennisclub.facade;

import cz.inqool.tennisclub.domain.model.SurfaceType;
import cz.inqool.tennisclub.domain.service.SurfaceTypeService;
import cz.inqool.tennisclub.dto.surfacetype.SurfaceTypeCreateDto;
import cz.inqool.tennisclub.dto.surfacetype.SurfaceTypeDto;
import cz.inqool.tennisclub.mapper.SurfaceTypeMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SurfaceTypeFacadeTest {

    @Mock
    private SurfaceTypeService surfaceTypeService;

    @Mock
    private SurfaceTypeMapper surfaceTypeMapper;

    @InjectMocks
    private SurfaceTypeFacade surfaceTypeFacade;

    @Test
    void createSurfaceType_shouldReturnDto() {
        SurfaceTypeCreateDto createDto = new SurfaceTypeCreateDto();
        SurfaceType entity = new SurfaceType();
        SurfaceType saved = new SurfaceType();
        SurfaceTypeDto dto = new SurfaceTypeDto();

        when(surfaceTypeMapper.toEntity(createDto)).thenReturn(entity);
        when(surfaceTypeService.create(entity)).thenReturn(saved);
        when(surfaceTypeMapper.toDto(saved)).thenReturn(dto);

        SurfaceTypeDto result = surfaceTypeFacade.createSurfaceType(createDto);

        assertEquals(dto, result);
        verify(surfaceTypeMapper).toEntity(createDto);
        verify(surfaceTypeService).create(entity);
        verify(surfaceTypeMapper).toDto(saved);
    }

    @Test
    void getById_shouldReturnMappedDto() {
        SurfaceType entity = new SurfaceType();
        SurfaceTypeDto dto = new SurfaceTypeDto();

        when(surfaceTypeService.getById(1L)).thenReturn(entity);
        when(surfaceTypeMapper.toDto(entity)).thenReturn(dto);

        SurfaceTypeDto result = surfaceTypeFacade.getById(1L);

        assertEquals(dto, result);
        verify(surfaceTypeService).getById(1L);
        verify(surfaceTypeMapper).toDto(entity);
    }

    @Test
    void getAll_shouldReturnAllMapped() {
        List<SurfaceType> list = List.of(new SurfaceType(), new SurfaceType());
        when(surfaceTypeService.getAll()).thenReturn(list);
        when(surfaceTypeMapper.toDto(any())).thenReturn(new SurfaceTypeDto());

        List<SurfaceTypeDto> result = surfaceTypeFacade.getAll();

        assertEquals(2, result.size());
        verify(surfaceTypeService).getAll();
        verify(surfaceTypeMapper, times(2)).toDto(any());
    }

    @Test
    void deleteById_shouldInvokeService() {
        surfaceTypeFacade.deleteById(42L);
        verify(surfaceTypeService).deleteById(42L);
    }
}
