package com.telegrambot.app.model.documents.docdata;

import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Embeddable
public class FiscalData {

    private String cashDeskKkm;
    private String cashShiftNumber;
    private String receiptNumber;
    private String cashShift;
}
