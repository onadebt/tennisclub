package cz.inqool.tennisclub.mapper;

import cz.inqool.tennisclub.domain.model.Court;
import cz.inqool.tennisclub.domain.model.SurfaceType;
import cz.inqool.tennisclub.domain.service.SurfaceTypeService;
import cz.inqool.tennisclub.dto.court.CourtCreateDto;
import cz.inqool.tennisclub.dto.court.CourtDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class CourtMapperTest {

    @Mock
    private SurfaceTypeService surfaceTypeService;

    @InjectMocks
    private CourtMapper courtMapper;

    @Test
    void toEntity_shouldMapCorrectly() {
        CourtCreateDto dto = new CourtCreateDto();
        dto.setCourtName("1");
        dto.setSurfaceTypeName("Clay");

        SurfaceType surfaceType = new SurfaceType();
        surfaceType.setId(1L);
        surfaceType.setName("Clay");

        Mockito.when(surfaceTypeService.getByName("Clay")).thenReturn(surfaceType);

        Court court = courtMapper.toEntity(dto);

        assertEquals("1", court.getCourtName());
        assertEquals(surfaceType, court.getSurfaceType());
    }

    @Test
    void toDto_shouldMapCorrectly() {
        SurfaceType surfaceType = new SurfaceType();
        surfaceType.setId(10L);

        Court court = new Court();
        court.setId(1L);
        court.setCourtName("2");
        court.setSurfaceType(surfaceType);

        CourtDto dto = courtMapper.toDto(court);

        assertEquals(1L, dto.getId());
        assertEquals("2", dto.getCourtNumber());
        assertEquals(10L, dto.getSurfaceTypeId());
    }
}

