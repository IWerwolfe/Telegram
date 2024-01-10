package com.supportbot.mockResponse.testDoc;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.supportbot.DTO.api.typeОbjects.EntityDocResponse;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class TestDocResponse extends EntityDocResponse {
}
