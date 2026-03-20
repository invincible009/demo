package com.sdl.demo.location;

import java.util.Map;

/**
 * Returned by {@code GET /location?persist=true} after a successful lookup and save.
 */
public record LocationLookupPersistResult(Map<String, Object> lookup, LocationResponse saved) {}
