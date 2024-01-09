package com.telegrambot.app.DTO.dadata;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class DaDataParty {

    private String kpp;
    private DaDataCapital capital;
    private DaDataManagement management;
    private DaDataFounders[] founders;
    private DaDataManagers[] managers;
    @JsonProperty("branch_type")
    private String branchType;
    @JsonProperty("branch_count")
    private int branchCount;
    private String source;
    private String qc;
    private String hid;
    private String type;
    private DaDataState state;
    private DaDataOpf opf;
    private DaDataName name;
    private String inn;
    private String ogrn;
    private String okpo;
    private String okato;
    private String oktmo;
    private String okogu;
    private String okfs;
    private String okved;
    private DaDataOkveds[] okveds;
    private DaDataAuthorities authorities;
    private DaDataDocuments documents;
    private DaDataFinance finance;
    private DaDataAddress address;
    private DaDataPhones[] phones;
    private DaDataEmails[] emails;
    @JsonProperty("ogrn_date")
    private long ogrnDate;
    @JsonProperty("okved_type")
    private String okvedType;
    @JsonProperty("employee_count")
    private int employeeCount;
}
