package com.telegrambot.app.services;

import com.telegrambot.app.DTO.Task;
import com.telegrambot.app.DTO.api_1C.CompanyDataResponse;
import com.telegrambot.app.DTO.api_1C.UserDataResponse;
import com.telegrambot.app.model.user.UserBD;

import java.util.List;

public interface API1CServices {

    CompanyDataResponse getCompany(String inn, String kpp);

    CompanyDataResponse getCompany(String inn);

    CompanyDataResponse getCompanyByGuid(String guid);

    UserDataResponse getUserData(String phone);

    Task getTask(String id);

    List<Task> getTaskList(String inn);

    List<Task> getTaskList(UserBD userBD);
}
