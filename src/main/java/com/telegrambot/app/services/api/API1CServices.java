package com.telegrambot.app.services.api;

import com.telegrambot.app.DTO.api_1C.CompanyDataResponse;
import com.telegrambot.app.DTO.api_1C.DefaultDataResponse;
import com.telegrambot.app.DTO.api_1C.SyncDataResponse;
import com.telegrambot.app.DTO.api_1C.UserDataResponse;
import com.telegrambot.app.DTO.api_1C.taskResponse.TaskDataListResponse;
import com.telegrambot.app.DTO.api_1C.taskResponse.TaskDataResponse;
import com.telegrambot.app.DTO.api_1C.taskResponse.TaskResponse;
import lombok.NonNull;

public interface API1CServices {

    DefaultDataResponse getDefaultData();

    CompanyDataResponse getCompanyData(@NonNull String inn, String kpp);

    CompanyDataResponse getCompanyData(@NonNull String inn);

    CompanyDataResponse getCompanyByGuid(@NonNull String guid);

    UserDataResponse getUserData(@NonNull String phone);

    TaskDataResponse getTaskByGuid(@NonNull String guid);

    TaskDataResponse getTaskByCode(@NonNull String code);

    SyncDataResponse createTask(@NonNull TaskResponse taskResponse);

    TaskDataListResponse getTaskListDataByCompany(String guidPartner);

    TaskDataListResponse getTaskListDataByUser(String guidUser);

    TaskDataListResponse getTaskListDataByDepartment(String guidDepartment);

    TaskDataListResponse getTaskListDataByManager(String guid);
}
