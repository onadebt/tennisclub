package cz.inqool.tennisclub.controller;

import cz.inqool.tennisclub.dto.surfacetype.SurfaceTypeCreateDto;
import cz.inqool.tennisclub.dto.surfacetype.SurfaceTypeDto;
import cz.inqool.tennisclub.facade.SurfaceTypeFacade;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(SurfaceTypeController.class)
class SurfaceTypeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private SurfaceTypeFacade surfaceTypeFacade;

    private final String SURFACE_TYPE_JSON = """
        {
            "name": "Clay",
            "pricePerMinute": 0.50
        }
        """;

    @Test
    void createSurfaceType_ShouldReturnCreatedSurfaceType() throws Exception {
        SurfaceTypeDto dto = new SurfaceTypeDto(1L, "Clay", new BigDecimal("0.50"));
        given(surfaceTypeFacade.createSurfaceType(any(SurfaceTypeCreateDto.class))).willReturn(dto);

        mockMvc.perform(post("/api/surface-types")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(SURFACE_TYPE_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Clay"))
                .andExpect(jsonPath("$.pricePerMinute").value(0.50));
    }

    @Test
    void getAllSurfaceTypes_ShouldReturnAllSurfaceTypes() throws Exception {
        List<SurfaceTypeDto> surfaceTypes = List.of(
                new SurfaceTypeDto(1L, "Clay", new BigDecimal("0.50")),
                new SurfaceTypeDto(2L, "Grass", new BigDecimal("0.75"))
        );
        given(surfaceTypeFacade.getAll()).willReturn(surfaceTypes);

        mockMvc.perform(get("/api/surface-types"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].name").value("Clay"))
                .andExpect(jsonPath("$[1].name").value("Grass"));
    }

    @Test
    void getSurfaceTypeById_ShouldReturnSurfaceType() throws Exception {
        SurfaceTypeDto dto = new SurfaceTypeDto(1L, "Clay", new BigDecimal("0.50"));
        given(surfaceTypeFacade.getById(1L)).willReturn(dto);

        mockMvc.perform(get("/api/surface-types/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Clay"))
                .andExpect(jsonPath("$.pricePerMinute").value(0.50));
    }

    @Test
    void deleteSurfaceType_ShouldReturnNoContent() throws Exception {
        doNothing().when(surfaceTypeFacade).deleteById(1L);

        mockMvc.perform(delete("/api/surface-types/1"))
                .andExpect(status().isNoContent());
    }
}
