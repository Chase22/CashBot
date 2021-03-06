package org.chase.telegram.cashbot.account.commands;

import org.chase.telegram.cashbot.account.Account;
import org.chase.telegram.cashbot.account.AccountMessageContext;
import org.chase.telegram.cashbot.account.AccountService;
import org.chase.telegram.cashbot.commands.CashBotReply;
import org.chase.telegram.cashbot.commands.anotations.AdminCommand;
import org.chase.telegram.cashbot.commands.anotations.EnableCommand;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.bots.AbsSender;

import java.util.Optional;

@Component
@AdminCommand
@EnableCommand
public class SetAmount extends AccountCommand {
    private static final String IDENTIFIER = "set";
    private static final String DESCRIPTION = "sets the accounts balance";
    private static final String EXTENDED_DESCRIPTION = "Sets the balance of the account provided by parameter or by reply. User /add [username] [amount]";

    private final AccountService accountService;
    private final AccountCashCommandArgumentParser argumentParser;

    public SetAmount(final AccountService accountService, final AccountCashCommandArgumentParser argumentParser) {
        super(IDENTIFIER, DESCRIPTION, EXTENDED_DESCRIPTION);

        this.accountService = accountService;
        this.argumentParser = argumentParser;
    }

    @Override
    public Optional<CashBotReply> executeCommand(final AbsSender absSender, final Message message, final String[] arguments) {
        final Chat chat = message.getChat();

        try {
            final AccountMessageContext context = argumentParser.parseContext(message, arguments);
            final Account account = context.getToAccount();
            account.setBalance(context.getAmount());
            accountService.saveAccount(account);
            return Optional.of(new CashBotReply(chat.getId(), "New Balance: %s", account.getBalance()));
        } catch (IllegalArgumentException e) {
            return Optional.of(new CashBotReply(message.getChatId(), e.getMessage()));
        }

    }

}