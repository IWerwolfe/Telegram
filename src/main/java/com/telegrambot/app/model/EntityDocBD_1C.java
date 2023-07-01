package com.telegrambot.app.model;

import com.telegrambot.app.model.task.Manager;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MappedSuperclass;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@MappedSuperclass
public class EntityDocBD_1C extends EntityBD_1C {
    private LocalDateTime date;
    private String comment;
    private Double amount;
    private String author;
    private String division;
    @ManyToOne
    private Manager manager;
}
