package com.telegrambot.app.DTO.api_1C.taskResponse;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.telegrambot.app.DTO.api_1C.DataResponse;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class TaskDataResponse extends DataResponse {
    private TaskResponse task;
}
