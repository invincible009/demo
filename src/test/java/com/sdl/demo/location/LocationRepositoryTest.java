package com.sdl.demo.location;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.sdl.demo.country.Country;
import com.sdl.demo.country.CountryRepository;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

@DataJpaTest
class LocationRepositoryTest {

    @Autowired private CountryRepository countryRepository;

    @Autowired private LocationRepository locationRepository;

    @Test
    void saveLocationWithCountry() {
        Country country = new Country();
        country.setCountryCode("GH");
        country.setCountryName("Ghana");
        countryRepository.save(country);

        Location loc = new Location();
        loc.setIp("8.8.8.8");
        loc.setCountry(country);
        loc.setCityName("Accra");
        locationRepository.save(loc);
        Long id = loc.getId();
        assertTrue(id != null && id > 0);

        Optional<Location> found = locationRepository.findById(id);
        assertTrue(found.isPresent());
        assertEquals("8.8.8.8", found.get().getIp());
        assertEquals("GH", found.get().getCountry().getCountryCode());
    }
}
