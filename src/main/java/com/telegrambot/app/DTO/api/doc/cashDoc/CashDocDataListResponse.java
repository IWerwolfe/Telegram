package com.telegrambot.app.DTO.api.doc.cashDoc;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.telegrambot.app.DTO.api.DataListResponse;
import com.telegrambot.app.DTO.api.doc.cardDoc.CardDocResponse;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class CashDocDataListResponse extends DataListResponse<CardDocResponse> {
    public CashDocDataListResponse(boolean result, String error) {
        super(result, error);
    }
}
