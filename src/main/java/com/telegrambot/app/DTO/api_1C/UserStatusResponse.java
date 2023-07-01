package com.telegrambot.app.DTO.api_1C;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserStatusResponse {
    private String guid;
    private String post;
}
