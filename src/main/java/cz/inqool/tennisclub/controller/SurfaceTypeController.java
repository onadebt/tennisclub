package cz.inqool.tennisclub.controller;


import cz.inqool.tennisclub.dto.surfacetype.SurfaceTypeCreateDto;
import cz.inqool.tennisclub.dto.surfacetype.SurfaceTypeDto;
import cz.inqool.tennisclub.facade.SurfaceTypeFacade;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/surface-types")
public class SurfaceTypeController {

    private final SurfaceTypeFacade surfaceTypeFacade;

    @Autowired
    public SurfaceTypeController(SurfaceTypeFacade surfaceTypeFacade) {
        this.surfaceTypeFacade = surfaceTypeFacade;
    }

    @PostMapping
    public ResponseEntity<SurfaceTypeDto> createSurfaceType(@RequestBody @Valid SurfaceTypeCreateDto surfaceTypeCreateDto) {
        SurfaceTypeDto createdSurfaceType = surfaceTypeFacade.createSurfaceType(surfaceTypeCreateDto);
        return ResponseEntity.ok(createdSurfaceType);
    }

    @GetMapping
    public ResponseEntity<List<SurfaceTypeDto>> getAllSurfaceTypes() {
        List<SurfaceTypeDto> surfaceTypes = surfaceTypeFacade.getAll();
        return ResponseEntity.ok(surfaceTypes);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SurfaceTypeDto> getSurfaceTypeById(@PathVariable Long id) {
        SurfaceTypeDto surfaceType = surfaceTypeFacade.getById(id);
        return ResponseEntity.ok(surfaceType);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSurfaceType(@PathVariable Long id) {
        surfaceTypeFacade.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
