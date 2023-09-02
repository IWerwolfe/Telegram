package com.telegrambot.app.services.converter;

import com.telegrambot.app.DTO.api.balance.BalanceResponse;
import com.telegrambot.app.model.balance.PartnerBalance;
import com.telegrambot.app.model.reference.legalentity.Partner;
import com.telegrambot.app.repositories.balance.PartnerBalanceRepository;
import com.telegrambot.app.repositories.reference.PartnerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
@Slf4j
public class BalanceConverter {
    private final PartnerRepository partnerRepository;
    private final PartnerBalanceRepository partnerBalanceRepository;
    private final PartnerConverter partnerConverter;

    public BalanceResponse convertToResponse(PartnerBalance entity) {
        if (entity == null) {
            return null;
        }
        BalanceResponse response = new BalanceResponse();
        response.setGuid(entity.getPartner().getGuidEntity());
        response.setInn(entity.getPartner().getInn());
        response.setKpp(entity.getPartner().getKpp());
        response.setAmount(String.valueOf(entity.getAmount()));
        return response;
    }

    public PartnerBalance updateEntity(BalanceResponse response, PartnerBalance entityBD) {
        if (entityBD == null || response == null) {
            return entityBD;
        }
        entityBD.setAmount(Integer.valueOf(response.getAmount()));
        return entityBD;
    }

    public PartnerBalance getOrCreateEntity(BalanceResponse dto) {
        Partner partner = getPartner(dto);
        Optional<PartnerBalance> optional = partnerBalanceRepository.findByPartner(partner);
        return optional.orElseGet(() -> new PartnerBalance(partner));
    }

    private Partner getPartner(BalanceResponse dto) {
        Optional<Partner> optional = partnerRepository.findBySyncDataNotNullAndSyncData_Guid(dto.getGuid());
        if (optional.isPresent()) {
            return optional.get();
        }
        List<Partner> partners = partnerRepository.findByInnAndKpp(dto.getInn(), dto.getKpp());
        if (!partners.isEmpty()) {
            return partners.get(0);
        }
        return partnerConverter.getOrCreateEntity(dto.getGuid(), true);
    }

    public PartnerBalance getOrCreateEntity(BalanceResponse dto, boolean isSaved) {
        PartnerBalance partnerBalance = getOrCreateEntity(dto);
        return isSaved ? partnerBalanceRepository.save(partnerBalance) : partnerBalance;
    }

}
