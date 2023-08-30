package com.telegrambot.app.DTO.api.legal.partner;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.telegrambot.app.DTO.api.DataResponse;
import com.telegrambot.app.DTO.api.balance.BalanceResponse;
import com.telegrambot.app.DTO.api.legal.contract.ContractResponse;
import com.telegrambot.app.DTO.api.legal.department.DepartmentResponse;
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
