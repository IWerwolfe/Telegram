package com.supportbot.DTO.api.reference.bank;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.supportbot.DTO.api.typeОbjects.DataListResponse;
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
