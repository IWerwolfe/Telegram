package com.supportbot.DTO.api.reference.taskStatus;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.supportbot.DTO.api.typeObjects.EntityResponse;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class TaskStatusResponse extends EntityResponse {

    @JsonCreator
    public TaskStatusResponse(String json) {
        fillToJson(json, TaskStatusResponse.class, this);
    }
}
