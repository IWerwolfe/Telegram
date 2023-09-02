package com.telegrambot.app.DTO.api.reference.legal.partner;

import com.telegrambot.app.model.balance.PartnerBalance;
import com.telegrambot.app.model.reference.legalentity.Contract;
import com.telegrambot.app.model.reference.legalentity.Department;
import com.telegrambot.app.model.reference.legalentity.Partner;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class PartnerListData {
    private List<Partner> partners;
    private List<Department> departments;
    private List<Contract> contracts;
    private List<PartnerBalance> balances;
}
