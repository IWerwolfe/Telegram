package com.supportbot.DTO.dadata;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class DaDataAddressData {

    @JsonProperty("postal_code")
    private String postalCode;
    @JsonProperty("country_code")
    private String countryCode;
    @JsonProperty("region_fias_id")
    private String regionFiasId;
    @JsonProperty("region_kladr_id")
    private String regionKladrId;
    @JsonProperty("region_with_type")
    private String regionWithType;
    @JsonProperty("region_type")
    private String regionType;
    @JsonProperty("region_type_full")
    private String regionTypeFull;
    private String region;
    @JsonProperty("area_fias_id")
    private String areaFiasId;
    @JsonProperty("area_kladr_id")
    private String areaKladrId;
    @JsonProperty("area_with_type")
    private String areaWithType;
    @JsonProperty("area_type")
    private String areaType;
    @JsonProperty("area_type_full")
    private String areaTypeFull;
    private String area;
    @JsonProperty("city-fias_id")
    private String cityFiasId;
    @JsonProperty("city_kladr_id")
    private String cityKladrId;
    @JsonProperty("city_with_type")
    private String cityWithType;
    @JsonProperty("city_type")
    private String cityType;
    @JsonProperty("city_type_full")
    private String cityTypeFull;
    private String city;
    @JsonProperty("settlement_fias_id")
    private String settlementFiasId;
    @JsonProperty("settlement_kladr_id")
    private String settlementKladrId;
    @JsonProperty("settlement_with_type")
    private String settlementWithType;
    @JsonProperty("settlement_type")
    private String settlementType;
    @JsonProperty("settlement-type_full")
    private String settlementTypeFull;
    private String settlement;
    @JsonProperty("street_fias_id")
    private String streetFiasId;
    @JsonProperty("street_kladr_id")
    private String streetKladrId;
    @JsonProperty("street_with_type")
    private String streetWithType;
    @JsonProperty("street_type")
    private String streetType;
    @JsonProperty("street_type_full")
    private String streetTypeFull;
    private String street;
    @JsonProperty("house_fias_id")
    private String houseFiasId;
    @JsonProperty("house_kladr_id")
    private String houseKladrId;
    @JsonProperty("house_type")
    private String houseType;
    @JsonProperty("house_type_full")
    private String houseTypeFull;
    private String house;
    @JsonProperty("block_type")
    private String blockType;
    @JsonProperty("block_type_full")
    private String blockTypeFull;
    private String block;
    @JsonProperty("flat_type")
    private String flatType;
    @JsonProperty("flatType_full")
    private String flatTypeFull;
    private String flat;
    @JsonProperty("flat_area")
    private String flatArea;
    @JsonProperty("square_meter_price")
    private String squareMeterPrice;
    @JsonProperty("flat_price")
    private String flatPrice;
    @JsonProperty("postal_box")
    private String postalBox;
    @JsonProperty("fias_id")
    private String fiasId;
    @JsonProperty("fias_code")
    private String fiasCode;
    @JsonProperty("fias_level")
    private String fiasLevel;
    @JsonProperty("fias_actuality_state")
    private String fiasActualityState;
    @JsonProperty("kladr_id")
    private String kladrId;
    @JsonProperty("geoname_id")
    private String geonameId;
    @JsonProperty("capital_marker")
    private String capitalMarker;
    private String okato;
    private String oktmo;
    @JsonProperty("tax_office")
    private String taxOffice;
    @JsonProperty("tax_office_legal")
    private String taxOfficeLegal;
    private String timezone;
    @JsonProperty("geo_lat")
    private String geoLat;
    @JsonProperty("geo_lon")
    private String geoLon;
    @JsonProperty("beltway_hit")
    private String beltwayHit;
    @JsonProperty("beltway_distance")
    private String beltwayDistance;
    @JsonProperty("qc_geo")
    private String qcGeo;
    @JsonProperty("qc_complete")
    private String qcComplete;
    @JsonProperty("qc_house")
    private String qcHouse;
    private String qc;
    @JsonProperty("unparsed_parts")
    private String unparsedParts;
}
