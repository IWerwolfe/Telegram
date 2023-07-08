package com.telegrambot.app.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.telegrambot.app.DTO.DataResponse;
import com.telegrambot.app.DTO.api_1C.*;
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
import java.util.function.Supplier;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class API1CServicesImpl implements API1CServices {

    private final Connector1C connector1C;
    private final DaDataService dataService;

    public <T, R> R executeRequest(T requestBody, Class<R> responseType, String method, String param, HttpMethod httpMethod) {
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
            return objectMapper.readValue(responseBody, responseType);
        } catch (Exception e) {
            log.error(e.getMessage());
            return null;
        }
    }

    public <R> R executeGetRequest(Class<R> responseType, String method, String param) {
        return executeRequest(null, responseType, method, param, HttpMethod.GET);
    }

    public <T, R> R executePostRequest(T requestBody, Class<R> responseType, String method) {
        return executeRequest(requestBody, responseType, method, "", HttpMethod.POST);
    }

    @Override
    public DefaultDataResponse getDefaultData() {
        String param = "apiKey=" + connector1C.getToken();
        DefaultDataResponse response = executeGetRequest(DefaultDataResponse.class, "defaultData", param);
        if (response == null || !response.isResult()) {
            log.warn(response == null ? "the default data is not received" : response.getError());
        }
        return response;
    }

    @Override
    public CompanyDataResponse getCompanyData(@NonNull String inn, String kpp) {
        return getCompany("inn=" + inn + "&kpp=" + kpp);
    }

    @Override
    public CompanyDataResponse getCompanyData(@NonNull String inn) {
        return getCompany("inn=" + inn + "&kpp=");
    }

    @Override
    public CompanyDataResponse getCompanyByGuid(@NonNull String guid) {
        return getCompany("guid=" + guid);
    }

    @Override
    public UserDataResponse getUserData(@NonNull String phone) {
        String param = "phone=" + phone;
        UserDataResponse response = executeGetRequest(UserDataResponse.class, "userdata", param);
        response = createEntityIfNull(response, UserDataResponse::new);
        if (!response.isResult()) {
            log.warn("The user's request was not executed, the server response: {}", response.getError());
        }
        return response;
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
    public TaskCreateResponse createTask(@NonNull TaskResponse taskResponse) {
        TaskCreateResponse response = executePostRequest(taskResponse, TaskCreateResponse.class, "task");
        if (response == null || !response.isResult()) {
            log.warn(response == null ? "create task error" : response.getError());
        }
        return response;
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
    public TaskDataListResponse getTaskListDataByDepartment(String guidDepartment) {
        return getTaskList("guidDepartment=" + guidDepartment);
    }

    private CompanyDataResponse getCompany(String param) {
        CompanyDataResponse response = executeGetRequest(CompanyDataResponse.class, "legal", param);
        response = createEntityIfNull(response, CompanyDataResponse::new);
        if (!response.isResult()) {
            log.warn("The user's request was not executed, the server response: {}", response.getError());
        }
        return response;
    }

    private TaskDataResponse getTask(String param) {
        TaskDataResponse response = executeGetRequest(TaskDataResponse.class, "task", param);
        response = createEntityIfNull(response, TaskDataResponse::new);
        if (!response.isResult()) {
            log.warn("The user's request was not executed, the server response: {}", response.getError());
        }
        return response;
    }

    private TaskDataListResponse getTaskList(String param) {
        TaskDataListResponse response = executeGetRequest(TaskDataListResponse.class, "task", param);
        response = createEntityIfNull(response, TaskDataListResponse::new);
        if (!response.isResult()) {
            log.warn("The user's request was not executed, the server response: {}", response.getError());
        }
        return response;
    }

    private String getAbbreviation(String string) {
        return Arrays.stream(string.split("\\s+"))
                .filter(s -> s.matches("[a-zA-Zа-яёА-ЯЁ\\-_]{3,}"))
                .map(s -> String.valueOf(s.charAt(0)).toUpperCase())
                .collect(Collectors.joining());
    }

    public <T extends DataResponse> T createEntityIfNull(T entity, Supplier<T> entitySupplier) {
        if (entity == null) {
            entity = entitySupplier.get();
            entity.setResult(false);
            entity.setError("Data not found");
        }
        return entity;
    }
}
