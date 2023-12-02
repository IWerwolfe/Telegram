package com.telegrambot.app.model.reference.legalentity;

import com.telegrambot.app.DTO.types.PartnerType;
import com.telegrambot.app.model.balance.EntityBalance;
import com.telegrambot.app.model.balance.PartnerBalance;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@DiscriminatorValue("partners")
public class Partner extends LegalEntity implements EntityBalance {

    private PartnerType partnerType;

    @OneToOne(mappedBy = "partner", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private PartnerBalance partnerBalance;

    @OneToMany(mappedBy = "partner", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Department> departments;

    @OneToMany(mappedBy = "partner", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Contract> contracts;

    public Contract getDefaultContract() {

        if (contracts == null) {
            return null;
        }

        switch (contracts.size()) {
            case 0 -> {
                Contract contract = new Contract(this);
                setDefaultContract(contract);
                return contract;
            }
            case 1 -> {
                return contracts.get(0);
            }
            default -> {
                return contracts.stream()
                        .filter(Contract::getIsDefault)
                        .findFirst()
                        .orElse(null);
            }
        }
    }

    public void setDefaultContract(Contract defaultContract) {

        if (contracts == null) {
            contracts = new ArrayList<>();
        }

        boolean is = false;

        if (!contracts.isEmpty()) {
            is = contracts.stream()
                    .anyMatch(c -> c == defaultContract);
            contracts.forEach(c -> c.setIsDefault(false));
        }

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

    @Override
    public int getBalance() {
        return partnerBalance.getAmount();
    }

    @Override
    public void setBalance(int amount) {
        partnerBalance.setAmount(amount);
    }
}
