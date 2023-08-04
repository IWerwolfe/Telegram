package com.telegrambot.app.DTO.api_1C.manager;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.telegrambot.app.DTO.api_1C.typeОbjects.Entity1C;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ManagerResponse extends Entity1C {
}
