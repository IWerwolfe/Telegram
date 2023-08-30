package com.telegrambot.app.DTO.api;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.telegrambot.app.DTO.api.legal.company.CompanyResponse;
import com.telegrambot.app.DTO.api.manager.ManagerResponse;
import com.telegrambot.app.DTO.api.taskStatus.TaskStatusResponse;
import com.telegrambot.app.DTO.api.typeОbjects.Entity1C;
import lombok.Data;

import java.util.List;

@Data
//@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class DefaultDataResponse extends DataResponse {
    private List<TaskStatusResponse> taskStatuses;
    private List<TaskTypeResponse> taskTypes;
    private List<ManagerResponse> managers;
    private List<CompanyResponse> companies;
    private byte[] key;
    private String guidDefaultClosedStatus;
    private String guidDefaultInitialStatus;
    private String nameDefaultType;

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class TaskTypeResponse extends Entity1C {
    }
}
