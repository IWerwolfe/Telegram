package com.telegrambot.app.services;

import com.telegrambot.app.DTO.api_1C.*;
import com.telegrambot.app.model.user.UserBD;
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

    TaskDataListResponse getTaskList(String inn);

    TaskDataListResponse getTaskList(UserBD userBD);
}
