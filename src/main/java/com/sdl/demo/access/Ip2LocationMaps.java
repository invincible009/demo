package com.sdl.demo.access;

import java.util.Map;

public final class Ip2LocationMaps {

    private Ip2LocationMaps() {}

    public static String countryCode(Map<String, ?> map) {
        if (map == null) {
            return null;
        }
        Object o = map.get("country_code");
        if (o == null) {
            o = map.get("countryCode");
        }
        if (o == null) {
            return null;
        }
        String s = o.toString().trim();
        return s.isEmpty() ? null : s.toUpperCase();
    }
}
