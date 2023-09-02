package com.telegrambot.app.DTO;

import com.telegrambot.app.DTO.api.typeОbjects.EntityResponse;

public interface DataResponseInt {

    <E extends EntityResponse> void setEntity(E entity);
}
