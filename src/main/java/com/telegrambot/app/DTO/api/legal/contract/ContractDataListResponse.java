package com.telegrambot.app.DTO.api.legal.contract;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.telegrambot.app.DTO.api.DataListResponse;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ContractDataListResponse extends DataListResponse<ContractResponse> {
    public ContractDataListResponse(boolean result, String error) {
        super(result, error);
    }
}
