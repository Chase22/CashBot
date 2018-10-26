package org.chase.telegram.cashbot.cashUser.commands;

import org.chase.telegram.cashbot.cashUser.CashUser;
import org.chase.telegram.cashbot.cashUser.CashUserService;
import org.chase.telegram.cashbot.commands.CashBotReply;
import org.chase.telegram.cashbot.commands.CashCommand;
import org.chase.telegram.cashbot.commands.HelpCategory;
import org.chase.telegram.cashbot.commands.anotations.EnableCommand;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.bots.AbsSender;

import java.util.Collections;
import java.util.Optional;
import java.util.Set;

@Component
@EnableCommand
public class ShowMe extends CashCommand {
    private static final String IDENTIFIER = "showMe";
    private static final String DESCRIPTION = "Shows data saved by the bot about you";
    private static final String EXTENDED_DESCRIPTION = "Shows data saved by the bot about you";

    private final CashUserService cashUserService;

    public ShowMe(final CashUserService cashUserService) {
        super(IDENTIFIER, DESCRIPTION, EXTENDED_DESCRIPTION);
        this.cashUserService = cashUserService;
    }

    @Override
    public Optional<CashBotReply> executeCommand(final AbsSender absSender, final Message message, final String[] arguments) {
        if (!message.getChat().isUserChat()) {
            return Optional.of(new CashBotReply(message.getChatId(), "This command can only be executed in private chats"));
        }

        Optional<CashUser> cashUser = cashUserService.getById(message.getFrom().getId());

        return cashUser.map(cashUser1 -> Optional.of(new CashBotReply(message.getChatId(),
                "Name: %s %s %n" +
                        "Username: %s %n" +
                        "UserId: %s %n",
                cashUser1.getFirstName(), cashUser1.getLastName(), cashUser1.getUsername(), cashUser1.getUserId())))
                .orElseGet(() -> Optional.of(new CashBotReply(message.getChatId(), "You are not registered with the bot")));

    }

    @Override
    public Set<HelpCategory> getCategory() {
        return Collections.singleton(HelpCategory.Private);
    }
}
