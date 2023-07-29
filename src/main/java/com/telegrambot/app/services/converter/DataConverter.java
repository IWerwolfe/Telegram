package com.telegrambot.app.services.converter;

import com.telegrambot.app.model.documents.docdata.PartnerData;
import com.telegrambot.app.model.legalentity.Contract;
import com.telegrambot.app.model.legalentity.Department;
import com.telegrambot.app.model.legalentity.Partner;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;

@Slf4j
@Component
@RequiredArgsConstructor
public class DataConverter {

    private final LegalEntityConverter legalConverter;
    private final ContractConverter contractConverter;
    private final DepartmentConverter departmentConverter;

    public String getFieldValue(Object object, String fieldName) {
        try {
            Field field = object.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            return field.get(object).toString();
        } catch (NoSuchFieldException | IllegalAccessException e) {
            log.error("field not found: " + e.getMessage());
        }
        return null;
    }

    public PartnerData getPartnerData(Object response) {
        Partner partner = legalConverter.getOrCreateEntity(getFieldValue(response, "guidPartner"), true);
        Department department = departmentConverter.getOrCreateEntity(getFieldValue(response, "guidDepartment"), true);
        Contract contract = contractConverter.getOrCreateEntity(getFieldValue(response, "guidContract"), true);
        return new PartnerData(partner, department, contract);
    }
}
