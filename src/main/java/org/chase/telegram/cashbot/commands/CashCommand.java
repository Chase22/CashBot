package org.chase.telegram.cashbot.commands;

import lombok.extern.slf4j.Slf4j;
import org.chase.telegram.cashbot.VerificationException;
import org.chase.telegram.cashbot.VerifierService;
import org.telegram.telegrambots.extensions.bots.commandbot.commands.helpCommand.ManCommand;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.Optional;

@Slf4j
public abstract class CashCommand extends ManCommand {

    private final VerifierService verifierService;

    public CashCommand(String commandIdentifier, String description, String extendedDescription, final VerifierService verifierService) {
        super(commandIdentifier, description, extendedDescription);
        this.verifierService = verifierService;
    }

    @Override
    public final void execute(AbsSender absSender, User user, Chat chat, String[] arguments) {
        try {
            try {
                verify(user, chat, arguments, verifierService);
                executeCommand(absSender, user, chat, arguments).ifPresent(cashBotReply -> {
                    try {
                        cashBotReply.sendMessage(absSender);
                    } catch (TelegramApiException e) {
                        log.error("Couldn't send Error to chat", e);
                    }
                });
            } catch (VerificationException e) {
                absSender.execute(new SendMessage(chat.getId(), e.getMessage()));
            }
        } catch (TelegramApiException e) {
            log.error("Error executing command", e);
        }
    }

    protected abstract void verify(User user, Chat chat, String[] arguments, VerifierService verifierService) throws VerificationException;

    protected abstract Optional<CashBotReply> executeCommand(AbsSender absSender, User user, Chat chat, String[] arguments) throws TelegramApiException;
}
