package cz.inqool.tennisclub.facade;

import cz.inqool.tennisclub.domain.model.Court;
import cz.inqool.tennisclub.domain.model.SurfaceType;
import cz.inqool.tennisclub.domain.service.CourtService;
import cz.inqool.tennisclub.domain.service.SurfaceTypeService;
import cz.inqool.tennisclub.dto.court.CourtCreateDto;
import cz.inqool.tennisclub.dto.court.CourtDto;
import cz.inqool.tennisclub.dto.court.CourtUpdateDto;

import cz.inqool.tennisclub.mapper.CourtMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CourtFacade {

    private final CourtService courtService;
    private final SurfaceTypeService surfaceTypeService;
    private final CourtMapper courtMapper;

    @Autowired
    public CourtFacade(CourtService courtService, SurfaceTypeService surfaceTypeService, CourtMapper courtMapper) {
        this.courtService = courtService;
        this.surfaceTypeService = surfaceTypeService;
        this.courtMapper = courtMapper;
    }

    public CourtDto create(CourtCreateDto courtCreateDto) {
        SurfaceType surfaceType = surfaceTypeService.getByName(courtCreateDto.getSurfaceTypeName());

        Court court = courtMapper.toEntity(courtCreateDto);
        court.setSurfaceType(surfaceType);
        Court createdCourt = courtService.create(court);
        return courtMapper.toDto(createdCourt);
    }

    public CourtDto update(String courtName, CourtUpdateDto updateDto) {
        if (courtName == null) {
            throw new IllegalArgumentException("Court name cannot be null");
        }
        SurfaceType surfaceType = surfaceTypeService.getByName(updateDto.getSurfaceTypeName());
        Court court = courtService.getByName(courtName);

        court.setCourtName(updateDto.getCourtName());
        court.setSurfaceType(surfaceType);
        Court updatedCourt = courtService.update(court);
        return courtMapper.toDto(updatedCourt);
    }

    public List<CourtDto> getAll() {
        List<Court> courts = courtService.getAll();
        return courts.stream()
                .map(courtMapper::toDto)
                .collect(Collectors.toList());
    }

    public CourtDto getById(Long id) {
        Court court = courtService.getById(id);
        return courtMapper.toDto(court);
    }

    public List<CourtDto> getAllBySurfaceTypeName(String surfaceTypeName) {
        List<Court> courts = courtService.getAllBySurfaceTypeName(surfaceTypeName);
        return courts.stream()
                .map(courtMapper::toDto)
                .collect(Collectors.toList());
    }

    public void deleteById(Long id) {
        courtService.deleteById(id);
    }

    public void deleteByName(String courtName) {
        courtService.deleteByName(courtName);
    }
}
