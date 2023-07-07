package com.telegrambot.app.model.task;

import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Embeddable
public class Properties {
    private Boolean isOutsourcing;
    private Boolean highPriority;
    private Boolean isBilling;

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        if (isBilling) {
            builder.append("По договору обслуживания")
                    .append(System.lineSeparator());
        }
        if (highPriority) {
            builder.append("Срочная!!!")
                    .append(System.lineSeparator());
        }
        if (isOutsourcing) {
            builder.append("Передана на аутсортинг");
        }
        return builder.toString().trim();
    }
}
