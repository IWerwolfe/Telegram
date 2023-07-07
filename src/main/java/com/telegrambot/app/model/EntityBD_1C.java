package com.telegrambot.app.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.format.DateTimeFormatter;

@Getter
@Setter
@NoArgsConstructor
@MappedSuperclass
public class EntityBD_1C {

    @Transient
    public DateTimeFormatter FORMATTER_DATE = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String code;
    private String guid;
    private String name;
    private Boolean markedForDel;

    @PrePersist
    private void prePersist() {
        EntityDefaults.initializeDefaultEntity1C(this);
    }

    public EntityBD_1C(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name == null ? ("Default " + this.getClass().getSimpleName().toLowerCase() + " " + this.id) : name;
    }
}
