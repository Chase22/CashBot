package org.chase.telegram.cashbot.Account.commands;

import org.chase.telegram.cashbot.Account.Account;
import org.chase.telegram.cashbot.Account.AccountException;
import org.chase.telegram.cashbot.Account.AccountService;
import org.chase.telegram.cashbot.CashUser.CashUser;
import org.chase.telegram.cashbot.CashUser.CashUserService;
import org.chase.telegram.cashbot.VerificationException;
import org.chase.telegram.cashbot.commands.CashBotReply;
import org.chase.telegram.cashbot.commands.CashCommand;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;

import javax.persistence.criteria.CriteriaBuilder;
import java.util.Optional;

import static java.util.Objects.requireNonNull;

@Component
public class TransferAmount extends CashCommand {
    private static final String IDENTIFIER = "transfer";
    private static final String DESCRIPTION = "";
    private static final String EXTENDED_DESCRIPTION = "";

    private final AccountService accountService;
    private final CashUserService cashUserService;

    public TransferAmount(final AccountService accountService, final CashUserService cashUserService) {
        super(IDENTIFIER, DESCRIPTION, EXTENDED_DESCRIPTION);

        this.accountService = requireNonNull(accountService, "accountService");
        this.cashUserService = requireNonNull(cashUserService, "cashUserService");
    }

    @Override
    protected void verify(final Message message, final String[] arguments, final AbsSender absSender) throws VerificationException {
        accountService.getAccount(message.getFrom().getId(), message.getChatId()).orElseThrow(() -> new VerificationException("You do not have an account"));
        if (message.isReply()) {
            User replyToUser = message.getReplyToMessage().getFrom();
            accountService.getAccount(replyToUser.getId(), message.getChatId())
                    .orElseThrow(() -> new VerificationException(
                            String.format("User %s %s does not have an account", replyToUser.getFirstName(), replyToUser.getLastName()))
                    );
        } else if (arguments.length < 2) {
            throw new VerificationException("Not enough arguments provided. Either add a username or reply to the message of a User you want to transfer money to");
        } else {
            try {
                Integer.parseInt(arguments[1]);
            } catch (NumberFormatException e) {
                throw new VerificationException("Amount has to be a Number");
            }
        }
    }

    @Override
    protected Optional<CashBotReply> executeCommand(final AbsSender absSender, final Message message, final String[] arguments) {
        final Chat chat = message.getChat();
        final User fromUser = message.getFrom();

        final Account fromAccount = accountService.getAccount(fromUser.getId(), chat.getId()).get();
        Account toAccount;

        if (message.isReply()) {
            toAccount = accountService.getAccount(message.getReplyToMessage().getFrom().getId(), chat.getId()).get();
        } else {
            Optional<CashUser> optionalCashUserTo = cashUserService.getByUsername(arguments[0]);

            if (optionalCashUserTo.isPresent()) {
                Optional<Account> optionalAccountTo = accountService.getAccount(optionalCashUserTo.get().getUserId(), chat.getId());

                if (optionalAccountTo.isPresent()) {
                    toAccount = optionalAccountTo.get();
                } else {
                    return Optional.of(new CashBotReply(chat.getId(), "Could not find Account for User %s. Does he have an Account?", arguments[0]));
                }
            } else {
                return Optional.of(new CashBotReply(chat.getId(), "Could not find User %s. Is he registered with the bot?", arguments[0]));
            }
        }
        try {
            accountService.transferTo(fromAccount, toAccount, Integer.parseInt(arguments[1]));
            return Optional.of(new CashBotReply(chat.getId(), "Transfer complete. New Balance: %s", fromAccount.getBalance()));
        } catch (AccountException e) {
            return Optional.of(new CashBotReply(chat.getId(), e.getMessage()));
        }

    }
}