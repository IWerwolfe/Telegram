package com.telegrambot.app.DTO.api.reference.taskStatus;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.telegrambot.app.DTO.api.reference.manager.ManagerResponse;
import com.telegrambot.app.DTO.api.typeОbjects.DataListResponse;
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
