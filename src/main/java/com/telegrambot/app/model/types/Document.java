package com.telegrambot.app.model.types;

import com.telegrambot.app.model.documents.docdata.PartnerData;
import com.telegrambot.app.model.reference.Manager;
import com.telegrambot.app.model.reference.legalentity.*;
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
    @ManyToOne
    @JoinColumn(name = "division_id")
    private Division division;
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

    public void setPartnerData(Partner partner) {
        setPartnerData(partner, null);
    }

    public void setPartnerData(Partner partner, Department department) {

        PartnerData partnerData = getPartnerData() == null ? new PartnerData() : getPartnerData();

        if (partner != null) {
            partnerData.setPartner(partner);
            partnerData.setContract(partner.getDefaultContract());
            partnerData.setDepartment(department);
        }

        this.partnerData = partnerData;
    }

    public void setPartnerData(PartnerData partnerData) {
        this.partnerData = partnerData;
    }

    protected abstract String getDescriptor();

    public String getPresentTotalAmount() {
        return String.valueOf(getTotalAmount() / 100);
    }

    @Override
    public String toString() {
        String code = (getSyncData() == null ? String.valueOf(getId()) : getSyncData().getCode());
        String date = getDate() == null ? "00.00.0000" : getDate().format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
        return getDescriptor() + " № " + code + " от " + date;
    }
}
