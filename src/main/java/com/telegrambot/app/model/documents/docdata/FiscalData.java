package com.telegrambot.app.model.documents.docdata;

import jakarta.persistence.Embeddable;
import jakarta.persistence.JoinColumn;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Embeddable
public class FiscalData {

    @JoinColumn(name = "cash_desk_kkm")
    private String cashDeskKkm;
    @JoinColumn(name = "cash_shift_number")
    private String cashShiftNumber;
    @JoinColumn(name = "receipt_number")
    private String receiptNumber;
    @JoinColumn(name = "cash_shift")
    private String cashShift;
}
