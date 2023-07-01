package com.telegrambot.app.model.task;

import com.telegrambot.app.model.EntityDocBD_1C;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@Entity
@DiscriminatorValue("tasks")
public class Task extends EntityDocBD_1C {

    private String description;
    private String decision;
    @ManyToOne
    private TaskStatus status;
    private DocPartnerData partnerData;
    private LocalDateTime closingDate;
    @ManyToOne
    @JoinColumn(name = "type_id")
    private TaskType type;
    private Properties properties;
}
