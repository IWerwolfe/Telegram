package com.supportbot.bot.command;

import com.supportbot.DTO.dadata.DaDataParty;
import com.supportbot.DTO.message.MessageText;
import com.supportbot.bot.SupportBot;
import com.supportbot.client.DaDataClient;
import com.supportbot.services.PartnerService;
import com.supportbot.services.SenderService;
import com.supportbot.services.ToStringServices;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.extensions.bots.commandbot.commands.IBotCommand;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.bots.AbsSender;

@Service
@Slf4j
@RequiredArgsConstructor
public class GetByInnCommand implements IBotCommand {

    private final SenderService senderService;
    private final ToStringServices toStringServices;
    private final DaDataClient daDataClient;

    @Override
    public String getCommandIdentifier() {
        return "info_by_inn";
    }

    @Override
    public String getDescription() {
        return "Получить информацию по ИНН";
    }

    @Override
    public void processMessage(AbsSender absSender, Message message, String[] arguments) {

        String text = null;

        switch (arguments.length) {
            case 0 -> text = "Вы забыли указать инн организации";
            case 1 -> {
                if (arguments[0].matches(PartnerService.REGEX_INN)) {
                    DaDataParty daDataParty = daDataClient.getCompanyDataByINN(arguments[0]);
                    text = daDataParty != null ?
                            toStringServices.toStringNonNullFields(daDataParty, true) :
                            MessageText.getNonFindPhone();
                }
            }
        }
        text = text == null ? "Указан некорректный ИНН" : text;
        senderService.sendBotMessage((SupportBot) absSender, text, message.getChatId());
    }
}
