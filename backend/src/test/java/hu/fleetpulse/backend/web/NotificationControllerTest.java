package hu.fleetpulse.backend.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import hu.fleetpulse.backend.controller.NotificationController;
import hu.fleetpulse.backend.dto.request.CreateNotificationRequest;
import hu.fleetpulse.backend.mapper.NotificationMapper;
import hu.fleetpulse.backend.service.NotificationService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(NotificationController.class)
class NotificationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private NotificationService notificationService;

    @MockitoBean
    private NotificationMapper notificationMapper;

    @Test
    void create_WhenValidRequest_Returns200() throws Exception {
        CreateNotificationRequest request = new CreateNotificationRequest(UUID.randomUUID(),"üzenet");
        doNothing().when(notificationService).create(any(CreateNotificationRequest.class));

        String json = new com.fasterxml.jackson.databind.ObjectMapper().writeValueAsString(request);

        mockMvc.perform(post("/notifications")
                .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk());
    }

    @Test
    void create_WhenInvalidRequest_Returns400() throws Exception {
        mockMvc.perform(post("/notifications")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void findAllByVehicleId_WhenValidId_Returns200() throws Exception {
        UUID id = UUID.randomUUID();
        when(notificationService.findAllByVehicleId(id)).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/notifications/vehicle/{vehicleId}", id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void findAllByVehicleId_WhenInvalidUuidFormat_Returns400() throws Exception {
        mockMvc.perform(get("/notifications/vehicle/ez-nem-egy-uuid")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }
}