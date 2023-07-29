package com.telegrambot.app.model.documents.doctype;

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
