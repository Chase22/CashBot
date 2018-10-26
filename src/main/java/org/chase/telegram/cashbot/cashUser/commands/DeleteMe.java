package org.chase.telegram.cashbot.cashUser.commands;

import org.chase.telegram.cashbot.account.AccountService;
import org.chase.telegram.cashbot.cashUser.CashUserService;
import org.chase.telegram.cashbot.commands.CashBotReply;
import org.chase.telegram.cashbot.commands.CashCommand;
import org.chase.telegram.cashbot.commands.anotations.EnableCommand;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.bots.AbsSender;

import java.util.Optional;

@Component
@EnableCommand
public class DeleteMe extends CashCommand {
    private static final String IDENTIFIER = "deleteMe";
    private static final String DESCRIPTION = "Deletes all data saved by the bot ";
    private static final String EXTENDED_DESCRIPTION = DESCRIPTION;

    private final CashUserService cashUserService;
    private final AccountService accountService;

    public DeleteMe(final CashUserService cashUserService, final AccountService accountService) {
        super(IDENTIFIER, DESCRIPTION, EXTENDED_DESCRIPTION);
        this.cashUserService = cashUserService;
        this.accountService = accountService;
    }

    @Override
    public Optional<CashBotReply> executeCommand(final AbsSender absSender, final Message message, final String[] arguments) {
        if (!message.getChat().isUserChat()) {
            return Optional.of(new CashBotReply(message.getChatId(), "This command can only be executed in private chats"));
        }


        return cashUserService.getById(message.getFrom().getId()).map(cashUser -> {
            accountService.getAccountsByUserId(cashUser.getUserId()).forEach(accountService::deleteAccount);
            cashUserService.delete(cashUser);
            return Optional.of(new CashBotReply(message.getChatId(), "You are no longer registered with the bot"));
        }).orElseGet(() -> Optional.of(new CashBotReply(message.getChatId(), "You are not registered with the bot")));

    }
}
