package com.telegrambot.app.services.api;

import com.telegrambot.app.DTO.api.doc.cardDoc.CardDocResponse;
import com.telegrambot.app.DTO.api.doc.taskDoc.TaskDocDataListResponse;
import com.telegrambot.app.DTO.api.doc.taskDoc.TaskDocDataResponse;
import com.telegrambot.app.DTO.api.doc.taskDoc.TaskDocResponse;
import com.telegrambot.app.DTO.api.other.DefaultDataResponse;
import com.telegrambot.app.DTO.api.other.SyncDataResponse;
import com.telegrambot.app.DTO.api.other.UserResponse;
import com.telegrambot.app.DTO.api.reference.legal.partner.PartnerDataResponse;
import com.telegrambot.app.DTO.api.reference.legal.partner.PartnerResponse;
import lombok.NonNull;

public interface ApiOutService {

    DefaultDataResponse getDefaultData();

    PartnerDataResponse getPartnerData(@NonNull String inn, String kpp);

    PartnerDataResponse getPartnerData(@NonNull String inn);

    PartnerDataResponse getPartnerByGuid(@NonNull String guid);

    UserResponse getUserData(@NonNull String phone);

    TaskDocDataResponse getTaskByGuid(@NonNull String guid);

    TaskDocDataResponse getTaskByCode(@NonNull String code);

    SyncDataResponse createPartner(@NonNull PartnerResponse response);

    SyncDataResponse createTask(@NonNull TaskDocResponse taskDocResponse);

    SyncDataResponse createCardDoc(@NonNull CardDocResponse cardDocResponse);

    SyncDataResponse updateTask(@NonNull TaskDocResponse taskDocResponse);

    SyncDataResponse updateCardDoc(@NonNull CardDocResponse cardDocResponse);

    TaskDocDataListResponse getTaskListDataByCompany(String guidPartner);

    TaskDocDataListResponse getTaskListDataByUser(String guidUser);

    TaskDocDataListResponse getTaskListDataByDepartment(String guidDepartment);

    TaskDocDataListResponse getTaskListDataByManager(String guid);
}
