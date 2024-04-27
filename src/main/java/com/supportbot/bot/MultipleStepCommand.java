package com.supportbot.bot;

import com.supportbot.bot.subCommand.Procedure;
import com.supportbot.model.command.CommandCache;
import com.supportbot.model.user.UserBD;
import com.supportbot.services.UserBotService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.extensions.bots.commandbot.commands.IBotCommand;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.bots.AbsSender;

import java.util.Arrays;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
@Getter
@Setter
public abstract class MultipleStepCommand implements IBotCommand {

    private final UserBotService userBotService;
    private final List<Procedure> procedures;

    private UserBD user;
    private CommandCache current;
    private SupportBot supportBot;
    private long chatId;
    private String text;

    @Override
    public void processMessage(AbsSender absSender, Message message, String[] arguments) {

        this.user = userBotService.getUser(message.getFrom());
        this.supportBot = (SupportBot) absSender;
        this.chatId = message.getChatId();
        this.text = arguments.length == 0 ? "" : Arrays.toString(arguments);
        this.current = user.getLatestCommand();

        execute();

//        ReplyKeyboardMarkup keyboard = button.keyboardMarkupDefault(user.getUserType());
//        senderService.sendBotMessage((SupportBot) absSender, text, keyboard, message.getChatId());
    }

    private void execute() {

        CommandCache command = user.getLatestCommand();

        if (command.isComplete()) {
            addNext();
            return;
        }

        Procedure pr = procedures.stream()
                .filter(procedure -> procedure.getDescription().equals(command.getSubCommand()))
                .findFirst()
                .orElseThrow();

        if (pr.execute(supportBot, command, text)) {
            addNext();
        }
    }

    protected abstract int getIndexInstruction(String name);

    protected abstract String getNameInstruction(int index);

    protected abstract int getInstructionSize();

    private void addNext() {

//        int index = instruction.indexOf(current.getSubCommand());
        int index = getIndexInstruction(current.getSubCommand());
        index++;

        if (index < getInstructionSize() - 1) {
            CommandCache command = new CommandCache();
            command.setCommand(getCommandIdentifier());
            command.setUserBD(user);
//            command.setSubCommand(instruction.get(index));
            command.setSubCommand(getNameInstruction(index));
            user.addCommand(command);
            this.current = command;
            execute();
        } else {
            end();
        }
    }

    public abstract void end();
}
