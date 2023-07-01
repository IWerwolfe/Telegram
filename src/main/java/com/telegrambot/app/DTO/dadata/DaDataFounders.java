package com.telegrambot.app.DTO.dadata;

import lombok.Data;

@Data
public class DaDataFounders {
    private String ogrn;
    private String inn;
    private String name;
    private String hid;
    private String type;
    private DaDataShare share;
}
