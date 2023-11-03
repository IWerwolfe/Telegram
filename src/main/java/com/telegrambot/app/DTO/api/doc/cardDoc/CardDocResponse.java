package com.telegrambot.app.DTO.api.doc.cardDoc;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.telegrambot.app.DTO.api.typeОbjects.EntityDocResponse;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class CardDocResponse extends EntityDocResponse {
    private String guidBankAccount;
    private String guidPayTerminal;
    private String guidCashDeskKkm;
    private String guidCashDesk;
    private String cashShiftNumber;
    private String receiptNumber;
    private String guidCashShift;
    private String ticketNumber;
    private String commissionPercentage;
    private String referenceNumber;
    private String commission;
    private String cardNumber;
    private String cardType;
    private String paymentTypeString;

    @JsonCreator
    public CardDocResponse(String json) {
        createToJson(json, CardDocResponse.class, this);
    }
}
