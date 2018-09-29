package org.chase.telegram.cashbot.Account.commands;

import org.chase.telegram.cashbot.Account.AccountService;
import org.chase.telegram.cashbot.CashChat.CashChatService;
import org.chase.telegram.cashbot.VerificationException;
import org.chase.telegram.cashbot.commands.CashBotReply;
import org.chase.telegram.cashbot.commands.CashCommand;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;

import java.util.Optional;

import static java.util.Objects.requireNonNull;

@Component
public class CloseAccount extends CashCommand {
    private static final String IDENTIFIER = "closeAccount";
    private static final String DESCRIPTION = "";
    private static final String EXTENDED_DESCRIPTION = "";

    private final AccountService accountService;
    private final CashChatService cashChatService;
    private final CashChatService cashUserService;

    public CloseAccount(final AccountService accountService, final CashChatService cashChatService, final CashChatService cashUserService) {
        super(IDENTIFIER, DESCRIPTION, EXTENDED_DESCRIPTION);

        this.accountService = requireNonNull(accountService, "accountService");
        this.cashChatService = requireNonNull(cashChatService, "cashChatService");
        this.cashUserService = cashUserService;
    }


    @Override
    protected void verify(final User user, final Chat chat, final String[] arguments, final AbsSender absSender) throws VerificationException {
        cashChatService.getById(chat.getId()).orElseThrow(() -> new VerificationException("The bot is not started"));
        cashUserService.getById(user.getId()).orElseThrow(() -> new VerificationException("You are not registered with the bot"));
        accountService.getAccount(user.getId(), chat.getId()).orElseThrow(() -> new VerificationException("No Account found"));
    }

    @Override
    protected Optional<CashBotReply> executeCommand(final AbsSender absSender, final Message message, final String[] arguments) {
        Chat chat = message.getChat();
        User user = message.getFrom();

        accountService.deleteAccount(accountService.getAccount(user.getId(), chat.getId()).get());
        return Optional.of(new CashBotReply(chat.getId(), "Your Account has been closed"));
    }
}