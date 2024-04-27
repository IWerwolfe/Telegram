package com.supportbot.bot.command;

import com.supportbot.DTO.api.other.UserResponse;
import com.supportbot.bot.SupportBot;
import com.supportbot.services.SenderService;
import com.supportbot.services.ToStringServices;
import com.supportbot.services.UserBotService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.extensions.bots.commandbot.commands.IBotCommand;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.bots.AbsSender;

@Service
@Slf4j
@RequiredArgsConstructor
public class GetByPhoneCommand implements IBotCommand {

    private final SenderService senderService;
    private final UserBotService userBotService;
    private final ToStringServices toStringServices;

    private String pattern = """
            %s
                        
            Statuses:
            %s""";

    @Override
    public String getCommandIdentifier() {
        return "info_by_phone";
    }

    @Override
    public String getDescription() {
        return "Получить информацию по номеру телефона";
    }

    @Override
    public void processMessage(AbsSender absSender, Message message, String[] arguments) {

        String text = null;

        switch (arguments.length) {
            case 0 -> text = "Вы забыли указать номер телефона";
            case 1 -> {
                if (arguments[0].matches(UserBotService.REGEX_PHONE)) {
                    UserResponse response = userBotService.getUserInfoByAPI(arguments[0]);
                    text = response.isResult() ?
                            getPresentation(response) :
                            "Не удалось найти информацию по этому телефону";
                }
            }
        }
        text = text == null ? "Вы указали некорректный телефон" : text;
        senderService.sendBotMessage((SupportBot) absSender, text, message.getChatId());
    }

    private String getPresentation(UserResponse response) {
        return String.format(pattern,
                toStringServices.toStringNonNullFields(response),
                toStringServices.toStringIterableNonNull(response.getStatusList(), false));
    }
}
