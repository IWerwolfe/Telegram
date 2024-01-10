package com.supportbot.model.reference;

import com.supportbot.model.types.Reference;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "banks")
@NoArgsConstructor
public class Bank extends Reference {

    @JoinColumn(name = "corr_account")
    private String corrAccount;
    private String city;
    private String address;
    private String phone;
    private String swift;
    private String country;

    public Bank(String guid) {
        super(guid);
    }

    public Bank(String guid, String code) {
        super(guid, code);
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
