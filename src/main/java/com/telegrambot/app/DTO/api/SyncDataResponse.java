package com.telegrambot.app.DTO.api;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class SyncDataResponse extends DataResponse {
    private String guid;
    private String code;
}
