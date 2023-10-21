package com.telegrambot.app.DTO.api.reference.legal.division;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.telegrambot.app.DTO.api.typeОbjects.EntityResponse;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class DivisionResponse extends EntityResponse {

    @JsonCreator
    public DivisionResponse(String json) {
        createToJson(json, DivisionResponse.class, this);
    }
}
