package org.chase.telegram.cashbot.account.commands;

import org.chase.telegram.cashbot.account.Account;
import org.chase.telegram.cashbot.account.AccountMessageContext;
import org.chase.telegram.cashbot.account.AccountService;
import org.chase.telegram.cashbot.commands.AdminCommand;
import org.chase.telegram.cashbot.commands.CashBotReply;
import org.chase.telegram.cashbot.commands.CashCommand;
import org.chase.telegram.cashbot.commands.EnableCommand;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.bots.AbsSender;

import java.util.Optional;

@Component
@AdminCommand
@EnableCommand
public class SetAmount extends CashCommand {
    private static final String IDENTIFIER = "set";
    private static final String DESCRIPTION = "";
    private static final String EXTENDED_DESCRIPTION = "";

    private final AccountService accountService;
    private final AccountCashCommandArgumentParser argumentParser;

    public SetAmount(final AccountService accountService, final AccountCashCommandArgumentParser argumentParser) {
        super(IDENTIFIER, DESCRIPTION, EXTENDED_DESCRIPTION);

        this.accountService = accountService;
        this.argumentParser = argumentParser;
    }

    @Override
    protected void verify(final AbsSender absSender, final Message message, final String[] arguments) {}

    @Override
    protected Optional<CashBotReply> executeCommand(final AbsSender absSender, final Message message, final String[] arguments) {
        final Chat chat = message.getChat();

        final AccountMessageContext context = argumentParser.parseContextWithAmount(message, arguments);
        final Account account = context.getFromAccount();
        account.setBalance(context.getAmount());
        return Optional.of(new CashBotReply(chat.getId(), "New Balance: %s", account.getBalance()));
    }

}