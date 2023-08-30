package com.telegrambot.app.DTO.api.legal.contract;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.telegrambot.app.DTO.api.DataEntityResponse;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ContractDataResponse extends DataEntityResponse<ContractResponse> {
}
