package com.telegrambot.app.DTO.api_1C;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class CompanyDataResponse extends DataResponse {
    private List<PartnerResponse> legalEntities;
    private List<DepartmentResponse> departments;
    private List<ContractResponse> contracts;
}
