package com.supportbot.bot;

import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.extensions.bots.commandbot.CommandBot;
import org.telegram.telegrambots.extensions.bots.commandbot.commands.CommandRegistry;
import org.telegram.telegrambots.extensions.bots.commandbot.commands.IBotCommand;
import org.telegram.telegrambots.extensions.bots.commandbot.commands.ICommandRegistry;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.bots.AbsSender;

import java.util.Collection;
import java.util.Map;
import java.util.function.BiConsumer;

public abstract class TelegramCommandBot extends TelegramLongPollingBot implements CommandBot, ICommandRegistry {
    private final CommandRegistry commandRegistry;

    public TelegramCommandBot(String botToken) {
        this(new DefaultBotOptions(), botToken);
    }

    public TelegramCommandBot(DefaultBotOptions options, String botToken) {
        this(options, true, botToken);
    }

    public TelegramCommandBot(DefaultBotOptions options, boolean allowCommandsWithUsername, String botToken) {
        super(options, botToken);
        this.commandRegistry = new CommandRegistry(allowCommandsWithUsername, this::getBotUsername);
    }

    public abstract void onUpdateReceived(Update update);

    public final boolean register(IBotCommand botCommand) {
        return this.commandRegistry.register(botCommand);
    }

    public final Map<IBotCommand, Boolean> registerAll(IBotCommand... botCommands) {
        return this.commandRegistry.registerAll(botCommands);
    }

    public final boolean deregister(IBotCommand botCommand) {
        return this.commandRegistry.deregister(botCommand);
    }

    public final Map<IBotCommand, Boolean> deregisterAll(IBotCommand... botCommands) {
        return this.commandRegistry.deregisterAll(botCommands);
    }

    public final Collection<IBotCommand> getRegisteredCommands() {
        return this.commandRegistry.getRegisteredCommands();
    }

    public void registerDefaultAction(BiConsumer<AbsSender, Message> defaultConsumer) {
        this.commandRegistry.registerDefaultAction(defaultConsumer);
    }

    public final IBotCommand getRegisteredCommand(String commandIdentifier) {
        return this.commandRegistry.getRegisteredCommand(commandIdentifier);
    }

    public final boolean executeCommand(Message message) {
        return this.commandRegistry.executeCommand(this, message);
    }

    public abstract String getBotUsername();
}