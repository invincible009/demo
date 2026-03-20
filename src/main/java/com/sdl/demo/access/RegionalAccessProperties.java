package com.sdl.demo.access;

import java.util.ArrayList;
import java.util.List;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "regional-access")
public class RegionalAccessProperties {

    /** When false, no geo check is applied (all requests pass). */
    private boolean enabled = true;

    /**
     * ISO 3166-1 alpha-2 codes (uppercase) for countries that may use the app.
     * Default rollout: Nigeria, Kenya, Ghana, Rwanda.
     */
    private List<String> allowedCountryCodes = new ArrayList<>(List.of("NG", "KE", "GH", "RW"));

    /**
     * When true, requests from loopback and site-local / link-local addresses skip the geo check
     * (useful for local dev). Set to false in production if you want strict checks everywhere.
     */
    private boolean allowPrivateNetworks = true;

    /** Paths that skip regional enforcement (e.g. H2 console, error dispatch). */
    private List<String> permitAllPathPrefixes = new ArrayList<>(List.of("/h2-console", "/error"));

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public List<String> getAllowedCountryCodes() {
        return allowedCountryCodes;
    }

    public void setAllowedCountryCodes(List<String> allowedCountryCodes) {
        this.allowedCountryCodes = allowedCountryCodes == null ? new ArrayList<>() : allowedCountryCodes;
    }

    public boolean isAllowPrivateNetworks() {
        return allowPrivateNetworks;
    }

    public void setAllowPrivateNetworks(boolean allowPrivateNetworks) {
        this.allowPrivateNetworks = allowPrivateNetworks;
    }

    public List<String> getPermitAllPathPrefixes() {
        return permitAllPathPrefixes;
    }

    public void setPermitAllPathPrefixes(List<String> permitAllPathPrefixes) {
        this.permitAllPathPrefixes = permitAllPathPrefixes == null ? new ArrayList<>() : permitAllPathPrefixes;
    }
}
