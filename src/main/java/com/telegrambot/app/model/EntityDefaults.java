package com.telegrambot.app.model;

import com.telegrambot.app.model.documents.doc.payment.BankDoc;
import com.telegrambot.app.model.documents.doc.payment.CardDoc;
import com.telegrambot.app.model.documents.doc.service.TaskDoc;
import com.telegrambot.app.model.reference.legalentity.Contract;
import com.telegrambot.app.model.types.Document;
import com.telegrambot.app.model.types.Entity;
import com.telegrambot.app.model.user.UserBD;
import com.telegrambot.app.repositories.DefaultDocParamRepository;
import lombok.AllArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@Service
public class EntityDefaults {

    private final DefaultDocParamRepository defaultDocParamRepository;

    @Cacheable("default_param")
    public DefaultDocParam getDefaultDocParam() {
        List<DefaultDocParam> defaultDocParams = defaultDocParamRepository.findAll();
        return defaultDocParams.isEmpty() ? new DefaultDocParam() : defaultDocParams.get(0);
    }

    public <E extends Document> void fillDefaultData(E doc) {

        DefaultDocParam defaultParam = getDefaultDocParam();
        doc.setCompany(defaultParam.getCompany());
        doc.setManager(defaultParam.getManager());
        doc.setDivision(doc.getDivision());
        doc.setDate(LocalDateTime.now());
        doc.setDivision(defaultParam.getDivision());

        if (doc instanceof CardDoc cardDoc) {
            cardDoc.setBankAccount(defaultParam.getBankAccount());
            cardDoc.setPayTerminal(defaultParam.getPayTerminal());
        }
        if (doc instanceof BankDoc bankDoc) {
            bankDoc.setBankAccount(defaultParam.getBankAccount());
        }
        if (doc instanceof TaskDoc taskDoc) {
            taskDoc.setType(defaultParam.getTaskDocType());
            taskDoc.setStatus(defaultParam.getInitTaskDocStatus());
        }
    }

    public <E extends Document> void fillDefaultData(E doc, UserBD user) {
        fillDefaultData(doc);
        doc.setCreator(user);
        doc.setAuthor(user.getGuidEntity());
    }

    public static void initializeDefaultEntity1C(Entity entity) {
        entity.setMarkedForDel(false);
    }

    public static void initializeDefaultContract(Contract entity) {
        entity.setDate(LocalDateTime.now());
        entity.setName("Основной договор");
        entity.setIsBilling(false);
    }
}
