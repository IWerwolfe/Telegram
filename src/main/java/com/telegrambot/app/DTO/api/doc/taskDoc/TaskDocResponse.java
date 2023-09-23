package com.telegrambot.app.DTO.api.doc.taskDoc;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.telegrambot.app.DTO.api.typeОbjects.EntityDocResponse;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class TaskDocResponse extends EntityDocResponse {
    private String description;
    private String decision;
    private String guidStatus;
    private String guidAuthor;
    @JsonFormat(pattern = DATE_PATTERN)
    private Date closingDate;
    private String type;
    private Boolean isOutsourcing;
    private Boolean highPriority;
    private Boolean isBilling;

    @JsonCreator
    public TaskDocResponse(String json) {
        createToJson(json, TaskDocResponse.class, this);
    }
}
