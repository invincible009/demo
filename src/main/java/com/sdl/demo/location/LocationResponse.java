package com.sdl.demo.location;

import com.sdl.demo.country.Country;

public class LocationResponse {

    private Long id;
    private String ip;
    private Long countryId;
    private String countryCode;
    private String countryName;
    private String regionName;
    private String cityName;
    private Double latitude;
    private Double longitude;
    private String zipCode;
    private String timeZone;
    private String asn;

    public static LocationResponse from(Location location, Country country) {
        LocationResponse r = new LocationResponse();
        r.setId(location.getId());
        r.setIp(location.getIp());
        r.setCountryId(country.getId());
        r.setCountryCode(country.getCountryCode());
        r.setCountryName(country.getCountryName());
        r.setRegionName(location.getRegionName());
        r.setCityName(location.getCityName());
        r.setLatitude(location.getLatitude());
        r.setLongitude(location.getLongitude());
        r.setZipCode(location.getZipCode());
        r.setTimeZone(location.getTimeZone());
        r.setAsn(location.getAsn());
        return r;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public Long getCountryId() {
        return countryId;
    }

    public void setCountryId(Long countryId) {
        this.countryId = countryId;
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
}
