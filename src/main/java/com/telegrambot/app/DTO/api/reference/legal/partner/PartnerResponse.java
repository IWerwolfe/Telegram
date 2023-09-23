package com.telegrambot.app.DTO.api.reference.legal.partner;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.telegrambot.app.DTO.api.reference.legal.LegalEntityResponse;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class PartnerResponse extends LegalEntityResponse {
    private String partnerTypeString;

    @JsonCreator
    public PartnerResponse(String json) {
        createToJson(json, PartnerResponse.class, this);
    }
}
