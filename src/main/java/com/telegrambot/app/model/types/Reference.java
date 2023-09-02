package com.telegrambot.app.model.types;

import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
//@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
//@jakarta.persistence.Entity
//@DiscriminatorValue("references_entities")
@MappedSuperclass
public abstract class Reference extends Entity {
    private String name;

    @Override
    public String toString() {
        return name == null ? ("Default " + this.getClass().getSimpleName().toLowerCase() + " " + this.getId()) : name;
    }
}
