package com.telegrambot.app.DTO.dadata;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class DaDataPhoneData {
    private String contact;
    private String source;
    private String qc;
    private String type;
    private String number;
    private String extension;
    private String provider;
    private String country;
    private String region;
    private String city;
    private String timezone;
    @JsonProperty("country_code")
    private String countryCode;
    @JsonProperty("city_code")
    private String cityCode;
    @JsonProperty("qc_conflict")
    private String qcConflict;
}
