package com.sdl.demo.testLocation;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

import com.sdl.demo.location.CreateLocationRequest;
import com.sdl.demo.location.LocationPersistenceService;
import com.sdl.demo.location.LocationResponse;
import feign.FeignException;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;

@ExtendWith(MockitoExtension.class)
class IpLookupControllerTest {

    @Mock private Ip2LocationFeignClient ip2LocationFeignClient;

    @Mock private LocationPersistenceService locationPersistenceService;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc =
                standaloneSetup(new IpLookupController(ip2LocationFeignClient, locationPersistenceService))
                        .build();
    }

    @Test
    void getLocation_withoutPersist_returnsLookupMap() throws Exception {
        Map<String, Object> payload =
                Map.of("ip", "41.0.0.1", "country_code", "KE", "country_name", "Kenya");
        when(ip2LocationFeignClient.getLocation("41.0.0.1")).thenReturn(payload);

        mockMvc.perform(get("/location").param("ip", "41.0.0.1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.country_code").value("KE"));
        verify(locationPersistenceService, never()).persist(any(CreateLocationRequest.class));
    }

    @Test
    void getLocation_usesCurrentWhenIpBlank() throws Exception {
        Map<String, Object> payload = Map.of("ip", "9.9.9.9");
        when(ip2LocationFeignClient.getCurrentLocation()).thenReturn(payload);

        mockMvc.perform(get("/location")).andExpect(status().isOk()).andExpect(jsonPath("$.ip").value("9.9.9.9"));
    }

    @Test
    void getLocation_withPersist_returnsLookupAndSaved() throws Exception {
        Map<String, Object> payload =
                Map.of(
                        "ip", "1.1.1.1",
                        "country_code", "NG",
                        "country_name", "Nigeria");
        when(ip2LocationFeignClient.getLocation("1.1.1.1")).thenReturn(payload);
        LocationResponse saved = new LocationResponse();
        saved.setId(100L);
        saved.setCountryCode("NG");
        when(locationPersistenceService.persist(any(CreateLocationRequest.class))).thenReturn(saved);

        mockMvc.perform(get("/location").param("ip", "1.1.1.1").param("persist", "true"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.saved.id").value(100))
                .andExpect(jsonPath("$.lookup.ip").value("1.1.1.1"));
    }

    @Test
    void getLocation_feignError_returnsUpstreamBodyAndStatus() throws Exception {
        FeignException ex = mock(FeignException.class);
        when(ex.status()).thenReturn(502);
        when(ex.contentUTF8()).thenReturn("bad gateway");
        when(ip2LocationFeignClient.getLocation("1.1.1.1")).thenThrow(ex);

        mockMvc.perform(get("/location").param("ip", "1.1.1.1"))
                .andExpect(status().is(502))
                .andExpect(content().string("bad gateway"));
    }

    @Test
    void getLocation_withPersist_validationError_returns422() throws Exception {
        Map<String, Object> payload = Map.of("ip", "1.1.1.1");
        when(ip2LocationFeignClient.getLocation("1.1.1.1")).thenReturn(payload);
        when(locationPersistenceService.persist(any(CreateLocationRequest.class)))
                .thenThrow(
                        new IllegalArgumentException("country_code is required to save a location"));

        mockMvc.perform(get("/location").param("ip", "1.1.1.1").param("persist", "true"))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.error").exists())
                .andExpect(jsonPath("$.lookup.ip").value("1.1.1.1"));
    }
}
