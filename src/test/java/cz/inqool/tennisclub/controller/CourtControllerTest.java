package cz.inqool.tennisclub.controller;

import cz.inqool.tennisclub.dto.court.CourtCreateDto;
import cz.inqool.tennisclub.dto.court.CourtDto;
import cz.inqool.tennisclub.dto.court.CourtUpdateDto;
import cz.inqool.tennisclub.facade.CourtFacade;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CourtController.class)
class CourtControllerTest {

    private final String COURT_CREATE_JSON = """
            {
                "courtName": "Court 1",
                "surfaceTypeName": "Clay"
            }
            """;
    private final String COURT_UPDATE_JSON = """
            {
                "courtName": "Court 1 Updated",
                "surfaceTypeName": "Grass"
            }
            """;
    @Autowired
    private MockMvc mockMvc;
    @MockitoBean
    private CourtFacade courtFacade;

    @Test
    void createCourt_ShouldReturnCreatedCourt() throws Exception {
        CourtDto dto = new CourtDto(1L, "Court 1", "Clay");
        given(courtFacade.create(any(CourtCreateDto.class))).willReturn(dto);

        mockMvc.perform(post("/api/courts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(COURT_CREATE_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.courtName").value("Court 1"))
                .andExpect(jsonPath("$.surfaceTypeName").value("Clay"));
    }

    @Test
    void updateCourt_ShouldReturnUpdatedCourt() throws Exception {
        CourtDto dto = new CourtDto(1L, "Court 1 Updated", "Grass");
        given(courtFacade.update(anyString(), any(CourtUpdateDto.class))).willReturn(dto);

        mockMvc.perform(put("/api/courts/Court 1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(COURT_UPDATE_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.courtName").value("Court 1 Updated"))
                .andExpect(jsonPath("$.surfaceTypeName").value("Grass"));
    }

    @Test
    void getAllCourts_ShouldReturnAllCourts() throws Exception {
        List<CourtDto> courts = List.of(
                new CourtDto(1L, "Court 1", "Clay"),
                new CourtDto(2L, "Court 2", "Grass")
        );
        given(courtFacade.getAll()).willReturn(courts);

        mockMvc.perform(get("/api/courts"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].courtName").value("Court 1"))
                .andExpect(jsonPath("$[1].courtName").value("Court 2"));
    }

    @Test
    void getCourtById_ShouldReturnCourt() throws Exception {
        CourtDto dto = new CourtDto(1L, "Court 1", "Clay");
        given(courtFacade.getById(1L)).willReturn(dto);

        mockMvc.perform(get("/api/courts/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.courtName").value("Court 1"))
                .andExpect(jsonPath("$.surfaceTypeName").value("Clay"));
    }

    @Test
    void getCourtsBySurfaceType_ShouldReturnFilteredCourts() throws Exception {
        List<CourtDto> courts = List.of(
                new CourtDto(1L, "Clay Court 1", "Clay"),
                new CourtDto(2L, "Clay Court 2", "Clay")
        );
        given(courtFacade.getAllBySurfaceTypeName("Clay")).willReturn(courts);

        mockMvc.perform(get("/api/courts/surface-types/Clay"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].surfaceTypeName").value("Clay"))
                .andExpect(jsonPath("$[1].surfaceTypeName").value("Clay"));
    }

    @Test
    void deleteCourtByName_ShouldReturnNoContent() throws Exception {
        doNothing().when(courtFacade).deleteByName("Court 1");

        mockMvc.perform(delete("/api/courts/Court 1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void deleteCourtById_ShouldReturnNoContent() throws Exception {
        doNothing().when(courtFacade).deleteById(1L);

        mockMvc.perform(delete("/api/courts/id/1"))
                .andExpect(status().isNoContent());
    }
}