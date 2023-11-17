package com.telegrambot.app.model.types;

import com.telegrambot.app.DTO.api.other.SyncDataResponse;
import com.telegrambot.app.model.EntityDefaults;
import com.telegrambot.app.model.documents.docdata.SyncData;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@MappedSuperclass
public abstract class Entity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(name = "sync_data")
    private SyncData syncData;
    private String author;
    @Column(name = "marked_for_del")
    private Boolean markedForDel = false;

    public Entity() {
    }

    public Entity(String guid) {
        this.setSyncData(new SyncData(guid));
    }

    public Entity(String guid, String code) {
        this.setSyncData(new SyncData(guid, code));
    }

    @PrePersist
    private void prePersist() {
        EntityDefaults.initializeDefaultEntity1C(this);
    }

    public void setSyncData(String guid, String code) {
        if (syncData == null) {
            this.syncData = new SyncData(guid, code);
            return;
        }
        if (code != null) {
            this.syncData.setCode(code);
        }
        this.syncData.setLastUpdate(LocalDateTime.now());
    }

    public void setSyncData(SyncData syncData) {
        this.syncData = syncData;
        this.syncData.setLastUpdate(LocalDateTime.now());
    }

    public void setSyncData(SyncDataResponse syncData) {
        if (syncData != null && syncData.isResult()) {
            this.syncData = new SyncData(syncData.getGuid(), syncData.getCode());
            this.syncData.setLastUpdate(LocalDateTime.now());
        }
    }

    public String getCodeEntity() {
        return this.getSyncData() == null || this.getSyncData().getCode() == null || this.getSyncData().getCode().isEmpty() ?
                String.valueOf(this.getId()) :
                this.getSyncData().getCode();
    }

    public String getGuidEntity() {
        return this.getSyncData() == null ? null :
                this.getSyncData().getGuid();
    }
}
