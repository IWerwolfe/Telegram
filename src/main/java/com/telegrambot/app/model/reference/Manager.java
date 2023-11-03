package com.telegrambot.app.model.reference;

import com.telegrambot.app.model.types.Reference;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "managers")
public class Manager extends Reference {

    public Manager(String guid) {
        super(guid);
    }

    public Manager(String guid, String code) {
        super(guid, code);
    }
}
