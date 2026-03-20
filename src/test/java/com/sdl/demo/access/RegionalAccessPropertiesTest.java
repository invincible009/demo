package com.sdl.demo.access;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import org.junit.jupiter.api.Test;

class RegionalAccessPropertiesTest {

    @Test
    void defaults_includeFourAfricanCountries() {
        RegionalAccessProperties p = new RegionalAccessProperties();
        assertTrue(p.isEnabled());
        assertEquals(List.of("NG", "KE", "GH", "RW"), p.getAllowedCountryCodes());
        assertTrue(p.isAllowPrivateNetworks());
        assertTrue(p.getPermitAllPathPrefixes().contains("/h2-console"));
    }

    @Test
    void setters_roundTrip() {
        RegionalAccessProperties p = new RegionalAccessProperties();
        p.setEnabled(false);
        p.setAllowedCountryCodes(List.of(" ng "));
        p.setAllowPrivateNetworks(false);
        p.setPermitAllPathPrefixes(List.of("/actuator"));
        assertFalse(p.isEnabled());
        assertEquals(List.of(" ng "), p.getAllowedCountryCodes());
        assertFalse(p.isAllowPrivateNetworks());
        assertEquals(List.of("/actuator"), p.getPermitAllPathPrefixes());
    }
}
