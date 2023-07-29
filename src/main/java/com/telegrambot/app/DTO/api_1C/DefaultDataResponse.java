package com.telegrambot.app.DTO.api_1C;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.telegrambot.app.DTO.api_1C.taskResponse.TaskStatusResponse;
import com.telegrambot.app.DTO.api_1C.taskResponse.TaskTypeResponse;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class DefaultDataResponse extends DataResponse {
    private List<TaskStatusResponse> taskStatuses;
    private List<TaskTypeResponse> taskTypes;
    private byte[] key;
    private String guidDefaultClosedStatus;
    private String guidDefaultInitialStatus;
    private String nameDefaultType;
}
