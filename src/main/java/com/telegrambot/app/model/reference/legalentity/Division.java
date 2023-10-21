package com.telegrambot.app.model.reference.legalentity;

import com.telegrambot.app.model.documents.docdata.SyncData;
import com.telegrambot.app.model.types.Reference;
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
        this.setSyncData(new SyncData(guid));
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
