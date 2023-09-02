package com.telegrambot.app.DTO.api.reference.taskType;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.telegrambot.app.DTO.api.typeОbjects.DataListResponse;
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
