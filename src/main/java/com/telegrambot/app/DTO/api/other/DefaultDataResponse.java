package com.telegrambot.app.DTO.api.other;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.telegrambot.app.DTO.api.reference.legal.company.CompanyResponse;
import com.telegrambot.app.DTO.api.reference.manager.ManagerResponse;
import com.telegrambot.app.DTO.api.reference.taskStatus.TaskStatusResponse;
import com.telegrambot.app.DTO.api.reference.taskType.TaskTypeResponse;
import com.telegrambot.app.DTO.api.typeОbjects.DataResponse;
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

}
