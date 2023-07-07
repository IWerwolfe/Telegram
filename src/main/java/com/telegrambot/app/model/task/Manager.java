package com.telegrambot.app.model.task;

import com.telegrambot.app.model.EntityBD_1C;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@DiscriminatorValue("managers")
public class Manager extends EntityBD_1C {

    public Manager(String guid) {
        setGuid(guid);
    }
}
