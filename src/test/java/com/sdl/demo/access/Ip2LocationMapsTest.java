package com.sdl.demo.access;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.Map;
import org.junit.jupiter.api.Test;

class Ip2LocationMapsTest {

    @Test
    void countryCode_nullMap_returnsNull() {
        assertNull(Ip2LocationMaps.countryCode(null));
    }

    @Test
    void countryCode_snakeCase_uppercases() {
        assertEquals(
                "KE",
                Ip2LocationMaps.countryCode(Map.of("country_code", "ke")));
    }

    @Test
    void countryCode_camelCase() {
        assertEquals(
                "NG",
                Ip2LocationMaps.countryCode(Map.of("countryCode", "ng")));
    }

    @Test
    void countryCode_prefersSnakeWhenBothPresent() {
        assertEquals(
                "GH",
                Ip2LocationMaps.countryCode(
                        Map.of("country_code", "gh", "countryCode", "xx")));
    }

    @Test
    void countryCode_blankString_returnsNull() {
        assertNull(Ip2LocationMaps.countryCode(Map.of("country_code", "  ")));
    }
}
