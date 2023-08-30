package com.telegrambot.app.DTO.api.doc.cardDoc;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.telegrambot.app.DTO.api.DataListResponse;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class CardDocDataListResponse extends DataListResponse<CardDocResponse> {
    public CardDocDataListResponse(boolean result, String error) {
        super(result, error);
    }
}
