package com.telegrambot.app.DTO.api.balance;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.telegrambot.app.DTO.api.DataListResponse;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class BalanceDataListResponse extends DataListResponse<BalanceResponse> {
}
