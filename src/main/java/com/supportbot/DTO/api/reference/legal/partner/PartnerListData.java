package com.supportbot.DTO.api.reference.legal.partner;

import com.supportbot.model.balance.PartnerBalance;
import com.supportbot.model.reference.legalentity.Contract;
import com.supportbot.model.reference.legalentity.Department;
import com.supportbot.model.reference.legalentity.Partner;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PartnerListData {
    private List<Partner> partners;
    private List<Department> departments;
    private List<Contract> contracts;
    private List<PartnerBalance> balances;
}
