package com.telegrambot.app.services;

import com.telegrambot.app.DTO.api_1C.*;
import lombok.NonNull;

public interface API1CServices {

    DefaultDataResponse getDefaultData();

    CompanyDataResponse getCompanyData(@NonNull String inn, String kpp);

    CompanyDataResponse getCompanyData(@NonNull String inn);

    CompanyDataResponse getCompanyByGuid(@NonNull String guid);

    UserDataResponse getUserData(@NonNull String phone);

    TaskDataResponse getTaskByGuid(@NonNull String guid);

    TaskDataResponse getTaskByCode(@NonNull String code);

    TaskCreateResponse createTask(@NonNull TaskResponse taskResponse);

    TaskDataListResponse getTaskListDataByCompany(String guidPartner);

    TaskDataListResponse getTaskListDataByUser(String guidUser);

    TaskDataListResponse getTaskListDataByDepartment(String guidDepartment);

    TaskDataListResponse getTaskListDataByManager(String guid);
}
