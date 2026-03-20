package com.sdl.demo.location;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.Test;

class CreateLocationRequestTest {

    @Test
    void fromIp2LocationMap_null_returnsEmptyRequest() {
        CreateLocationRequest r = CreateLocationRequest.fromIp2LocationMap(null);
        assertNotNull(r);
        assertNull(r.getIp());
    }

    @Test
    void fromIp2LocationMap_snakeCase_fullMapping() {
        Map<String, Object> map = new HashMap<>();
        map.put("ip", "41.90.137.253");
        map.put("country_code", "KE");
        map.put("country_name", "Kenya");
        map.put("region_name", "Nairobi City");
        map.put("city_name", "Nairobi");
        map.put("latitude", -1.28333);
        map.put("longitude", 36.81667);
        map.put("zip_code", null);
        map.put("time_zone", "+03:00");
        map.put("asn", 33771L);
        CreateLocationRequest r = CreateLocationRequest.fromIp2LocationMap(map);
        assertEquals("41.90.137.253", r.getIp());
        assertEquals("KE", r.getCountryCode());
        assertEquals("Kenya", r.getCountryName());
        assertEquals("Nairobi City", r.getRegionName());
        assertEquals("Nairobi", r.getCityName());
        assertEquals(-1.28333, r.getLatitude());
        assertEquals(36.81667, r.getLongitude());
        assertNull(r.getZipCode());
        assertEquals("+03:00", r.getTimeZone());
        assertEquals("33771", r.getAsn());
    }

    @Test
    void fromIp2LocationMap_camelCase() {
        Map<String, Object> map = new HashMap<>();
        map.put("ip", "1.1.1.1");
        map.put("countryCode", "NG");
        map.put("countryName", "Nigeria");
        map.put("latitude", "6.5");
        CreateLocationRequest r = CreateLocationRequest.fromIp2LocationMap(map);
        assertEquals("NG", r.getCountryCode());
        assertEquals("Nigeria", r.getCountryName());
        assertEquals(6.5, r.getLatitude());
    }
}
