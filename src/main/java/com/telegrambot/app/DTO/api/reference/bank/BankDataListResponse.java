package com.telegrambot.app.DTO.api.reference.bank;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.telegrambot.app.DTO.api.typeОbjects.DataListResponse;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class BankDataListResponse extends DataListResponse<BankResponse> {
    public BankDataListResponse(boolean result, String error) {
        super(result, error);
    }
}
