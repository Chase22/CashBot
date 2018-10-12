package org.chase.telegram.cashbot.account.commands;

import org.chase.telegram.cashbot.VerificationException;
import org.chase.telegram.cashbot.account.Account;
import org.chase.telegram.cashbot.account.AccountException;
import org.chase.telegram.cashbot.account.AccountService;
import org.chase.telegram.cashbot.bot.TelegramUserRightService;
import org.chase.telegram.cashbot.cashUser.CashUserService;
import org.chase.telegram.cashbot.commands.CashBotReply;
import org.chase.telegram.cashbot.commands.EnableCommand;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.Optional;

import static java.util.Objects.requireNonNull;

@Component
@EnableCommand
public class AddAmount extends AccountCashCommand {
    private static final String IDENTIFIER = "addAmount";
    private static final String DESCRIPTION = "";
    private static final String EXTENDED_DESCRIPTION = "";

    private final AccountService accountService;
    private final CashUserService cashUserService;
    private final TelegramUserRightService telegramUserRightService;

    public AddAmount(final AccountService accountService, final CashUserService cashUserService,
                     final TelegramUserRightService telegramUserRightService) {
        super(IDENTIFIER, DESCRIPTION, EXTENDED_DESCRIPTION, accountService);

        this.accountService = requireNonNull(accountService, "accountService");
        this.cashUserService = requireNonNull(cashUserService, "cashUserService");
        this.telegramUserRightService = requireNonNull(telegramUserRightService, "telegramUserRightService");
    }

    @Override
    protected void verify(final AbsSender absSender, final Message message, final String[] arguments) throws VerificationException {
        try {
            if (!telegramUserRightService.isAdministrator(absSender, message.getChat(), message.getFrom())) {
                throw new VerificationException("This command can only be used by admins");
            }
        } catch (TelegramApiException e) {
            throw new VerificationException("Error verifying GroupAdmins");
        }

        super.verify(absSender, message, arguments);
    }

    @Override
    protected Optional<CashBotReply> executeCommand(final AbsSender absSender, final Message message, final String[] arguments) {
        final Chat chat = message.getChat();

        try {
            final Account account = getAccountFromMessage(accountService, cashUserService, message, arguments);
            account.addToBalance(Integer.parseInt(arguments[arguments.length-1]));
            return Optional.of(new CashBotReply(chat.getId(), "New Balance: %s", account.getBalance()));
        } catch (AccountException | UserNotFoundException e) {
            return Optional.of(new CashBotReply(chat.getId(), e.getMessage()));
        }
    }

}