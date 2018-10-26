package org.chase.telegram.cashbot.account.commands;

import org.chase.telegram.cashbot.account.Account;
import org.chase.telegram.cashbot.account.AccountException;
import org.chase.telegram.cashbot.account.AccountMessageContext;
import org.chase.telegram.cashbot.account.AccountService;
import org.chase.telegram.cashbot.commands.CashBotReply;
import org.chase.telegram.cashbot.commands.CashCommand;
import org.chase.telegram.cashbot.commands.EnableCommand;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.bots.AbsSender;

import java.util.Optional;

@Component
@EnableCommand
public class TransferAmount extends CashCommand {
    private static final String IDENTIFIER = "transfer";
    private static final String DESCRIPTION = "";
    private static final String EXTENDED_DESCRIPTION = "";

    private final AccountService accountService;
    private final AccountCashCommandArgumentParser argumentParser;

    public TransferAmount(final AccountService accountService, final AccountCashCommandArgumentParser argumentParser) {
        super(IDENTIFIER, DESCRIPTION, EXTENDED_DESCRIPTION);

        this.accountService = accountService;
        this.argumentParser = argumentParser;
    }

    @Override
    public Optional<CashBotReply> executeCommand(final AbsSender absSender, final Message message, final String[] arguments) {
        try {
            final Chat chat = message.getChat();
            final AccountMessageContext context = argumentParser.parseContext(message, arguments);
            if (context.getToAccount() == null) {
                return Optional.of(new CashBotReply(chat.getId(), "Account not found"));
            }

            Account fromAccount;
            if(context.getFromAccount().isPresent()) {
                fromAccount = context.getFromAccount().get();
            } else {
                return Optional.of(new CashBotReply(chat.getId(), "Account not found. Are you registered with the bot?"));
            }

            try {
                accountService.transferTo(fromAccount, context.getToAccount(), context.getAmount());
                return Optional.of(new CashBotReply(chat.getId(), "Transfer complete. New Balance: %s", fromAccount.getBalance()));
            } catch (AccountException e) {
                return Optional.of(new CashBotReply(chat.getId(), e.getMessage()));
            }
        } catch (IllegalArgumentException e) {
            return Optional.of(new CashBotReply(message.getChatId(), e.getMessage()));
        }

    }
}