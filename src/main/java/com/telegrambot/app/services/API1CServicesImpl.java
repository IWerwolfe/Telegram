package com.telegrambot.app.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.telegrambot.app.DTO.DataResponse;
import com.telegrambot.app.DTO.Task;
import com.telegrambot.app.DTO.api_1C.CompanyDataResponse;
import com.telegrambot.app.DTO.api_1C.UserDataResponse;
import com.telegrambot.app.config.Connector1C;
import com.telegrambot.app.model.user.UserBD;
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
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class API1CServicesImpl implements API1CServices {

    private final Connector1C connector1C;
    private final DaDataService dataService;

    public <T, R> R executePostRequest(T requestBody, Class<R> responseType, String method, String param) {
        ByteArrayHttpMessageConverter converter = new ByteArrayHttpMessageConverter();
        converter.setSupportedMediaTypes(Collections.singletonList(MediaType.APPLICATION_OCTET_STREAM));

        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().add(converter);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", connector1C.getToken());
        headers.setBasicAuth(connector1C.getLogin(), connector1C.getPassword());
        HttpEntity<T> requestEntity = new HttpEntity<>(requestBody, headers);
        param = param.isEmpty() ? "" : "?" + param;

        try {
            byte[] responseBody = restTemplate.exchange(
                            connector1C.getUrl() + method + param, HttpMethod.GET, requestEntity, byte[].class)
                    .getBody();
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readValue(responseBody, responseType);
        } catch (Exception e) {
            log.error(e.getMessage());
            return null;
        }
    }

    public <T, R> R executePostRequest(T requestBody, Class<R> responseType, String method) {
        return executePostRequest(requestBody, responseType, method, "");
    }

    public <R> R executePostRequest(Class<R> responseType, String method, String param) {
        return executePostRequest(null, responseType, method, param);
    }

    public <R> R executePostRequest(Class<R> responseType, String method) {
        return executePostRequest(null, responseType, method, "");
    }

    @Override
    public CompanyDataResponse getCompany(@NonNull String inn, String kpp) {
        String param = "inn=" + inn + "&kpp=" + kpp;
        CompanyDataResponse response = executePostRequest(CompanyDataResponse.class, "legal", param);
        if (response == null || !response.isResult()) {
            log.warn(response == null ? "The organization's request was not fulfilled" : response.getError());
        }
        return response;
    }

    @Override
    public CompanyDataResponse getCompany(String inn) {
        return getCompany(inn, "");
    }

    @Override
    public CompanyDataResponse getCompanyByGuid(@NonNull String guid) {
        String param = "guid=" + guid;
        CompanyDataResponse response = executePostRequest(CompanyDataResponse.class, "legal", param);
        response = createEntityIfNull(response, CompanyDataResponse::new);
        if (!response.isResult()) {
            log.warn("The user's request was not executed, the server response: {}", response.getError());
        }
        return response;
    }

    @Override
    public UserDataResponse getUserData(String phone) {
        String param = "phone=" + phone;
        UserDataResponse response = executePostRequest(UserDataResponse.class, "userdata", param);
        response = createEntityIfNull(response, UserDataResponse::new);
        if (!response.isResult()) {
            log.warn("The user's request was not executed, the server response: {}", response.getError());
        }
        return response;
    }

    @Override
    public Task getTask(String id) {
        return null;
    }

    @Override
    public List<Task> getTaskList(String inn) {
        return null;
    }

    @Override
    public List<Task> getTaskList(UserBD userBD) {
        return null;
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
