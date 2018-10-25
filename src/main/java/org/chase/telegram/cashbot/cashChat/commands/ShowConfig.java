package org.chase.telegram.cashbot.cashChat.commands;

import org.chase.telegram.cashbot.cashChat.CashChatService;
import org.chase.telegram.cashbot.commands.CashBotReply;
import org.chase.telegram.cashbot.commands.CashCommand;
import org.chase.telegram.cashbot.commands.EnableCommand;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.bots.AbsSender;

import java.util.Optional;

@EnableCommand
@Component
public class ShowConfig extends CashCommand {
    private static final String IDENTIFIER = "showConfig";
    private static final String DESCRIPTION = "Shows the current Config";
    private static final String EXTENDED_DESCRIPTION = "Shows the current Config for the group";
    private final CashChatService cashChatService;

    public ShowConfig(final CashChatService cashChatService) {
        super(IDENTIFIER, DESCRIPTION, EXTENDED_DESCRIPTION);

        this.cashChatService = cashChatService;
    }

    @Override
    protected void verify(final AbsSender absSender, final Message message, final String[] arguments) {}

    @Override
    public Optional<CashBotReply> executeCommand(final AbsSender absSender, final Message message, final String[] arguments) {
        return cashChatService.getById(message.getChatId())
                .map(cashChat -> Optional.of(new CashBotReply(message.getChatId(), cashChat.toString())))
                .orElse(Optional.of(new CashBotReply(message.getChatId(), "The bot is not running for this chat")));
    }
}
