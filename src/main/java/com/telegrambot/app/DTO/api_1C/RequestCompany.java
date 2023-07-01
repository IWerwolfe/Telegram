package com.telegrambot.app.DTO.api_1C;

import lombok.Data;

@Data
public class RequestCompany {
    private String inn;
    private String kpp;

    public RequestCompany(String inn, String kpp) {
        this.inn = inn;
        this.kpp = kpp;
    }
}
