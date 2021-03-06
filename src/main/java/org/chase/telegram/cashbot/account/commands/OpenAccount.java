package org.chase.telegram.cashbot.account.commands;

import org.chase.telegram.cashbot.account.Account;
import org.chase.telegram.cashbot.account.AccountService;
import org.chase.telegram.cashbot.commands.CashBotReply;
import org.chase.telegram.cashbot.commands.anotations.EnableCommand;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.bots.AbsSender;

import java.util.Optional;

import static java.util.Objects.requireNonNull;

@Component
@EnableCommand
public class OpenAccount extends AccountCommand {
    private static final String IDENTIFIER = "openAccount";
    private static final String DESCRIPTION = "Opens an Account";
    private static final String EXTENDED_DESCRIPTION = "Opens an account in the group with the standard amount set by the admins";

    private final AccountService accountService;

    public OpenAccount(final AccountService accountService) {
        super(IDENTIFIER, DESCRIPTION, EXTENDED_DESCRIPTION);
        this.accountService = requireNonNull(accountService, "accountService");
    }

    @Override
    public Optional<CashBotReply> executeCommand(final AbsSender absSender, final Message message, final String[] arguments) {
        Account account = accountService.createNew(message.getFrom().getId(), message.getChat().getId());
        return Optional.of(new CashBotReply(message.getChat().getId(), "New account created. Initial Balance: %s", account.getBalance()));

    }
}