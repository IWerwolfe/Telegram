package com.telegrambot.app.model.reference;

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
@Table(name = "cash_desks")
@NoArgsConstructor
public class CashDesk extends Reference {

    public CashDesk(String guid) {
        this.setSyncData(new SyncData(guid));
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
