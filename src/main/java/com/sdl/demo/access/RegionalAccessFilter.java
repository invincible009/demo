package com.sdl.demo.access;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Set;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.MediaType;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE + 20)
public class RegionalAccessFilter extends OncePerRequestFilter {

    public static final String HEADER_ACCESS_DENIED = "X-Regional-Access-Denied";

    private final RegionalAccessProperties properties;
    private final GeoEnforcementService geoEnforcementService;

    public RegionalAccessFilter(
            RegionalAccessProperties properties, GeoEnforcementService geoEnforcementService) {
        this.properties = properties;
        this.geoEnforcementService = geoEnforcementService;
    }

    @Override
    protected boolean shouldNotFilter(@NonNull HttpServletRequest request) {
        if (!properties.isEnabled()) {
            return true;
        }
        String path = request.getRequestURI();
        for (String prefix : properties.getPermitAllPathPrefixes()) {
            if (path.startsWith(prefix)) {
                return true;
            }
        }
        return false;
    }

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain chain)
            throws ServletException, IOException {

        if (!properties.isEnabled()) {
            chain.doFilter(request, response);
            return;
        }

        String clientIp = ClientIpResolver.resolve(request);

        if (properties.isAllowPrivateNetworks() && NonPublicAddressChecker.isNonPublicClient(clientIp)) {
            chain.doFilter(request, response);
            return;
        }

        Set<String> allowed = geoEnforcementService.allowedCountryCodesUpper();
        if (allowed.isEmpty()) {
            writeForbid(response, "ALLOWLIST_EMPTY", clientIp, null);
            return;
        }

        String country = geoEnforcementService.lookupCountryCode(clientIp);

        if (country == null) {
            writeForbid(response, "COUNTRY_UNKNOWN", clientIp, null);
            return;
        }

        if (!allowed.contains(country)) {
            writeForbid(response, "REGION_NOT_ALLOWED", clientIp, country);
            return;
        }

        chain.doFilter(request, response);
    }

    private void writeForbid(HttpServletResponse response, String code, String clientIp, String resolvedCountry)
            throws IOException {
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.setHeader(HEADER_ACCESS_DENIED, "true");
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");

        String message =
                "Access is restricted to selected regions only. "
                        + "Nigeria, Kenya, Ghana, and Rwanda (NG, KE, GH, RW) are currently allowed.";
        StringBuilder json = new StringBuilder(256);
        json.append("{\"error\":\"").append(jsonEscape(code)).append("\",\"message\":\"");
        json.append(jsonEscape(message)).append("\",\"clientIp\":\"").append(jsonEscape(clientIp)).append('"');
        if (resolvedCountry != null) {
            json.append(",\"resolvedCountryCode\":\"").append(jsonEscape(resolvedCountry)).append('"');
        }
        json.append('}');
        response.getWriter().write(json.toString());
    }

    private static String jsonEscape(String s) {
        if (s == null) {
            return "";
        }
        return s.replace("\\", "\\\\").replace("\"", "\\\"");
    }
}
