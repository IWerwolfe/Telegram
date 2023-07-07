package com.telegrambot.app.services.converter;

import com.telegrambot.app.DTO.PartnerType;
import com.telegrambot.app.DTO.api_1C.LegalEntityResponse;
import com.telegrambot.app.model.legalentity.LegalEntity;
import com.telegrambot.app.model.legalentity.Partner;
import com.telegrambot.app.repositories.LegalEntityRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
@Slf4j
public class LegalEntityConverter extends Request1CConverter {

    private final LegalEntityRepository legalEntityRepository;

    @Override
    public <T, R> T updateEntity(R dto, T entity) {
        if (dto instanceof LegalEntityResponse response && entity instanceof LegalEntity entityBD) {
            entityBD.setName(response.getName());
            entityBD.setGuid(response.getGuid());
            entityBD.setCode(response.getCode());
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
    public <T, R> T getOrCreateEntity(R dto) {
        if (dto instanceof LegalEntityResponse response) {
            if (response.getGuid() == null || response.getGuid().isEmpty()) {
                return null;
            }
            Optional<LegalEntity> existingEntity = legalEntityRepository.findByGuid(response.getGuid());
            return (T) existingEntity.orElseGet(() -> new Partner(response.getGuid()));
        }
        return null;
    }
}
