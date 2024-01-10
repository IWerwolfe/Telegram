package com.supportbot.model.types.doctype;

import com.supportbot.model.types.Document;
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
