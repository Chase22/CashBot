package org.chase.telegram.cashbot.commands;

import lombok.extern.slf4j.Slf4j;
import org.chase.telegram.cashbot.VerificationException;
import org.telegram.telegrambots.extensions.bots.commandbot.commands.helpCommand.ManCommand;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Message;
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
    public void processMessage(final AbsSender absSender, final Message message, final String[] arguments) {
        try {
            try {
                verify(message, arguments, absSender);
                executeCommand(absSender, message, arguments).ifPresent(cashBotReply -> {
                    try {
                        cashBotReply.sendMessage(absSender, message.getMessageId());
                    } catch (TelegramApiException e) {
                        log.error("Couldn't send Error to chat", e);
                    }
                });
            } catch (VerificationException e) {
                absSender.execute(new SendMessage(message.getChatId(), e.getMessage()).setReplyToMessageId(message.getMessageId()));
            }
        } catch (TelegramApiException e) {
            log.error("Error executing command", e);
        }
    }

    protected abstract void verify(Message message, String[] arguments, final AbsSender absSender) throws VerificationException;

    protected abstract Optional<CashBotReply> executeCommand(AbsSender absSender, Message message, String[] arguments) throws TelegramApiException;

    @Override
    public void execute(final AbsSender absSender, final User user, final Chat chat, final String[] arguments) {}
}
