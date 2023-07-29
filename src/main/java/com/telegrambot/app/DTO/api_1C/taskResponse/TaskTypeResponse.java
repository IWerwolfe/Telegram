package com.telegrambot.app.DTO.api_1C.taskResponse;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.telegrambot.app.DTO.api_1C.typesОbjects.Entity1C;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class TaskTypeResponse extends Entity1C {
}
