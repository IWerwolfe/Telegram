package com.telegrambot.app.DTO.api.doc.bankDoc;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.telegrambot.app.DTO.api.DataEntityResponse;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class BankDocDataResponse extends DataEntityResponse<BankDocResponse> {
}
