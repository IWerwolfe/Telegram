package com.telegrambot.app.DTO.api.doc.cashDoc;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.telegrambot.app.DTO.api.doc.cardDoc.CardDocResponse;
import com.telegrambot.app.DTO.api.typeОbjects.DataListResponse;
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
