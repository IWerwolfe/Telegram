package com.supportbot.model.reference;

import com.supportbot.model.types.Reference;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "cash_desk_kkm")
@NoArgsConstructor
public class CashDeskKkm extends Reference {

    public CashDeskKkm(String guid) {
        super(guid);
    }

    public CashDeskKkm(String guid, String code) {
        super(guid, code);
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
