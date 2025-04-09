package cz.inqool.tennisclub.mapper;

import cz.inqool.tennisclub.domain.model.SurfaceType;
import cz.inqool.tennisclub.dto.surfacetype.SurfaceTypeCreateDto;
import cz.inqool.tennisclub.dto.surfacetype.SurfaceTypeDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface SurfaceTypeMapper {
     SurfaceTypeDto toDto(SurfaceType surfaceType);
     SurfaceType toEntity(SurfaceTypeDto surfaceTypeDto);
     SurfaceType toEntity(SurfaceTypeCreateDto surfaceTypeCreateDto);
}
