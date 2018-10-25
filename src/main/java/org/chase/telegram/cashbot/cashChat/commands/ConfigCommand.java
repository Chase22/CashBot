package org.chase.telegram.cashbot.cashChat.commands;

import org.chase.telegram.cashbot.VerificationException;
import org.chase.telegram.cashbot.cashChat.CashChat;
import org.chase.telegram.cashbot.cashChat.CashChatService;
import org.chase.telegram.cashbot.commands.AdminCommand;
import org.chase.telegram.cashbot.commands.CashBotReply;
import org.chase.telegram.cashbot.commands.CashCommand;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.bots.AbsSender;

import java.util.Optional;

import static java.util.Objects.requireNonNull;

@AdminCommand
public abstract class ConfigCommand extends CashCommand {

    private final CashChatService cashChatService;


    public ConfigCommand(final String commandIdentifier, final String description, final String extendedDescription,
                         final CashChatService cashChatService) {
        super(commandIdentifier, description, extendedDescription);
        this.cashChatService = requireNonNull(cashChatService, "cashChatService");
    }

    @Override
    protected void verify(final AbsSender absSender, final Message message, final String[] arguments) throws VerificationException {

    }

    @Override
    public Optional<CashBotReply> executeCommand(final AbsSender absSender, final Message message, final String[] arguments) {
        if (!message.getChat().isSuperGroupChat() && !message.getChat().isGroupChat()) {
            return Optional.of(new CashBotReply(message.getChatId(), "This command can only be used in groups"));
        }

        CashChat chat;
        Integer amount;

        final Optional<CashChat> cashChatOptional = cashChatService.getById(message.getChatId());

        if (cashChatOptional.isPresent()) {
            chat = cashChatOptional.get();
        } else {
            return Optional.of(new CashBotReply(message.getChatId(), "The bot is not running for this chat"));
        }

        if (arguments.length < 1) {
            return Optional.of(new CashBotReply(message.getChatId(), "No Amount was provided"));
        }
        try {
            amount = Integer.parseInt(arguments[0]);
        } catch (NumberFormatException e) {
            return Optional.of(new CashBotReply(message.getChatId(), "The Amount must be a number"));
        }

        return setValue(chat, amount);
    }

    protected abstract Optional<CashBotReply> setValue(CashChat cashChat, Integer amount);
}