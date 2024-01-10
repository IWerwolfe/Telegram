package com.supportbot.DTO;

import com.supportbot.DTO.api.typeОbjects.EntityResponse;

public interface DataResponseInt {

    <E extends EntityResponse> void setEntity(E entity);
}
