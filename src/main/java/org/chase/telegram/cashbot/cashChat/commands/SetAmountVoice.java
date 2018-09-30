package org.chase.telegram.cashbot.cashChat.commands;

import org.chase.telegram.cashbot.VerificationException;
import org.chase.telegram.cashbot.cashChat.CashChat;
import org.chase.telegram.cashbot.cashChat.CashChatService;
import org.chase.telegram.cashbot.commands.CashBotReply;
import org.chase.telegram.cashbot.commands.EnableCommand;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.bots.AbsSender;

import java.util.Optional;

import static java.util.Objects.requireNonNull;

@Component
@EnableCommand
public class SetAmountVoice extends ConfigCommand {
    private static final String IDENTIFIER = "setAmountVoice";
    private static final String DESCRIPTION = "Sets the Amount given for a VoiceMessage";
    private static final String EXTENDED_DESCRIPTION = String.format("%s. Use /%s [Amount]", DESCRIPTION, IDENTIFIER);

    private final CashChatService cashChatService;

    public SetAmountVoice(final CashChatService cashChatService) {
        super(IDENTIFIER, DESCRIPTION, EXTENDED_DESCRIPTION, cashChatService);

        this.cashChatService = requireNonNull(cashChatService, "cashChatService");
    }

    @Override
    protected void verify(final Message message, final String[] arguments, final AbsSender absSender) throws VerificationException {
        super.verify(message, arguments, absSender);
        if (arguments.length < 1) {
            throw new VerificationException("No Amount was provided");
        }
        try {
            Integer.parseInt(arguments[0]);
        } catch (NumberFormatException e) {
            throw new VerificationException("The Amount must be a number");
        }
    }

    @Override
    protected Optional<CashBotReply> executeCommand(final AbsSender absSender, Message message, final String[] arguments) {
        CashChat cashChat = cashChatService.getById(message.getChatId()).get();
        cashChat.setAmountVoice(Integer.parseInt(arguments[0]));
        cashChatService.save(cashChat);

        return Optional.of(new CashBotReply(message.getChatId(), "Amount set to %s", cashChat.getAmountVoice()));
    }
}