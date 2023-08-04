package com.telegrambot.app.DTO.api_1C.legal.company;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.telegrambot.app.DTO.api_1C.legal.LegalEntityResponse;
import lombok.Data;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class CompanyResponse extends LegalEntityResponse {

}
