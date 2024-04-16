package com.supportbot.config;    /*
 *created by WerWolfe on TelegramBotConfiguration
 */

import com.supportbot.bot.SupportBot;
import com.supportbot.repositories.EntityRepository;
import com.supportbot.utils.ClassScanner;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Slf4j
@Configuration
@RequiredArgsConstructor
@Getter
@Setter
public class TelegramBotConfiguration {

    private SupportBot bot;
    private final ClassScanner classScanner;
    private List<Class<?>> referenceList;

    final long delay = 1;

    @Bean
    TelegramBotsApi telegramBotsApi(SupportBot supportBot) {

        TelegramBotsApi botsApi = null;

        try {

            botsApi = new TelegramBotsApi(DefaultBotSession.class);
            botsApi.registerBot(supportBot);
            this.bot = supportBot;
//            startScheduleSynchronization();

            log.info("Telegram bot {} launched", supportBot.getBotUsername());

        } catch (TelegramApiException e) {
            log.error("Error occurred while sending message to telegram!", e);
        }
        return botsApi;
    }

    private void startScheduleSynchronization() {

        try {
            referenceList = classScanner.scan("com.supportbot.repositories.reference", EntityRepository.class);
        } catch (ClassNotFoundException e) {
            log.error(e.getMessage());
        }

        ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
        executorService.scheduleAtFixedRate(this::checkRef
                , delay
                , delay
                , TimeUnit.MINUTES);
    }

    void checkRef() {

        if (referenceList == null || referenceList.size() == 0) {
            log.error("Reference list is empty");
            return;
        }

        for (Class<?> ref : referenceList) {
            log.error(ref.getName());
        }
    }
}