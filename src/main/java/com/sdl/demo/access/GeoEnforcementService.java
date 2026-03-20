package com.sdl.demo.access;

import com.sdl.demo.testLocation.Ip2LocationFeignClient;
import feign.FeignException;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

@Service
public class GeoEnforcementService {

    private final Ip2LocationFeignClient ip2LocationFeignClient;
    private final RegionalAccessProperties properties;

    public GeoEnforcementService(
            Ip2LocationFeignClient ip2LocationFeignClient, RegionalAccessProperties properties) {
        this.ip2LocationFeignClient = ip2LocationFeignClient;
        this.properties = properties;
    }

    /**
     * Resolve country code for an IP using IP2Location.io. Empty if lookup fails or code missing.
     */
    public String lookupCountryCode(String clientIp) {
        try {
            Map<String, Object> payload = ip2LocationFeignClient.getLocation(clientIp);
            return Ip2LocationMaps.countryCode(payload);
        } catch (FeignException ignored) {
            return null;
        }
    }

    /** Normalized uppercase allowlist from configuration. */
    public Set<String> allowedCountryCodesUpper() {
        return properties.getAllowedCountryCodes().stream()
                .map(s -> s == null ? "" : s.trim().toUpperCase())
                .filter(s -> !s.isEmpty())
                .collect(Collectors.toSet());
    }
}
