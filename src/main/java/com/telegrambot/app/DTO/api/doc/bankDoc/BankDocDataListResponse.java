package com.telegrambot.app.DTO.api.doc.bankDoc;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.telegrambot.app.DTO.api.typeОbjects.DataListResponse;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class BankDocDataListResponse extends DataListResponse<BankDocResponse> {
    public BankDocDataListResponse(boolean result, String error) {
        super(result, error);
    }
}
