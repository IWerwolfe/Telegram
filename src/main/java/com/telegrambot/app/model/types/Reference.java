package com.telegrambot.app.model.types;

import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@MappedSuperclass
public abstract class Reference extends Entity {

    private String name;

    public Reference() {
    }

    public Reference(String guid) {
        super(guid);
    }

    public Reference(String guid, String code) {
        super(guid, code);
    }

    @Override
    public String toString() {
        return name == null ? ("Default " + this.getClass().getSimpleName().toLowerCase() + " " + this.getId()) : name;
    }
}
