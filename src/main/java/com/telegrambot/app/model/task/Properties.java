package com.telegrambot.app.model.task;

import jakarta.persistence.Embeddable;
import lombok.Data;

@Data
@Embeddable
public class Properties {
    private Boolean isOutsourcing;
    private Boolean highPriority;
    private Boolean isBilling;
}
