package com.telegrambot.app.DTO.api_1C.test;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.Date;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class LegalEntityDto {
    private String code;
    private String guid;
    private String name;
    private String inn;
    private String kpp;
    private String guidBankAccount;
    private String comment;
    private String ogrn;
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private Date commencement;
    private String certificate;
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private Date dateCertificate;
    private String okpo;
    private String guidDefaultContract;
    private String partnerTypeString;
}
