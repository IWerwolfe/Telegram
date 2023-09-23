package com.telegrambot.app.DTO.api.reference.taskStatus;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.telegrambot.app.DTO.api.typeОbjects.EntityResponse;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class TaskStatusResponse extends EntityResponse {

    @JsonCreator
    public TaskStatusResponse(String json) {
        createToJson(json, TaskStatusResponse.class, this);
    }
}
