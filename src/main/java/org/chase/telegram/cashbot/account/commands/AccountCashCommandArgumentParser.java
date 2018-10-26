package org.chase.telegram.cashbot.account.commands;

import org.chase.telegram.cashbot.account.Account;
import org.chase.telegram.cashbot.account.AccountMessageContext;
import org.chase.telegram.cashbot.account.AccountService;
import org.chase.telegram.cashbot.cashUser.CashUser;
import org.chase.telegram.cashbot.cashUser.CashUserService;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.User;

@Component
public class AccountCashCommandArgumentParser {
    private final AccountService accountService;
    private final CashUserService cashUserService;

    public AccountCashCommandArgumentParser(final AccountService accountService, final CashUserService cashUserService) {
        this.accountService = accountService;
        this.cashUserService = cashUserService;
    }

    public AccountMessageContext parseContext(Message message, String... arguments) {
        final Integer userId = message.getFrom().getId();
        final Long chatId = message.getChatId();

        Account accountFrom = accountService.getAccount(userId, chatId).orElse(null);

        final Account accountTo;
        if(message.isReply()) {
            accountTo = parseAccountFromReply(message, arguments);
        } else {
            accountTo = parseAccountFromArguments(message, arguments);
        }

        Integer amount;
        try {
            amount = Integer.parseUnsignedInt(arguments[arguments.length-1]);
        } catch (NumberFormatException e) {
                throw new IllegalArgumentException("Amount is not a positive Number");
        }

        return AccountMessageContext.builder()
                .fromAccount(accountFrom)
                .toAccount(accountTo)
                .amount(amount)
                .build();
    }

    private Account parseAccountFromArguments(Message message, String... arguments) {
        if (arguments.length < 2) {
            throw new IllegalArgumentException("Not enough Arguments provided");
        }
        CashUser userTo = cashUserService.getByUsername(arguments[0]).orElseThrow(() -> new IllegalArgumentException("User " + arguments[0] + " not found. Is he registered with the bot?"));
        return accountService.getAccount(userTo.getUserId(), message.getChatId()).orElse(null);
    }

    private Account parseAccountFromReply(Message message, String... arguments) {
        if (arguments.length < 1) {
            throw new IllegalArgumentException("Not enough Arguments provided");
        }

        final User userReply = message.getReplyToMessage().getFrom();

        CashUser userTo = cashUserService.getById(userReply.getId()).orElseThrow(() -> new IllegalArgumentException("User " + userReply.getUserName() + " not found. Is he registered with the bot?"));
        return accountService.getAccount(userTo.getUserId(), message.getChatId()).orElse(null);

    }
}
