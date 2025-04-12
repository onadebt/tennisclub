package cz.inqool.tennisclub.facade;

import cz.inqool.tennisclub.domain.model.SurfaceType;
import cz.inqool.tennisclub.domain.service.SurfaceTypeService;
import cz.inqool.tennisclub.dto.surfacetype.SurfaceTypeCreateDto;
import cz.inqool.tennisclub.dto.surfacetype.SurfaceTypeDto;
import cz.inqool.tennisclub.mapper.SurfaceTypeMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SurfaceTypeFacade {

    private final SurfaceTypeService surfaceTypeService;
    private final SurfaceTypeMapper surfaceTypeMapper;

    @Autowired
    public SurfaceTypeFacade(SurfaceTypeService surfaceTypeService, SurfaceTypeMapper surfaceTypeMapper) {
        this.surfaceTypeService = surfaceTypeService;
        this.surfaceTypeMapper = surfaceTypeMapper;
    }


    public SurfaceTypeDto createSurfaceType(SurfaceTypeCreateDto surfaceTypeCreateDto) {
        SurfaceType newSurfaceType = surfaceTypeService.create(surfaceTypeMapper.toEntity(surfaceTypeCreateDto));
        return surfaceTypeMapper.toDto(newSurfaceType);
    }

    public SurfaceTypeDto getById(Long id) {
        SurfaceType surfaceType = surfaceTypeService.getById(id);
        return surfaceTypeMapper.toDto(surfaceType);
    }

    public List<SurfaceTypeDto> getAll() {
        List<SurfaceType> surfaceTypes = surfaceTypeService.getAll();
        return surfaceTypes.stream()
                .map(surfaceTypeMapper::toDto)
                .collect(Collectors.toList());
    }

    public void deleteById(Long id) {
        surfaceTypeService.deleteById(id);
    }
}
