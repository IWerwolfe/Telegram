package com.supportbot.DTO.api.reference.legal.partner;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.supportbot.DTO.api.reference.legal.LegalEntityResponse;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class PartnerResponse extends LegalEntityResponse {
    private String partnerTypeString;

    @JsonCreator
    public PartnerResponse(String json) {
        fillToJson(json, PartnerResponse.class, this);
    }
}
