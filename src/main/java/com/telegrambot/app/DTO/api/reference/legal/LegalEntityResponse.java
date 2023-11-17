package com.telegrambot.app.DTO.api.reference.legal;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.telegrambot.app.DTO.api.typeОbjects.EntityResponse;
import lombok.Data;

import java.util.Date;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public abstract class LegalEntityResponse extends EntityResponse {
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

}
