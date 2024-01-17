package com.supportbot.DTO.api.doc.cardDoc;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.supportbot.DTO.api.typeObjects.DataListResponse;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class CardDocDataListResponse extends DataListResponse<CardDocResponse> {
    public CardDocDataListResponse(boolean result, String error) {
        super(result, error);
    }
}
