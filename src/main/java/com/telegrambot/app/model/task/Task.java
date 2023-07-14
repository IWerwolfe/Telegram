package com.telegrambot.app.model.task;

import com.telegrambot.app.model.EntityDefaults;
import com.telegrambot.app.model.EntityDocBD_1C;
import com.telegrambot.app.model.documents.docdata.PartnerData;
import com.telegrambot.app.model.user.UserBD;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@DiscriminatorValue("tasks")
public class Task extends EntityDocBD_1C {

    @Column(columnDefinition = "text")
    private String description;
    @Column(columnDefinition = "text")
    private String decision;
    @ManyToOne
    private TaskStatus status;
    private PartnerData partnerData;
    private LocalDateTime closingDate;
    @ManyToOne
    @JoinColumn(name = "type_id")
    private TaskType type;
    private Properties properties;
    @ManyToOne
    @JoinColumn(name = "create_id")
    private UserBD creator;
    private Boolean successfully;

    public Task(String guid) {
        this();
        setGuid(guid);
    }

    public Task() {
        EntityDefaults.initializeDefaultTask(this);
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
            if (getAmount() != null && getAmount() > 0) {
                builder.append("Сумма: ")
                        .append(getAmount())
                        .append(" р.");
            }
            builder.append(dualSeparator)
                    .append(partnerData)
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
}
