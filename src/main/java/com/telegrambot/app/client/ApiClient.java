package com.telegrambot.app.client;

import com.telegrambot.app.DTO.api.doc.cardDoc.CardDocResponse;
import com.telegrambot.app.DTO.api.doc.taskDoc.TaskDocDataListResponse;
import com.telegrambot.app.DTO.api.doc.taskDoc.TaskDocDataResponse;
import com.telegrambot.app.DTO.api.doc.taskDoc.TaskDocResponse;
import com.telegrambot.app.DTO.api.other.DefaultDataResponse;
import com.telegrambot.app.DTO.api.other.SyncDataResponse;
import com.telegrambot.app.DTO.api.other.UserResponse;
import com.telegrambot.app.DTO.api.reference.legal.partner.PartnerDataResponse;
import com.telegrambot.app.DTO.api.reference.legal.partner.PartnerResponse;
import com.telegrambot.app.DTO.api.typeОbjects.DataResponse;
import com.telegrambot.app.config.Connector1C;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class ApiClient extends ClientHttp {

    private final Connector1C connector1C;

    @Override
    public <T, R> R executeRequest(T requestBody, Class<R> responseType, String url, HttpMethod httpMethod) {
        R result = super.executeRequest(requestBody, responseType, url, httpMethod);
        return result instanceof DataResponse ? result : (R) getDefaultDataResponse();
    }

    @Override
    public String getUrl(String method) {
        return connector1C.getUrl() + method;
    }

    @Override
    public String getUrl(String method, String param) {
        return getUrl(method) + (param.isEmpty() ? "" : "?" + param);
    }

    public HttpHeaders getHttpHeaders() {
        HttpHeaders headers = new HttpHeaders();
//        headers.add("Authorization", connector1C.getToken());
        headers.add("token", connector1C.getToken());
        headers.setBasicAuth(connector1C.getLogin(), connector1C.getPassword());
        return headers;
    }

    public DefaultDataResponse getDefaultData() {
        return executeGetRequest(
                DefaultDataResponse.class,
                getUrl("defaultData", "apiKey=" + connector1C.getToken()));
    }

    public PartnerDataResponse getPartnerData(@NonNull String inn, String kpp) {
        return executeGetRequest(
                PartnerDataResponse.class,
                getUrl("legal", "inn=" + inn + "&kpp=" + kpp));
    }

    public PartnerDataResponse getPartnerData(@NonNull String inn) {
        return executeGetRequest(
                PartnerDataResponse.class,
                getUrl("legal", "inn=" + inn + "&kpp="));
    }

    public PartnerDataResponse getPartnerByGuid(@NonNull String guid) {
        String param = "guid=" + guid;
        return executeGetRequest(
                PartnerDataResponse.class,
                getUrl("legal", param));
    }

    public UserResponse getUserData(@NonNull String phone) {
        return executeGetRequest(
                UserResponse.class,
                getUrl("userdata", "phone=" + phone));
    }

    public TaskDocDataResponse getTaskByGuid(@NonNull String guid) {
        return executeGetRequest(
                TaskDocDataResponse.class,
                getUrl("task", "guid=" + guid));
    }

    public TaskDocDataResponse getTaskByCode(@NonNull String code) {
        return executeGetRequest(
                TaskDocDataResponse.class,
                getUrl("task", "code=" + code));
    }

    public SyncDataResponse createPartner(@NonNull PartnerResponse response) {
        return executePostRequest(
                response,
                SyncDataResponse.class,
                getUrl("partner"));
    }

    public SyncDataResponse createTask(@NonNull TaskDocResponse taskDocResponse) {
        return executePostRequest(
                taskDocResponse,
                SyncDataResponse.class,
                getUrl("task"));
    }

    public SyncDataResponse createCardDoc(@NonNull CardDocResponse cardDocResponse) {
        return executePostRequest(
                cardDocResponse,
                SyncDataResponse.class,
                getUrl("cardDoc"));
    }

    public SyncDataResponse updateTask(@NonNull TaskDocResponse taskDocResponse) {
        return executePutRequest(
                taskDocResponse,
                SyncDataResponse.class,
                getUrl("task"));
    }

    public SyncDataResponse updateCardDoc(@NonNull CardDocResponse cardDocResponse) {
        return executePutRequest(
                cardDocResponse,
                SyncDataResponse.class,
                getUrl("cardDoc"));
    }

    public TaskDocDataListResponse getTaskListDataByCompany(String guidPartner) {
        return executeGetRequest(
                TaskDocDataListResponse.class,
                getUrl("taskList", "guidPartner=" + guidPartner));
    }

    public TaskDocDataListResponse getTaskListDataByUser(String guidUser) {
        return executeGetRequest(
                TaskDocDataListResponse.class,
                getUrl("taskList", "guidUser=" + guidUser));
    }

    public TaskDocDataListResponse getTaskListDataByManager(String guidUser) {
        return executeGetRequest(
                TaskDocDataListResponse.class,
                getUrl("taskList", "guidManager=" + guidUser));
    }

    public TaskDocDataListResponse getTaskListDataByDepartment(String guidDepartment) {
        return executeGetRequest(
                TaskDocDataListResponse.class,
                getUrl("taskList", "guidDepartment=" + guidDepartment));
    }

    private DataResponse getDefaultDataResponse() {
        DataResponse response = new DataResponse();
        response.setResult(false);
        response.setError("Data not found");
        return response;
    }
}
