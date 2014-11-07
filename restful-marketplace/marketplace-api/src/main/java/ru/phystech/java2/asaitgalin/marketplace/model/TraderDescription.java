package ru.phystech.java2.asaitgalin.marketplace.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class TraderDescription {
    private String name;

    @JsonProperty("country")
    private String countryCode;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }
}
