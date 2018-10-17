package org.chase.telegram.cashbot.cashChat.commands;

import org.chase.telegram.cashbot.VerificationException;
import org.chase.telegram.cashbot.bot.TelegramUserRightService;
import org.chase.telegram.cashbot.cashChat.CashChatService;
import org.chase.telegram.cashbot.commands.CashCommand;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import static java.util.Objects.requireNonNull;

public abstract class ConfigCommand extends CashCommand {

    private final CashChatService cashChatService;
    private final TelegramUserRightService telegramUserRightService;

    public ConfigCommand(final String commandIdentifier, final String description, final String extendedDescription,
                         final CashChatService cashChatService, final TelegramUserRightService telegramUserRightService) {
        super(commandIdentifier, description, extendedDescription);
        this.cashChatService = requireNonNull(cashChatService, "cashChatService");
        this.telegramUserRightService = requireNonNull(telegramUserRightService, "telegramUserRightService");
    }

    @Override
    protected void verify(final AbsSender absSender, final Message message, final String[] arguments) throws VerificationException {
        if (!message.getChat().isSuperGroupChat() && !message.getChat().isGroupChat()) {
            throw new VerificationException("This command can only be used in groups");
        }
        cashChatService.getById(message.getChatId()).orElseThrow(() -> new VerificationException("The bot is not running for this chat"));
        try {
            if (!telegramUserRightService.isAdministrator(absSender, message.getChat(), message.getFrom())) {
                throw new VerificationException("This command can only be used by admins");
            }
        } catch (TelegramApiException e) {
            throw new VerificationException("Error verifying groupadmins");
        }
    }

}
