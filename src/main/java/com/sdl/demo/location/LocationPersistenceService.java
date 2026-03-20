package com.sdl.demo.location;

import com.sdl.demo.country.Country;
import com.sdl.demo.country.CountryRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class LocationPersistenceService {

    private final CountryRepository countryRepository;
    private final LocationRepository locationRepository;

    public LocationPersistenceService(
            CountryRepository countryRepository, LocationRepository locationRepository) {
        this.countryRepository = countryRepository;
        this.locationRepository = locationRepository;
    }

    /**
     * Validates and saves country (find-or-create by code) + location row.
     *
     * @throws IllegalArgumentException if ip, country code, or country name is missing
     */
    @Transactional
    public LocationResponse persist(CreateLocationRequest body) {
        if (body.getIp() == null || body.getIp().isBlank()) {
            throw new IllegalArgumentException("ip is required to save a location");
        }
        if (body.getCountryCode() == null || body.getCountryCode().isBlank()) {
            throw new IllegalArgumentException("country_code is required to save a location");
        }
        if (body.getCountryName() == null || body.getCountryName().isBlank()) {
            throw new IllegalArgumentException("country_name is required to save a location");
        }

        String code = body.getCountryCode().trim().toUpperCase();
        Country country = countryRepository
                .findByCountryCode(code)
                .orElseGet(() -> {
                    Country c = new Country();
                    c.setCountryCode(code);
                    c.setCountryName(body.getCountryName().trim());
                    return countryRepository.save(c);
                });

        Location location = new Location();
        location.setIp(body.getIp().trim());
        location.setCountry(country);
        location.setRegionName(body.getRegionName());
        location.setCityName(body.getCityName());
        location.setLatitude(body.getLatitude());
        location.setLongitude(body.getLongitude());
        location.setZipCode(body.getZipCode());
        location.setTimeZone(body.getTimeZone());
        location.setAsn(body.getAsn());

        Location saved = locationRepository.save(location);
        return LocationResponse.from(saved, country);
    }
}
