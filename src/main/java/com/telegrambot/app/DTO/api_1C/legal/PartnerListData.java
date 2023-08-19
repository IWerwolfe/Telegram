package com.telegrambot.app.DTO.api_1C.legal;

import com.telegrambot.app.model.balance.LegalBalance;
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
    private List<LegalBalance> balances;
}
