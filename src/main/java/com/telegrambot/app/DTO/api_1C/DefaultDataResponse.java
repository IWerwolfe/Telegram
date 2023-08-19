package com.telegrambot.app.DTO.api_1C;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.telegrambot.app.DTO.api_1C.legal.company.CompanyResponse;
import com.telegrambot.app.DTO.api_1C.manager.ManagerResponse;
import com.telegrambot.app.DTO.api_1C.taskResponse.TaskStatusResponse;
import com.telegrambot.app.DTO.api_1C.taskResponse.TaskTypeResponse;
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
