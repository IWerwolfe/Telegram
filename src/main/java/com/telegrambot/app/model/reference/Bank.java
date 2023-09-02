package com.telegrambot.app.model.reference;

import com.telegrambot.app.model.types.Reference;
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

    @JoinColumn(name = "correspondent_account")
    private Long correspondentAccount;
    private String city;
    private String address;
    private String phone;
    private String swift;
    private String country;
}
