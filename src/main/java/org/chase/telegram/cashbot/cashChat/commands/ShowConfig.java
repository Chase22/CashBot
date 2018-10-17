package org.chase.telegram.cashbot.cashChat.commands;

import org.chase.telegram.cashbot.bot.TelegramUserRightService;
import org.chase.telegram.cashbot.cashChat.CashChat;
import org.chase.telegram.cashbot.cashChat.CashChatService;
import org.chase.telegram.cashbot.commands.CashBotReply;
import org.chase.telegram.cashbot.commands.EnableCommand;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.Optional;

@EnableCommand
@Component
public class ShowConfig extends ConfigCommand {
    private static final String IDENTIFIER = "showConfig";
    private static final String DESCRIPTION = "Shows the current Config";
    private static final String EXTENDED_DESCRIPTION = String.format("Shows the current Config for the group");
    private final CashChatService cashChatService;

    public ShowConfig(final CashChatService cashChatService, final TelegramUserRightService telegramUserRightService) {
        super(IDENTIFIER, DESCRIPTION, EXTENDED_DESCRIPTION, cashChatService, telegramUserRightService);

        this.cashChatService = cashChatService;
    }

    @Override
    protected Optional<CashBotReply> executeCommand(final AbsSender absSender, final Message message, final String[] arguments) throws TelegramApiException {
        CashChat cashChat = cashChatService.getById(message.getChatId()).get();

        return Optional.of(new CashBotReply(message.getChatId(), cashChat.toString()));
    }
}
