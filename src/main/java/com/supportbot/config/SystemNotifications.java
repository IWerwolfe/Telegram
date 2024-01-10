package com.supportbot.config;    /*
 *created by WerWolfe on BotConfig
 */

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@Data
@PropertySource("application.properties")
public class SystemNotifications {

    @Value("${system.isUse}")
    boolean isUse;

    @Value("${system.idToWorkGroup}")
    String idToWorkGroup;

    @Value("${system.isSendCreateNewTask}")
    boolean isSendCreateNewTask;

    @Value("${system.isSendEditTask}")
    boolean isSendEditTask;

    @Value("${system.isClosedTask}")
    boolean isClosedTask;

    @Value("${system.isUserClosedTask}")
    boolean isUserClosedTask;
}
