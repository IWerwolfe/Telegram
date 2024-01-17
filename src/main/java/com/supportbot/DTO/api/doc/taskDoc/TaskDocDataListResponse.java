package com.supportbot.DTO.api.doc.taskDoc;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.supportbot.DTO.api.typeObjects.DataListResponse;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class TaskDocDataListResponse extends DataListResponse<TaskDocResponse> {
    public TaskDocDataListResponse(boolean result, String error) {
        super(result, error);
    }
}
