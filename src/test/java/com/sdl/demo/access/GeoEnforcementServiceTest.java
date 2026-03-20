package com.sdl.demo.access;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.sdl.demo.testLocation.Ip2LocationFeignClient;
import feign.FeignException;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class GeoEnforcementServiceTest {

    @Mock private Ip2LocationFeignClient feign;

    private RegionalAccessProperties properties;
    private GeoEnforcementService service;

    @BeforeEach
    void setUp() {
        properties = new RegionalAccessProperties();
        properties.setAllowedCountryCodes(List.of("NG", " ke "));
        service = new GeoEnforcementService(feign, properties);
    }

    @Test
    void lookupCountryCode_returnsUppercaseFromPayload() {
        when(feign.getLocation("41.90.0.1"))
                .thenReturn(Map.of("country_code", "ke", "ip", "41.90.0.1"));
        assertEquals("KE", service.lookupCountryCode("41.90.0.1"));
    }

    @Test
    void lookupCountryCode_onFeignException_returnsNull() {
        FeignException ex = mock(FeignException.class);
        when(feign.getLocation(anyString())).thenThrow(ex);
        assertNull(service.lookupCountryCode("8.8.8.8"));
    }

    @Test
    void lookupCountryCode_missingCountryInPayload_returnsNull() {
        when(feign.getLocation("8.8.8.8")).thenReturn(Map.of("ip", "8.8.8.8"));
        assertNull(service.lookupCountryCode("8.8.8.8"));
    }

    @Test
    void allowedCountryCodesUpper_trimsAndUppercases() {
        assertEquals(Set.of("NG", "KE"), service.allowedCountryCodesUpper());
    }
}
