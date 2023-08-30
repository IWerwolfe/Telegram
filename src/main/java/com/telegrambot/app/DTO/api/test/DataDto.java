package com.telegrambot.app.DTO.api.test;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class DataDto {
    private boolean result;
    private List<LegalEntityDto> legalEntities;
    private List<DepartmentDto> departments;
    private List<ContractDto> contracts;
    private String error;
}
