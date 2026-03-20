package com.sdl.demo.country;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;

class CountryTest {

    @Test
    void gettersAndSetters() {
        Country c = new Country();
        assertNull(c.getId());
        c.setId(1L);
        c.setCountryCode("RW");
        c.setCountryName("Rwanda");
        assertEquals(1L, c.getId());
        assertEquals("RW", c.getCountryCode());
        assertEquals("Rwanda", c.getCountryName());
    }
}
