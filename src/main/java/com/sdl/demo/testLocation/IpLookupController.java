package com.sdl.demo.testLocation;

import com.sdl.demo.location.CreateLocationRequest;
import com.sdl.demo.location.LocationLookupPersistResult;
import com.sdl.demo.location.LocationPersistenceService;
import feign.FeignException;
import java.util.HashMap;
import java.util.Map;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/location")
public class IpLookupController {

    private final Ip2LocationFeignClient ip2LocationFeignClient;
    private final LocationPersistenceService locationPersistenceService;

    public IpLookupController(
            Ip2LocationFeignClient ip2LocationFeignClient,
            LocationPersistenceService locationPersistenceService) {
        this.ip2LocationFeignClient = ip2LocationFeignClient;
        this.locationPersistenceService = locationPersistenceService;
    }

    @GetMapping
    public ResponseEntity<?> getLocation(
            @RequestParam(required = false) String ip,
            @RequestParam(defaultValue = "false") boolean persist) {
        try {
            Map<String, Object> lookup =
                    ip == null || ip.isBlank()
                            ? ip2LocationFeignClient.getCurrentLocation()
                            : ip2LocationFeignClient.getLocation(ip);

            if (!persist) {
                return ResponseEntity.ok(lookup);
            }

            try {
                CreateLocationRequest toSave = CreateLocationRequest.fromIp2LocationMap(lookup);
                return ResponseEntity.ok(new LocationLookupPersistResult(lookup, locationPersistenceService.persist(toSave)));
            } catch (IllegalArgumentException ex) {
                Map<String, Object> err = new HashMap<>();
                err.put("lookup", lookup);
                err.put("error", ex.getMessage());
                return ResponseEntity.unprocessableContent().body(err);
            }
        } catch (FeignException exception) {
            String body = exception.contentUTF8();
            if (body == null || body.isBlank()) {
                body = exception.getMessage();
            }
            return ResponseEntity.status(exception.status()).body(body);
        }
    }
}
