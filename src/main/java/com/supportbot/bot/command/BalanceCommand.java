package com.supportbot.bot.command;

import com.supportbot.DTO.message.MessageText;
import com.supportbot.bot.SupportBot;
import com.supportbot.model.balance.PartnerBalance;
import com.supportbot.model.reference.legalentity.Partner;
import com.supportbot.model.user.UserBD;
import com.supportbot.repositories.balance.PartnerBalanceRepository;
import com.supportbot.services.PartnerService;
import com.supportbot.services.SenderService;
import com.supportbot.services.UserBotService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.extensions.bots.commandbot.commands.IBotCommand;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.bots.AbsSender;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class BalanceCommand implements IBotCommand {

    private final SenderService senderService;
    private final UserBotService userBotService;
    private final PartnerService partnerService;
    private final PartnerBalanceRepository partnerBalanceRepository;

    private UserBD user;

    @Override
    public String getCommandIdentifier() {
        return "balance";
    }

    @Override
    public String getDescription() {
        return "Получить информацию о состоянии счета";
    }

    @Override
    public void processMessage(AbsSender absSender, Message message, String[] arguments) {

        user = userBotService.getUser(message.getFrom());
        String text;

        List<PartnerBalance> balances = getBalances();
        if (balances == null || balances.isEmpty()) {
            text = MessageText.getBalanceUser(user.getBalance());
        } else {
            StringBuilder builder = new StringBuilder();
            balances.forEach(balance ->
                    builder.append(MessageText.getBalanceLegal(balance.getPartner(), balance.getAmount()))
                            .append(System.lineSeparator()));
            text = builder.toString();
        }
        senderService.sendBotMessage((SupportBot) absSender, text, message.getChatId());
    }

    private List<PartnerBalance> getBalances() {
        List<Partner> partners = partnerService.getPartnerByUserStatus(user);
        return partnerBalanceRepository.findByPartnerInOrderByPartner_NameAsc(partners);
    }
}
