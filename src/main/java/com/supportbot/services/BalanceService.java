package com.supportbot.services;

import com.supportbot.DTO.api.balance.BalanceResponse;
import com.supportbot.model.balance.PartnerBalance;
import com.supportbot.model.reference.legalentity.Partner;
import com.supportbot.model.transaction.FinTransaction;
import com.supportbot.model.types.Document;
import com.supportbot.model.user.UserBD;
import com.supportbot.repositories.reference.PartnerRepository;
import com.supportbot.repositories.transaction.FinTransactionRepository;
import com.supportbot.repositories.user.UserRepository;
import com.supportbot.services.converter.PartnerConverter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class BalanceService {
    private final UserRepository userRepository;
    private final PartnerRepository partnerRepository;
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
        UserBD user = doc.getCreator();
        if (user != null) {
            user.updateBalance(doc.getTransactionAmount());
            userRepository.save(user);
        }
    }

    private void updateLegalBalance(Document doc) {
        Partner partner = doc.getPartner();
        if (partner != null) {
            partner.updateBalance(doc.getTransactionAmount());
            partnerRepository.save(partner);
        }
    }

    public PartnerBalance updateLegalBalance(BalanceResponse response) {
        Partner partner = partnerConverter.getOrCreateEntity(response.getGuid(), true);
        partner.setBalance(Integer.parseInt(response.getAmount()));
        partnerRepository.save(partner);
        return partner.getPartnerBalance();
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
