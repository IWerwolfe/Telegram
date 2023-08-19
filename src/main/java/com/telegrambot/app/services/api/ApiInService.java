package com.telegrambot.app.services.api;

import com.telegrambot.app.DTO.api_1C.taskResponse.TaskDataListResponse;
import com.telegrambot.app.DTO.api_1C.taskResponse.TaskDataResponse;

public interface ApiInService {

    TaskDataResponse getTask(String guid);

    TaskDataListResponse getTasks(String guidManager, String guidCompany, String guidDepartment);
}
