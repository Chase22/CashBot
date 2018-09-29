package org.chase.telegram.cashbot.Account.commands;

import org.chase.telegram.cashbot.Account.Account;
import org.chase.telegram.cashbot.Account.AccountService;
import org.chase.telegram.cashbot.CashChat.CashChat;
import org.chase.telegram.cashbot.CashChat.CashChatService;
import org.chase.telegram.cashbot.CashUser.CashUser;
import org.chase.telegram.cashbot.CashUser.CashUserService;
import org.chase.telegram.cashbot.commands.CashBotReply;
import org.chase.telegram.cashbot.commands.CashCommand;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;

import java.util.Optional;

import static java.util.Objects.requireNonNull;

@Component
public class ShowAccounts extends CashCommand {
    private static final String IDENTIFIER = "showAccounts";
    private static final String DESCRIPTION = "";
    private static final String EXTENDED_DESCRIPTION = "";

    private final AccountService accountService;
    private final CashChatService cashChatService;
    private final CashUserService cashUserService;

    public ShowAccounts(final AccountService accountService, final CashChatService cashChatService, final CashUserService cashUserService) {
        super(IDENTIFIER, DESCRIPTION, EXTENDED_DESCRIPTION);
        this.accountService = requireNonNull(accountService, "accountService");
        this.cashChatService = requireNonNull(cashChatService, "cashChatService");
        this.cashUserService = requireNonNull(cashUserService, "cashUserService");
    }

    @Override
    protected Optional<CashBotReply> executeCommand(final AbsSender absSender, final User user, final Chat chat, final String[] arguments) {
        if(chat.isUserChat()) {
            return handleUserChat(user);
        } else if (chat.isGroupChat()) {
            return handleGroupChat(chat);
        }
        return Optional.empty();
    }

    private Optional<CashBotReply> handleGroupChat(final Chat chat) {
        Optional<CashChat> cashChatOptional = cashChatService.getById(chat.getId());

        if (cashChatOptional.isPresent()) {
            StringBuilder replyBuilder = new StringBuilder();
            replyBuilder.append(String.format("Accounts for Chat %s", chat.getTitle()));
            for (Account account : accountService.getAccountsByChatId(chat.getId())) {
                cashUserService.getById(account.getUserId()).ifPresent( cashUser -> {
                    String accountLine = String.format(
                            "%s %s: %s",
                            cashUser.getFirstName(),
                            cashUser.getLastName(),
                            account.getBalance()
                    );

                    replyBuilder.append(accountLine);
                });
            }
            return Optional.of(new CashBotReply(chat.getId(), replyBuilder.toString()));
        }
        return Optional.of(new CashBotReply(chat.getId(), "Group is not registered with the bot"));
    }

    private Optional<CashBotReply> handleUserChat(final User user) {
        Optional<CashUser> cashUserOptional = cashUserService.getById(user.getId());

        if (cashUserOptional.isPresent()) {
            StringBuilder replyBuilder = new StringBuilder();
            replyBuilder.append(String.format("Accounts for User %s %s", user.getFirstName(), user.getLastName()));
            for (Account account : accountService.getAccountsByUserId(user.getId())) {
                cashChatService.getById(account.getUserId()).ifPresent( cashChat -> {
                    String accountLine = String.format(
                            "%s: %s",
                            cashChat.getTitle(),
                            account.getBalance()
                    );

                    replyBuilder.append(accountLine);
                });
            }
            return Optional.of(new CashBotReply(user.getId(), replyBuilder.toString()));
        }
        return Optional.of(new CashBotReply(user.getId(), "You are not registered with the Bot"));
    }
}