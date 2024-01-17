package com.supportbot.DTO.api.other;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.supportbot.DTO.api.typeObjects.DataResponse;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class SyncDataResponse extends DataResponse {
    private String guid;
    private String code;
}
