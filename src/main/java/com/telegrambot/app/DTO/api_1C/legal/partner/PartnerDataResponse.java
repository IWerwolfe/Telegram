package com.telegrambot.app.DTO.api_1C.legal.partner;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.telegrambot.app.DTO.api_1C.BalanceResponse;
import com.telegrambot.app.DTO.api_1C.DataResponse;
import lombok.Data;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class PartnerDataResponse extends DataResponse {
    private List<PartnerResponse> partners;
    private List<DepartmentResponse> departments;
    private List<ContractResponse> contracts;
    private List<BalanceResponse> balance;
}
