package com.telegrambot.app.model;

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

    public EntitySavedEvent(Object source, OperationType operator) {
        super(source);
        this.type = operator;
    }

    public EntitySavedEvent(Object source, OperationType operator, UserBD sourceUser) {
        super(source);
        this.type = operator;
        this.sourceUser = sourceUser;
    }

}
