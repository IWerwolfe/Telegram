package com.telegrambot.app.model;

import com.telegrambot.app.model.documents.docdata.SyncData;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

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

    @PrePersist
    private void prePersist() {
        EntityDefaults.initializeDefaultEntity1C(this);
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
