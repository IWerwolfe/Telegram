package com.supportbot.services;

import com.supportbot.model.command.CommandCache;
import com.supportbot.model.user.UserBD;
import com.supportbot.repositories.command.CommandCacheRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.TimeZone;

@Service
@Slf4j
@RequiredArgsConstructor()
public class SchedulerService {

    private final CommandCacheRepository commandCacheRepository;
    private final BotCommandsImpl botCommands;

    private final String commandExit = "/exit";
    private final String interval = "PT01H";

    @Scheduled(fixedDelayString = interval)
    public void clearSubcommands() {

        LocalDateTime now = LocalDateTime.now(TimeZone.getDefault().toZoneId());

        Duration duration = Duration.parse(interval);
        List<CommandCache> commandCacheList = commandCacheRepository.findByDateBefore(now.minus(duration));

        List<UserBD> userList = commandCacheList
                .stream()
                .map(CommandCache::getUserBD)
                .distinct()
                .toList();

        long userCount = userList.size();

        if (userCount > 0) {
            userList.forEach(user -> botCommands.botAnswerUtils(commandExit, user.getId(), user));
            log.debug("Start clearing command cache in {} for {} users", now, userList.size());
        }
    }
}
