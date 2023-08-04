package com.telegrambot.app.services.converter;

import com.telegrambot.app.DTO.api_1C.legal.LegalEntityResponse;
import com.telegrambot.app.DTO.api_1C.typeОbjects.Entity1C;
import com.telegrambot.app.DTO.types.PartnerType;
import com.telegrambot.app.model.Entity;
import com.telegrambot.app.model.legalentity.LegalEntity;
import com.telegrambot.app.model.legalentity.Partner;
import com.telegrambot.app.repositories.LegalEntityRepository;
import com.telegrambot.app.repositories.PartnerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class LegalEntityConverter extends Converter1C {
    private final PartnerRepository partnerRepository;

    private final LegalEntityRepository legalEntityRepository;

    @Override
    public <T extends Entity, R extends Entity1C> R convertToResponse(T entity) {
        return null;
    }

    @Override
    public <T extends Entity, R extends Entity1C> T updateEntity(R dto, T entity) {
        if (dto instanceof LegalEntityResponse response && entity instanceof LegalEntity entityBD) {
            entityBD.setName(response.getName());
            entityBD.setSyncData(response.getGuid(), response.getCode());
            entityBD.setInn(response.getInn());
            entityBD.setKpp(response.getKpp());
            entityBD.setBankAccount(response.getGuidBankAccount());
            entityBD.setComment(response.getComment());
            entityBD.setOGRN(response.getOgrn());
            entityBD.setCommencement(convertToLocalDateTime(response.getCommencement()));
            entityBD.setCertificate(response.getCertificate());
            entityBD.setDateCertificate(convertToLocalDateTime(response.getDateCertificate()));
            entityBD.setOKPO(response.getOkpo());
            if (entityBD instanceof Partner partner) {
                partner.setPartnerType(convertToEnum(response.getPartnerTypeString(), PartnerType.class));
            }
            return (T) entityBD;
        }
        return null;
    }

    @Override
    public <T extends Entity, R extends Entity1C> T getOrCreateEntity(R dto) {
        return (T) Converter1C.getOrCreateEntity(dto, partnerRepository, Partner.class);
    }

    @Override
    public <T extends Entity> T getOrCreateEntity(String guid, boolean isSaved) {
        return (T) Converter1C.getOrCreateEntity(guid, partnerRepository, Partner.class, isSaved);
    }
}
