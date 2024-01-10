package com.supportbot.model.reference.legalentity;

import com.supportbot.model.types.Reference;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "division")
@NoArgsConstructor
public class Division extends Reference {

    public Division(String guid) {
        super(guid);
    }

    public Division(String guid, String code) {
        super(guid, code);
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
