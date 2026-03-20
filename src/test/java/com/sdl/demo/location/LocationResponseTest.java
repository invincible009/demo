package com.sdl.demo.location;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.sdl.demo.country.Country;
import org.junit.jupiter.api.Test;

class LocationResponseTest {

    @Test
    void from_copiesAllFields() {
        Country country = new Country();
        country.setId(10L);
        country.setCountryCode("GH");
        country.setCountryName("Ghana");

        Location loc = new Location();
        loc.setId(20L);
        loc.setIp("1.2.3.4");
        loc.setCountry(country);
        loc.setRegionName("R");
        loc.setCityName("C");
        loc.setLatitude(5.0);
        loc.setLongitude(-0.2);
        loc.setZipCode("Z");
        loc.setTimeZone("T");
        loc.setAsn("123");

        LocationResponse r = LocationResponse.from(loc, country);
        assertEquals(20L, r.getId());
        assertEquals("1.2.3.4", r.getIp());
        assertEquals(10L, r.getCountryId());
        assertEquals("GH", r.getCountryCode());
        assertEquals("Ghana", r.getCountryName());
        assertEquals("R", r.getRegionName());
        assertEquals("C", r.getCityName());
        assertEquals(5.0, r.getLatitude());
        assertEquals(-0.2, r.getLongitude());
        assertEquals("Z", r.getZipCode());
        assertEquals("T", r.getTimeZone());
        assertEquals("123", r.getAsn());
    }
}
