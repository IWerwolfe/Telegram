package com.telegrambot.app.DTO.api_1C.taskResponse;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.telegrambot.app.DTO.api_1C.DataResponse;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class TaskDataListResponse extends DataResponse {
    private List<TaskResponse> tasks;
}
