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
public class BugNotifications {

    @Value("${bug.isUse}")
    boolean isUse;

    @Value("${bug.idTelegramUser}")
    String idTelegramUser;
}
