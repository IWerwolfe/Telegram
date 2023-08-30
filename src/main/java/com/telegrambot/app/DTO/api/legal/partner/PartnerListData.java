package com.telegrambot.app.DTO.api.legal.partner;

import com.telegrambot.app.model.balance.PartnerBalance;
import com.telegrambot.app.model.legalentity.Contract;
import com.telegrambot.app.model.legalentity.Department;
import com.telegrambot.app.model.legalentity.Partner;
import lombok.Data;

import java.util.List;

@Data
public class PartnerListData {
    private List<Partner> partners;
    private List<Department> departments;
    private List<Contract> contracts;
    private List<PartnerBalance> balances;
}
