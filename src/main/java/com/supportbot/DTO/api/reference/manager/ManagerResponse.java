package com.supportbot.DTO.api.reference.manager;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.supportbot.DTO.api.typeОbjects.EntityResponse;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class ManagerResponse extends EntityResponse {

    @JsonCreator
    public ManagerResponse(String json) {
        fillToJson(json, ManagerResponse.class, this);
    }
}
