package com.telegrambot.app.model;

import com.telegrambot.app.model.documents.doc.service.TaskDoc;
import com.telegrambot.app.model.documents.docdata.PartnerData;
import com.telegrambot.app.model.documents.docdata.PersonData;
import com.telegrambot.app.model.documents.docdata.PropertyData;
import com.telegrambot.app.model.documents.doctype.Document;
import com.telegrambot.app.model.legalentity.Contract;
import com.telegrambot.app.model.user.UserBD;

import java.time.LocalDateTime;

public class EntityDefaults {

    public static void initializeDefaultEntity1C(Entity entity) {
        entity.setMarkedForDel(false);
    }

    public static void initializeDefaultEntityDoc(Document entity) {
        entity.setDate(LocalDateTime.now());
    }

    public static void initializeDefaultTask(TaskDoc entity) {
        entity.setPartnerData(new PartnerData());
        entity.setProperties(new PropertyData());
    }

    public static void initializeDefaultPersonalFields(UserBD entity) {
        entity.setPerson(new PersonData());
    }

    public static void initializeDefaultContract(Contract entity) {
        entity.setDate(LocalDateTime.now());
        entity.setName("Основной договор");
        entity.setBilling(false);
    }
}
