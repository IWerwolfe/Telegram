package com.supportbot.moskModel;

import com.supportbot.model.documents.docdata.SyncData;
import com.supportbot.model.types.doctype.SalesDoc;
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
