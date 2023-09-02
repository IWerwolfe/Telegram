package com.telegrambot.app.DTO.api.doc.cashDoc;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.telegrambot.app.DTO.api.typeОbjects.EntityDocResponse;
import lombok.Data;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class CashDocResponse extends EntityDocResponse {
    private String cashDeskKkm;
    private String cashShiftNumber;
    private String receiptNumber;
    private String cashShift;
    private String correspondence;
    private String paymentBasis;

}
