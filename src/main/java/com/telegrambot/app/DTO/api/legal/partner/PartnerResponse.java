package com.telegrambot.app.DTO.api.legal.partner;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.telegrambot.app.DTO.api.legal.LegalEntityResponse;
import lombok.Data;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class PartnerResponse extends LegalEntityResponse {
    private String partnerTypeString;
}
