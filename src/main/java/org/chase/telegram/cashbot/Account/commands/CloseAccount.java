package org.chase.telegram.cashbot.Account.commands;

import org.chase.telegram.cashbot.Account.AccountService;
import org.chase.telegram.cashbot.CashChat.CashChatService;
import org.chase.telegram.cashbot.VerifierService;
import org.chase.telegram.cashbot.commands.CashBotReply;
import org.chase.telegram.cashbot.commands.CashCommand;
import org.chase.telegram.cashbot.flags.FlagService;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;

import java.util.Optional;

import static java.util.Objects.requireNonNull;

@Component
public class CloseAccount extends CashCommand {
    private static final String IDENTIFIER = "closeAccount";
    private static final String DESCRIPTION = "";
    private static final String EXTENDED_DESCRIPTION = "";

    private final AccountService accountService;
    private final CashChatService cashChatService;
    private final VerifierService verifierService;
    private final FlagService flagService;

    public CloseAccount(final AccountService accountService, final CashChatService cashChatService, final VerifierService verifierService, final FlagService flagService) {
        super(IDENTIFIER, DESCRIPTION, EXTENDED_DESCRIPTION);

        this.accountService = requireNonNull(accountService, "accountService");
        this.cashChatService = requireNonNull(cashChatService, "cashChatService");
        this.verifierService = requireNonNull(verifierService, "verifierService");
        this.flagService = requireNonNull(flagService, "flagService");
    }

    @Override
    protected Optional<CashBotReply> executeCommand(final AbsSender absSender, final User user, final Chat chat, final String[] arguments) {
        return Optional.of(verifierService.verifyIsCashChat(chat.getId()).orElseGet(() ->
                verifierService.verifyIsCashUser(user.getId(), chat.getId()).orElseGet(() ->
                        verifierService.verifyUserHasAccount(user.getId(), chat.getId()).orElseGet(() -> {
                            accountService.deleteAccount(accountService.getAccount(user.getId(), chat.getId()).get());

                            return new CashBotReply(chat.getId(), "Your Account has been closed");
                        })
                )
        ));
    }
}