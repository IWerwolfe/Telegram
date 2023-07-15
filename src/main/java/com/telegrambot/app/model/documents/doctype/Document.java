package com.telegrambot.app.model.documents.doctype;

import com.telegrambot.app.model.Entity;
import com.telegrambot.app.model.EntityDefaults;
import com.telegrambot.app.model.documents.docdata.PartnerData;
import com.telegrambot.app.model.legalentity.Company;
import com.telegrambot.app.model.reference.Manager;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Getter
@Setter
//@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
//@jakarta.persistence.Entity
//@DiscriminatorColumn(name = "documents")
@MappedSuperclass
public abstract class Document extends Entity {

    private LocalDateTime date;
    @Column(columnDefinition = "text")
    private String comment;
    @ManyToOne
    @JoinColumn(name = "company_id")
    private Company company;
    @Column(name = "partner_data")
    private PartnerData partnerData;
    private String division;
    @ManyToOne
    @JoinColumn(name = "manager_id")
    private Manager manager;
    @Column(name = "total_amount", precision = 15, scale = 3)
    private BigDecimal totalAmount = BigDecimal.valueOf(0);
//    @Column(name = "parent_doc")
//    private Document parentDoc;

    protected abstract String getDescriptor();

    @PrePersist
    private void prePersist() {
        EntityDefaults.initializeDefaultEntityDoc(this);
    }

    @Override
    public String toString() {
        String code = (getSyncData() == null ? String.valueOf(getId()) : getSyncData().getCode());
        String date = getDate().format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
        return getDescriptor() + " № " + code + " от " + date;
    }
}
