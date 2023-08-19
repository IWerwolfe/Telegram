package com.telegrambot.app.DTO.api_1C;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.telegrambot.app.DTO.api_1C.legal.partner.PartnerDataResponse;
import com.telegrambot.app.DTO.api_1C.taskResponse.TaskResponse;
import lombok.Data;

import java.util.Date;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class UserDataResponse extends DataResponse {
    private String phone;
    private String presentation;
    private String gender;
    private Boolean notValid;
    private Boolean isEmployee;
    private Boolean isMaster;
    private Date birthday;
    private String fio;
    private String guid;
    private List<UserStatusResponse> statusList;
    private List<TaskResponse> taskList;
    private PartnerDataResponse partnerListData;
    //TODO добавить информацию о задачах
}
