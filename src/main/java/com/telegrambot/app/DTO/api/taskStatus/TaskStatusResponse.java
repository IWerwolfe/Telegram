package com.telegrambot.app.DTO.api.taskStatus;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.telegrambot.app.DTO.api.typeОbjects.Entity1C;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class TaskStatusResponse extends Entity1C {
}
