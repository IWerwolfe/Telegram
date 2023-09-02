package com.telegrambot.app.services;

import com.telegrambot.app.DTO.api.balance.BalanceResponse;
import com.telegrambot.app.model.balance.PartnerBalance;
import com.telegrambot.app.model.balance.UserBalance;
import com.telegrambot.app.model.reference.legalentity.Partner;
import com.telegrambot.app.model.transaction.FinTransaction;
import com.telegrambot.app.model.types.Document;
import com.telegrambot.app.repositories.balance.PartnerBalanceRepository;
import com.telegrambot.app.repositories.balance.UserBalanceRepository;
import com.telegrambot.app.repositories.transaction.FinTransactionRepository;
import com.telegrambot.app.services.converter.PartnerConverter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class BalanceService {
    private final UserBalanceRepository userBalanceRepository;
    private final PartnerBalanceRepository partnerBalanceRepository;
    private final FinTransactionRepository finTransactionRepository;
    private final PartnerConverter partnerConverter;

    public void updateBalanceAndFinTransaction(Document doc) {

        if (doc.getTransactionAmount() == 0) {
            return;
        }

        addFinTransaction(doc);
        if (doc.getPartner() != null) {
            updateLegalBalance(doc);
        }
        if (doc.getCreator() != null) {
            updateUserBalance(doc);
        }
    }

    private void updateUserBalance(Document doc) {
        Optional<UserBalance> optional = userBalanceRepository.findByUser(doc.getCreator());
        UserBalance userBalance = optional.orElse(new UserBalance(doc.getCreator()));
        userBalance.setDate(System.currentTimeMillis());
        userBalance.setAmount(userBalance.getAmount() + doc.getTransactionAmount());
        userBalanceRepository.save(userBalance);
    }

    private void updateLegalBalance(Document doc) {
        Optional<PartnerBalance> optional = partnerBalanceRepository.findByPartner(doc.getPartner());
        PartnerBalance partnerBalance = optional.orElse(new PartnerBalance(doc.getPartner()));
        partnerBalance.setDate(System.currentTimeMillis());
        partnerBalance.setAmount(partnerBalance.getAmount() + doc.getTransactionAmount());
        partnerBalanceRepository.save(partnerBalance);
    }

    public PartnerBalance updateLegalBalance(BalanceResponse response) {
        Partner partner = partnerConverter.getOrCreateEntity(response.getGuid(), true);
        Optional<PartnerBalance> optional = partnerBalanceRepository.findByPartner(partner);
        PartnerBalance partnerBalance = optional.orElse(new PartnerBalance(partner));
        partnerBalance.setDate(System.currentTimeMillis());
        partnerBalance.setAmount(Integer.valueOf(response.getAmount()));
        return partnerBalanceRepository.save(partnerBalance);
    }

    private void addFinTransaction(Document doc) {
        FinTransaction transaction = new FinTransaction();
        transaction.setDate(System.currentTimeMillis());
        transaction.setPartner(doc.getPartner());
        transaction.setDepartment(doc.getDepartment());
//        transaction.setBasicDoc(doc);
        transaction.setUser(doc.getCreator());
        transaction.setAmount(doc.getTransactionAmount());
        transaction.setIdBasicDoc(doc.getId());
        finTransactionRepository.save(transaction);
    }
}
