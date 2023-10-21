package com.telegrambot.app.DTO.api.reference.bankAccount;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.telegrambot.app.DTO.api.typeОbjects.DataListResponse;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class BankAccountDataListResponse extends DataListResponse<BankAccountResponse> {
    public BankAccountDataListResponse(boolean result, String error) {
        super(result, error);
    }
}
