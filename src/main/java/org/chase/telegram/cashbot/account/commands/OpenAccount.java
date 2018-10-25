package org.chase.telegram.cashbot.account.commands;

import org.chase.telegram.cashbot.VerificationException;
import org.chase.telegram.cashbot.account.Account;
import org.chase.telegram.cashbot.account.AccountService;
import org.chase.telegram.cashbot.cashChat.CashChatService;
import org.chase.telegram.cashbot.cashUser.CashUserService;
import org.chase.telegram.cashbot.commands.CashBotReply;
import org.chase.telegram.cashbot.commands.CashCommand;
import org.chase.telegram.cashbot.commands.EnableCommand;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.bots.AbsSender;

import java.util.Optional;

import static java.util.Objects.requireNonNull;

@Component
@EnableCommand
public class OpenAccount extends CashCommand {
    private static final String IDENTIFIER = "openAccount";
    private static final String DESCRIPTION = "";
    private static final String EXTENDED_DESCRIPTION = "";

    private final AccountService accountService;
    private final CashChatService cashChatService;
    private final CashUserService cashUserService;

    public OpenAccount(final AccountService accountService, final CashChatService cashChatService, final CashUserService cashUserService) {
        super(IDENTIFIER, DESCRIPTION, EXTENDED_DESCRIPTION);
        this.accountService = requireNonNull(accountService, "accountService");
        this.cashChatService = requireNonNull(cashChatService, "cashChatService");
        this.cashUserService = cashUserService;
    }

    @Override
    protected void verify(final AbsSender absSender, final Message message, final String[] arguments) throws VerificationException {
        cashChatService.getById(message.getChatId()).orElseThrow(() -> new VerificationException("The bot is not started"));
        cashUserService.getById(message.getFrom().getId()).orElseThrow(() -> new VerificationException("You are not registered with the bot"));
        if (accountService.getAccount(message.getFrom().getId(), message.getChatId()).isPresent()) {
            throw new VerificationException("User has already an account");
        }
    }

    @Override
    public Optional<CashBotReply> executeCommand(final AbsSender absSender, final Message message, final String[] arguments) {
        Account account = accountService.createNew(message.getFrom().getId(), message.getChat().getId());
        return Optional.of(new CashBotReply(message.getChat().getId(), "New account created. Initial Balance: %s", account.getBalance()));

    }
}