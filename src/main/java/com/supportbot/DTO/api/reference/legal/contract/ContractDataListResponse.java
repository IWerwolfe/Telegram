package com.supportbot.DTO.api.reference.legal.contract;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.supportbot.DTO.api.typeОbjects.DataListResponse;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class ContractDataListResponse extends DataListResponse<ContractResponse> {
    public ContractDataListResponse(boolean result, String error) {
        super(result, error);
    }
}
