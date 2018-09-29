package org.chase.telegram.cashbot.Account.commands;

import org.chase.telegram.cashbot.Account.Account;
import org.chase.telegram.cashbot.Account.AccountService;
import org.chase.telegram.cashbot.ChatNotRegisteredException;
import org.chase.telegram.cashbot.commands.CashBotReply;
import org.chase.telegram.cashbot.commands.CashCommand;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;
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

    public OpenAccount(final AccountService accountService) {
        super(IDENTIFIER, DESCRIPTION, EXTENDED_DESCRIPTION);
        this.accountService = requireNonNull(accountService, "accountService");
    }

    @Override
    protected Optional<CashBotReply> executeCommand(final AbsSender absSender, final User user, final Chat chat, final String[] arguments) throws TelegramApiException {
        if (accountService.getAccount(user.getId(), chat.getId()).isPresent()) {
            return Optional.of(new CashBotReply(chat.getId(), "User has already an account"));
        }
        try {
            Account account = accountService.createNew(user.getId(), chat.getId());
            return Optional.of(new CashBotReply(chat.getId(), "New Account created. Initial Balance: %s", account.getBalance()));
        } catch (ChatNotRegisteredException e) {
            return Optional.of(new CashBotReply(chat.getId(), "Chat is not registered with the bot"));
        }
    }
}