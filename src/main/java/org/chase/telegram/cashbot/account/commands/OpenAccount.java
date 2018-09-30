package org.chase.telegram.cashbot.account.commands;

import org.chase.telegram.cashbot.account.Account;
import org.chase.telegram.cashbot.account.AccountService;
import org.chase.telegram.cashbot.cashChat.CashChatService;
import org.chase.telegram.cashbot.VerificationException;
import org.chase.telegram.cashbot.commands.CashBotReply;
import org.chase.telegram.cashbot.commands.CashCommand;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.Optional;

import static java.util.Objects.requireNonNull;

@Component
public class OpenAccount extends CashCommand {
    private static final String IDENTIFIER = "openAccount";
    private static final String DESCRIPTION = "";
    private static final String EXTENDED_DESCRIPTION = "";

    private final AccountService accountService;
    private final CashChatService cashChatService;

    public OpenAccount(final AccountService accountService, final CashChatService cashChatService) {
        super(IDENTIFIER, DESCRIPTION, EXTENDED_DESCRIPTION);
        this.accountService = requireNonNull(accountService, "accountService");
        this.cashChatService = requireNonNull(cashChatService, "cashChatService");
    }

    @Override
    protected void verify(final Message message, final String[] arguments, final AbsSender absSender) throws VerificationException {
        cashChatService.getById(message.getChatId()).orElseThrow(() -> new VerificationException("The bot is not started"));
        if (accountService.getAccount(message.getFrom().getId(), message.getChatId()).isPresent()) {
            throw new VerificationException("User has already an account");
        }
    }

    @Override
    protected Optional<CashBotReply> executeCommand(final AbsSender absSender, final Message message, final String[] arguments) throws TelegramApiException {
        Account account = accountService.createNew(message.getFrom().getId(), message.getChat().getId());
        return Optional.of(new CashBotReply(message.getChat().getId(), "New account created. Initial Balance: %s", account.getBalance()));

    }
}