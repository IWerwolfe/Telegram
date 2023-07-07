package com.telegrambot.app.model.task;

import com.telegrambot.app.model.EntityDefaults;
import com.telegrambot.app.model.EntityDocBD_1C;
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
    private DocPartnerData partnerData;
    private LocalDateTime closingDate;
    @ManyToOne
    @JoinColumn(name = "type_id")
    private TaskType type;
    private Properties properties;
    @ManyToOne
    @JoinColumn(name = "create_id")
    private UserBD creator;

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
                    .append(status)
                    .append(dualSeparator)
                    .append(partnerData)
                    .append(dualSeparator)
                    .append(getManager() == null ? "не назначен" : getManager())
                    .append(properties).append(dualSeparator)
                    .append(description).append(dualSeparator);

            if (getComment() != null && !getComment().isEmpty()) {
                builder.append(dualSeparator).append("Примечание: ").append(separator).append(getComment());
            }
            return builder.toString();
        }
        return super.toString();
    }
}
