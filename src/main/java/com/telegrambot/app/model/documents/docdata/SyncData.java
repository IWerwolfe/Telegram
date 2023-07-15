package com.telegrambot.app.model.documents.docdata;

import jakarta.persistence.Embeddable;
import jakarta.persistence.JoinColumn;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Clock;
import java.time.LocalDateTime;

@Getter
@Setter
@Embeddable
@NoArgsConstructor
public class SyncData {
    private String guid;
    private String code;
    @JoinColumn(name = "last_update")
    private LocalDateTime lastUpdate;

    public SyncData(String guid) {
        this.guid = guid;
        this.lastUpdate = LocalDateTime.now(Clock.systemDefaultZone());
    }

    public SyncData(String guid, String code) {
        this(guid);
        this.code = code;
    }
}
