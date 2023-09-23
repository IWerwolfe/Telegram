package com.telegrambot.app.DTO.api.typeОbjects;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;

import java.io.Serializable;

@Data
@Slf4j
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class EntityResponse implements Serializable {

    public final String DATE_PATTERN = "yyyy-MM-dd'T'HH:mm:ss";
    private String code;
    private String guid;
    private String name;
    private Boolean markedForDel;

    @JsonCreator
    public EntityResponse(String json) {
        createToJson(json, EntityResponse.class, this);
    }

    public static <E> void createToJson(String json, Class<E> entityClass, E entity) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            E response = objectMapper.readValue(json, entityClass);
            BeanUtils.copyProperties(response, entity);
        } catch (JsonProcessingException e) {
            log.error("The error occurred during the creation of an {} from a JSON: {}{}",
                    entityClass.getSimpleName(),
                    System.lineSeparator(),
                    e.getMessage());
        }
    }
}
