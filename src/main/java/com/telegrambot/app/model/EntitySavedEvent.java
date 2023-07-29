package com.telegrambot.app.model;

import org.springframework.context.ApplicationEvent;

public class EntitySavedEvent extends ApplicationEvent {


    public EntitySavedEvent(Object source) {
        super(source);
    }
}
