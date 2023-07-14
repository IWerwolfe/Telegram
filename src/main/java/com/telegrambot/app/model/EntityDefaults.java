package com.telegrambot.app.model;

import com.telegrambot.app.model.documents.docdata.PartnerData;
import com.telegrambot.app.model.documents.docdata.PersonData;
import com.telegrambot.app.model.legalentity.Contract;
import com.telegrambot.app.model.task.Properties;
import com.telegrambot.app.model.task.Task;
import com.telegrambot.app.model.user.UserBD;

import java.time.LocalDateTime;

public class EntityDefaults {

    public static void initializeDefaultEntity1C(EntityBD_1C entity) {
        entity.setMarkedForDel(false);
    }

    public static void initializeDefaultEntityDoc(EntityDocBD_1C entity) {
        entity.setDate(LocalDateTime.now());
    }

    public static void initializeDefaultTask(Task entity) {
        entity.setPartnerData(new PartnerData());
        entity.setProperties(new Properties());
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
