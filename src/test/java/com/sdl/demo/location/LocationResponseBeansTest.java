package com.sdl.demo.location;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;

class LocationResponseBeansTest {

    @Test
    void gettersAndSetters() {
        LocationResponse r = new LocationResponse();
        r.setId(1L);
        r.setIp("a");
        r.setCountryId(2L);
        r.setCountryCode("KE");
        r.setCountryName("Kenya");
        r.setRegionName("r");
        r.setCityName("c");
        r.setLatitude(0.1);
        r.setLongitude(0.2);
        r.setZipCode("z");
        r.setTimeZone("t");
        r.setAsn("x");
        assertEquals(1L, r.getId());
        assertEquals("a", r.getIp());
        assertEquals(2L, r.getCountryId());
        assertEquals("KE", r.getCountryCode());
        assertEquals("Kenya", r.getCountryName());
        assertEquals("r", r.getRegionName());
        assertEquals("c", r.getCityName());
        assertEquals(0.1, r.getLatitude());
        assertEquals(0.2, r.getLongitude());
        assertEquals("z", r.getZipCode());
        assertEquals("t", r.getTimeZone());
        assertEquals("x", r.getAsn());
    }

    @Test
    void newInstance_hasNullFields() {
        LocationResponse r = new LocationResponse();
        assertNull(r.getId());
        assertNull(r.getIp());
    }
}
