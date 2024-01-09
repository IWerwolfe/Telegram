package com.telegrambot.app.client;

import com.telegrambot.app.DTO.dadata.DaDataData;
import com.telegrambot.app.DTO.dadata.DaDataParty;
import com.telegrambot.app.DTO.dadata.DaDataResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class DaDataClient extends ClientHttp {

    @Value("${dadata.api.key}")
    private String apiKey;

    @Override
    public String getUrl(String method) {
        return "https://suggestions.dadata.ru/suggestions/api/4_1/rs/findById/" + method;
    }

    @Override
    public String getUrl(String method, String param) {
        return getUrl(method) + "?query=" + param;
    }

    @Override
    public HttpHeaders getHttpHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Token " + apiKey);
        return headers;
    }

    public DaDataParty getCompanyDataByINN(String inn) {

        DaDataResponse result = executeGetRequest(DaDataResponse.class, getUrl("party", inn));
        DaDataData[] data = result.getSuggestions();
        return data.length == 0 ? null : data[0].getData();
    }
}
