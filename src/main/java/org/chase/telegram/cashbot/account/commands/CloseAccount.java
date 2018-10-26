package org.chase.telegram.cashbot.account.commands;

import org.chase.telegram.cashbot.account.AccountService;
import org.chase.telegram.cashbot.commands.CashBotReply;
import org.chase.telegram.cashbot.commands.CashCommand;
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
public class CloseAccount extends CashCommand {
    private static final String IDENTIFIER = "closeAccount";
    private static final String DESCRIPTION = "Closes your account. The Account will be deleted";
    private static final String EXTENDED_DESCRIPTION = "Closes your account. The Account will be deleted";

    private final AccountService accountService;

    public CloseAccount(final AccountService accountService) {
        super(IDENTIFIER, DESCRIPTION, EXTENDED_DESCRIPTION);

        this.accountService = requireNonNull(accountService, "accountService");
    }


    @Override
    public Optional<CashBotReply> executeCommand(final AbsSender absSender, final Message message, final String[] arguments) {
        Chat chat = message.getChat();
        User user = message.getFrom();

        accountService.deleteAccount(accountService.getAccount(user.getId(), chat.getId()).get());
        return Optional.of(new CashBotReply(chat.getId(), "Your account has been closed"));
    }
}