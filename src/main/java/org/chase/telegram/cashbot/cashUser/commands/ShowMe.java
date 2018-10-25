package org.chase.telegram.cashbot.cashUser.commands;

import org.chase.telegram.cashbot.VerificationException;
import org.chase.telegram.cashbot.cashUser.CashUser;
import org.chase.telegram.cashbot.cashUser.CashUserService;
import org.chase.telegram.cashbot.commands.CashBotReply;
import org.chase.telegram.cashbot.commands.CashCommand;
import org.chase.telegram.cashbot.commands.EnableCommand;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.Optional;

@Component
@EnableCommand
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
    protected void verify(final AbsSender absSender, final Message message, final String[] arguments) throws VerificationException {
        if (!message.getChat().isUserChat()) {
            throw new VerificationException("This command can only be executed in private chats");
        }
        cashUserService.getById(message.getFrom().getId()).orElseThrow(() -> new VerificationException("You are not registered with the bot"));
    }

    @Override
    public Optional<CashBotReply> executeCommand(final AbsSender absSender, final Message message, final String[] arguments) throws TelegramApiException {
        CashUser cashUser = cashUserService.getById(message.getFrom().getId()).get();
        return Optional.of(new CashBotReply(message.getChatId(),
                "Name: %s %s %n" +
                        "Username: %s %n" +
                        "UserId: %s %n",
                cashUser.getFirstName(), cashUser.getLastName(), cashUser.getUsername(), cashUser.getUserId()));
    }
}
