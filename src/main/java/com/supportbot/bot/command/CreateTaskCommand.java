package com.supportbot.bot.command;

import com.supportbot.bot.MultipleStepCommand;
import lombok.RequiredArgsConstructor;
import lombok.experimental.SuperBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
@SuperBuilder
public class CreateTaskCommand extends MultipleStepCommand {

    private static final List<String> instruction = List.of("getDescription", "getPartner", "getDepartment", "createTask");

    @Override
    public String getCommandIdentifier() {
        return "start";
    }

    @Override
    public String getDescription() {
        return "Начало работы с ботом";
    }

    public void end() {
        log.error(super.getUser().getCommandsCache().toString());
    }

    @Override
    protected int getIndexInstruction(String name) {
        return instruction.indexOf(name);
    }

    @Override
    protected String getNameInstruction(int index) {
        return instruction.get(index);
    }

    @Override
    protected int getInstructionSize() {
        return instruction.size();
    }
//
//    private void getDescriptionTask() {
//
//        if (current.getAndIncrement() == 0) {
//            sendMessage(MessageText.getStartDescription());
//            return;
//        }
//
//        current.increment();
//
//        if (text.matches(".{10,}")) {
//            current.setResult(text);
//            current.setComplete(true);
//            return;
//        }
//
//        sendMessage(MessageText.getErrorDescription());
//    }

//    private CommandInfo getPartner() {
//        CommandInfo info = new CommandInfo();
//        info.setStartMessage(MessageText.getPartnersByList());
//        info.setErrorMessage(MessageText.errorWhenEditTask());
//        return info;
//    }
//
//    private CommandInfo getDepartment() {
//        CommandInfo info = new CommandInfo();
//        info.setStartMessage(MessageText.getDepartmentsByList());
//        info.setErrorMessage(MessageText.errorWhenEditTask());
//        return info;
//    }
//
//    private CommandInfo createTask() {
//        CommandInfo info = new CommandInfo();
//        info.setStartMessage(MessageText.getSuccessfullyCreatingTask(new TaskDoc()));
//        info.setErrorMessage(MessageText.errorWhenEditTask());
//        return info;
//    }
//
//    private void sendMessage(@NonNull String message, ReplyKeyboard keyboard) {
//        senderService.sendBotMessage(supportBot, message, keyboard, chatId, user);
//    }
//
//    private void sendMessage(@NonNull String message) {
//        senderService.sendBotMessage(supportBot, message, null, chatId, user);
//    }
}
