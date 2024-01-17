package com.supportbot.DTO.api.doc.cashDoc;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.supportbot.DTO.api.doc.cardDoc.CardDocResponse;
import com.supportbot.DTO.api.typeObjects.DataListResponse;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class CashDocDataListResponse extends DataListResponse<CardDocResponse> {
    public CashDocDataListResponse(boolean result, String error) {
        super(result, error);
    }
}
