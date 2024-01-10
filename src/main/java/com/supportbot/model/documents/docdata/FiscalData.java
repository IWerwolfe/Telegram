package com.supportbot.model.documents.docdata;

import com.supportbot.model.reference.CashDeskKkm;
import jakarta.persistence.Embeddable;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Embeddable
@NoArgsConstructor
@AllArgsConstructor
public class FiscalData {

    @ManyToOne
    @JoinColumn(name = "cash_desk_kkm")
    private CashDeskKkm cashDeskKkm;
    @JoinColumn(name = "cash_shift_number")
    private String cashShiftNumber;
    @JoinColumn(name = "receipt_number")
    private String receiptNumber;
    @JoinColumn(name = "cash_shift")
    private String cashShift;
}
