package com.supportbot.services;

import com.supportbot.model.EntitySavedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class EntityUpdateListener {

    @EventListener
    public void handleEntitySavedEvent(EntitySavedEvent event) {
        Object entity = event.getSource();
    }
}
