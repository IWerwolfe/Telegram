package com.telegrambot.app.model.documents.doctype;

import com.telegrambot.app.model.Entity;
import com.telegrambot.app.model.EntityDefaults;
import com.telegrambot.app.model.documents.docdata.PartnerData;
import com.telegrambot.app.model.legalentity.Company;
import com.telegrambot.app.model.legalentity.Contract;
import com.telegrambot.app.model.legalentity.Department;
import com.telegrambot.app.model.legalentity.Partner;
import com.telegrambot.app.model.reference.Manager;
import com.telegrambot.app.model.user.UserBD;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Getter
@Setter
@Inheritance(strategy = InheritanceType.JOINED)
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
    @Column(name = "total_amount")
    private Integer totalAmount = 0;
    //    @Column(name = "parent_doc")
//    private Document parentDoc;
    @ManyToOne
    @JoinColumn(name = "creator_id")
    private UserBD creator;

    public abstract Integer getTransactionAmount();

    public Partner getPartner() {
        return getPartnerData() == null ? null : getPartnerData().getPartner();
    }

    public Department getDepartment() {
        return getPartnerData() == null ? null : getPartnerData().getDepartment();
    }

    public Contract getContract() {
        return getPartnerData() == null ? null : getPartnerData().getContract();
    }

    protected abstract String getDescriptor();

    public String getPresentTotalAmount() {
        return String.valueOf(getTotalAmount() / 100);
    }

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
