package com.telegrambot.app.DTO.api_1C;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class CompanyResponse extends LegalEntityResponse {

}
