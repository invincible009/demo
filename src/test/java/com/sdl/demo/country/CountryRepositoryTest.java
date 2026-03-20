package com.sdl.demo.country;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

@DataJpaTest
class CountryRepositoryTest {

    @Autowired private CountryRepository countryRepository;

    @Test
    void save_andFindByCountryCode() {
        Country c = new Country();
        c.setCountryCode("KE");
        c.setCountryName("Kenya");
        countryRepository.save(c);

        Optional<Country> found = countryRepository.findByCountryCode("KE");
        assertTrue(found.isPresent());
        assertEquals("Kenya", found.get().getCountryName());
    }
}
