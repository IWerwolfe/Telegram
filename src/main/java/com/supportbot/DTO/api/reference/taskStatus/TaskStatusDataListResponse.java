package com.supportbot.DTO.api.reference.taskStatus;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.supportbot.DTO.api.reference.manager.ManagerResponse;
import com.supportbot.DTO.api.typeОbjects.DataListResponse;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class TaskStatusDataListResponse extends DataListResponse<ManagerResponse> {
    public TaskStatusDataListResponse(boolean result, String error) {
        super(result, error);
    }
}
