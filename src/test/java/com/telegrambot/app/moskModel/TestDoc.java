package com.telegrambot.app.moskModel;

import com.telegrambot.app.model.documents.docdata.SyncData;
import com.telegrambot.app.model.types.doctype.SalesDoc;
import lombok.Getter;
import lombok.Setter;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@Getter
@Setter
@ExtendWith(MockitoExtension.class)
public class TestDoc extends SalesDoc {

    public TestDoc(String guid) {
        this.setSyncData(new SyncData(guid));
    }

    public TestDoc() {
    }

    @Override
    protected String getDescriptor() {
        return "Test document";
    }
}
