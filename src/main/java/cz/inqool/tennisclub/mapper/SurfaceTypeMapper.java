package cz.inqool.tennisclub.mapper;

import cz.inqool.tennisclub.domain.model.SurfaceType;
import cz.inqool.tennisclub.dto.surfacetype.SurfaceTypeCreateDto;
import cz.inqool.tennisclub.dto.surfacetype.SurfaceTypeDto;
import org.springframework.stereotype.Component;

@Component
public class SurfaceTypeMapper {
    public SurfaceType toEntity(SurfaceTypeCreateDto surfaceTypeCreateDto) {
        SurfaceType surfaceType = new SurfaceType();
        surfaceType.setName(surfaceTypeCreateDto.getName());
        surfaceType.setPricePerMinute(surfaceTypeCreateDto.getPricePerMinute());
        return surfaceType;
    }

    public SurfaceTypeDto toDto(SurfaceType surfaceType) {
        SurfaceTypeDto surfaceTypeDto = new SurfaceTypeDto();
        surfaceTypeDto.setId(surfaceType.getId());
        surfaceTypeDto.setName(surfaceType.getName());
        surfaceTypeDto.setPricePerMinute(surfaceType.getPricePerMinute());
        return surfaceTypeDto;
    }
}
