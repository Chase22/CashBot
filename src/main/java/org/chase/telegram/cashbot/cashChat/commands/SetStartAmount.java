package org.chase.telegram.cashbot.cashChat.commands;

import org.chase.telegram.cashbot.cashChat.CashChat;
import org.chase.telegram.cashbot.cashChat.CashChatService;
import org.chase.telegram.cashbot.commands.CashBotReply;
import org.chase.telegram.cashbot.commands.anotations.EnableCommand;
import org.springframework.stereotype.Component;

import java.util.Optional;

import static java.util.Objects.requireNonNull;

@Component
@EnableCommand
public class SetStartAmount extends ConfigCommand {
    private static final String IDENTIFIER = "setStartAmount";
    private static final String DESCRIPTION = "Sets the Amount given when opening a new account";
    private static final String EXTENDED_DESCRIPTION = String.format("%s. Use /%s [Amount]", DESCRIPTION, IDENTIFIER);

    private final CashChatService cashChatService;

    public SetStartAmount(final CashChatService cashChatService) {
        super(IDENTIFIER, DESCRIPTION, EXTENDED_DESCRIPTION, cashChatService);

        this.cashChatService = requireNonNull(cashChatService, "cashChatService");
    }

    @Override
    protected Optional<CashBotReply> setValue(final CashChat cashChat, final Integer amount) {
        cashChat.setStartAmount(amount);
        cashChatService.save(cashChat);

        return Optional.of(new CashBotReply(cashChat.getChatId(), "Amount set to %s", cashChat.getAmountSticker()));
    }
}