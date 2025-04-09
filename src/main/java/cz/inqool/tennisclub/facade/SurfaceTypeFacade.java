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

    @Autowired
    private SurfaceTypeService surfaceTypeService;
    @Autowired
    private SurfaceTypeMapper surfaceTypeMapper;


    public SurfaceTypeDto createSurfaceType(SurfaceTypeCreateDto surfaceTypeCreateDto) {
        if (surfaceTypeCreateDto.getName() == null) {
            throw new IllegalArgumentException("Surface type name cannot be null");
        }

        SurfaceType newSurfaceType = surfaceTypeService.create(surfaceTypeMapper.toEntity(surfaceTypeCreateDto));
        return surfaceTypeMapper.toDto(newSurfaceType);
    }

    public SurfaceTypeDto getById(Long id) {
        SurfaceType surfaceType = surfaceTypeService.getById(id);
        return surfaceTypeMapper.toDto(surfaceType);
    }

    public SurfaceTypeDto getByName(String name) {
        SurfaceType surfaceType = surfaceTypeService.getByName(name);
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
