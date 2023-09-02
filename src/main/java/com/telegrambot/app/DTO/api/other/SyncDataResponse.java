package com.telegrambot.app.DTO.api.other;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.telegrambot.app.DTO.api.typeОbjects.DataResponse;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class SyncDataResponse extends DataResponse {
    private String guid;
    private String code;
}
