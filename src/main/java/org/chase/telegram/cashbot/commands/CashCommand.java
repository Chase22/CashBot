package org.chase.telegram.cashbot.commands;

import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.extensions.bots.commandbot.commands.helpCommand.ManCommand;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.Optional;

@Slf4j
public abstract class CashCommand extends ManCommand {

    public CashCommand(String commandIdentifier, String description, String extendedDescription) {
        super(commandIdentifier, description, extendedDescription);
    }

    @Override
    public final void execute(AbsSender absSender, User user, Chat chat, String[] arguments) {
        try {
            executeCommand(absSender, user, chat, arguments).ifPresent(cashBotReply -> {
                try {
                    cashBotReply.sendMessage(absSender);
                } catch (TelegramApiException e) {
                    log.error("Couldn't send Error to chat", e);
                }
            });
        } catch (TelegramApiException e) {
            log.error("Error executing command", e);
        }
    }

    protected abstract Optional<CashBotReply> executeCommand(AbsSender absSender, User user, Chat chat, String[] arguments) throws TelegramApiException;
}
