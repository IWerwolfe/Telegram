package com.telegrambot.app.DTO.api.doc.bankDoc;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.telegrambot.app.DTO.api.typeОbjects.EntityDoc1C;
import lombok.Data;

import java.util.Date;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class BankDocResponse extends EntityDoc1C {
    private String guidBankAccount;
    private String guidPayerBankAccount;
    @JsonFormat(pattern = DATE_PATTERN)
    private Date docDate;
    private String docCode;
    private String paymentPurpose;
    private String commission;
    private String decodingFillingOption;

}
