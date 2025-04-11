package cz.inqool.tennisclub.mapper;

import cz.inqool.tennisclub.domain.model.Court;
import cz.inqool.tennisclub.domain.service.SurfaceTypeService;
import cz.inqool.tennisclub.dto.court.CourtCreateDto;
import cz.inqool.tennisclub.dto.court.CourtDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CourtMapper {

    private final SurfaceTypeService surfaceTypeService;

    @Autowired
    public CourtMapper(SurfaceTypeService surfaceTypeService) {
        this.surfaceTypeService = surfaceTypeService;
    }

    public Court toEntity(CourtCreateDto createDto) {
        Court court = new Court();
        court.setCourtName(createDto.getCourtName());
        court.setSurfaceType(surfaceTypeService.getByName(createDto.getSurfaceTypeName()));
        return court;
    }

    public CourtDto toDto(Court court) {
        CourtDto courtDto = new CourtDto();
        courtDto.setId(court.getId());
        courtDto.setCourtNumber(court.getCourtName());
        courtDto.setSurfaceTypeId(court.getSurfaceType().getId());
        return courtDto;
    }
}
