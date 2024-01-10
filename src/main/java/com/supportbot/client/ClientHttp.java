package com.supportbot.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.converter.ByteArrayHttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.util.Collections;

@Service
@Slf4j
@RequiredArgsConstructor
public abstract class ClientHttp {

    private final ObjectMapper objectMapper = new ObjectMapper();
    private RestTemplate restTemplate;
    private UriComponentsBuilder builder;


    public abstract String getUrl(String method);

    public abstract String getUrl(String method, String param);

    public abstract HttpHeaders getHttpHeaders();

    public <R> R executeGetRequest(Class<R> responseType, String url) {
        return executeRequest(null, responseType, url, HttpMethod.GET);
    }

    public <T, R> R executeGetRequest(T requestBody, Class<R> responseType, String url) {
        return executeRequest(requestBody, responseType, url, HttpMethod.GET);
    }

    public <T, R> R executePostRequest(T requestBody, Class<R> responseType, String url) {
        return executeRequest(requestBody, responseType, url, HttpMethod.POST);
    }

    public <T, R> R executePutRequest(T requestBody, Class<R> responseType, String url) {
        return executeRequest(requestBody, responseType, url, HttpMethod.PUT);
    }

    public <T, R> R executeRequest(T requestBody, Class<R> responseType, String url, HttpMethod httpMethod) {

        long start = System.currentTimeMillis();

        HttpHeaders headers = getHttpHeaders();
        HttpEntity<T> requestEntity = new HttpEntity<>(requestBody, headers);
        R response = null;

        try {
            response = getR(responseType, url, httpMethod, requestEntity);
        } catch (Exception e) {
            log.error(e.getMessage());
        }

        log.info("the user's request was executed for {} ms", System.currentTimeMillis() - start);
        return response;
    }

    private ByteArrayHttpMessageConverter getByteArrayHttpMessageConverter() {
        ByteArrayHttpMessageConverter converter = new ByteArrayHttpMessageConverter();
        converter.setSupportedMediaTypes(Collections.singletonList(MediaType.APPLICATION_OCTET_STREAM));
        return converter;
    }

    private RestTemplate getRestTemplate() {
        if (restTemplate != null) {
            return restTemplate;
        }
        restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().add(getByteArrayHttpMessageConverter());
        return restTemplate;
    }

    private <T, R> R getR(Class<R> responseType, String url, HttpMethod httpMethod, HttpEntity<T> requestEntity) throws IOException {
        byte[] responseBody = getRestTemplate()
                .exchange(url, httpMethod, requestEntity, byte[].class)
                .getBody();
        return objectMapper.readValue(responseBody, responseType);
    }

}
