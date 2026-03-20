package com.sdl.demo.location;

import static org.junit.jupiter.api.Assertions.assertSame;

import java.util.Map;
import org.junit.jupiter.api.Test;

class LocationLookupPersistResultTest {

    @Test
    void recordHoldsLookupAndSaved() {
        Map<String, Object> lookup = Map.of("ip", "1.1.1.1");
        LocationResponse saved = new LocationResponse();
        saved.setId(1L);
        LocationLookupPersistResult r = new LocationLookupPersistResult(lookup, saved);
        assertSame(lookup, r.lookup());
        assertSame(saved, r.saved());
    }
}
