package org.chase.telegram.cashbot;

import org.chase.telegram.cashbot.Account.AccountService;
import org.chase.telegram.cashbot.CashChat.CashChatService;
import org.chase.telegram.cashbot.CashUser.CashUserService;
import org.chase.telegram.cashbot.commands.CashBotReply;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static java.util.Objects.requireNonNull;

@Service
public class VerifierService {
    private final AccountService accountService;
    private final CashUserService cashUserService;
    private final CashChatService cashChatService;

    public VerifierService(final AccountService accountService, final CashUserService cashUserService, final CashChatService cashChatService) {
        this.accountService = requireNonNull(accountService, "accountService");
        this.cashUserService = requireNonNull(cashUserService, "cashUserService");
        this.cashChatService = requireNonNull(cashChatService, "cashChatService");
    }

    public Optional<CashBotReply> verifyIsCashChat(long chatId) {
        if (cashChatService.getById(chatId).isPresent()) {
            return Optional.empty();
        }
        return Optional.of(new CashBotReply(chatId, "Bot is not started"));
    }

    public Optional<CashBotReply> verifyIsCashUser(int userId, long chatId) {
        if (cashUserService.getById(userId).isPresent()) {
            return Optional.empty();
        }
        return Optional.of(new CashBotReply(chatId, "Bot is not started"));
    }

    public Optional<CashBotReply> verifyUserHasAccount(int userId, long chatId) {
        if (accountService.getAccount(userId, chatId).isPresent()) {
            return Optional.empty();
        }
        return Optional.of(new CashBotReply(chatId, "User has no Account in the Group"));
    }
}
