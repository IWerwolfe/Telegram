package com.telegrambot.app.DTO.api;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.telegrambot.app.DTO.api.doc.taskDoc.TaskDocResponse;
import com.telegrambot.app.DTO.api.legal.partner.PartnerDataResponse;
import lombok.Data;

import java.util.Date;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class UserResponse extends DataResponse {
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
    private List<TaskDocResponse> taskList;
    private PartnerDataResponse partnerListData;
    //TODO добавить информацию о задачах
}
