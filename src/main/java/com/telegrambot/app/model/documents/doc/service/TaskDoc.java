package com.telegrambot.app.model.documents.doc.service;

import com.telegrambot.app.DTO.types.TaskType;
import com.telegrambot.app.model.documents.docdata.PartnerData;
import com.telegrambot.app.model.documents.docdata.PropertyData;
import com.telegrambot.app.model.documents.docdata.SyncData;
import com.telegrambot.app.model.reference.TaskStatus;
import com.telegrambot.app.model.types.doctype.SalesDoc;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "tasks")
public class TaskDoc extends SalesDoc {

    @Column(columnDefinition = "text")
    private String description;
    @Column(columnDefinition = "text")
    private String decision;
    @ManyToOne
    private TaskStatus status;
    private LocalDateTime closingDate;
    @ManyToOne
    @JoinColumn(name = "type_id")
    private TaskType type;
    private PropertyData properties;
    private Boolean successfully;

    public TaskDoc(String guid) {
        this();
        this.setSyncData(new SyncData(guid));
    }

    public TaskDoc() {
        this.setPartnerData(new PartnerData());
        this.setProperties(new PropertyData());
    }

    public String toString(boolean full) {

        String dualSeparator = System.lineSeparator() + System.lineSeparator();
        String separator = System.lineSeparator();

        if (full) {
            StringBuilder builder = new StringBuilder();
            builder.append(super.toString())
                    .append(separator)
                    .append("Вид: ")
                    .append(getType())
                    .append(separator)
                    .append("Статус: ")
                    .append(status);
            if (getTotalAmount() != null && getTotalAmount() > 0) {
                builder.append(separator)
                        .append("Сумма: ")
                        .append(getPresentTotalAmount())
                        .append(" р.");
            }
            builder.append(dualSeparator)
                    .append(getPartnerData())
                    .append(dualSeparator)
                    .append(getManager() == null ? "не назначен" : getManager());
            if (properties != null) {
                builder.append(separator)
                        .append(properties);
            }
            builder.append(dualSeparator)
                    .append(description)
                    .append(dualSeparator);
            if (decision != null && !decision.isEmpty()) {
                builder.append("Решение: ")
                        .append(separator)
                        .append(decision)
                        .append(dualSeparator);
            }
            if (getComment() != null && !getComment().isEmpty()) {
                builder.append("Примечание: ")
                        .append(separator)
                        .append(getComment())
                        .append(dualSeparator);
            }
            return builder.toString();
        }
        return super.toString();
    }

    @Override
    protected String getDescriptor() {
        return "Задача";
    }
}