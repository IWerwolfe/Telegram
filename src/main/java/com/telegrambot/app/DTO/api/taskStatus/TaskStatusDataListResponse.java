package com.telegrambot.app.DTO.api.taskStatus;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.telegrambot.app.DTO.api.DataListResponse;
import com.telegrambot.app.DTO.api.manager.ManagerResponse;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class TaskStatusDataListResponse extends DataListResponse<ManagerResponse> {
    public TaskStatusDataListResponse(boolean result, String error) {
        super(result, error);
    }
}
