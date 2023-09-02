package com.telegrambot.app.model.types.doctype;

import com.telegrambot.app.model.types.Document;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@MappedSuperclass
public abstract class SalesDoc extends Document {

    @Override
    public Integer getTransactionAmount() {
        return -getTotalAmount();
    }
}
