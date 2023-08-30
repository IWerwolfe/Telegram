package com.telegrambot.app.DTO.api.balance;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class BalanceResponse {
    private String guid;
    private String inn;
    private String kpp;
    private String amount;
}
