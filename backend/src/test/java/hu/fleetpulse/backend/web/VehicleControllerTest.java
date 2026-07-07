package hu.fleetpulse.backend.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import hu.fleetpulse.backend.controller.VehicleController;
import hu.fleetpulse.backend.dto.request.FindVehiclesInRadiusRequest;
import hu.fleetpulse.backend.dto.request.PositionUpdateRequest;
import hu.fleetpulse.backend.mapper.VehicleMapper;
import hu.fleetpulse.backend.service.VehicleService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(VehicleController.class)
class VehicleControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private VehicleService vehicleService;

    @MockitoBean
    private VehicleMapper vehicleMapper;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void create_Returns200() throws Exception {
        when(vehicleService.create()).thenReturn(UUID.randomUUID());

        mockMvc.perform(post("/vehicles"))
                .andExpect(status().isOk());
    }

    @Test
    void updatePosition_WhenValidRequest_Returns200() throws Exception {
        UUID id = UUID.randomUUID();
        PositionUpdateRequest request = new PositionUpdateRequest(47.0, 19.0);
        doNothing().when(vehicleService).updatePosition(any(UUID.class), any(PositionUpdateRequest.class));

        mockMvc.perform(post("/vehicle/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
    }

    @Test
    void updatePosition_WhenInvalidIdFormat_Returns400() throws Exception {
        mockMvc.perform(post("/vehicle/invalid-uuid"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void findAllByRadius_WhenValidRequest_Returns200() throws Exception {
        when(vehicleService.findAllByRadius(any())).thenReturn(Collections.emptyList());
        mockMvc.perform(get("/vehicles")
                        .param("latitude", "47.0")
                        .param("longitude", "19.0")
                        .param("radius", "10.0"))
                .andExpect(status().isOk());
    }

    @Test
    void findAll_Returns200() throws Exception {
        when(vehicleService.findAll()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/")) // A kontrollerben @GetMapping van út nélkül
                .andExpect(status().isOk());
    }
}