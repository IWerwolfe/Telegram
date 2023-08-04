package com.telegrambot.app.services.api;

import com.telegrambot.app.DTO.api_1C.DefaultDataResponse;
import com.telegrambot.app.DTO.api_1C.SyncDataResponse;
import com.telegrambot.app.DTO.api_1C.UserDataResponse;
import com.telegrambot.app.DTO.api_1C.legal.partner.PartnerDataResponse;
import com.telegrambot.app.DTO.api_1C.taskResponse.TaskDataListResponse;
import com.telegrambot.app.DTO.api_1C.taskResponse.TaskDataResponse;
import com.telegrambot.app.DTO.api_1C.taskResponse.TaskResponse;
import lombok.NonNull;

public interface API1CServices {

    DefaultDataResponse getDefaultData();

    PartnerDataResponse getPartnerData(@NonNull String inn, String kpp);

    PartnerDataResponse getPartnerData(@NonNull String inn);

    PartnerDataResponse getPartnerByGuid(@NonNull String guid);

    UserDataResponse getUserData(@NonNull String phone);

    TaskDataResponse getTaskByGuid(@NonNull String guid);

    TaskDataResponse getTaskByCode(@NonNull String code);

    SyncDataResponse createTask(@NonNull TaskResponse taskResponse);

    TaskDataListResponse getTaskListDataByCompany(String guidPartner);

    TaskDataListResponse getTaskListDataByUser(String guidUser);

    TaskDataListResponse getTaskListDataByDepartment(String guidDepartment);

    TaskDataListResponse getTaskListDataByManager(String guid);
}
