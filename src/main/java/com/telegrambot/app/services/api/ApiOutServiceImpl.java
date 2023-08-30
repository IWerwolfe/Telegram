package com.telegrambot.app.services.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.telegrambot.app.DTO.api.DataResponse;
import com.telegrambot.app.DTO.api.DefaultDataResponse;
import com.telegrambot.app.DTO.api.SyncDataResponse;
import com.telegrambot.app.DTO.api.UserResponse;
import com.telegrambot.app.DTO.api.doc.taskDoc.TaskDocDataListResponse;
import com.telegrambot.app.DTO.api.doc.taskDoc.TaskDocDataResponse;
import com.telegrambot.app.DTO.api.doc.taskDoc.TaskDocResponse;
import com.telegrambot.app.DTO.api.legal.partner.PartnerDataResponse;
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

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ApiOutServiceImpl implements ApiOutService {

    private final Connector1C connector1C;

    public <T, R extends DataResponse> R executeRequest(T requestBody, Class<R> responseType, String method, String param, HttpMethod httpMethod) {
        long start = System.currentTimeMillis();
        HttpHeaders headers = getHttpHeaders();
        HttpEntity<T> requestEntity = new HttpEntity<>(requestBody, headers);
        String url = connector1C.getUrl() + method + (param.isEmpty() ? "" : "?" + param);
        R response;
        try {
            response = getR(responseType, url, httpMethod, requestEntity);
        } catch (Exception e) {
            log.error(e.getMessage());
            response = (R) new DataResponse(false, "Data not found");
        }
        log.info("the user's request was executed for {} ms", System.currentTimeMillis() - start);
        return response;
    }

    private HttpHeaders getHttpHeaders() {
        HttpHeaders headers = new HttpHeaders();
//        headers.add("Authorization", connector1C.getToken());
        headers.add("token", connector1C.getToken());
        headers.setBasicAuth(connector1C.getLogin(), connector1C.getPassword());
        return headers;
    }

    private static ByteArrayHttpMessageConverter getByteArrayHttpMessageConverter() {
        ByteArrayHttpMessageConverter converter = new ByteArrayHttpMessageConverter();
        converter.setSupportedMediaTypes(Collections.singletonList(MediaType.APPLICATION_OCTET_STREAM));
        return converter;
    }

    private static RestTemplate getRestTemplate() {
        ByteArrayHttpMessageConverter converter = getByteArrayHttpMessageConverter();
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().add(converter);
        return restTemplate;
    }

    private <T, R extends DataResponse> R getR(Class<R> responseType, String url, HttpMethod httpMethod, HttpEntity<T> requestEntity) throws IOException {
        RestTemplate restTemplate = getRestTemplate();
        byte[] responseBody = restTemplate
                .exchange(url, httpMethod, requestEntity, byte[].class)
                .getBody();
        ObjectMapper objectMapper = new ObjectMapper();
        R request = objectMapper.readValue(responseBody, responseType);
        if (!request.isResult()) {
            log.warn("The user's request was not executed, the server response: {}", request.getError());
        }
        return request;
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
    public UserResponse getUserData(@NonNull String phone) {
        return executeGetRequest(UserResponse.class, "userdata", "phone=" + phone);
    }

    @Override
    public TaskDocDataResponse getTaskByGuid(@NonNull String guid) {
        return getTask("guid=" + guid);
    }

    @Override
    public TaskDocDataResponse getTaskByCode(@NonNull String code) {
        return getTask("code=" + code);
    }

    @Override
    public SyncDataResponse createTask(@NonNull TaskDocResponse taskDocResponse) {
        return executePostRequest(taskDocResponse, SyncDataResponse.class, "task");
    }

    @Override
    public TaskDocDataListResponse getTaskListDataByCompany(String guidPartner) {
        return getTaskList("guidPartner=" + guidPartner);
    }

    @Override
    public TaskDocDataListResponse getTaskListDataByUser(String guidUser) {
        return getTaskList("guidUser=" + guidUser);
    }

    @Override
    public TaskDocDataListResponse getTaskListDataByManager(String guidUser) {
        return getTaskList("guidManager=" + guidUser);
    }

    @Override
    public TaskDocDataListResponse getTaskListDataByDepartment(String guidDepartment) {
        return getTaskList("guidDepartment=" + guidDepartment);
    }

    private PartnerDataResponse getCompany(String param) {
        return executeGetRequest(PartnerDataResponse.class, "legal", param);
    }

    private TaskDocDataResponse getTask(String param) {
        return executeGetRequest(TaskDocDataResponse.class, "task", param);
    }

    private TaskDocDataListResponse getTaskList(String param) {
        return executeGetRequest(TaskDocDataListResponse.class, "taskList", param);
    }

    private String getAbbreviation(String string) {
        return Arrays.stream(string.split("\\s+"))
                .filter(s -> s.matches("[a-zA-Zа-яёА-ЯЁ\\-_]{3,}"))
                .map(s -> String.valueOf(s.charAt(0)).toUpperCase())
                .collect(Collectors.joining());
    }
}
