package com.supportbot.DTO.api.doc.cashDoc;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.supportbot.DTO.api.typeObjects.EntityDocResponse;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class CashDocResponse extends EntityDocResponse {
    private String cashDeskKkm;
    private String cashShiftNumber;
    private String receiptNumber;
    private String cashShift;
    private String correspondence;
    private String paymentBasis;

    @JsonCreator
    public CashDocResponse(String json) {
        fillToJson(json, CashDocResponse.class, this);
    }
}
