package com.supportbot.model.documents.docdata;

import jakarta.persistence.Embeddable;
import jakarta.persistence.JoinColumn;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Embeddable
public class IncomingData {

    @JoinColumn(name = "doc_date")
    private LocalDateTime docDate;
    @JoinColumn(name = "doc_code")
    private String docCode;
}
