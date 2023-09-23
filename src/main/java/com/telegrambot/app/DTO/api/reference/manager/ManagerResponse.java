package com.telegrambot.app.DTO.api.reference.manager;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.telegrambot.app.DTO.api.typeОbjects.EntityResponse;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class ManagerResponse extends EntityResponse {

    @JsonCreator
    public ManagerResponse(String json) {
        createToJson(json, ManagerResponse.class, this);
    }
}
