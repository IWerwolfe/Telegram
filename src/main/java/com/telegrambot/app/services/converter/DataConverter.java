package com.telegrambot.app.services.converter;

import com.telegrambot.app.DTO.api.typeОbjects.EntityDoc1C;
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

    private final PartnerConverter partnerConverter;
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

    public <R extends EntityDoc1C> PartnerData getPartnerData(R response) {
        Partner partner = partnerConverter.getOrCreateEntity(response.getGuidPartner(), true);
        Department department = departmentConverter.getOrCreateEntity(response.getGuidDepartment(), true);
        Contract contract = contractConverter.getOrCreateEntity(response.getGuidContract(), true);
        return new PartnerData(partner, department, contract);
    }

    public <R extends EntityDoc1C> void fillPartnerDataToResponse(PartnerData partnerData, R response) {
        if (partnerData != null) {
            response.setGuidPartner(Converter1C.convertToGuid(partnerData.getPartner()));
            response.setGuidContract(Converter1C.convertToGuid(partnerData.getContract()));
            response.setGuidDepartment(Converter1C.convertToGuid(partnerData.getDepartment()));
        }
    }
}
