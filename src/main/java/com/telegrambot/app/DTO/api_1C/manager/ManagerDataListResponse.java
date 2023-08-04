package com.telegrambot.app.DTO.api_1C.manager;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.telegrambot.app.DTO.api_1C.DataResponse;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ManagerDataListResponse extends DataResponse {
    private List<ManagerResponse> managers;
}
