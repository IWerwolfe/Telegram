package com.telegrambot.app.services.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.telegrambot.app.DTO.api_1C.DataResponse;
import com.telegrambot.app.DTO.api_1C.DefaultDataResponse;
import com.telegrambot.app.DTO.api_1C.SyncDataResponse;
import com.telegrambot.app.DTO.api_1C.UserDataResponse;
import com.telegrambot.app.DTO.api_1C.legal.partner.PartnerDataResponse;
import com.telegrambot.app.DTO.api_1C.taskResponse.TaskDataListResponse;
import com.telegrambot.app.DTO.api_1C.taskResponse.TaskDataResponse;
import com.telegrambot.app.DTO.api_1C.taskResponse.TaskResponse;
import com.telegrambot.app.config.Connector1C;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.converter.ByteArrayHttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.Collections;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class API1CServicesImpl implements API1CServices {

    private final Connector1C connector1C;
    private final DaDataService dataService;

    public <T, R extends DataResponse> R executeRequest(T requestBody, Class<R> responseType, String method, String param, HttpMethod httpMethod) {
        ByteArrayHttpMessageConverter converter = new ByteArrayHttpMessageConverter();
        converter.setSupportedMediaTypes(Collections.singletonList(MediaType.APPLICATION_OCTET_STREAM));

        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().add(converter);

        HttpHeaders headers = new HttpHeaders();
//        headers.add("Authorization", connector1C.getToken());
        headers.add("token", connector1C.getToken());
        headers.setBasicAuth(connector1C.getLogin(), connector1C.getPassword());
        HttpEntity<T> requestEntity = new HttpEntity<>(requestBody, headers);
        param = param.isEmpty() ? "" : "?" + param;

        try {
            byte[] responseBody = restTemplate.exchange(
                            connector1C.getUrl() + method + param, httpMethod, requestEntity, byte[].class)
                    .getBody();
            ObjectMapper objectMapper = new ObjectMapper();
            R request = objectMapper.readValue(responseBody, responseType);
            if (!request.isResult()) {
                log.warn("The user's request was not executed, the server response: {}", request.getError());
            }
            return request;
        } catch (Exception e) {
            log.error(e.getMessage());
            return (R) new DataResponse(false, "Data not found");
        }
    }

    public <R extends DataResponse> R executeGetRequest(Class<R> responseType, String method, String param) {
        return executeRequest(null, responseType, method, param, HttpMethod.GET);
    }

    public <T, R extends DataResponse> R executePostRequest(T requestBody, Class<R> responseType, String method) {
        return executeRequest(requestBody, responseType, method, "", HttpMethod.POST);
    }

    @Override
    public DefaultDataResponse getDefaultData() {
        String param = "apiKey=" + connector1C.getToken();
        return executeGetRequest(DefaultDataResponse.class, "defaultData", param);
    }

    @Override
    public PartnerDataResponse getPartnerData(@NonNull String inn, String kpp) {
        return getCompany("inn=" + inn + "&kpp=" + kpp);
    }

    @Override
    public PartnerDataResponse getPartnerData(@NonNull String inn) {
        return getCompany("inn=" + inn + "&kpp=");
    }

    @Override
    public PartnerDataResponse getPartnerByGuid(@NonNull String guid) {
        return getCompany("guid=" + guid);
    }

    @Override
    public UserDataResponse getUserData(@NonNull String phone) {
        String param = "phone=" + phone;
        return executeGetRequest(UserDataResponse.class, "userdata", param);
    }

    @Override
    public TaskDataResponse getTaskByGuid(@NonNull String guid) {
        return getTask("guid=" + guid);
    }

    @Override
    public TaskDataResponse getTaskByCode(@NonNull String code) {
        return getTask("code=" + code);
    }

    @Override
    public SyncDataResponse createTask(@NonNull TaskResponse taskResponse) {
        return executePostRequest(taskResponse, SyncDataResponse.class, "task");
    }

    @Override
    public TaskDataListResponse getTaskListDataByCompany(String guidPartner) {
        return getTaskList("guidPartner=" + guidPartner);
    }

    @Override
    public TaskDataListResponse getTaskListDataByUser(String guidUser) {
        return getTaskList("guidUser=" + guidUser);
    }

    @Override
    public TaskDataListResponse getTaskListDataByManager(String guidUser) {
        return getTaskList("guidManager=" + guidUser);
    }

    @Override
    public TaskDataListResponse getTaskListDataByDepartment(String guidDepartment) {
        return getTaskList("guidDepartment=" + guidDepartment);
    }

    private PartnerDataResponse getCompany(String param) {
        return executeGetRequest(PartnerDataResponse.class, "legal", param);
    }

    private TaskDataResponse getTask(String param) {
        return executeGetRequest(TaskDataResponse.class, "task", param);
    }

    private TaskDataListResponse getTaskList(String param) {
        return executeGetRequest(TaskDataListResponse.class, "taskList", param);
    }

    private String getAbbreviation(String string) {
        return Arrays.stream(string.split("\\s+"))
                .filter(s -> s.matches("[a-zA-Zа-яёА-ЯЁ\\-_]{3,}"))
                .map(s -> String.valueOf(s.charAt(0)).toUpperCase())
                .collect(Collectors.joining());
    }
}
