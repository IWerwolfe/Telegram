package com.telegrambot.app.services;

import com.telegrambot.app.model.EntitySavedEvent;
import com.telegrambot.app.model.documents.doctype.Document;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class EntitySavedListener {
    private final BalanceService balanceService;

    @EventListener
    public void handleEntitySavedEvent(EntitySavedEvent event) {
        Object entity = event.getSource();
        if (entity instanceof Document doc) {
            balanceService.updateBalanceAndFinTransaction(doc);
        }
    }
}
