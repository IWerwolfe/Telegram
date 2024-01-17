package com.supportbot.DTO.api.reference.bankAccount;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.supportbot.DTO.api.typeObjects.EntityResponse;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class BankAccountResponse extends EntityResponse {

    private String guidLegal;
    private String currency;
    private String guidBank;
    private String guidPaymentBank;
    private String number;
    private String nameType;
    private String correspondent;
    private String paymentPurpose;
    private String permitNumberAndDate;
    private Date openingDate;
    private Date closingDate;

    @JsonCreator
    public BankAccountResponse(String json) {
        fillToJson(json, BankAccountResponse.class, this);
    }
}
