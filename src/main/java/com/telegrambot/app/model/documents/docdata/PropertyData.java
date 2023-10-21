package com.telegrambot.app.model.documents.docdata;

import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Embeddable
public class PropertyData {
    private Boolean isOutsourcing;
    private Boolean highPriority;
    private Boolean isBilling;

    public PropertyData() {
        this.isOutsourcing = false;
        this.highPriority = false;
        this.isBilling = false;
    }

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
