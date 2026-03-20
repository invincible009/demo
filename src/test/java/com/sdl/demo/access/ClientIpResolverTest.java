package com.sdl.demo.access;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ClientIpResolverTest {

    @Mock private HttpServletRequest request;

    @Test
    void resolve_usesFirstForwardedForAddress() {
        when(request.getHeader("X-Forwarded-For")).thenReturn("203.0.113.5, 10.0.0.1");
        assertEquals("203.0.113.5", ClientIpResolver.resolve(request));
    }

    @Test
    void resolve_usesXRealIpWhenNoForwardedFor() {
        when(request.getHeader("X-Forwarded-For")).thenReturn(null);
        when(request.getHeader("X-Real-IP")).thenReturn("198.51.100.2");
        assertEquals("198.51.100.2", ClientIpResolver.resolve(request));
    }

    @Test
    void resolve_fallsBackToRemoteAddr() {
        when(request.getHeader("X-Forwarded-For")).thenReturn(null);
        when(request.getHeader("X-Real-IP")).thenReturn(null);
        when(request.getRemoteAddr()).thenReturn("192.0.2.1");
        assertEquals("192.0.2.1", ClientIpResolver.resolve(request));
    }

    @Test
    void resolve_stripsBracketsFromIpv6() {
        when(request.getHeader("X-Forwarded-For")).thenReturn("[2001:db8::1]");
        assertEquals("2001:db8::1", ClientIpResolver.resolve(request));
    }
}
