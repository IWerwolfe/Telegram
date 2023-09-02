package com.telegrambot.app.DTO.api.other;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserStatusResponse {
    private String guid;
    private String post;
}
