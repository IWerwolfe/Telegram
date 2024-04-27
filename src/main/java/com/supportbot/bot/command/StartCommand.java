package com.supportbot.bot.command;

import com.supportbot.DTO.message.MessageText;
import com.supportbot.bot.SupportBot;
import com.supportbot.components.Buttons;
import com.supportbot.model.user.UserBD;
import com.supportbot.model.user.UserType;
import com.supportbot.services.SenderService;
import com.supportbot.services.UserBotService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.extensions.bots.commandbot.commands.IBotCommand;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.bots.AbsSender;

@Service
@Slf4j
@RequiredArgsConstructor
public class StartCommand implements IBotCommand {

    private final SenderService senderService;
    private final UserBotService userBotService;
    private final Buttons button;

    @Override
    public String getCommandIdentifier() {
        return "start";
    }

    @Override
    public String getDescription() {
        return "Начало работы с ботом";
    }

    @Override
    public void processMessage(AbsSender absSender, Message message, String[] arguments) {

        UserBD user = userBotService.getUser(message.getFrom());
        boolean phoneFilled = (user.getPhone() != null && !user.getPhone().isEmpty());

        if (phoneFilled) {
            userBotService.subUpdateUserByAPI(user);
        }

        String separator = System.lineSeparator();
        String text = user.getUserType() == UserType.UNAUTHORIZED ?
                MessageText.getWelcomeMessage() :
                MessageText.getShotWelcomeMessage() +
                        separator + separator +
                        MessageText.getAfterSendingPhone(user.getPerson().getFirstName(), user.getStatuses());

        ReplyKeyboardMarkup keyboard = button.keyboardMarkupDefault(user.getUserType());
        senderService.sendBotMessage((SupportBot) absSender, text, keyboard, message.getChatId());
    }
}
