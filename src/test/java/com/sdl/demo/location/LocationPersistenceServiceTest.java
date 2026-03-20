package com.sdl.demo.location;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.sdl.demo.country.Country;
import com.sdl.demo.country.CountryRepository;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class LocationPersistenceServiceTest {

    @Mock private CountryRepository countryRepository;
    @Mock private LocationRepository locationRepository;

    private LocationPersistenceService service;

    @BeforeEach
    void setUp() {
        service = new LocationPersistenceService(countryRepository, locationRepository);
    }

    @Test
    void persist_blankIp_throws() {
        CreateLocationRequest b = new CreateLocationRequest();
        b.setCountryCode("KE");
        b.setCountryName("Kenya");
        assertThrows(IllegalArgumentException.class, () -> service.persist(b));
    }

    @Test
    void persist_blankCountryCode_throws() {
        CreateLocationRequest b = new CreateLocationRequest();
        b.setIp("1.1.1.1");
        b.setCountryName("Kenya");
        assertThrows(IllegalArgumentException.class, () -> service.persist(b));
    }

    @Test
    void persist_blankCountryName_throws() {
        CreateLocationRequest b = new CreateLocationRequest();
        b.setIp("1.1.1.1");
        b.setCountryCode("KE");
        assertThrows(IllegalArgumentException.class, () -> service.persist(b));
    }

    @Test
    void persist_createsCountryWhenMissing() {
        CreateLocationRequest b = new CreateLocationRequest();
        b.setIp("  41.90.0.1  ");
        b.setCountryCode("ke");
        b.setCountryName(" Kenya ");
        Country savedCountry = new Country();
        savedCountry.setId(1L);
        savedCountry.setCountryCode("KE");
        savedCountry.setCountryName("Kenya");
        when(countryRepository.findByCountryCode("KE")).thenReturn(Optional.empty());
        when(countryRepository.save(any(Country.class))).thenReturn(savedCountry);

        Location savedLoc = new Location();
        savedLoc.setId(99L);
        savedLoc.setIp("41.90.0.1");
        savedLoc.setCountry(savedCountry);
        when(locationRepository.save(any(Location.class))).thenReturn(savedLoc);

        LocationResponse out = service.persist(b);
        assertEquals(99L, out.getId());
        assertEquals("KE", out.getCountryCode());
        verify(countryRepository).save(any(Country.class));
    }

    @Test
    void persist_reusesExistingCountry() {
        CreateLocationRequest b = new CreateLocationRequest();
        b.setIp("1.1.1.1");
        b.setCountryCode("NG");
        b.setCountryName("Nigeria");
        Country existing = new Country();
        existing.setId(5L);
        existing.setCountryCode("NG");
        existing.setCountryName("Nigeria");
        when(countryRepository.findByCountryCode("NG")).thenReturn(Optional.of(existing));

        Location savedLoc = new Location();
        savedLoc.setId(7L);
        savedLoc.setIp("1.1.1.1");
        savedLoc.setCountry(existing);
        when(locationRepository.save(any(Location.class))).thenReturn(savedLoc);

        LocationResponse out = service.persist(b);
        assertEquals(5L, out.getCountryId());
        verify(countryRepository, never()).save(any());
    }

    @Test
    void persist_passesGeoFieldsToLocation() {
        CreateLocationRequest b = new CreateLocationRequest();
        b.setIp("8.8.8.8");
        b.setCountryCode("RW");
        b.setCountryName("Rwanda");
        b.setRegionName("r");
        b.setCityName("c");
        b.setLatitude(1.0);
        b.setLongitude(2.0);
        b.setZipCode("z");
        b.setTimeZone("t");
        b.setAsn("9");
        Country c = new Country();
        c.setId(1L);
        c.setCountryCode("RW");
        c.setCountryName("Rwanda");
        when(countryRepository.findByCountryCode("RW")).thenReturn(Optional.of(c));
        when(locationRepository.save(any(Location.class)))
                .thenAnswer(
                        inv -> {
                            Location l = inv.getArgument(0);
                            l.setId(3L);
                            return l;
                        });

        service.persist(b);

        ArgumentCaptor<Location> cap = ArgumentCaptor.forClass(Location.class);
        verify(locationRepository).save(cap.capture());
        Location sent = cap.getValue();
        assertEquals("8.8.8.8", sent.getIp());
        assertEquals("r", sent.getRegionName());
        assertEquals("c", sent.getCityName());
        assertEquals(1.0, sent.getLatitude());
        assertEquals(2.0, sent.getLongitude());
        assertEquals("z", sent.getZipCode());
        assertEquals("t", sent.getTimeZone());
        assertEquals("9", sent.getAsn());
    }
}
