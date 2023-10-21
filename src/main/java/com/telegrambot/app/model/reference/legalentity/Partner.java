package com.telegrambot.app.model.reference.legalentity;

import com.telegrambot.app.DTO.types.PartnerType;
import com.telegrambot.app.model.balance.PartnerBalance;
import com.telegrambot.app.model.documents.docdata.SyncData;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Entity
@DiscriminatorValue("partners")
public class Partner extends LegalEntity {

    private PartnerType partnerType;

    @OneToOne(mappedBy = "partner", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private PartnerBalance partnerBalance;

    @OneToMany(mappedBy = "partner", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Department> departments;

    @OneToMany(mappedBy = "partner", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Contract> contracts;

    public Contract getDefaultContract() {
        if (contracts.size() == 1) {
            return contracts.get(0);
        }
        return contracts.stream()
                .filter(Contract::getIsDefault)
                .findFirst()
                .orElse(null);
    }

    @Override
    public void setDefaultContract(Contract defaultContract) {

        boolean is = contracts.stream()
                .anyMatch(c -> c == defaultContract);

        if (contracts.isEmpty() || is) {
            contracts.add(defaultContract);
        }

        contracts.forEach(c -> c.setIsDefault(false));
        defaultContract.setIsDefault(true);
    }

    public Partner(String guid) {
        this();
        setSyncData(new SyncData(guid));
    }

    public Partner(String guid, String code) {
        this();
        setSyncData(new SyncData(guid, code));
    }

    public Partner() {
        this.partnerBalance = new PartnerBalance(this);
    }

    public int getBalance() {
        return partnerBalance.getAmount();
    }

    public void setBalance(int balance) {
        partnerBalance.setAmount(balance);
    }

    public void updateBalance(int sum) {
        partnerBalance.setAmount(partnerBalance.getAmount() + sum);
    }
}
