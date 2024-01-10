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
public class UserNotifications {

    @Value("${user.isUse}")
    boolean isUse;

    @Value("${user.isSendCreateNewTask}")
    boolean isSendCreateNewTask;

    @Value("${user.isSendEditTask}")
    boolean isSendEditTask;

    @Value("${user.isClosedTask}")
    boolean isClosedTask;

    @Value("${user.balanceIsReplenished}")
    boolean balanceIsReplenished;

    @Value("${user.needToPayTask}")
    boolean needToPayTask;

    @Value("${user.taskIsPaid}")
    boolean taskIsPaid;

}
