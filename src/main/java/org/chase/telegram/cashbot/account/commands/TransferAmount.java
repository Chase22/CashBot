package org.chase.telegram.cashbot.account.commands;

import org.chase.telegram.cashbot.VerificationException;
import org.chase.telegram.cashbot.account.Account;
import org.chase.telegram.cashbot.account.AccountException;
import org.chase.telegram.cashbot.account.AccountService;
import org.chase.telegram.cashbot.cashUser.CashUserService;
import org.chase.telegram.cashbot.commands.CashBotReply;
import org.chase.telegram.cashbot.commands.EnableCommand;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;

import java.util.Optional;

import static java.util.Objects.requireNonNull;

@Component
@EnableCommand
public class TransferAmount extends AccountCashCommand {
    private static final String IDENTIFIER = "transferto";
    private static final String DESCRIPTION = "";
    private static final String EXTENDED_DESCRIPTION = "";

    private final AccountService accountService;
    private final CashUserService cashUserService;

    public TransferAmount(final AccountService accountService, final CashUserService cashUserService) {
        super(IDENTIFIER, DESCRIPTION, EXTENDED_DESCRIPTION, accountService);

        this.accountService = requireNonNull(accountService, "accountService");
        this.cashUserService = requireNonNull(cashUserService, "cashUserService");
    }

    @Override
    protected void verify(final AbsSender absSender, final Message message, final String[] arguments) throws VerificationException {
        super.verify(absSender, message, arguments);
    }

    @Override
    protected Optional<CashBotReply> executeCommand(final AbsSender absSender, final Message message, final String[] arguments) {
        final Chat chat = message.getChat();
        final User fromUser = message.getFrom();

        final Account fromAccount = accountService.getAccount(fromUser.getId(), chat.getId()).get();

        final Account toAccount;
        try {
            toAccount = getAccountFromMessage(accountService, cashUserService, message, arguments);
            accountService.transferTo(fromAccount, toAccount, Integer.parseInt(arguments[1]));
            return Optional.of(new CashBotReply(chat.getId(), "Transfer complete. New Balance: %s", fromAccount.getBalance()));
        } catch (AccountException | UserNotFoundException e) {
            return Optional.of(new CashBotReply(chat.getId(), e.getMessage()));
        }

    }
}