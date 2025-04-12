package cz.inqool.tennisclub.mapper;

import cz.inqool.tennisclub.domain.model.SurfaceType;
import cz.inqool.tennisclub.dto.surfacetype.SurfaceTypeCreateDto;
import cz.inqool.tennisclub.dto.surfacetype.SurfaceTypeDto;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SurfaceTypeMapperTest {

    private final SurfaceTypeMapper mapper = new SurfaceTypeMapper();

    @Test
    void toEntity_shouldMapCorrectly() {
        SurfaceTypeCreateDto dto = new SurfaceTypeCreateDto();
        dto.setName("Beton");
        dto.setPricePerMinute(BigDecimal.valueOf(2.5));

        SurfaceType entity = mapper.toEntity(dto);

        assertEquals("Beton", entity.getName());
        assertEquals(BigDecimal.valueOf(2.5), entity.getPricePerMinute());
    }

    @Test
    void toDto_shouldMapCorrectly() {
        SurfaceType entity = new SurfaceType();
        entity.setId(1L);
        entity.setName("Grass");
        entity.setPricePerMinute(BigDecimal.valueOf(3.0));

        SurfaceTypeDto dto = mapper.toDto(entity);

        assertEquals(1L, dto.getId());
        assertEquals("Grass", dto.getName());
        assertEquals(BigDecimal.valueOf(3.0), dto.getPricePerMinute());
    }
}

