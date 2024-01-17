package com.supportbot.DTO.api.reference.taskType;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.supportbot.DTO.api.typeObjects.DataListResponse;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class TaskTypeDataListResponse extends DataListResponse<TaskTypeResponse> {
    public TaskTypeDataListResponse(boolean result, String error) {
        super(result, error);
    }
}
