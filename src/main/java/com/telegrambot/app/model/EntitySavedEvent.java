package com.telegrambot.app.model;

import com.telegrambot.app.DTO.types.EventSource;
import com.telegrambot.app.DTO.types.OperationType;
import com.telegrambot.app.model.user.UserBD;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationEvent;

@Getter
@Setter
public class EntitySavedEvent extends ApplicationEvent {

    private final OperationType type;
    private UserBD sourceUser = null;

    private EventSource eventSource = EventSource.BOT;

    public EntitySavedEvent(Object source, OperationType operator) {
        super(source);
        this.type = operator;
    }

    public EntitySavedEvent(Object source, OperationType operator, UserBD sourceUser) {
        super(source);
        this.type = operator;
        this.sourceUser = sourceUser;
    }

    public EntitySavedEvent(Object source, OperationType operator, EventSource eventSource) {
        super(source);
        this.type = operator;
        this.eventSource = eventSource;
    }

    public EntitySavedEvent(Object source, OperationType operator, EventSource eventSource, UserBD sourceUser) {
        super(source);
        this.type = operator;
        this.eventSource = eventSource;
        this.sourceUser = sourceUser;
    }
}
