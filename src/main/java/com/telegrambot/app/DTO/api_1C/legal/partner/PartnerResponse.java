package com.telegrambot.app.DTO.api_1C.legal.partner;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.telegrambot.app.DTO.api_1C.legal.LegalEntityResponse;
import lombok.Data;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class PartnerResponse extends LegalEntityResponse {
    private String partnerTypeString;
}
