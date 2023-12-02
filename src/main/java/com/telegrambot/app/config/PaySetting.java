package com.telegrambot.app.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@Data
@PropertySource("application.properties")
public class PaySetting {

    @Value("${pay.sbpStatic}")
    String sbpStatic;
    @Value("${pay.isUse}")
    boolean isUse;
    @Value("${pay.isBalanceVisibly}")
    boolean isBalanceVisibly;
    @Value("${pay.isAddBalance}")
    boolean isAddBalance;
    @Value("${pay.isCard}")
    boolean isCard;
    @Value("${pay.isBank}")
    boolean isBank;
    @Value("${pay.isSBPStatic}")
    boolean isSBPStatic;
    @Value("${pay.isSBP}")
    boolean isSBP;
    @Value("${pay.isCrypto}")
    boolean isCrypto;

    public boolean isSBPStatic() {
        return isSBPStatic;
    }
}
