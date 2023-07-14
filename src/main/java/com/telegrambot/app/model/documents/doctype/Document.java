package com.telegrambot.app.model.documents.doctype;

import com.telegrambot.app.model.documents.docdata.SyncData;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@MappedSuperclass
public abstract class Document {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private LocalDateTime date;
    @Column(columnDefinition = "text")
    private String comment;
    private Boolean markedForDel;
    private String author;
    private SyncData syncData;
}
