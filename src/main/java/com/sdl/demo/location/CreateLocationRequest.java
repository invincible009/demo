package com.sdl.demo.location;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CreateLocationRequest {

    @JsonProperty("ip")
    private String ip;

    @JsonProperty("country_code")
    private String countryCode;

    @JsonProperty("country_name")
    private String countryName;

    @JsonProperty("region_name")
    private String regionName;

    @JsonProperty("city_name")
    private String cityName;

    @JsonProperty("latitude")
    private Double latitude;

    @JsonProperty("longitude")
    private Double longitude;

    @JsonProperty("zip_code")
    private String zipCode;

    @JsonProperty("time_zone")
    private String timeZone;

    @JsonProperty("asn")
    private String asn;

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    public String getRegionName() {
        return regionName;
    }

    public void setRegionName(String regionName) {
        this.regionName = regionName;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public String getTimeZone() {
        return timeZone;
    }

    public void setTimeZone(String timeZone) {
        this.timeZone = timeZone;
    }

    public String getAsn() {
        return asn;
    }

    public void setAsn(String asn) {
        this.asn = asn;
    }

    /**
     * Maps an IP2Location-style JSON object (typically snake_case keys) as returned by the Feign client.
     */
    public static CreateLocationRequest fromIp2LocationMap(Map<String, ?> map) {
        if (map == null) {
            return new CreateLocationRequest();
        }
        CreateLocationRequest r = new CreateLocationRequest();
        r.setIp(firstString(map, "ip"));
        r.setCountryCode(firstString(map, "country_code", "countryCode"));
        r.setCountryName(firstString(map, "country_name", "countryName"));
        r.setRegionName(firstString(map, "region_name", "regionName"));
        r.setCityName(firstString(map, "city_name", "cityName"));
        r.setLatitude(firstDouble(map, "latitude"));
        r.setLongitude(firstDouble(map, "longitude"));
        r.setZipCode(firstStringAllowEmpty(map, "zip_code", "zipCode"));
        r.setTimeZone(firstString(map, "time_zone", "timeZone"));
        r.setAsn(firstString(map, "asn", "ASN"));
        return r;
    }

    private static String firstString(Map<String, ?> m, String... keys) {
        String s = firstStringAllowEmpty(m, keys);
        return s == null || s.isBlank() ? null : s;
    }

    /** Returns trimmed string or null; treats blank as null (except when you need to preserve empty zip). */
    private static String firstStringAllowEmpty(Map<String, ?> m, String... keys) {
        for (String key : keys) {
            Object v = m.get(key);
            if (v == null) {
                continue;
            }
            String s = v.toString().trim();
            if (!s.isEmpty()) {
                return s;
            }
        }
        return null;
    }

    private static Double firstDouble(Map<String, ?> m, String... keys) {
        for (String key : keys) {
            Object v = m.get(key);
            if (v == null) {
                continue;
            }
            if (v instanceof Number n) {
                return n.doubleValue();
            }
            if (v instanceof String s && !s.isBlank()) {
                try {
                    return Double.parseDouble(s.trim());
                } catch (NumberFormatException ignored) {
                    // try next key
                }
            }
        }
        return null;
    }
}
