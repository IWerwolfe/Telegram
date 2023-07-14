package com.telegrambot.app.model.documents.docdata;

import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Embeddable
public class SyncData {
    private String guid;
    private String code;
    private LocalDateTime lastUpdate;
}
