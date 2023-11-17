package com.telegrambot.app.moskModel;

import com.telegrambot.app.model.documents.docdata.SyncData;
import com.telegrambot.app.model.types.Reference;
import lombok.Getter;
import lombok.Setter;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@Getter
@Setter
@ExtendWith(MockitoExtension.class)
public class TestRef extends Reference {

    public TestRef(String guid) {
        this.setSyncData(new SyncData(guid));
    }

    public TestRef() {
    }

}
