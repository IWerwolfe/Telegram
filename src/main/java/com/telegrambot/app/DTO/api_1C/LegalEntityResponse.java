package com.telegrambot.app.DTO.api_1C;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.telegrambot.app.DTO.api_1C.typesОbjects.Entity1C;
import lombok.Data;

import java.util.Date;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public abstract class LegalEntityResponse extends Entity1C {
    private String inn;
    private String kpp;
    private String guidBankAccount;
    private String comment;
    private String ogrn;
    @JsonFormat(pattern = DATE_PATTERN)
    private Date commencement;
    private String certificate;
    @JsonFormat(pattern = DATE_PATTERN)
    private Date dateCertificate;
    private String okpo;
    private String guidDefaultContract;
    private String partnerTypeString;
}
