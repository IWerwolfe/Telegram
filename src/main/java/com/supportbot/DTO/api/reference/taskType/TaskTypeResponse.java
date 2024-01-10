package com.supportbot.DTO.api.reference.taskType;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.supportbot.DTO.api.typeОbjects.EntityResponse;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class TaskTypeResponse extends EntityResponse {

    @JsonCreator
    public TaskTypeResponse(String json) {
        fillToJson(json, TaskTypeResponse.class, this);
    }
}