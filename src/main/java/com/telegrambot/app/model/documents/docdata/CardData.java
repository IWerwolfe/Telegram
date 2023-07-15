package com.telegrambot.app.model.documents.docdata;

import jakarta.persistence.Embeddable;
import jakarta.persistence.JoinColumn;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Embeddable
public class CardData {

    @JoinColumn(name = "card_number")
    private String cardNumber;
    @JoinColumn(name = "card_type")
    private String cardType;
}
