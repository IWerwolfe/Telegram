package com.telegrambot.app.DTO.api.doc.taskDoc;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.telegrambot.app.DTO.api.DataListResponse;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class TaskDocDataListResponse extends DataListResponse<TaskDocResponse> {
    public TaskDocDataListResponse(boolean result, String error) {
        super(result, error);
    }
}
