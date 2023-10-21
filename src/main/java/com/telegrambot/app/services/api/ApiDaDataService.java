package com.telegrambot.app.services.api;

import com.telegrambot.app.DTO.dadata.DaDataData;
import com.telegrambot.app.DTO.dadata.DaDataParty;
import com.telegrambot.app.DTO.dadata.DaDataResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Objects;

@Service
@Slf4j
public class ApiDaDataService {

    private static final String URL_DADATA_PARTY = "https://suggestions.dadata.ru/suggestions/api/4_1/rs/findById/party";
    @Value("${dadata.api.key}")
    private String apiKey;

    public DaDataParty getCompanyDataByINN(String inn) {

        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        headers.set("Authorization", "Token " + apiKey);

        UriComponentsBuilder builder = UriComponentsBuilder
                .fromHttpUrl(URL_DADATA_PARTY)
                .queryParam("query", inn);

        HttpEntity<?> entity = new HttpEntity<>(headers);
        ResponseEntity<DaDataResponse> response = null;
        try {
            RestTemplate restTemplate = new RestTemplate();
            response = restTemplate.exchange(
                    builder.toUriString(),
                    HttpMethod.GET,
                    entity,
                    DaDataResponse.class
            );
        } catch (Exception e) {
            log.error(e.getMessage());
        }

        if (response != null && response.getStatusCode().is2xxSuccessful()) {
            DaDataData[] data = Objects.requireNonNull(response.getBody()).getSuggestions();
            return data.length == 0 ? null : data[0].getData();
        } else {
            log.error("Failed to retrieve company data from DaData API");
            return null;
        }
    }
}
