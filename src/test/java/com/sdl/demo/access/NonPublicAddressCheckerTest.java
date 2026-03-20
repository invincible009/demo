package com.sdl.demo.access;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class NonPublicAddressCheckerTest {

    @Test
    void nullOrBlank_treatedAsNonPublic() {
        assertTrue(NonPublicAddressChecker.isNonPublicClient(null));
        assertTrue(NonPublicAddressChecker.isNonPublicClient(""));
        assertTrue(NonPublicAddressChecker.isNonPublicClient("   "));
    }

    @Test
    void loopback_isNonPublic() {
        assertTrue(NonPublicAddressChecker.isNonPublicClient("127.0.0.1"));
        assertTrue(NonPublicAddressChecker.isNonPublicClient("::1"));
    }

    @Test
    void siteLocal_isNonPublic() {
        assertTrue(NonPublicAddressChecker.isNonPublicClient("10.0.0.1"));
        assertTrue(NonPublicAddressChecker.isNonPublicClient("192.168.1.1"));
        assertTrue(NonPublicAddressChecker.isNonPublicClient("172.16.0.1"));
    }

    @Test
    void publicIpv4_notNonPublic() {
        assertFalse(NonPublicAddressChecker.isNonPublicClient("8.8.8.8"));
    }
}
