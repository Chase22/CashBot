package org.chase.telegram.cashbot.cashChat.commands;

import org.chase.telegram.cashbot.cashChat.CashChat;
import org.chase.telegram.cashbot.cashChat.CashChatService;
import org.chase.telegram.cashbot.commands.AdminCommand;
import org.chase.telegram.cashbot.commands.CashBotReply;
import org.chase.telegram.cashbot.commands.CashCommand;
import org.chase.telegram.cashbot.commands.EnableCommand;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.bots.AbsSender;

import java.util.Optional;

import static java.util.Objects.requireNonNull;

@Component
@AdminCommand
@EnableCommand
public class SetCurrencyName extends CashCommand {
    private static final String IDENTIFIER = "setCurrencyName";
    private static final String DESCRIPTION = "Sets the Currency Name";
    private static final String EXTENDED_DESCRIPTION = String.format("Sets the Name of the Currency. Use /%s [Name]", IDENTIFIER);

    private final CashChatService cashChatService;

    public SetCurrencyName(final CashChatService cashChatService) {
        super(IDENTIFIER, DESCRIPTION, EXTENDED_DESCRIPTION);

        this.cashChatService = requireNonNull(cashChatService, "cashChatService");
    }

    @Override
    public Optional<CashBotReply> executeCommand(final AbsSender absSender, Message message, final String[] arguments) {
        if (!message.getChat().isSuperGroupChat() && !message.getChat().isGroupChat()) {
            return Optional.of(new CashBotReply(message.getChatId(), "This command can only be used in groups"));
        }

        CashChat cashChat;

        final Optional<CashChat> cashChatOptional = cashChatService.getById(message.getChatId());

        if (cashChatOptional.isPresent()) {
            cashChat = cashChatOptional.get();
        } else {
            return Optional.of(new CashBotReply(message.getChatId(), "The bot is not running for this chat"));
        }

        if (arguments.length < 1) {
            return Optional.of(new CashBotReply(message.getChatId(),"No Name was provided"));
        }
        cashChat.setCurrencyName(arguments[0]);
        cashChatService.save(cashChat);

        return Optional.of(new CashBotReply(message.getChatId(), "Currency Name set to %s", cashChat.getCurrencyName()));
    }
}