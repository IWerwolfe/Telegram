package com.telegrambot.app.model.documents.doctype;

import com.telegrambot.app.model.documents.docdata.PartnerData;
import com.telegrambot.app.model.legalentity.Company;
import com.telegrambot.app.model.task.Manager;
import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MappedSuperclass;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@MappedSuperclass
public abstract class SalesDoc extends Document {

    @ManyToOne
    @JoinColumn(name = "company_id")
    private Company company;
    private PartnerData partnerData;
    private String division;
    @ManyToOne
    private Manager manager;
    @Column(name = "total_amount", precision = 15, scale = 3)
    private BigDecimal totalAmount;
}
