package com.telegrambot.app.DTO.api.doc.cashDoc;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.telegrambot.app.DTO.api.typeОbjects.EntityDoc1C;
import lombok.Data;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class CashDocResponse extends EntityDoc1C {
    private String cashDeskKkm;
    private String cashShiftNumber;
    private String receiptNumber;
    private String cashShift;
    private String correspondence;
    private String paymentBasis;

}
