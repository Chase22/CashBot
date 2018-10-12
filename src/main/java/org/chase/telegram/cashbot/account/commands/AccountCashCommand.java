package org.chase.telegram.cashbot.account.commands;

import org.chase.telegram.cashbot.VerificationException;
import org.chase.telegram.cashbot.account.Account;
import org.chase.telegram.cashbot.account.AccountException;
import org.chase.telegram.cashbot.account.AccountService;
import org.chase.telegram.cashbot.cashUser.CashUser;
import org.chase.telegram.cashbot.cashUser.CashUserService;
import org.chase.telegram.cashbot.commands.CashCommand;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;

import java.util.Optional;

public abstract class AccountCashCommand extends CashCommand {
    private AccountService accountService;

    public AccountCashCommand(final String commandIdentifier, final String description, final String extendedDescription, final AccountService accountService) {
        super(commandIdentifier, description, extendedDescription);
        this.accountService = accountService;
    }

    @Override
    protected void verify(final AbsSender absSender, final Message message, final String[] arguments) throws VerificationException {
        if (!message.getChat().isSuperGroupChat() && message.getChat().isGroupChat()) {
            throw new VerificationException("This command can only be used in groups");
        }

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
                Integer.parseInt(arguments[arguments.length-1]);
            } catch (NumberFormatException e) {
                throw new VerificationException("Amount has to be a Number");
            }
        }
    }

    protected Account getAccountFromMessage(AccountService accountService, CashUserService cashUserService, Message message, String... arguments) throws AccountException, UserNotFoundException {
        Account account;
        final Chat chat = message.getChat();

        if (message.isReply()) {
            account = accountService.getAccount(message.getReplyToMessage().getFrom().getId(), chat.getId()).get();
        } else {
            Optional<CashUser> optionalCashUserTo = cashUserService.getByUsername(arguments[0]);

            if (optionalCashUserTo.isPresent()) {
                Optional<Account> optionalAccountTo = accountService.getAccount(optionalCashUserTo.get().getUserId(), chat.getId());

                if (optionalAccountTo.isPresent()) {
                    account = optionalAccountTo.get();
                } else {
                    throw new AccountException(String.format("Could not find account for User %s. Does he have an account?", arguments[0]));
                }
            } else {
                throw new UserNotFoundException("Could not find User %s. Is he registered with the bot?", arguments[0]);
            }
        }

        return account;
    }
}
