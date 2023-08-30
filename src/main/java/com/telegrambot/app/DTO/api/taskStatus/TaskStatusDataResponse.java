package com.telegrambot.app.DTO.api.taskStatus;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.telegrambot.app.DTO.api.DataEntityResponse;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class TaskStatusDataResponse extends DataEntityResponse<TaskStatusResponse> {
}
