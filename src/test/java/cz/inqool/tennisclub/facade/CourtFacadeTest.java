package cz.inqool.tennisclub.facade;

import cz.inqool.tennisclub.domain.model.Court;
import cz.inqool.tennisclub.domain.model.SurfaceType;
import cz.inqool.tennisclub.domain.service.CourtService;
import cz.inqool.tennisclub.domain.service.SurfaceTypeService;
import cz.inqool.tennisclub.dto.court.CourtCreateDto;
import cz.inqool.tennisclub.dto.court.CourtDto;
import cz.inqool.tennisclub.dto.court.CourtUpdateDto;
import cz.inqool.tennisclub.mapper.CourtMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CourtFacadeTest {

    @Mock
    private CourtService courtService;

    @Mock
    private SurfaceTypeService surfaceTypeService;

    @Mock
    private CourtMapper courtMapper;

    @InjectMocks
    private CourtFacade courtFacade;

    @Test
    void create_shouldReturnMappedDto() {
        CourtCreateDto createDto = new CourtCreateDto();
        createDto.setCourtName("Court A");
        createDto.setSurfaceTypeName("Clay");

        SurfaceType surfaceType = new SurfaceType();
        Court court = new Court();
        court.setSurfaceType(surfaceType);

        Court createdCourt = new Court();
        CourtDto courtDto = new CourtDto();

        when(surfaceTypeService.getByName("Clay")).thenReturn(surfaceType);
        when(courtMapper.toEntity(createDto)).thenReturn(court);
        when(courtService.create(court)).thenReturn(createdCourt);
        when(courtMapper.toDto(createdCourt)).thenReturn(courtDto);

        CourtDto result = courtFacade.create(createDto);

        assertEquals(courtDto, result);
        verify(surfaceTypeService).getByName("Clay");
        verify(courtMapper).toEntity(createDto);
        verify(courtService).create(court);
        verify(courtMapper).toDto(createdCourt);
    }

    @Test
    void update_shouldReturnUpdatedDto() {
        String courtName = "Court A";
        CourtUpdateDto updateDto = new CourtUpdateDto();
        updateDto.setCourtName("Court B");
        updateDto.setSurfaceTypeName("Grass");

        Court existingCourt = new Court();
        SurfaceType surfaceType = new SurfaceType();
        Court updatedCourt = new Court();
        CourtDto updatedDto = new CourtDto();

        when(surfaceTypeService.getByName("Grass")).thenReturn(surfaceType);
        when(courtService.getByName(courtName)).thenReturn(existingCourt);
        when(courtService.update(existingCourt)).thenReturn(updatedCourt);
        when(courtMapper.toDto(updatedCourt)).thenReturn(updatedDto);

        CourtDto result = courtFacade.update(courtName, updateDto);

        assertEquals(updatedDto, result);
        verify(courtService).getByName(courtName);
        verify(courtService).update(existingCourt);
        verify(courtMapper).toDto(updatedCourt);
    }

    @Test
    void update_shouldThrowException_whenCourtNameIsNull() {
        CourtUpdateDto updateDto = new CourtUpdateDto();
        assertThrows(IllegalArgumentException.class, () -> courtFacade.update(null, updateDto));
    }

    @Test
    void getAll_shouldReturnMappedList() {
        List<Court> courts = List.of(new Court(), new Court());

        when(courtService.getAll()).thenReturn(courts);
        when(courtMapper.toDto(any(Court.class))).thenReturn(new CourtDto());

        List<CourtDto> result = courtFacade.getAll();

        assertEquals(2, result.size());
        verify(courtService).getAll();
        verify(courtMapper, times(2)).toDto(any(Court.class));
    }

    @Test
    void getById_shouldReturnDto() {
        Court court = new Court();
        CourtDto dto = new CourtDto();

        when(courtService.getById(1L)).thenReturn(court);
        when(courtMapper.toDto(court)).thenReturn(dto);

        CourtDto result = courtFacade.getById(1L);

        assertEquals(dto, result);
        verify(courtService).getById(1L);
        verify(courtMapper).toDto(court);
    }

    @Test
    void getAllBySurfaceTypeName_shouldMapAllCourts() {
        List<Court> courts = List.of(new Court(), new Court());
        when(courtService.getAllBySurfaceTypeName("Clay")).thenReturn(courts);
        when(courtMapper.toDto(any())).thenReturn(new CourtDto());

        List<CourtDto> result = courtFacade.getAllBySurfaceTypeName("Clay");

        assertEquals(2, result.size());
        verify(courtService).getAllBySurfaceTypeName("Clay");
        verify(courtMapper, times(2)).toDto(any());
    }

    @Test
    void deleteById_shouldInvokeService() {
        courtFacade.deleteById(1L);
        verify(courtService).deleteById(1L);
    }

    @Test
    void deleteByName_shouldInvokeService() {
        courtFacade.deleteByName("Court A");
        verify(courtService).deleteByName("Court A");
    }
}

