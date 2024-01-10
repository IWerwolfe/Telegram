package com.supportbot.DTO.api.reference.legal.company;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.supportbot.DTO.api.reference.legal.LegalEntityResponse;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class CompanyResponse extends LegalEntityResponse {

    @JsonCreator
    public CompanyResponse(String json) {
        fillToJson(json, CompanyResponse.class, this);
    }
}
