package com.supportbot.DTO.api.reference.legal.partner;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.supportbot.DTO.api.balance.BalanceResponse;
import com.supportbot.DTO.api.reference.legal.contract.ContractResponse;
import com.supportbot.DTO.api.reference.legal.department.DepartmentResponse;
import com.supportbot.DTO.api.typeObjects.DataResponse;
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
