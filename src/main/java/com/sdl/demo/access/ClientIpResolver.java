package com.sdl.demo.access;

import jakarta.servlet.http.HttpServletRequest;

public final class ClientIpResolver {

    private ClientIpResolver() {}

    /**
     * Best-effort client IP: first address in {@code X-Forwarded-For} if present, else {@code
     * X-Real-IP}, else {@code RemoteAddr}.
     */
    public static String resolve(HttpServletRequest request) {
        String forwarded = request.getHeader("X-Forwarded-For");
        if (forwarded != null && !forwarded.isBlank()) {
            int comma = forwarded.indexOf(',');
            String first = comma < 0 ? forwarded : forwarded.substring(0, comma);
            String trimmed = first.trim();
            if (!trimmed.isEmpty()) {
                return normalizeIp(trimmed);
            }
        }
        String realIp = request.getHeader("X-Real-IP");
        if (realIp != null && !realIp.isBlank()) {
            return normalizeIp(realIp.trim());
        }
        return normalizeIp(request.getRemoteAddr());
    }

    private static String normalizeIp(String ip) {
        if (ip.startsWith("[") && ip.endsWith("]")) {
            return ip.substring(1, ip.length() - 1);
        }
        return ip;
    }
}
