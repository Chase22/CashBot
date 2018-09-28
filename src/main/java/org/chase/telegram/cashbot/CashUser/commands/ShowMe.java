package org.chase.telegram.cashbot.CashUser.commands;

import org.chase.telegram.cashbot.CashChat.CashChat;
import org.chase.telegram.cashbot.CashUser.CashUser;
import org.chase.telegram.cashbot.CashUser.CashUserService;
import org.chase.telegram.cashbot.commands.CashBotReply;
import org.chase.telegram.cashbot.commands.CashCommand;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.Optional;

@Component
public class ShowMe extends CashCommand {
    private static final String IDENTIFIER = "showMe";
    private static final String DESCRIPTION = "";
    private static final String EXTENDED_DESCRIPTION = "";

    private final CashUserService cashUserService;

    public ShowMe(final CashUserService cashUserService) {
        super(IDENTIFIER, DESCRIPTION, EXTENDED_DESCRIPTION);
        this.cashUserService = cashUserService;
    }

    @Override
    protected Optional<CashBotReply> executeCommand(final AbsSender absSender, final User user, final Chat chat, final String[] arguments) throws TelegramApiException {
        Optional<CashUser> optionalCashUser = cashUserService.getById(user.getId());
        if (optionalCashUser.isPresent()) {
            CashUser cashUser = optionalCashUser.get();
            return Optional.of(new CashBotReply(chat.getId(),
                    "Name: %s %s %n" +
                            "Username: %s %n" +
                            "UserId: %s %n",
                    cashUser.getFirstName(), cashUser.getLastName(), cashUser.getUsername(), cashUser.getUserId()));
        } else {
            return Optional.of(new CashBotReply(chat.getId(), "You are not registered with the bot"));
        }
    }
}
