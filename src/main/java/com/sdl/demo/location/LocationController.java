package com.sdl.demo.location;

import java.net.URI;
import java.util.Map;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/locations")
public class LocationController {

    private final LocationPersistenceService locationPersistenceService;

    public LocationController(LocationPersistenceService locationPersistenceService) {
        this.locationPersistenceService = locationPersistenceService;
    }

    @PostMapping
    public ResponseEntity<?> createLocation(@RequestBody CreateLocationRequest body) {
        try {
            LocationResponse saved = locationPersistenceService.persist(body);
            return ResponseEntity.created(URI.create("/locations/" + saved.getId())).body(saved);
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body(Map.of("error", ex.getMessage()));
        }
    }
}
