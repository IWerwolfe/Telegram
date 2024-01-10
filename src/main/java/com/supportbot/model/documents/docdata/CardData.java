package com.supportbot.model.documents.docdata;

import jakarta.persistence.Embeddable;
import jakarta.persistence.JoinColumn;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Embeddable
@NoArgsConstructor
@AllArgsConstructor
public class CardData {

    @JoinColumn(name = "card_number")
    private String cardNumber;
    @JoinColumn(name = "card_type")
    private String cardType;

    public static CardData createToDefault() {
        return new CardData("0000-0000-0000-0000", "Visa");
    }
}
