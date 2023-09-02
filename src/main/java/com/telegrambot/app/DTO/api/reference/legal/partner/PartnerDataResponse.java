package com.telegrambot.app.DTO.api.reference.legal.partner;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.telegrambot.app.DTO.api.balance.BalanceResponse;
import com.telegrambot.app.DTO.api.reference.legal.contract.ContractResponse;
import com.telegrambot.app.DTO.api.reference.legal.department.DepartmentResponse;
import com.telegrambot.app.DTO.api.typeОbjects.DataResponse;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
@NoArgsConstructor
@Data
public class PartnerDataResponse extends DataResponse {
    private List<PartnerResponse> partners;
    private List<DepartmentResponse> departments;
    private List<ContractResponse> contracts;
    private List<BalanceResponse> balance;
}
