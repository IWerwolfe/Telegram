package com.telegrambot.app.services.api;

import com.telegrambot.app.DTO.api.DefaultDataResponse;
import com.telegrambot.app.DTO.api.SyncDataResponse;
import com.telegrambot.app.DTO.api.UserResponse;
import com.telegrambot.app.DTO.api.doc.taskDoc.TaskDocDataListResponse;
import com.telegrambot.app.DTO.api.doc.taskDoc.TaskDocDataResponse;
import com.telegrambot.app.DTO.api.doc.taskDoc.TaskDocResponse;
import com.telegrambot.app.DTO.api.legal.partner.PartnerDataResponse;
import lombok.NonNull;

public interface ApiOutService {

    DefaultDataResponse getDefaultData();

    PartnerDataResponse getPartnerData(@NonNull String inn, String kpp);

    PartnerDataResponse getPartnerData(@NonNull String inn);

    PartnerDataResponse getPartnerByGuid(@NonNull String guid);

    UserResponse getUserData(@NonNull String phone);

    TaskDocDataResponse getTaskByGuid(@NonNull String guid);

    TaskDocDataResponse getTaskByCode(@NonNull String code);

    SyncDataResponse createTask(@NonNull TaskDocResponse taskDocResponse);

    TaskDocDataListResponse getTaskListDataByCompany(String guidPartner);

    TaskDocDataListResponse getTaskListDataByUser(String guidUser);

    TaskDocDataListResponse getTaskListDataByDepartment(String guidDepartment);

    TaskDocDataListResponse getTaskListDataByManager(String guid);
}
