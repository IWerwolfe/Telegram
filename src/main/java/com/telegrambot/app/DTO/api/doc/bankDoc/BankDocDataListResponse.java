package com.telegrambot.app.DTO.api.doc.bankDoc;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.telegrambot.app.DTO.api.DataListResponse;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class BankDocDataListResponse extends DataListResponse<BankDocResponse> {
    public BankDocDataListResponse(boolean result, String error) {
        super(result, error);
    }
}
