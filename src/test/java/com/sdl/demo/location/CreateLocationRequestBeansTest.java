package com.sdl.demo.location;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;

/** Covers JavaBean accessors not exercised by JSON/map parsing tests. */
class CreateLocationRequestBeansTest {

    @Test
    void gettersAndSetters() {
        CreateLocationRequest r = new CreateLocationRequest();
        r.setIp("1.1.1.1");
        r.setCountryCode("NG");
        r.setCountryName("Nigeria");
        r.setRegionName("Lag");
        r.setCityName("Lagos");
        r.setLatitude(6.0);
        r.setLongitude(3.0);
        r.setZipCode("");
        r.setTimeZone("UTC");
        r.setAsn("1");
        assertEquals("1.1.1.1", r.getIp());
        assertEquals("NG", r.getCountryCode());
        assertEquals("Nigeria", r.getCountryName());
        assertEquals("Lag", r.getRegionName());
        assertEquals("Lagos", r.getCityName());
        assertEquals(6.0, r.getLatitude(), 1e-9);
        assertEquals(3.0, r.getLongitude(), 1e-9);
        assertEquals("", r.getZipCode());
        assertEquals("UTC", r.getTimeZone());
        assertEquals("1", r.getAsn());
    }

    @Test
    void newInstance_hasNullFields() {
        CreateLocationRequest r = new CreateLocationRequest();
        assertNull(r.getIp());
        assertNull(r.getCountryCode());
    }
}
