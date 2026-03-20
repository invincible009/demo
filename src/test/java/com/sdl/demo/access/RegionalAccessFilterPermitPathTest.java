package com.sdl.demo.access;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import jakarta.servlet.FilterChain;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

@ExtendWith(MockitoExtension.class)
class RegionalAccessFilterPermitPathTest {

    @Mock private GeoEnforcementService geo;

    @Test
    void permitAllPrefix_skipsGeoLookup() throws Exception {
        RegionalAccessProperties props = new RegionalAccessProperties();
        props.setEnabled(true);
        props.setAllowPrivateNetworks(false);
        RegionalAccessFilter filter = new RegionalAccessFilter(props, geo);
        MockHttpServletRequest req = new MockHttpServletRequest();
        req.setRequestURI("/h2-console/login.do");
        req.setRemoteAddr("8.8.8.8");
        MockHttpServletResponse res = new MockHttpServletResponse();
        FilterChain chain = org.mockito.Mockito.mock(FilterChain.class);
        filter.doFilter(req, res, chain);
        verify(chain).doFilter(req, res);
        verify(geo, never()).lookupCountryCode(any());
    }
}
