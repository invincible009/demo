package com.sdl.demo.access;

import java.net.InetAddress;
import java.net.UnknownHostException;

public final class NonPublicAddressChecker {

    private NonPublicAddressChecker() {}

    /**
     * True for loopback, site-local (RFC1918-style), link-local, or wildcard any-address.
     * Used to optionally bypass regional checks during local development.
     */
    public static boolean isNonPublicClient(String ip) {
        if (ip == null || ip.isBlank()) {
            return true;
        }
        try {
            InetAddress addr = InetAddress.getByName(ip.strip());
            return addr.isLoopbackAddress()
                    || addr.isAnyLocalAddress()
                    || addr.isSiteLocalAddress()
                    || addr.isLinkLocalAddress();
        } catch (UnknownHostException e) {
            return false;
        }
    }
}
