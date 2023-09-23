package com.telegrambot.app.DTO.api.reference.legal.company;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.telegrambot.app.DTO.api.reference.legal.LegalEntityResponse;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class CompanyResponse extends LegalEntityResponse {

    @JsonCreator
    public CompanyResponse(String json) {
        createToJson(json, CompanyResponse.class, this);
    }
}
