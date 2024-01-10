package com.supportbot.DTO.api.reference.manager;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.supportbot.DTO.api.typeОbjects.DataListResponse;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class ManagerDataListResponse extends DataListResponse<ManagerResponse> {
    public ManagerDataListResponse(boolean result, String error) {
        super(result, error);
    }
}
