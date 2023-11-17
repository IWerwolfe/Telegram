package com.telegrambot.app.DTO.api.doc.bankDoc;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.telegrambot.app.DTO.api.typeОbjects.EntityDocResponse;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class BankDocResponse extends EntityDocResponse {
    private String guidBankAccount;
    private String guidPayerBankAccount;
    @JsonFormat(pattern = DATE_PATTERN)
    private Date docDate;
    private String docCode;
    private String paymentPurpose;
    private String commission;
    private String decodingFillingOption;

    @JsonCreator
    public BankDocResponse(String json) {
        fillToJson(json, BankDocResponse.class, this);
    }
}
