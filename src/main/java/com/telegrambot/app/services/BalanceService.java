package com.telegrambot.app.services;

import com.telegrambot.app.model.balance.LegalBalance;
import com.telegrambot.app.model.balance.UserBalance;
import com.telegrambot.app.model.documents.doctype.Document;
import com.telegrambot.app.model.transaction.FinTransaction;
import com.telegrambot.app.repositories.FinTransactionRepository;
import com.telegrambot.app.repositories.LegalBalanceRepository;
import com.telegrambot.app.repositories.UserBalanceRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class BalanceService {
    private final UserBalanceRepository userBalanceRepository;
    private final LegalBalanceRepository legalBalanceRepository;
    private final FinTransactionRepository finTransactionRepository;

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
        Optional<LegalBalance> optional = legalBalanceRepository.findByLegal(doc.getPartner());
        LegalBalance legalBalance = optional.orElse(new LegalBalance(doc.getPartner()));
        legalBalance.setDate(System.currentTimeMillis());
        legalBalance.setAmount(legalBalance.getAmount() + doc.getTransactionAmount());
        legalBalanceRepository.save(legalBalance);
    }

    private void addFinTransaction(Document doc) {
        FinTransaction transaction = new FinTransaction();
        transaction.setDate(System.currentTimeMillis());
        transaction.setLegal(doc.getPartner());
        transaction.setDepartment(doc.getDepartment());
//        transaction.setBasicDoc(doc);
        transaction.setUser(doc.getCreator());
        transaction.setAmount(doc.getTransactionAmount());
        transaction.setIdBasicDoc(doc.getId());
        finTransactionRepository.save(transaction);
    }
}
