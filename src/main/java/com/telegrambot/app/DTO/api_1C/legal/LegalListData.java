package com.telegrambot.app.DTO.api_1C.legal;

import com.telegrambot.app.model.legalentity.Contract;
import com.telegrambot.app.model.legalentity.Department;
import com.telegrambot.app.model.legalentity.LegalEntity;
import lombok.Data;

import java.util.List;

@Data
public class LegalListData {
    private List<LegalEntity> legals;
    private List<Department> departments;
    private List<Contract> contracts;
}
