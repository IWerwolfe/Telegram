package com.telegrambot.app.DTO.api.doc.cardDoc;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.telegrambot.app.DTO.api.typeОbjects.EntityDoc1C;
import lombok.Data;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class CardDocResponse extends EntityDoc1C {
    private String guidBankAccount;
    private String paymentTerminal;
    private String cashDeskKkm;
    private String cashShiftNumber;
    private String receiptNumber;
    private String cashShift;
    private String ticketNumber;
    private String commissionPercentage;
    private String referenceNumber;
    private String commission;
    private String cardNumber;
    private String cardType;

}
