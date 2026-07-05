package hu.fleetpulse.backend.web;

import hu.fleetpulse.backend.controller.PositionHistoryController;
import hu.fleetpulse.backend.exception.VehicleNotFoundException;
import hu.fleetpulse.backend.mapper.PositionHistoryMapper;
import hu.fleetpulse.backend.service.PositionHistoryService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.UUID;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PositionHistoryController.class)
class PositionHistoryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private PositionHistoryService service;

    @MockitoBean
    private PositionHistoryMapper mapper;

    @Test
    void findAllByVehicleId_WhenValidId_Returns200() throws Exception {
        UUID id = UUID.randomUUID();
        when(service.findAllByVehicleId(id)).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/v1/history/{vehicleId}", id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void findAllByVehicleId_WhenVehicleNotFound_Returns404() throws Exception {
        UUID id = UUID.randomUUID();
        when(service.findAllByVehicleId(id)).thenThrow(new VehicleNotFoundException("Not found"));

        mockMvc.perform(get("/api/v1/history/{vehicleId}", id))
                .andExpect(status().isNotFound());
    }

    @Test
    void findAllByVehicleId_WhenInvalidUuidFormat_Returns400() throws Exception {
        mockMvc.perform(get("/api/v1/history/ez-nem-egy-uuid")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }
}