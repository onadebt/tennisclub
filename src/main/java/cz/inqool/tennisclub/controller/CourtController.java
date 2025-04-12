package cz.inqool.tennisclub.controller;

import cz.inqool.tennisclub.dto.court.CourtCreateDto;
import cz.inqool.tennisclub.dto.court.CourtDto;
import cz.inqool.tennisclub.dto.court.CourtUpdateDto;
import cz.inqool.tennisclub.facade.CourtFacade;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/courts")
public class CourtController {

    private final CourtFacade courtFacade;

    @Autowired
    public CourtController(CourtFacade courtFacade) {
        this.courtFacade = courtFacade;
    }

    @PostMapping
    public ResponseEntity<CourtDto> create(@RequestBody @Valid CourtCreateDto courtDto) {
        return new ResponseEntity<>(courtFacade.create(courtDto), HttpStatus.CREATED);
    }

    @PutMapping("/{courtName}")
    public ResponseEntity<CourtDto> update(@PathVariable String courtName, @RequestBody @Valid CourtUpdateDto courtDto) {
        return ResponseEntity.ok(courtFacade.update(courtName, courtDto));
    }

    @GetMapping
    public ResponseEntity<List<CourtDto>> getAll() {
        return ResponseEntity.ok(courtFacade.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CourtDto> getById(@PathVariable Long id) {
        return ResponseEntity.ok(courtFacade.getById(id));
    }

    @GetMapping("/surface-types/{surfaceTypeName}")
    public ResponseEntity<List<CourtDto>> getAllBySurfaceType(@PathVariable String surfaceTypeName) {
        return ResponseEntity.ok(courtFacade.getAllBySurfaceTypeName(surfaceTypeName));
    }

    @DeleteMapping("/{courtName}")
    public ResponseEntity<Void> deleteByName(@PathVariable String courtName) {
        courtFacade.deleteByName(courtName);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/id/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        courtFacade.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
