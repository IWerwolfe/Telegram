package com.telegrambot.app.model.documents.docdata;

import jakarta.persistence.Embeddable;
import jakarta.persistence.JoinColumn;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Clock;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

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

    @Override
    public String toString() {
        boolean isSync = guid != null && lastUpdate != null;
        return isSync ? "Синхронизирован " + getDescriptor() :
                "НЕ синхронизировано";
    }

    private String getDescriptor() {
        StringBuilder string = new StringBuilder();
        if (lastUpdate != null) {
            string.append(lastUpdate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")))
                    .append(System.lineSeparator());
        }
        if (guid != null && !guid.isEmpty()) {
            string.append("guid: ")
                    .append(guid)
                    .append(System.lineSeparator());
        }
        if (code != null && !code.isEmpty()) {
            string.append("code: ")
                    .append(code);
        }
        return string.toString();
    }
}
