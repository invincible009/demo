package com.sdl.demo.access;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import jakarta.servlet.FilterChain;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

@ExtendWith(MockitoExtension.class)
class RegionalAccessFilterTest {

    @Mock private GeoEnforcementService geo;

    @Test
    void whenDisabled_delegatesToChain() throws Exception {
        RegionalAccessProperties props = new RegionalAccessProperties();
        props.setEnabled(false);
        RegionalAccessFilter filter = new RegionalAccessFilter(props, geo);
        MockHttpServletRequest req = new MockHttpServletRequest();
        MockHttpServletResponse res = new MockHttpServletResponse();
        FilterChain chain = org.mockito.Mockito.mock(FilterChain.class);
        filter.doFilter(req, res, chain);
        verify(chain).doFilter(req, res);
        verify(geo, never()).lookupCountryCode(anyString());
    }

    @Test
    void whenPrivateNetworkAndAllowed_bypassesGeoLookup() throws Exception {
        RegionalAccessProperties props = new RegionalAccessProperties();
        props.setEnabled(true);
        props.setAllowPrivateNetworks(true);
        RegionalAccessFilter filter = new RegionalAccessFilter(props, geo);
        MockHttpServletRequest req = new MockHttpServletRequest();
        req.setRemoteAddr("127.0.0.1");
        MockHttpServletResponse res = new MockHttpServletResponse();
        FilterChain chain = org.mockito.Mockito.mock(FilterChain.class);
        filter.doFilter(req, res, chain);
        verify(chain).doFilter(req, res);
        verify(geo, never()).lookupCountryCode(anyString());
    }

    @Test
    void whenCountryAllowed_delegatesToChain() throws Exception {
        RegionalAccessProperties props = new RegionalAccessProperties();
        props.setEnabled(true);
        props.setAllowPrivateNetworks(false);
        RegionalAccessFilter filter = new RegionalAccessFilter(props, geo);
        MockHttpServletRequest req = new MockHttpServletRequest();
        req.setRemoteAddr("8.8.8.8");
        MockHttpServletResponse res = new MockHttpServletResponse();
        FilterChain chain = org.mockito.Mockito.mock(FilterChain.class);
        when(geo.allowedCountryCodesUpper()).thenReturn(Set.of("US"));
        when(geo.lookupCountryCode("8.8.8.8")).thenReturn("US");
        filter.doFilter(req, res, chain);
        verify(chain).doFilter(req, res);
    }

    @Test
    void whenCountryBlocked_returns403() throws Exception {
        RegionalAccessProperties props = new RegionalAccessProperties();
        props.setEnabled(true);
        props.setAllowPrivateNetworks(false);
        RegionalAccessFilter filter = new RegionalAccessFilter(props, geo);
        MockHttpServletRequest req = new MockHttpServletRequest();
        req.setRemoteAddr("8.8.8.8");
        MockHttpServletResponse res = new MockHttpServletResponse();
        FilterChain chain = org.mockito.Mockito.mock(FilterChain.class);
        when(geo.allowedCountryCodesUpper()).thenReturn(Set.of("NG"));
        when(geo.lookupCountryCode("8.8.8.8")).thenReturn("US");
        filter.doFilter(req, res, chain);
        assertEquals(HttpStatus.FORBIDDEN.value(), res.getStatus());
        assertTrue(res.getHeader(RegionalAccessFilter.HEADER_ACCESS_DENIED).contains("true"));
        assertTrue(res.getContentAsString().contains("REGION_NOT_ALLOWED"));
        verify(chain, never()).doFilter(any(), any());
    }

    @Test
    void whenAllowlistEmpty_returns403() throws Exception {
        RegionalAccessProperties props = new RegionalAccessProperties();
        props.setEnabled(true);
        props.setAllowPrivateNetworks(false);
        props.setAllowedCountryCodes(List.of());
        RegionalAccessFilter filter = new RegionalAccessFilter(props, geo);
        MockHttpServletRequest req = new MockHttpServletRequest();
        req.setRemoteAddr("8.8.8.8");
        MockHttpServletResponse res = new MockHttpServletResponse();
        FilterChain chain = org.mockito.Mockito.mock(FilterChain.class);
        when(geo.allowedCountryCodesUpper()).thenReturn(Set.of());
        filter.doFilter(req, res, chain);
        assertEquals(HttpStatus.FORBIDDEN.value(), res.getStatus());
        assertTrue(res.getContentAsString().contains("ALLOWLIST_EMPTY"));
        verify(chain, never()).doFilter(any(), any());
    }

    @Test
    void whenCountryUnknown_returns403() throws Exception {
        RegionalAccessProperties props = new RegionalAccessProperties();
        props.setEnabled(true);
        props.setAllowPrivateNetworks(false);
        RegionalAccessFilter filter = new RegionalAccessFilter(props, geo);
        MockHttpServletRequest req = new MockHttpServletRequest();
        req.setRemoteAddr("8.8.8.8");
        MockHttpServletResponse res = new MockHttpServletResponse();
        FilterChain chain = org.mockito.Mockito.mock(FilterChain.class);
        when(geo.allowedCountryCodesUpper()).thenReturn(Set.of("NG"));
        when(geo.lookupCountryCode("8.8.8.8")).thenReturn(null);
        filter.doFilter(req, res, chain);
        assertEquals(HttpStatus.FORBIDDEN.value(), res.getStatus());
        assertTrue(res.getContentAsString().contains("COUNTRY_UNKNOWN"));
        verify(chain, never()).doFilter(any(), any());
    }
}
