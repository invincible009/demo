package com.sdl.demo.location;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@ExtendWith(MockitoExtension.class)
class LocationControllerTest {

    @Mock private LocationPersistenceService locationPersistenceService;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = standaloneSetup(new LocationController(locationPersistenceService)).build();
    }

    @Test
    void createLocation_returns201WhenValid() throws Exception {
        LocationResponse saved = new LocationResponse();
        saved.setId(42L);
        saved.setIp("1.1.1.1");
        saved.setCountryCode("KE");
        when(locationPersistenceService.persist(any(CreateLocationRequest.class))).thenReturn(saved);

        String json =
                """
                {
                  "ip": "1.1.1.1",
                  "country_code": "KE",
                  "country_name": "Kenya"
                }
                """;

        mockMvc.perform(
                        post("/locations")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(json))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/locations/42"))
                .andExpect(jsonPath("$.id").value(42))
                .andExpect(jsonPath("$.ip").value("1.1.1.1"));
    }

    @Test
    void createLocation_returns400WhenServiceRejects() throws Exception {
        when(locationPersistenceService.persist(any(CreateLocationRequest.class)))
                .thenThrow(new IllegalArgumentException("country_code is required to save a location"));

        mockMvc.perform(
                        post("/locations")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("{}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").exists());
    }
}
