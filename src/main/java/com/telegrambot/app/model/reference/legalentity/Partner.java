package com.telegrambot.app.model.reference.legalentity;

import com.telegrambot.app.DTO.types.PartnerType;
import com.telegrambot.app.model.balance.PartnerBalance;
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
        return contracts.size() == 1 ?
                contracts.get(0) :
                contracts.stream()
                        .filter(Contract::getIsDefault)
                        .findFirst()
                        .orElse(null);
    }

    public void setDefaultContract(Contract defaultContract) {

        boolean is = contracts.stream()
                .anyMatch(c -> c == defaultContract);

        contracts.forEach(c -> c.setIsDefault(false));

        if (contracts.isEmpty() || !is) {
            contracts.add(defaultContract);
        }

        defaultContract.setIsDefault(true);
    }

    public Partner(String guid) {
        super(guid);
        this.partnerBalance = new PartnerBalance(this);
    }

    public Partner(String guid, String code) {
        super(guid, code);
        this.partnerBalance = new PartnerBalance(this);
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
