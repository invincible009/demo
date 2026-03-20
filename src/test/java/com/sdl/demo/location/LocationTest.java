package com.sdl.demo.location;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import com.sdl.demo.country.Country;
import org.junit.jupiter.api.Test;

class LocationTest {

    @Test
    void gettersAndSetters() {
        Country country = new Country();
        country.setId(2L);
        country.setCountryCode("KE");

        Location loc = new Location();
        assertNull(loc.getId());
        loc.setId(10L);
        loc.setIp("9.9.9.9");
        loc.setCountry(country);
        loc.setRegionName("R");
        loc.setCityName("C");
        loc.setLatitude(1.1);
        loc.setLongitude(2.2);
        loc.setZipCode("Z");
        loc.setTimeZone("T");
        loc.setAsn("A");
        assertEquals(10L, loc.getId());
        assertEquals("9.9.9.9", loc.getIp());
        assertEquals(country, loc.getCountry());
        assertEquals("R", loc.getRegionName());
        assertEquals("C", loc.getCityName());
        assertEquals(1.1, loc.getLatitude());
        assertEquals(2.2, loc.getLongitude());
        assertEquals("Z", loc.getZipCode());
        assertEquals("T", loc.getTimeZone());
        assertEquals("A", loc.getAsn());
    }
}
