package com.telegrambot.app.model;

import com.telegrambot.app.model.task.Manager;
import jakarta.persistence.Column;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@MappedSuperclass
public class EntityDocBD_1C extends EntityBD_1C {
    private LocalDateTime date;
    @Column(columnDefinition = "text")
    private String comment;
    private Double amount;
    private String author;
    private String division;
    @ManyToOne
    private Manager manager;

    @PrePersist
    private void prePersist() {
        EntityDefaults.initializeDefaultEntityDoc(this);
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName() + " № " + (getCode() == null ? getId() : getCode()) + " от " + getDate().format(FORMATTER_DATE);
    }
}
