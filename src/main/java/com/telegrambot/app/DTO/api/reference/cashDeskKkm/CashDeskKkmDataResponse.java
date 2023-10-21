package com.telegrambot.app.DTO.api.reference.cashDeskKkm;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.telegrambot.app.DTO.api.typeОbjects.DataEntityResponse;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class CashDeskKkmDataResponse extends DataEntityResponse<CashDeskKkmResponse> {
}
